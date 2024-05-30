package com.example.demo12.service;
import com.example.demo12.common.JwtInterceptor;
import com.example.demo12.common.JwtTokenUtils;
import com.example.demo12.dao.DepartmentDao;
import com.example.demo12.entity.*;
import com.example.demo12.exception.CustomException;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.demo12.dao.EmployeesDao;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import com.github.pagehelper.PageHelper;
@Service
public class EmployeesService {

    @Resource
    private EmployeesDao EmployeesDao;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private OverTimeService overTimeService;
    @Resource
    private AttendanceService attendanceService;
    @Resource
    private SalariesService SalariesService;

    public List<Employees> getEmployees() {
        return EmployeesDao.getEmployees();
    }

    public PageInfo<Employees> searchEmployees(Params params) {
        //分页
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        //搜索
        List<Employees> list = EmployeesDao.searchEmployees(params);
        return PageInfo.of(list);
    }

    public void addEmployees(Employees Employees) {
        //判空
        if (Employees.getPhone() == null || Employees.getPhone().equals("")) {
            throw new CustomException("电话号码不能为空");
        }
        if(Employees.getPhone().length()> 11){
            throw new CustomException("电话号码长度不能超过11位");
        }
        //重复判断
        Employees user = EmployeesDao.selectByPhone(Employees.getPhone());
        if (user != null) {
            throw new CustomException("该手机号已注册");
        }
        //密码默认值
        if (Employees.getPassword() == null || Employees.getPassword().equals("")) {
            Employees.setPassword("123456");
        }
        if (Employees.getDepartmentName() != null && !Employees.getDepartmentName().equals(""))
        {
                departmentIdCheck(Employees);
            if (!departmentSizeCheck(Employees)) {
                throw new CustomException("部门下员工数量已达到上限");
            }
        }
        EmployeesDao.insertSelective(Employees);
    }

    public void updateEmployees(Employees Employees) {
        if(Employees.getDepartmentName() == null )
        {
            throw new CustomException("部门不能为空");
        }
        if(departmentIdCheck(Employees))
        {
            if (!departmentSizeCheck(Employees)) {
                throw new CustomException("部门下员工数量已达到上限");
            }EmployeesDao.updateByPrimaryKeySelective(Employees);
        }
        EmployeesDao.updateByPrimaryKeySelective(Employees);
    }

    public void deleteEmployees(Integer id) {
        Attendance attendance = attendanceService.getAttendanceByEmployeeId(id);
        if (attendance != null) {
            attendanceService.deleteByEmployeeId(id);
        }
        OverTime overTime = overTimeService.getOverTimeByEmployeeId(id);
        if (overTime != null) {
            overTimeService.deleteByEmployeeId(id);
        }
        Salaries salaries = SalariesService.getSalariesByEmployeeId(id);
        if (salaries != null) {
            SalariesService.deleteByEmployeeId(id);
        }
        EmployeesDao.deleteByPrimaryKey(id);
    }

    public Employees login(Employees Employees) {
        //判空
        if (Employees.getPhone() == null || Employees.getPhone().equals("")) {
            throw new CustomException("号码不能为空");
        }
        if (Employees.getPassword() == null || Employees.getPassword().equals("")) {
            throw new CustomException("密码不能为空");
        }
        Employees user = EmployeesDao.selectByPhoneAndPassword(Employees.getPhone(), Employees.getPassword());
        if (user == null) {
            throw new CustomException("手机号或密码错误");
        }
        //生成token，并打包返回
        String token = JwtTokenUtils.genToken(user.getId().toString(), user.getPassword());
        user.setToken(token);
        return user;
    }

    public Employees FindById(Integer id) {
        return EmployeesDao.selectByPrimaryKey(id);
    }

    public boolean departmentIdCheck(Employees Employees) {
        // 使用departmentService查询对应的Departments对象
        Departments department = departmentService.findByDepartmentName(Employees.getDepartmentName());
        // 检查是否找到了对应的Departments对象
        if (department != null) {
            if (Objects.equals(department.getId(), Employees.getDepartmentId())) {
                // 如果departmentId和departmentName都匹配，则不需要更新departmentId
                return false;
            } else {
                // 如果找到了，设置Employees的departmentId
                Employees.setDepartmentId(department.getId());
                return true;
            }
        }
        // 如果没找到，抛出异常
        throw new CustomException("部门不存在");
    }

    public boolean departmentSizeCheck(Employees Employees) {
        Integer departmentId = Employees.getDepartmentId();//获取部门id
        int employeeCount = EmployeesDao.getEmployeeCountByDepartmentId(departmentId);//根据部门id查询员工数量
        //判断部门下员工数量是否超过限制
        if ( employeeCount < departmentService.getDepartmentSizeForCheck(departmentId)) {
            return true;
        } else {
            return false;
        }
    }

    public Employees SelectByEmployeeId(Integer employeeId) {
       return EmployeesDao.selectByEmployeeId(employeeId);
    }

    public Attendance getAttendanceByEmployeeId(Integer employeeId) {
        return EmployeesDao.getAttendanceByEmployeeId(employeeId);
    }

    public void updateUser(Employees employees) {
        EmployeesDao.updateByPrimaryKeySelective(employees);
    }
}
