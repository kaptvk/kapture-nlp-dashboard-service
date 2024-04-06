package com.kapturecrm.nlpdashboardservice.exception;

import com.kapturecrm.nlpdashboardservice.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class KaptureException extends Exception {
    private ResponseEntity<BaseResponse.ResponseDto> baseResponse;
}
