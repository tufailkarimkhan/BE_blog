package com.springboot.blog.service.impl;

import com.springboot.blog.DTO.PostDto;
import com.springboot.blog.DTO.PostResponse;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto){
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        return mapToDTO(newPost);
    }
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> listsOfPost = posts.getContent();
        List<PostDto> content = listsOfPost.stream().map(record -> mapToDTO(record)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
                postResponse.setContent(content);
                postResponse.setPageSize(postResponse.getPageSize());
                postResponse.setPageNo(posts.getNumber());
                postResponse.setTotalElement(posts.getTotalElements());
                postResponse.setTotalPage(posts.getTotalPages());
                postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id){
        Post postData = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToDTO(postData);
    }

    @Override
    public  PostDto updatePostById(PostDto postDto,Long id){
        Post postData = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postData.setContent(postDto.getContent());
        postData.setDescription(postDto.getDescription());
        postData.setTitle(postDto.getTitle());
        Post updated = postRepository.save(postData);
        return mapToDTO(postData);
    }

    @Override
    public void deletePostById(Long id) {
        Post postData = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.deleteById(id);
    }

    private Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto,Post.class);

//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
//        post.setTitle(postDto.getTitle());
        return post;
    }
//    private

    private PostDto mapToDTO(Post post){
        /*convert post entity to DTO*/
            PostDto postResponse = modelMapper.map(post,PostDto.class);
//        PostDto postResponse = new PostDto();
//        postResponse.setId(post.getId());
//        postResponse.setContent(post.getContent());
//        postResponse.setDescription(post.getDescription());
//        postResponse.setTitle(post.getTitle());
        return postResponse;
    }
}
