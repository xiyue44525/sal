package com.example.demo12.service;

import com.example.demo12.dao.AttendanceDao;
import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params3;
import com.example.demo12.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class AttendanceService {
    @Resource
    private AttendanceDao attendanceDao;
    @Resource
    private EmployeesService employeeService;

    public  Attendance getAttendanceById(Integer id) {
        return  attendanceDao.getAttendanceById(id);
    }

    public  Attendance getAttendanceByEmployeeId(Integer employeeId) {
        return attendanceDao.selectByEmployeeId(employeeId);
    }

    public PageInfo<Attendance> searchAttendance(Params3 params3) {
        //分页
        PageHelper.startPage(params3.getPageNum(), params3.getPageSize());
        //搜索
        List<Attendance> list = attendanceDao.searchAttendance(params3);
        return PageInfo.of(list);
    }

    public void addAttendance(Attendance attendance) {
        checkAttendance(attendance);
        attendanceDao.insertSelective(attendance);
    }
    public void updateAttendance(Attendance attendance) {
        attendanceDao.updateByPrimaryKeySelective(attendance);
    }

    public void deleteAttendance(Integer id) {
        attendanceDao.deleteByPrimaryKey(id);
    }
    public void checkAttendance(Attendance attendance) {
        //查询员工是否存在
        Integer employeeId = attendance.getEmployeeId();
        Employees employee = employeeService.SelectByEmployeeId(employeeId);
        if(employee == null) {
            throw new CustomException("输入的员工id不存在");
        }
        //查重
        Attendance a = attendanceDao.selectByEmployeeId(employeeId);
        if(a!= null) {
            throw new CustomException("该员工已存在考勤记录");
        }
    }

    public void deleteByEmployeeId(Integer id) {
        attendanceDao.deleteByEmployeeId(id);
    }

    public List<Attendance> getAllAttendance() {
        return  attendanceDao.getAllAttendance();
    }
}
