package com.example.demo12.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo12.common.JwtInterceptor;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params2;
import com.example.demo12.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws IOException {
        List<Departments> all = departmentService.findDepartment();
        if (CollectionUtil.isEmpty(all)) {
            throw new ClassCastException("没有数据");
        }
        List<String> headers = Arrays.asList("部门id", "部门名称", "部门人数", "联系电话", "邮箱", "缺勤一天扣款", "加班一天费用");
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        for (Departments departments : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String header : headers) {
                switch (header) {
                    case "部门id":
                        map.put(header, departments.getId());
                        break;
                    case "部门名称":
                        map.put(header,departments.getDepartmentName());
                        break;
                    case "部门人数":
                        map.put(header, departments.getDepartmentSize());
                        break;
                    case "联系电话":
                        map.put(header, departments.getContactNumber());
                        break;
                    case "邮箱":
                        map.put(header, departments.getEmail());
                        break;
                    case "缺勤一天扣款":
                        map.put(header, departments.getDeductionAmount());
                        break;
                    case "加班一天费用":
                        map.put(header, departments.getOneHourPay());
                        break;
                }
            }
            list.add(map);
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=department.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
        return Result.success();
    }
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<Departments> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Departments.class);
        if(!CollectionUtil.isEmpty(infoList)) {
            for (Departments info : infoList) {
                try {
                    if(departmentService.getDepartmentById(info.getId()) != null)
                    {
                       departmentService.updateDepartment(info);
                    }else{
                        departmentService.addDepartment(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }
}
