package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        try {
            Category category = categoryMapper.toCategory(newCategoryDto);
            categoryRepository.save(category);
            return categoryMapper.toCategoryDto(category);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public void deleteCategory(Integer categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public CategoryDto updateCategory(Integer categoryId, NewCategoryDto newCategoryDto) {
        try {
            Category category = findCategoryById(categoryId);
            category.setName(newCategoryDto.getName());
            categoryRepository.save(category);
            return categoryMapper.toCategoryDto(category);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
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
