package com.example.demo12.dao;

import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Params2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DepartmentDao extends Mapper<Departments> {
    @Select("select * from departments where department_name = #{departmentName}")
    Departments selectByDepartmentName(@Param("departmentName") String departmentName);

    @Select("select *  from departments ")
    List<Departments> findDepartment();

    @Select("select * from departments where department_name = #{departmentName}")
    Departments findByDepartmentName(String departmentName);

    List<Departments> searchDepartments(@Param("params2") Params2 params2);

@Select("select department_size from departments where id = #{departmentId}")
    Integer getDepartmentSizeForCheck(@Param("departmentId") Integer departmentId);
}