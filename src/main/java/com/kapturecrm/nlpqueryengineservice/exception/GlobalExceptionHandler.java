package com.kapturecrm.nlpqueryengineservice.exception;


import com.kapturecrm.nlpqueryengineservice.utils.ConversionUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {InvalidSessionException.class})
    public ResponseEntity<?> handleInvalidSession(InvalidSessionException ex) {
        JSONObject response = ConversionUtils.setResponse("error", (ex.getMessage() == null) ? "Invalid session, UNAUTHORIZED!" : ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        JSONObject response = ConversionUtils.setResponse("failed", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        try {
            JSONObject response = ConversionUtils.setResponse("failed", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
        } catch (Exception e) {
            LOGGER.error("Error in handleCustomException() :", e);
            JSONObject response = ConversionUtils.setResponse("failed", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
