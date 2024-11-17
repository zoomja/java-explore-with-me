package ru.practicum.mainservice.repository;

import ru.practicum.mainservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByIdIn(Collection<Integer> id, Pageable pageable);
}
