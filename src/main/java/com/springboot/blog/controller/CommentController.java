package com.springboot.blog.controller;

import com.springboot.blog.DTO.CommentsDTO;
import com.springboot.blog.model.Comment;
import com.springboot.blog.service.impl.CommentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentsDTO>createComment(@PathVariable(name = "postId") Long postId,
                                                    @Validated @RequestBody CommentsDTO commentsDTO){
        return new ResponseEntity<>(commentService.createComment(postId,commentsDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentsDTO>> findAllCommentsByPostId(@PathVariable(name = "postId") Long postId){
        return new ResponseEntity<>(commentService.findAllCommentsByPostId(postId),HttpStatus.FOUND);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/")
    public ResponseEntity<CommentsDTO> getCommentById(@PathVariable(name = "postId") Long postId,
                                                      @PathVariable(name = "commentId") Long commentId){
        return new ResponseEntity<>(commentService.findById(commentId,postId),HttpStatus.FOUND);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}/")
    public ResponseEntity<CommentsDTO> updateComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
          @Validated @RequestBody CommentsDTO commentsDTO
    ){
        return new ResponseEntity<>(commentService.updateComment(postId,commentId,commentsDTO),HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}/")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId
    ){
        return new ResponseEntity<>(commentService.deleteCommentById(postId,commentId),HttpStatus.ACCEPTED);
    }
}
