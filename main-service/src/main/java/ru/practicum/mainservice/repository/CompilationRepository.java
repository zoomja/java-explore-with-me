package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Compilation;



public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    Page<Compilation> findCompilationsByPinnedTrue(Pageable pageable);
}
