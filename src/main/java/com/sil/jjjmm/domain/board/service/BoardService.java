package com.sil.jjjmm.domain.board.service;

import com.sil.jjjmm.domain.attachment.dto.AttachmentDto;
import com.sil.jjjmm.domain.attachment.service.AttachmentService;
import com.sil.jjjmm.domain.board.dto.BoardDto;
import com.sil.jjjmm.domain.board.entity.Board;
import com.sil.jjjmm.domain.board.repository.BoardRepository;
import com.sil.jjjmm.global.code.ParentType;
import com.sil.jjjmm.global.exception.CustomException;
import com.sil.jjjmm.global.response.ResponseCode;
import com.sil.jjjmm.global.util.UtilCommon;
import com.sil.jjjmm.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final AttachmentService attachmentService;
    private final UtilMessage utilMessage;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<BoardDto.Response> boardList(BoardDto.Search search) {
        // Search 정보로 Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                search.getPage(),
                search.getSize(),
                search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                search.getSortBy()
        );

        return boardRepository.findBoardAll(search, pageable);
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public BoardDto.Response boardDetail(String id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        BoardDto.Response response = BoardDto.Response.toDto(board);

        // 파일정보추가
        List<AttachmentDto.Response> attachments = new ArrayList<>();
        board.getAttachments().forEach(attachment -> attachments.add(AttachmentDto.Response.toDto(attachment)));

        response.setAttachments(attachments);
        response.setAttachmentCount((long) attachments.size());
        return response;
    }

    /**
     * 등록
     * @param request
     * @return
     */
    @Transactional
    public BoardDto.Response boardCreate(BoardDto.CreateRequest request, MultipartFile[] mFiles) throws IOException {
        Board board = boardRepository.save(request.toEntity());

        // 파일저장
        if(UtilCommon.isNotEmpty(mFiles)) {
            AttachmentDto.CreateBaseRequest baseRequest = new AttachmentDto.CreateBaseRequest();
            baseRequest.setParentType(ParentType.BOARD);
            baseRequest.setBoard(board);

            attachmentService.attachmentCreate(baseRequest, mFiles);
        }

        return BoardDto.Response.toDto(board);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    @Transactional
    public BoardDto.Response boardModify(String id, BoardDto.ModifyRequest request, MultipartFile[] mFiles) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        request.boardModify(board);

        // UI상에서 삭제된 파일은 삭제처리해야함
        // 파일정보삭제
        AttachmentDto.DeleteRequest attachmentDeleteRequest;
        if(UtilCommon.isNotEmpty(request.getAttachmentIds())) {
            for(String attachmentId : request.getAttachmentIds()) {
                attachmentDeleteRequest = new AttachmentDto.DeleteRequest();
                attachmentDeleteRequest.setParentType(ParentType.BOARD);
                attachmentDeleteRequest.setId(attachmentId);
                attachmentService.attachmentDelete(attachmentDeleteRequest);
            }
        }

        // 파일저장
        if(UtilCommon.isNotEmpty(mFiles)) {
            AttachmentDto.CreateBaseRequest baseRequest = new AttachmentDto.CreateBaseRequest();
            baseRequest.setParentType(ParentType.BOARD);
            baseRequest.setBoard(board);

            attachmentService.attachmentCreate(baseRequest, mFiles);
        }
        return BoardDto.Response.toDto(board);
    }

    /**
     * 삭제
     * @param id
     */
    @Transactional
    public void boardDelete(String id) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 파일 삭제
        AttachmentDto.DeleteRequest attachmentDeleteRequest = new AttachmentDto.DeleteRequest();
        attachmentDeleteRequest.setParentType(ParentType.BOARD);
        attachmentDeleteRequest.setBoard(board);
        attachmentService.attachmentDeleteAll(attachmentDeleteRequest);

        // 게시판삭제
        boardRepository.deleteById(board.getId());
    }
}