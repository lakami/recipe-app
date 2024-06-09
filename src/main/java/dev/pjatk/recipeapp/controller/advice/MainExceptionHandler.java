package dev.pjatk.recipeapp.controller.advice;

import dev.pjatk.recipeapp.exception.ForbiddenModificationException;
import dev.pjatk.recipeapp.exception.NoUserFoundException;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.exception.TooWeakPasswordException;
import dev.pjatk.recipeapp.service.ImageService;
import dev.pjatk.recipeapp.util.Loggable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MainExceptionHandler implements Loggable {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(ResourceNotFoundException e) {
        log().warn("Resource not found: {}", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TooWeakPasswordException.class)
    public void handleTooWeakPasswordException(TooWeakPasswordException e) {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoUserFoundException.class)
    public void handleNoUserFoundException(NoUserFoundException e) {
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ImageService.CannotDeleteFileException.class)
    public void handleCannotDeleteFileException(ImageService.CannotDeleteFileException e) {
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenModificationException.class)
    public void handleForbiddenModificationException(ForbiddenModificationException e) {
    }
}
