package com.springboot.blog.DTO;

import com.springboot.blog.model.Post;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentsDTO {

    private Long id;
    @NotEmpty(message = "Name shouldn't be null or Empty")
    private String name;

    @NotEmpty(message = "Email should not be null or Empty")
    @Email
    private String email;

    @NotEmpty(message = "Body should not be empty or null")
    @Size(min = 10,message = "Comment body must be minimum 10 characters.")
    private String body;

}
