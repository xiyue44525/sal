package com.example.demo12.service;

import com.example.demo12.dao.PositionDao;
import com.example.demo12.entity.Params4;
import com.example.demo12.entity.PositionSal;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PositionService {
    @Resource
    private PositionDao positionDao;


    public PageInfo<PositionSal> searchPosition(Params4 params4) {
        //分页
        PageHelper.startPage(params4.getPageNum(), params4.getPageSize());
        //搜索
        List<PositionSal> list = positionDao.searchPosition(params4);
        return PageInfo.of(list);
    }

    public void addPosition(PositionSal positionSal) {
        positionDao.insertSelective(positionSal);
    }

    public void updatePosition(PositionSal positionSal) {
        positionDao.updateByPrimaryKeySelective(positionSal);
    }

    public void deletePosition(Integer id) {
        //positionDao.deleteByPrimaryKey(id);
        positionDao.deleteById(id);
    }

    public List<PositionSal> getAllPosition() {
       return positionDao.getAllPosition();
    }

    public PositionSal getPositionById(Integer id) {
        return positionDao.getPositionById(id);
    }

    public PositionSal getPositionByPosition(String position) {
        return positionDao.getPositionByPosition(position);
    }

    public List<PositionSal> FindPositionName() {
        return positionDao.FindPositionName();
    }
}
