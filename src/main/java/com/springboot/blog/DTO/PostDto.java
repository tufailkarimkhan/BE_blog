package com.springboot.blog.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private  Long id;
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters.")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post Description should have at least 10 characters.")
    private String description;

    @NotEmpty
    private String content;

    private Set<CommentsDTO> comments;
}
