package com.example.demo12.dao;

import com.example.demo12.entity.Employees;
import com.example.demo12.entity.OverTime;
import com.example.demo12.entity.Params3;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface OverTimeDao extends Mapper<OverTime> {

    List<OverTime> searchOverTime(@Param("params3") Params3 params3);
    @Select("SELECT * FROM overtime WHERE employee_id = #{employeeId}")
    OverTime selectByEmployeeId(Integer employeeId);
    @Select("SELECT * FROM overtime WHERE employee_id = #{employeeId}")
    OverTime selectOverTimeHours(Integer employeeId);
    @Delete("DELETE FROM overtime WHERE employee_id = #{id}")
    void deleteByEmployeeId(Integer id);
    @Select("SELECT * FROM overtime WHERE employee_id = #{id} ")
    OverTime getOverTimeById(Integer id);
    @Select("SELECT * FROM overtime")
    List<OverTime> getAllOverTime();
}
