package com.campfire.api.utils;

import com.campfire.api.dto.ErrorResponseDTO;
import com.campfire.api.dto.SuccessResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApiResponseBuilder {

    public static ResponseEntity<Object> buildSuccessResponse(String message, Object response) {
        SuccessResponseDTO successResponse = new SuccessResponseDTO();

        successResponse.setResponse(response);
        successResponse.setMessage(message);

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public static ResponseEntity<Object> buildSuccessResponse(String message) {
        SuccessResponseDTO successResponse = new SuccessResponseDTO();

        successResponse.setMessage(message);

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public static ResponseEntity<Object> buildErrorResponse(String errorMessage) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
