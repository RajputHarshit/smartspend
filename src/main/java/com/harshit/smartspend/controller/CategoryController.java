package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.CategoryRequestDto;
import com.harshit.smartspend.dto.CategoryResponseDto;
import com.harshit.smartspend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.catalog.CatalogResolver;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto){
        CategoryResponseDto categoryResponseDto=categoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>(categoryResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }
}
