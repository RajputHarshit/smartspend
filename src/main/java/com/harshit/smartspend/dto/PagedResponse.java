package com.harshit.smartspend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> content= new ArrayList<T>();
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean last;

}
