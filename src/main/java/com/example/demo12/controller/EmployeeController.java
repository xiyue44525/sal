package com.example.demo12.controller;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import com.example.demo12.service.EmployeesService;
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
@RequestMapping("/test")
public class EmployeeController {

    @Resource
    private EmployeesService EmployeesService;

    @GetMapping
    public Result getEmployees() {
        List<Employees> list = EmployeesService.getEmployees();
        return Result.success(list);
    }
    @GetMapping("/search")
    public Result searchEmployees(Params params) {
        PageInfo<Employees> info = EmployeesService.searchEmployees(params);
        return Result.success(info);
    }
    @PostMapping("/add")
    public Result addEmployees(@RequestBody Employees Employees) {
        if(Employees.getId() == null){
                EmployeesService.addEmployees(Employees);}
        else{
                EmployeesService.updateEmployees(Employees);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result deleteEmployees(@PathVariable Integer id) {
        EmployeesService.deleteEmployees(id);
        return Result.success();
    }
    @PostMapping("/login")
    public Result login(@RequestBody Employees Employees) {
        Employees loginUser = EmployeesService.login(Employees);
        return Result.success(loginUser);
    }
    @PostMapping("/register")
    public Result register(@RequestBody Employees Employees) {
        EmployeesService.addEmployees(Employees);
        return Result.success();
    }
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody Employees Employees) {
        EmployeesService.updateUser(Employees);
        return Result.success();
    }
    @PutMapping("/deleteMuch")
    public Result deleteMuch(@RequestBody List<Employees> employees) {
        for (Employees employee : employees) {
            EmployeesService.deleteEmployees(employee.getId());
        }
        return Result.success();
    }
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws IOException {
        List<Employees> all = EmployeesService.getEmployees();
        if (CollectionUtil.isEmpty(all)) {
            throw new ClassCastException("没有数据");
        }
        List<String> headers = Arrays.asList("id", "姓名", "性别", "部门", "入职时间", "联系方式", "权限");
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        for (Employees employee : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String header : headers) {
                switch (header) {
                    case "id":
                        map.put(header, employee.getId());
                        break;
                    case "姓名":
                        map.put(header, employee.getUserName());
                        break;
                    case "性别":
                        map.put(header, employee.getSex());
                        break;
                    case "部门":
                        map.put(header, employee.getDepartmentName());
                        break;
                    case "入职时间":
                        map.put(header, employee.getJoinDate());
                        break;
                    case "联系方式":
                        map.put(header, employee.getPhone());
                        break;
                    case "权限":
                        map.put(header, employee.getSign() == 1 ? "管理员" : "员工");
                        break;
            }
        }
            list.add(map);
    }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=employee.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
        return Result.success();
    }
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<Employees> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Employees.class);
        if(!CollectionUtil.isEmpty(infoList)) {
            for (Employees info : infoList) {
                String role = info.getRole();
                if (role.equals("管理员")) {
                    info.setSign(1);
                } else {
                    info.setSign(0);
                }
                String joinDataStr = info.getDateForMoment();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date joinTimeDate = sdf.parse(joinDataStr);
                    Timestamp joinTimeTimestamp = new Timestamp(joinTimeDate.getTime());
                    info.setJoinDate(joinTimeTimestamp);
                    if(EmployeesService.FindById(info.getId()) != null)
                    {
                        EmployeesService.updateEmployees(info);
                    }else{
                        EmployeesService.addEmployees(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }
    //调用层级：controller -> service -> dao（mapper.xml -> db
}
