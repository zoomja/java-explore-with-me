package ru.practicum.mainservice.repository;

import ru.practicum.mainservice.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAllBy(Pageable pageable);
}
