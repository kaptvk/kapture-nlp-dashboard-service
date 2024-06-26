package com.kapture.nlpdashboardservice.utility;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class BaseResponse {

    @Data
    public static class ResponseDto {
        private boolean status;
        private Object data;
        private String message;
        private Calendar timestamp = Calendar.getInstance();
    }


    public static ResponseEntity<?> success() {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage("Success");
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> success(String msg) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(msg);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> success(Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setData(data);
        responseDto.setMessage("Success");
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> success(Object data, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setData(data);
        responseDto.setMessage(message);
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<?> error() {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(false);
        responseDto.setMessage("Something went wrong!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> error(Exception e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Something went wrong!");
        responseDto.setData(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> error(HttpStatus httpStatus, String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return ResponseEntity.status(httpStatus).body(responseDto);
    }

}
