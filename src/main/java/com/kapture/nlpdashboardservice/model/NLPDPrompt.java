package com.kapture.nlpdashboardservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "nlpd_prompt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NLPDPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "prompt")
    private String prompt;

    @Column(name = "cm_id")
    private int cmId;

    @Column(name = "emp_id")
    private int empId;

    @Column(name = "dashboard_type")
    private String dashboardType;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "is_satisfied")
    private Boolean isSatisfied;

    @Column(name = "feedback")
    private String feedback;

}
