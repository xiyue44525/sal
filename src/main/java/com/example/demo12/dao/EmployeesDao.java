package com.example.demo12.dao;
import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface EmployeesDao extends Mapper <Employees> {
    @Select("SELECT e.*, d.department_name AS departmentName " +
            "FROM employees e " +
            "LEFT JOIN departments d ON e.department_id = d.id")
    List<Employees> getEmployees();

    List<Employees> searchEmployees(@Param("params") Params params);
    @Select("select * from employees where phone = #{phone} limit 1")
    Employees selectByPhone(@Param("phone") String phone);
    @Select("select  e.*, d.department_name AS departmentName\n" +
            "        FROM employees e\n" +
            "        LEFT OUTER JOIN departments d ON e.department_id = d.id  where e.phone = #{phone} and e.password = #{password} limit 1")
    Employees selectByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);
    @Select("select count(*) from employees where department_id = #{departmentId}")
    int getEmployeeCountByDepartmentId(@Param("departmentId") Integer departmentId);
    @Select("select * from employees where id = #{employeeId}")
    Employees selectByEmployeeId(Integer employeeId);
    @Select("select * from attendance where employee_id = #{employeeId}")
    Attendance getAttendanceByEmployeeId(Integer employeeId);
}
