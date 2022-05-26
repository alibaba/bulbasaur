package com.alibaba.pokemon.bulbasaur.persist.mapper;

import com.alibaba.pokemon.bulbasaur.persist.domain.ProcessDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.ProcessDOExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProcessDOMapper {
    int countByExample(ProcessDOExample example);

    int deleteByExample(ProcessDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProcessDO record);

    int insertSelective(ProcessDO record);

    List<ProcessDO> selectByExample(ProcessDOExample example);

    ProcessDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProcessDO record, @Param("example") ProcessDOExample example);

    int updateByExample(@Param("record") ProcessDO record, @Param("example") ProcessDOExample example);

    int updateByPrimaryKeySelective(ProcessDO record);

    int updateByPrimaryKey(ProcessDO record);
}
