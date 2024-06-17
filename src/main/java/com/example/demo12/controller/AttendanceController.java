package com.example.demo12.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Params3;
import com.example.demo12.service.AttendanceService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws IOException {
        List<Attendance> all = attendanceService.getAllAttendance();
        if (CollectionUtil.isEmpty(all)) {
            throw new ClassCastException("没有数据");
        }
        List<String> headers = Arrays.asList("记录id","职工号","缺勤天数");
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        for (Attendance attendance : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String header : headers) {
                switch (header) {
                    case "记录id":
                        map.put(header, attendance.getId());
                        break;
                    case "职工号":
                        map.put(header, attendance.getEmployeeId());
                        break;
                    case "缺勤天数":
                        map.put(header,attendance.getLeaveDays());
                        break;
                }
            }
            list.add(map);
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=Attendance.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
        return Result.success();
    }
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<Attendance> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Attendance.class);
        if(!CollectionUtil.isEmpty(infoList)) {
            for (Attendance info : infoList) {
                try {
                    if((attendanceService.getAttendanceById(info.getId()) != null) ||(attendanceService.getAttendanceByEmployeeId(info.getEmployeeId()) != null))
                    {
                        attendanceService.updateAttendance(info);
                    }else{
                        attendanceService.addAttendance(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }
}
