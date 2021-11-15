package ru.tikskit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
