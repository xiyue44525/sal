package com.example.demo12.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "departments")
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Alias("部门id")
    private Integer id;
    @Alias("部门名称")
    private String departmentName;
    @Alias("部门人数")
    private Integer departmentSize;
    @Alias("联系电话")
    private String contactNumber;
    @Alias("邮箱")
    private String email;
    @Alias("缺勤一天扣款")
    private BigDecimal deductionAmount;
    @Alias("加班一天费用")
    private BigDecimal oneHourPay;
    private Timestamp createTime;

    private Timestamp updateTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentSize() {
        return departmentSize;
    }

    public void setDepartmentSize(Integer departmentSize) {
        this.departmentSize = departmentSize;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public BigDecimal getOneHourPay() {
        return oneHourPay;
    }

    public void setOneHourPay(BigDecimal oneHourPay) {
        this.oneHourPay = oneHourPay;
    }
}
