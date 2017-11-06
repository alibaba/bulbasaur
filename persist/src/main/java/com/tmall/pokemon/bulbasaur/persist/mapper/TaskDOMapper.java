package com.tmall.pokemon.bulbasaur.persist.mapper;

import com.tmall.pokemon.bulbasaur.persist.domain.TaskDO;
import com.tmall.pokemon.bulbasaur.persist.domain.TaskDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskDOMapper {
    int countByExample(TaskDOExample example);

    int deleteByExample(TaskDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskDO record);

    int insertSelective(TaskDO record);

    List<TaskDO> selectByExample(TaskDOExample example);

    TaskDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TaskDO record, @Param("example") TaskDOExample example);

    int updateByExample(@Param("record") TaskDO record, @Param("example") TaskDOExample example);

    int updateByPrimaryKeySelective(TaskDO record);

    int updateByPrimaryKey(TaskDO record);
}