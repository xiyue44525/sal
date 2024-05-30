package com.example.demo12.controller;

import com.example.demo12.common.JwtInterceptor;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Params2;
import com.example.demo12.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @RequestMapping("/findDepartment")
    public Result findDepartment() {
        List<Departments> departments = departmentService.findDepartment();
        return Result.success(departments);
    }

    @GetMapping("/search")
    public Result searchEmployees(Params2 params2) {
        PageInfo<Departments> info = departmentService.searchDepartments(params2);
        //log.info("查询结果：{}", info);
        return Result.success(info);
    }

    @PostMapping("/add")
    public Result addDepartment(@RequestBody Departments Departments) {
        if(Departments.getId() == null){
            departmentService.addDepartment(Departments);}
        else{
            departmentService.updateDepartment(Departments);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }
}
