package com.lishui.tourism.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.result.Result;
import com.lishui.tourism.config.security.RequireRole;
import com.lishui.tourism.entity.Comment;
import com.lishui.tourism.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 评论控制器
 */
@Tag(name = "评论管理")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @Operation(summary = "发表评论")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping
    public Result<Long> createComment(@Valid @RequestBody CommentRequest request) {
        Long id = commentService.createComment(
                request.getSpotId(),
                request.getContent(),
                request.getRating(),
                request.getImages(),
                request.getParentId()
        );
        return Result.success(id);
    }
    
    @Operation(summary = "获取景点评论列表")
    @GetMapping("/spot/{spotId}")
    public Result<IPage<Comment>> getSpotComments(
            @PathVariable Long spotId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Comment> comments = commentService.getSpotComments(spotId, page, size);
        return Result.success(comments);
    }
    
    @Operation(summary = "获取评论回复")
    @GetMapping("/{commentId}/replies")
    public Result<List<Comment>> getCommentReplies(@PathVariable Long commentId) {
        List<Comment> replies = commentService.getCommentReplies(commentId);
        return Result.success(replies);
    }
    
    @Operation(summary = "删除评论")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @DeleteMapping("/{commentId}")
    public Result<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return Result.success();
    }
    
    @Operation(summary = "隐藏/显示评论（管理员）")
    @RequireRole(Constants.Role.ADMIN)
    @PutMapping("/{commentId}/status")
    public Result<Void> toggleCommentStatus(
            @PathVariable Long commentId,
            @RequestParam Integer status) {
        commentService.toggleCommentStatus(commentId, status);
        return Result.success();
    }
    
    @Data
    public static class CommentRequest {
        @NotNull(message = "景点ID不能为空")
        private Long spotId;
        
        @NotBlank(message = "评论内容不能为空")
        private String content;
        
        @Min(value = 1, message = "评分最小为1")
        @Max(value = 5, message = "评分最大为5")
        private Integer rating;
        
        private String images;
        
        private Long parentId;
    }
}

