package ru.tikskit.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.model.Holiday;
import ru.tikskit.repositories.HolidayRepository;

import java.util.List;

@RestController
@ExposesResourceFor(Holiday.class)
@AllArgsConstructor
public class HolidayController {

    private final HolidayRepository holidayRepository;
    private final EntityLinks entityLinks;

    @GetMapping("/holiday")
    public List<Holiday> getHolidays() {
        return holidayRepository.findAll();
    }

    @GetMapping("/holiday/{id}")
    public Holiday holiday(@PathVariable("id") Holiday holiday) {
        return holiday;
    }
}
