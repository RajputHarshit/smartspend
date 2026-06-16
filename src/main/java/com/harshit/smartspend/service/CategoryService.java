package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.CategoryRequestDto;
import com.harshit.smartspend.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto requestDto);
    List<CategoryResponseDto> getAllCategories();
}
