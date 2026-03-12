package com.wajba.restaurant.Exceptions;

import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> HandleValidationsErrorsExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (first, second) -> first + "; " + second
                ));

        return errors;
    }

    @ExceptionHandler(FoodAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String HandleFoodAlreadyExistsException(FoodAlreadyExistsException ex)
    {
        return "Message is :- " + ex.getMessage();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleIllegalArgumentExceptions(IllegalArgumentException ex)
    {
        return "Message is :- " + ex.getMessage();
    }
    @ExceptionHandler(FoodNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleFoodNotFoundException(FoodNotFoundException ex)
    {
        return "Message is :- " + ex.getMessage();
    }

    @ExceptionHandler({InvalidContentTypeException.class, MissingServletRequestPartException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleInvalidContentTypeException()
    {
        return " Message is :- The food addition has been failed, you have to provide the food details.";
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex)
    {
        return "Message is :- " + ex.getMessage();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleCategoryNotFoundException(CategoryNotFoundException ex)
    {
        return "Message is :- " + ex.getMessage();
    }

    @ExceptionHandler(ModifierNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleModifierNotFoundException(ModifierNotFoundException ex)
    {
        return "Message is :- " + ex.getMessage();
    }

    @ExceptionHandler(ModifierAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String HandleModifierAlreadyExistsException(ModifierAlreadyExistsException ex)
    {
        return "Message is :- " + ex.getMessage();
    }
}


