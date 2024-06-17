package com.example.demo12.service;
import com.example.demo12.dao.OverTimeDao;
import com.example.demo12.entity.Employees;
import com.example.demo12.entity.OverTime;
import com.example.demo12.entity.Params3;
import com.example.demo12.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OverTimeService {
    @Resource
    private OverTimeDao overTimeDao;
    @Resource
    private EmployeesService employeeService;

    public OverTime getOverTimeHours(Integer employeeId) {
        return overTimeDao.selectOverTimeHours(employeeId);
    }


    public PageInfo<OverTime> searchOverTime(Params3 params3) {
        //分页
        PageHelper.startPage(params3.getPageNum(), params3.getPageSize());
        //搜索
        List<OverTime> list = overTimeDao.searchOverTime(params3);
        return PageInfo.of(list);
    }


    public void addOverTime(OverTime overTime) {
        if(overTime.getEmployeeId() == null) {
            throw new CustomException("员工id不能为空");
        }
        //查询员工是否存在
        Integer employeeId = overTime.getEmployeeId();
        Employees employee = employeeService.SelectByEmployeeId(employeeId);
        if(employee == null) {
            throw new CustomException("输入的员工id不存在");
        }
        //查重
        OverTime O = overTimeDao.selectByEmployeeId(employeeId);
        if(O!= null) {
            throw new CustomException("该员工已存在加班记录");
        }
        overTimeDao.insertSelective(overTime);
    }

    public void updateOverTime(OverTime overTime) {
        overTimeDao.updateByPrimaryKeySelective(overTime);
    }

    public void deleteOverTime(Integer id) {
        overTimeDao.deleteByPrimaryKey(id);
    }

    public void deleteByEmployeeId(Integer id) {
        overTimeDao.deleteByEmployeeId(id);
    }

    public OverTime getOverTimeByEmployeeId(Integer id) {
        return overTimeDao.selectByEmployeeId(id);
    }

    public OverTime getOverTimeById(Integer id) {
        return overTimeDao.getOverTimeById(id);
    }

    public List<OverTime> getAllOverTime() {
        return overTimeDao.getAllOverTime();
    }
}
