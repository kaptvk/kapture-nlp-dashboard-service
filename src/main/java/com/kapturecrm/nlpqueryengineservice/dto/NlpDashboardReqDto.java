package com.kapturecrm.nlpqueryengineservice.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NlpDashboardReqDto {
    @NotNull
    @NotEmpty
    private String prompt;
}
