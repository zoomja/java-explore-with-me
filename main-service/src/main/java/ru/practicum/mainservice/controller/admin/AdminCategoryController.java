package ru.practicum.mainservice.controller.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.service.CategoryService;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId, @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.updateCategory(catId, newCategoryDto);
    }
}
