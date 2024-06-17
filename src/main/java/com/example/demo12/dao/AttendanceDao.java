package com.example.demo12.dao;

import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params3;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface AttendanceDao extends Mapper<Attendance> {
    @Select("select * FROM attendance  WHERE employee_id = #{id}")
     Attendance getAttendanceById(Integer id);

    List<Attendance> searchAttendance(@Param("params3") Params3 params3);
    @Select("SELECT * FROM attendance WHERE employee_id = #{employeeId}")
    Attendance selectByEmployeeId(Integer employeeId);
    @Delete("DELETE FROM attendance WHERE employee_id = #{id}")
    void deleteByEmployeeId(Integer id);
}
