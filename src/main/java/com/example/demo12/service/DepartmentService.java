package com.example.demo12.service;
import com.example.demo12.dao.DepartmentDao;
import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params2;
import com.example.demo12.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentService {
    @Resource
    private DepartmentDao departmentDao;
    @Resource
    private EmployeesService employeesService;

    public List<Departments> findDepartment() {
        return departmentDao.findDepartment();
    }

    public Departments findByDepartmentName(String departmentName) {
        return departmentDao.findByDepartmentName(departmentName);
    }

    public PageInfo<Departments> searchDepartments(Params2 params2) {
        //分页
        PageHelper.startPage(params2.getPageNum(), params2.getPageSize());
        //搜索
        List<Departments> list = departmentDao.searchDepartments(params2);
        return PageInfo.of(list);
    }

    public void addDepartment(Departments departments) {
        //判断部门名称是否为空
        if (departments.getDepartmentName() == null || departments.getDepartmentName().equals("")) {
            throw new CustomException("部门名称不能为空");
        }
        //判断加班金额是否为空
        if (departments.getOneHourPay() == null || departments.getOneHourPay().equals("")) {
            throw new CustomException("加班金额不能为空");
        }
        //判断缺勤扣款是否为空
        if (departments.getDeductionAmount() == null || departments.getDeductionAmount().equals("")) {
            throw new CustomException("缺勤扣款不能为空");
        }
        //重复判断
        Departments department = departmentDao.selectByDepartmentName(departments.getDepartmentName());
        if (department != null) {
            throw new CustomException("该部门已存在");
        }
        departmentDao.insertSelective(departments);
    }

    public void updateDepartment(Departments departments) {
        departmentDao.updateByPrimaryKeySelective(departments);
    }

    public void deleteDepartment(Integer id) {
        List<Employees> list =employeesService.selectEmployeesByDepartmentId(id);
        System.out.println(list);
        if(!list.isEmpty())
        {
            throw new CustomException("部门存在员工，不可删除");
        }else{
            departmentDao.deleteByPrimaryKey(id);
        }
    }

    public int getDepartmentSizeForCheck(Integer departmentId) {
        return departmentDao.getDepartmentSizeForCheck(departmentId);
    }

    public Departments selectDepartmentById(Integer id) {
        return departmentDao.selectByPrimaryKey(id);
    }
}
