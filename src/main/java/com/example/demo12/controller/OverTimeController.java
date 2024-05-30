package com.example.demo12.controller;

import com.example.demo12.common.Result;
import com.example.demo12.entity.Attendance;
import com.example.demo12.entity.OverTime;
import com.example.demo12.entity.Params3;
import com.example.demo12.service.OverTimeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
}
