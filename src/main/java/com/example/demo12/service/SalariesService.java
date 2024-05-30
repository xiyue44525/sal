package com.example.demo12.service;


import com.example.demo12.dao.SalariesDao;
import com.example.demo12.entity.*;
import com.example.demo12.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SalariesService {
    @Resource
    private SalariesDao salariesDao;
    @Resource
    private EmployeesService employeeService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private OverTimeService overTimeService;
    @Resource
    private AttendanceService attendanceService;
    @Resource
    private SalariesDao SalariesDao;

    public  Salaries getSalariesByEmployeeId(Integer id) {
        return SalariesDao.getSalariesByEmployeeId(id);
    }

    public PageInfo<Salaries> searchSalaries(Params3 params3) {
        //分页
        PageHelper.startPage(params3.getPageNum(), params3.getPageSize());
        //搜索
        List<Salaries> list =salariesDao.searchSalaries(params3);
        return PageInfo.of(list);
    }

    public void addSalaries(Salaries salaries) {
        if(salaries.getEmployeeId() == null) {
            throw new CustomException("员工id不能为空");
        }
        //查询员工是否存在
        Integer employeeId = salaries.getEmployeeId();
        Employees employee = employeeService.SelectByEmployeeId(employeeId);
        if(employee == null) {
            throw new CustomException("输入的员工id不存在");
        }
        //查重
        Salaries s = salariesDao.selectByEmployeeId(employeeId);
        if(s!= null) {
            throw new CustomException("该员工已存在加班记录");
        }
        Integer id =employee.getDepartmentId();
        if(id == null) {
            throw new CustomException("员工没有部门信息");
        }
        Departments department = departmentService.selectDepartmentById(id);
        //计算扣除金额
        BigDecimal deductionAmount = calculateDeduction(employeeId,department);
        //计算加班工资
        BigDecimal overtimePay = calculateOvertimePay(employeeId,department);
        salaries.setDeduction(deductionAmount);
        salaries.setOvertimePay(overtimePay);
        salariesDao.insertSelective(salaries);
    }

    public void updateSalaries(Salaries salaries) {
        salariesDao.updateSalaries(salaries);
    }
    public void deleteSalaries(Integer id) {
        salariesDao.deleteByPrimaryKey(id);
    }
    //计算扣除金额
    public BigDecimal calculateDeduction(Integer employeeId,Departments department) {
        BigDecimal deductionAmount = department.getDeductionAmount();
        Attendance attendance = attendanceService.getAttendanceByEmployeeId(employeeId);
        if(attendance == null) {
            return new BigDecimal(0);
        }else{
        Integer LeaveDays = attendance.getLeaveDays();
        return deductionAmount.multiply(new BigDecimal(LeaveDays));}
    }
    //计算加班工资
    public BigDecimal calculateOvertimePay(Integer employeeId,Departments department) {
        BigDecimal oneHourPay = department.getOneHourPay();
        OverTime overTime = overTimeService.getOverTimeHours(employeeId);
        if(overTime == null) {
            return new BigDecimal(0);
        }else{
        Integer overTimeHours = overTime.getOvertimeHours();
        return oneHourPay.multiply(new BigDecimal(overTimeHours));}
    }
    public void refreshSalaries() {
        List<Salaries> list = salariesDao.selectAll();
        for (Salaries salaries : list) {
            Integer employeeId = salaries.getEmployeeId();
            Employees employee = employeeService.SelectByEmployeeId(employeeId);
            Integer id = employee.getDepartmentId();
            Departments department = departmentService.selectDepartmentById(id);
            //计算扣除金额
            BigDecimal deductionAmount = calculateDeduction(employeeId, department);
            //计算加班工资
            BigDecimal overtimePay = calculateOvertimePay(employeeId, department);
            salaries.setDeduction(deductionAmount);
            salaries.setOvertimePay(overtimePay);
            salariesDao.refreshSalaries(salaries);
        }
    }

    public void deleteByEmployeeId(Integer id) {
        salariesDao.deleteByEmployeeId(id);
    }
}
