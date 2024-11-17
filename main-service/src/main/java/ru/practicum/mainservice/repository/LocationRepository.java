package ru.practicum.mainservice.repository;

import ru.practicum.mainservice.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {
}
