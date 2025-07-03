package com.sil.jjjmm.domain.board.repository;

import com.sil.jjjmm.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, String>, BoardRepositoryCustom {
}