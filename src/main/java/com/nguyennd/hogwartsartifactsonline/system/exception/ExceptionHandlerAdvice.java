package com.nguyennd.hogwartsartifactsonline.system.exception;

import com.nguyennd.hogwartsartifactsonline.system.Result;
import com.nguyennd.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
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

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(ObjectNotFoundException exc) {
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

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception exc) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "username or password is incorrect",
                exc.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(AccountStatusException exc) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "User account is abnormal",
                exc.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidTokenException(InvalidBearerTokenException exc) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "The access token provided is expired, revoke, malformed, or invalid for other reasons",
                exc.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException exc) {
        return new Result(
                false,
                StatusCode.FORBIDDEN,
                "No permission",
                exc.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleInsufficientAuthenticationException(InsufficientAuthenticationException exc) {
        return new Result(
                false,
                StatusCode.UNAUTHORIZED,
                "JWT is required for Authorization",
                exc.getMessage());
    }

    /**
     * Fallback handles any un handled exceptions
     * @param exc
     * @return response of controller
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleOtherException(Exception exc) {
        return new Result(
                false,
                StatusCode.INTERNAL_SERVER_ERROR,
                "A server internal error occurs",
                exc.getMessage());
    }


}
