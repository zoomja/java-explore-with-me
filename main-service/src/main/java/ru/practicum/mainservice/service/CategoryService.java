package ru.practicum.mainservice.service;

import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import ru.practicum.mainservice.model.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.repository.CategoryRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(category);
        return categoryMapper.toCategoryDto(category);
    }

    public void deleteCategory(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public CategoryDto updateCategory(Integer categoryId, NewCategoryDto newCategoryDto) {
        Category category = findCategoryById(categoryId);
        category.setName(newCategoryDto.getName());
        categoryRepository.save(category);
        return categoryMapper.toCategoryDto(category);
    }

    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        return categoryMapper.toCategoryDtoList(categoryRepository.findAllBy(PageRequest.of(from, size)).getContent());
    }

    public CategoryDto getCategoryById(Integer categoryId) {
        Category category = findCategoryById(categoryId);
        return categoryMapper.toCategoryDto(category);
    }

    public Category findCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
    }
}
