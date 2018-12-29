package com.future.assist.model.response_model;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Boolean last;
    private Integer numberOfElements;
    private Boolean first;
    private Integer size;
    private Integer number;

}
