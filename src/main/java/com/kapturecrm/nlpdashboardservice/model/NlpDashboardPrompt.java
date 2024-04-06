package com.kapturecrm.nlpdashboardservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "nlp_dashboard_prompt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NlpDashboardPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String prompt;
    private int cmId;
    private int empId;
    private String dashboardType;
    private Timestamp createTime;
    private Boolean isSatisfied;
    private String suggestion;

}
