package com.example.demo12.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.OverTime;
import com.example.demo12.entity.Params3;
import com.example.demo12.service.OverTimeService;
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
@RequestMapping("/overtime")
public class OverTimeController {
    @Resource
    private OverTimeService overTimeService;
    @GetMapping("/search")
    public Result searchOverTime(Params3 params3) {
        PageInfo<OverTime> info = overTimeService.searchOverTime(params3);
        return Result.success(info);
    }
    @PostMapping("/add")
    public Result addOverTime(@RequestBody OverTime overTime) {
        if(overTime.getId() == null){
            overTimeService.addOverTime(overTime);}
        else{
            overTimeService.updateOverTime(overTime);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result deleteOverTime(@PathVariable Integer id) {
        overTimeService.deleteOverTime(id);
        return Result.success();
    }
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws IOException {
        List<OverTime> all = overTimeService.getAllOverTime();
        if (CollectionUtil.isEmpty(all)) {
            throw new ClassCastException("没有数据");
        }
        List<String> headers = Arrays.asList("记录id","职工号","加班小时数");
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        for (OverTime overTime : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String header : headers) {
                switch (header) {
                    case "记录id":
                        map.put(header, overTime.getId());
                        break;
                    case "职工号":
                        map.put(header, overTime.getEmployeeId());
                        break;
                    case "加班小时数":
                        map.put(header,overTime.getOvertimeHours());
                        break;
                }
            }
            list.add(map);
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=OverTime.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
        return Result.success();
    }
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<OverTime> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(OverTime.class);
        if(!CollectionUtil.isEmpty(infoList)) {
            for (OverTime info : infoList) {
                try {
                    if((overTimeService.getOverTimeById(info.getId()) != null) || (overTimeService.getOverTimeByEmployeeId(info.getEmployeeId()) != null))
                    {
                        overTimeService.updateOverTime(info);
                    }else{
                        overTimeService.addOverTime(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }
}
