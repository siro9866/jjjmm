package com.sil.jjjmm.domain.attachment.repository;

import com.sil.jjjmm.domain.attachment.entity.Attachment;
import com.sil.jjjmm.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {

    List<Attachment> findByParentType(String parentType);

    List<Attachment> findByBoard(Board board);
}
