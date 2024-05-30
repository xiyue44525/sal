package com.example.demo12.controller;

import com.example.demo12.common.Result;
import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Params3;
import com.example.demo12.service.AttendanceService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Resource
    private AttendanceService attendanceService;

    @GetMapping("/search")
    public Result searchAttendance(Params3 params3) {
        PageInfo<Attendance> info = attendanceService.searchAttendance(params3);
        return Result.success(info);
    }
    @PostMapping("/add")
    public Result addAttendance(@RequestBody Attendance attendance) {
        if(attendance.getId() == null){
            attendanceService.addAttendance(attendance);}
        else{
            attendanceService.updateAttendance(attendance);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return Result.success();
    }

}
