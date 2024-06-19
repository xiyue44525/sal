package com.example.demo12.dao;

import com.example.demo12.entity.Params4;
import com.example.demo12.entity.PositionSal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PositionDao extends Mapper<PositionSal> {

    List<PositionSal> searchPosition(@Param("params4") Params4 params4);

    @Select("SELECT * FROM position_sal")
    List<PositionSal> getAllPosition();

    @Select("SELECT * FROM position_sal WHERE id = #{id}")
    PositionSal getPositionById(Integer id);
    @Select("SELECT * FROM position_sal WHERE position = #{position}")
    PositionSal getPositionByPosition(String position);
    @Delete("DELETE FROM position_sal WHERE id = #{id}")
    void deleteById(Integer id);
    @Select("SELECT * FROM position_sal")
    List<PositionSal> FindPositionName();
}
