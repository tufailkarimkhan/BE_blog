package com.springboot.blog.controller;

import com.springboot.blog.DTO.PostDto;
import com.springboot.blog.DTO.PostResponse;
import com.springboot.blog.service.impl.PostServiceImpl;
import com.springboot.blog.util.PaginationDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private PostServiceImpl postServiceImpl;

    public PostController(PostServiceImpl postServiceImpl) {
        this.postServiceImpl = postServiceImpl;
    }

    @PostMapping("")
    public ResponseEntity<PostDto> createPost(@Validated @RequestBody PostDto postDto){
        return new ResponseEntity<>(postServiceImpl.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<PostResponse> getAllPosts(

            @Validated
            @RequestParam(name = "pageNo", defaultValue = PaginationDetails.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = PaginationDetails.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = PaginationDetails.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = PaginationDetails.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(postServiceImpl.getAllPosts(pageNo,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById( @PathVariable(name = "id") Long postId){
        return new ResponseEntity<>(postServiceImpl.getPostById(postId), HttpStatus.OK);
   }

   @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@Validated @RequestBody() PostDto postDto, @PathVariable(name = "id") Long postId){
        return new ResponseEntity<>(postServiceImpl.updatePostById(postDto,postId),HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable(name = "id") Long id){
        postServiceImpl.deletePostById(id);
        return new ResponseEntity<>("Post got deleted successfully...", HttpStatus.OK);
   }
}
