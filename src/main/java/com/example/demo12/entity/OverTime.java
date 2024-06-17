package com.example.demo12.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
@Table(name = "overtime")
public class OverTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Alias("记录id")
    private Integer id;
    @Alias("职工号")
    private Integer employeeId;
    @Alias("加班小时数")
    private  Integer overtimeHours;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Integer overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
}
