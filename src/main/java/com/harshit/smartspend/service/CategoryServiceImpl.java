package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.CategoryRequestDto;
import com.harshit.smartspend.dto.CategoryResponseDto;
import com.harshit.smartspend.entity.Category;
import com.harshit.smartspend.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new RuntimeException("Category already exists: " + requestDto.getName());
        }

        Category category = Category.builder()
                .name(requestDto.getName())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Override
    @Cacheable(value = "categories")
    public List<CategoryResponseDto> getAllCategories() {
        System.out.println("DB hit");
        return categoryRepository.findAll().stream()
                .map(category -> CategoryResponseDto.builder()
                        .id(category.getId())
                        .name(category.getName()).build()).collect(Collectors.toList());
    }
}
