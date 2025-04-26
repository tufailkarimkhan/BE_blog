package com.springboot.blog.service.impl;

import com.springboot.blog.DTO.CommentsDTO;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Comment;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.CommentsRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentsRepository commentsRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(CommentsRepository commentsRepository, PostRepository postRepository,ModelMapper mapper) {
        this.commentsRepository = commentsRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentsDTO createComment(Long postId, CommentsDTO commentsDTO) {
        Comment comment = mapToEntity(commentsDTO);
        Post post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);
       Comment response =  commentsRepository.save(comment);

        return mapToDto(response);
    }

    @Override
    public List<CommentsDTO> findAllCommentsByPostId(Long postId){
        List<Comment> comments = commentsRepository.findAllCommentsByPostId(postId);
            return comments.stream().map((comment)-> mapToDto(comment)).collect(Collectors.toList()) ;

    }

    @Override
    public CommentsDTO findById(Long commentId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentsRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment doesn't belong to Post");
        }
        CommentsDTO commentsDTO = mapToDto(comment);
        return commentsDTO ;
    }

    @Override
    public  CommentsDTO updateComment(Long postId, Long commentId, CommentsDTO commentsDTO){
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentsRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment doesn't belong to Post");
        }
        comment.setName(commentsDTO.getName());
        comment.setBody(commentsDTO.getBody());
        comment.setEmail(commentsDTO.getEmail());

        return mapToDto(commentsRepository.save(comment));

    }

    @Override
    public String deleteCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentsRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment not belongs to the post.");
        }
        commentsRepository.deleteById(commentId);
        return "Comment with: "+comment.getBody()+" got Deleted successfully";
    }

    private CommentsDTO mapToDto(Comment comment){
          CommentsDTO commentsDTO = mapper.map(comment,CommentsDTO.class);
//        CommentsDTO commentsDTO = new CommentsDTO();
//        commentsDTO.setId(comment.getId());
//        commentsDTO.setBody(comment.getBody());
//        commentsDTO.setEmail(comment.getEmail());
//        commentsDTO.setName(comment.getName());
        return  commentsDTO;
    }
    private Comment mapToEntity(CommentsDTO commentsDTO){
        Comment comment = mapper.map(commentsDTO,Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentsDTO.getId());
//        comment.setBody(commentsDTO.getBody());
//        comment.setEmail(commentsDTO.getEmail());
//        comment.setName(commentsDTO.getName());

        return comment;
    }
}
