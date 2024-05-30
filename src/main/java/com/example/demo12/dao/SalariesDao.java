package com.example.demo12.dao;

import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params3;
import com.example.demo12.entity.Salaries;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SalariesDao extends Mapper<Salaries> {
    @Select("SELECT * FROM salaries WHERE employee_id = #{id}")
     Salaries getSalariesByEmployeeId(Integer id);

    @Select("SELECT * FROM salaries WHERE employee_id = #{employeeId}")
   Salaries selectByEmployeeId(Integer employeeId);

   List<Salaries> searchSalaries(@Param("params3") Params3 params3);

   @Update("UPDATE salaries SET  base_salary = #{baseSalary}, allowance = #{allowance} WHERE id = #{id}")
   void updateSalaries(Salaries salaries);
    @Update("UPDATE salaries SET  deduction=#{deduction},overtime_pay =#{overtimePay} WHERE id = #{id}")
    void refreshSalaries(Salaries salaries);
    @Delete("DELETE FROM salaries WHERE employee_id = #{id}")
    void deleteByEmployeeId(Integer id);
}
