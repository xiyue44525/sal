package com.example.demo12.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.demo12.common.Result;
import com.example.demo12.entity.Departments;
import com.example.demo12.entity.Params4;
import com.example.demo12.entity.PositionSal;
import com.example.demo12.service.PositionService;
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
@RequestMapping("/position")
public class PositionController {
    @Resource
    private PositionService positionService;
    @GetMapping("/search")
    public Result searchOverTime(Params4 params4) {
        PageInfo<PositionSal> info = positionService.searchPosition(params4);
        return Result.success(info);
    }
    @PostMapping("/add")
    public Result addPosition(@RequestBody PositionSal positionSal) {
        if(positionSal.getId() == null){
            positionService.addPosition(positionSal);}
        else{
            positionService.updatePosition(positionSal);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
        return Result.success();
    }

    @GetMapping("/findPositionName")
    public Result FindPositionName(){
        List<PositionSal>  list = positionService.FindPositionName();
        return Result.success(list);
    }

    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws IOException {
        List<PositionSal> all = positionService.getAllPosition();
        if (CollectionUtil.isEmpty(all)) {
            throw new ClassCastException("没有数据");
        }
        List<String> headers = Arrays.asList("编号","职称","基础工资","补贴");
        List<Map<String, Object>> list = new ArrayList<>(all.size());
        for (PositionSal positionSal : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String header : headers) {
                switch (header) {
                    case "编号":
                        map.put(header, positionSal.getId());
                        break;
                    case "职称":
                        map.put(header, positionSal.getPosition());
                        break;
                    case "基础工资":
                        map.put(header, positionSal.getBaseSalary());
                        break;
                    case "补贴":
                        map.put(header, positionSal.getAllowance());
                        break;
                }
            }
            list.add(map);
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=PositionSal.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
        return Result.success();
    }
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<PositionSal> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(PositionSal.class);
        if(!CollectionUtil.isEmpty(infoList)) {
            for (PositionSal info : infoList) {
                try {
                    if((positionService.getPositionById(info.getId()) != null) || (positionService.getPositionByPosition(info.getPosition()) != null))
                    {
                        positionService.updatePosition(info);
                    }else{
                        positionService.addPosition(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }
}

