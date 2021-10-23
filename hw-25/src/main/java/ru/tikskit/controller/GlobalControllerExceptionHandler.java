package ru.tikskit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    public ModelAndView handleAuthorNotFoundException(AuthorNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("entitynotfounderror");
        modelAndView.addObject("title", "Автор не найден!");
        modelAndView.addObject("text", "Автор не найден!");
        return modelAndView;
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ModelAndView handleAuthorNotFoundException(GenreNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("entitynotfounderror");
        modelAndView.addObject("title", "Жанр не найден!");
        modelAndView.addObject("text", "Жанр не найден!");
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("title", "Ошибка!");
        modelAndView.addObject("text", String.format("Произошла ошибка: %s", e.getMessage()));
        return modelAndView;
    }

    @ExceptionHandler(BookCrudException.class)
    public ModelAndView handleBookCrudException(BookCrudException e) {
        ModelAndView modelAndView = new ModelAndView("bookerror");
        modelAndView.addObject("title", "Ошибка при работе с книгой!");
        modelAndView.addObject("text", String.format("Произошла ошибка: %s", e.getMessage()));
        return modelAndView;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied() {
        return ResponseEntity.of(Optional.of("Нет прав для доступа к ресурсу"));
    }


}
