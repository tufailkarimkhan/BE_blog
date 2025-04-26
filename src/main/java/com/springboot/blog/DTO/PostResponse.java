package com.springboot.blog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> content;
    private int pageSize;
    private int pageNo;
    private long totalElement;
    private int totalPage;
    private boolean last;

}
