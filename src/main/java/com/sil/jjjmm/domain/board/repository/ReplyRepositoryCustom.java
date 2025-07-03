package com.sil.jjjmm.domain.board.repository;

import com.sil.jjjmm.domain.board.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepositoryCustom {
    /**
     * 댓글글리스트
     */
    Page<ReplyDto.Response> findReplyAll(String boardId, Pageable pageable);
}
