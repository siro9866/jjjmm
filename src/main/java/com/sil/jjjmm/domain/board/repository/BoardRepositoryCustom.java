package com.sil.jjjmm.domain.board.repository;

import com.sil.jjjmm.domain.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardDto.Response> findBoardAll(BoardDto.Search search, Pageable pageable);
}
