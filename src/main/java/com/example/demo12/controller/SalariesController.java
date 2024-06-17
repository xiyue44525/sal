package com.example.demo12.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo12.common.Result;
import com.example.demo12.entity.*;
import com.example.demo12.service.AttendanceService;
import com.example.demo12.service.OverTimeService;
import com.example.demo12.service.SalariesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws IOException {
        List<Salaries> all = salariesService.getAllSalary();
        if (CollectionUtil.isEmpty(all)) {
            throw new ClassCastException("没有数据");
        }
        List<String> headers = Arrays.asList("员工ID","基本工资","补贴","扣除","加班费","实际工资");
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        for (Salaries salaries: all) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String header : headers) {
                switch (header) {
                    case "员工ID":
                        map.put(header,salaries.getEmployeeId() );
                        break;
                    case "基本工资":
                        map.put(header, salaries.getBaseSalary());
                        break;
                    case "补贴":
                        map.put(header, salaries.getAllowance());
                        break;
                    case "扣除":
                        map.put(header,salaries.getDeduction());
                        break;
                    case "加班费":
                        map.put(header,salaries.getOvertimePay());
                        break;
                    case "实际工资":
                        map.put(header,salaries.getNetIncome());
                        break;
                }
            }
            list.add(map);
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=Salaries.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
        return Result.success();
    }
}
