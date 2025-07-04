package com.sil.jjjmm.domain.board.dto;

import com.sil.jjjmm.domain.board.entity.Board;
import com.sil.jjjmm.domain.board.entity.Reply;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ReplyDto {
    /**
     * 등록
     */
    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String boardId;	// 게시글아이디

        @Schema(description = "댓글")
        @NotBlank
        private String commentary;    // 댓글

        @Hidden // 파라미터에 포함시키지 않음
        private Board board;     // 게시판아이디

        public Reply toEntity() {
            return Reply.builder()
                    .board(board)
                    .commentary(commentary)
                    .build();
        }
    }

    /**
     * 수정
     */
    @Getter
    @Setter
    public static class ModifyRequest {

        @Schema(description = "댓글")
        @NotBlank
        private String commentary;    // 댓글

        public void modifyReply(Reply comment) {
            comment.setCommentary(this.commentary);
        }
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String commentary;   // 댓글
        private boolean enabled; // 활성화여부

        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static Response toDto(Reply comment) {
            return Response.builder()
                    .id(comment.getId())
                    .commentary(comment.getCommentary())
                    .enabled(comment.isEnabled())
                    .createdBy(comment.getCreatedBy())
                    .createdAt(comment.getCreatedAt())
                    .modifiedBy(comment.getModifiedBy())
                    .modifiedAt(comment.getModifiedAt())
                    .build();
        }
    }

    /**
     * 조회조건
     */
    @Getter
    @Setter
    public static class Search {

        @Schema(description = "게시판 아이디", defaultValue = "685fe124733ffe0de4c3ac53")
        @NotBlank
        private String boardId;     // 게시판아이디

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
        private int page = 0;

        @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
        private int size = 10;

        @Schema(description = "정렬 기준 필드", example = "createdAt", defaultValue = "createdAt")
        private String sortBy = "createdAt";

        @Schema(description = "내림차순 여부", example = "true", defaultValue = "true")
        private boolean desc = true;
    }
}
