package com.sil.jjjmm.domain.board.entity;

import com.sil.jjjmm.domain.attachment.entity.Attachment;
import com.sil.jjjmm.global.entity.Base;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시판
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "BOARD")
public class Board extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ID", nullable = false, length = 36)
    private String id;

    @Comment("게시판유형")
    @Column(name="BOARD_TYPE", nullable = false, length = 20)
    private String boardType;

    @Comment("게시글제목")
    @Column(name="TITLE", nullable = false, length = 100)
    private String title;

    @Comment("게시글내용")
    @Column(name="CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Comment("활성화여부")
    @Builder.Default
    @Column(name="ENABLED")
    private boolean enabled = true; // 활성화여부

    @OneToMany(mappedBy = "board")
    private List<Reply> replys = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Attachment> attachments = new ArrayList<>();
}
