package com.nguyennd.hogwartsartifactsonline.system.exception;

import com.nguyennd.hogwartsartifactsonline.artifact.ArtifactNotFoundException;
import com.nguyennd.hogwartsartifactsonline.system.Result;
import com.nguyennd.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleArtifactNotFoundException(NotFoundException exc) {
        return new Result(false, StatusCode.NOT_FOUND, exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException exc) {
        List<ObjectError> errors = exc.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach(error -> {
            String key =((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key,val);
        });
        return new Result(
                false,
                StatusCode.INVALID_ARGUMENT,
                "provided arguments are invalid, see data for details",
                map
        );
    }

}
