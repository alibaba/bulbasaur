package com.alibaba.pokemon.bulbasaur.persist.mapper;

import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDOExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JobDOMapper {
    int countByExample(JobDOExample example);

    int deleteByExample(JobDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(JobDO record);

    int insertSelective(JobDO record);

    List<JobDO> selectByExample(JobDOExample example);

    JobDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") JobDO record, @Param("example") JobDOExample example);

    int updateByExample(@Param("record") JobDO record, @Param("example") JobDOExample example);

    int updateByPrimaryKeySelective(JobDO record);

    int updateByPrimaryKey(JobDO record);
}
