package com.kapture.nlpdashboardservice.exception;

import com.kapture.nlpdashboardservice.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class KaptureException extends Exception {
    private ResponseEntity<BaseResponse.ResponseDto> baseResponse;
}
