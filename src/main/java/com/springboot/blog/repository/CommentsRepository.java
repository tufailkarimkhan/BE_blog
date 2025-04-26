package com.springboot.blog.repository;

import com.springboot.blog.DTO.CommentsDTO;
import com.springboot.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
public List<Comment> findAllCommentsByPostId(Long postId);
}
