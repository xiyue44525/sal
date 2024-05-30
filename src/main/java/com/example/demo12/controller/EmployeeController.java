package com.example.demo12.controller;
import com.example.demo12.common.JwtInterceptor;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.Params;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.example.demo12.service.EmployeesService;
import javax.annotation.Resource;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/test")
public class EmployeeController {
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);
    @Resource
    private EmployeesService EmployeesService;

//    @GetMapping("/hello")
//    public Result test() {
//        return Result.success("Hello World");
//    }

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
    //调用层级：controller -> service -> dao（mapper.xml -> db
}
