package com.example.demo12.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name="position_sal")
public class PositionSal {
    @Alias("id")
    private Integer id;
    @Alias("职称")
    private String position;
    @Alias("基础工资")
    private BigDecimal baseSalary;
    @Alias("补贴")
    private BigDecimal allowance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }
}
