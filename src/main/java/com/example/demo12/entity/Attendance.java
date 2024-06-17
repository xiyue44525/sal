package com.example.demo12.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Alias("记录id")
    private Integer id;
    @Alias("职工号")
    private Integer employeeId;
    @Alias("缺勤天数")
    private Integer leaveDays;

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

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }
}
