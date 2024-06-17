package com.example.demo12.controller;

import com.example.demo12.common.Result;
import com.example.demo12.entity.*;
import com.example.demo12.service.AttendanceService;
import com.example.demo12.service.OverTimeService;
import com.example.demo12.service.SalariesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/salary")
public class SalariesController {
    @Resource
    private SalariesService salariesService;
    @Resource
    private AttendanceService attendanceService;
    @Resource
    private OverTimeService overTimeService;
    @GetMapping("/search")
    public Result searchSalaries(Params3 params3) {
        PageInfo<Salaries> info = salariesService.searchSalaries(params3);
        return Result.success(info);
    }
    @PostMapping("/add")
    public Result addSalaries(@RequestBody Salaries salaries) {
        if(salaries.getId() == null){
            salariesService.addSalaries(salaries);}
        else{
            salariesService.updateSalaries(salaries);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result deleteSalaries(@PathVariable Integer id) {
        salariesService.deleteSalaries(id);
        return Result.success();
    }
    @GetMapping("/refresh")
    public Result refreshSalaries() {
        salariesService.refreshSalaries();
        return Result.success();
    }
    @PostMapping("/ownSal/{id}")
    public Result getSalariesByEmployeeId(@PathVariable Integer id) {
        Salaries salaries = salariesService.getSalariesByEmployeeId(id);
        return Result.success(salaries);
    }
    @PostMapping("/OwnMsgForSal/{id}")
    public  Result getSalariesMag(@PathVariable Integer id){
        Attendance attendance = attendanceService.getAttendanceById(id);
        OverTime overTime = overTimeService.getOverTimeById(id);
        AttendanceAndOverTimeDTO dto = new AttendanceAndOverTimeDTO(attendance, overTime);
        return Result.success(dto);
    }
}
