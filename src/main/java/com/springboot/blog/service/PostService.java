package com.springboot.blog.service;

import com.springboot.blog.DTO.PostDto;
import com.springboot.blog.DTO.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);

    PostDto getPostById(Long id);

    PostDto updatePostById(PostDto postDto, Long id);

    void deletePostById(Long id);
}
