package com.tmall.pokemon.bulbasaur.persist.mapper;

import com.tmall.pokemon.bulbasaur.persist.domain.ParticipationDO;
import com.tmall.pokemon.bulbasaur.persist.domain.ParticipationDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ParticipationDOMapper {
    int countByExample(ParticipationDOExample example);

    int deleteByExample(ParticipationDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ParticipationDO record);

    int insertSelective(ParticipationDO record);

    List<ParticipationDO> selectByExample(ParticipationDOExample example);

    ParticipationDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ParticipationDO record, @Param("example") ParticipationDOExample example);

    int updateByExample(@Param("record") ParticipationDO record, @Param("example") ParticipationDOExample example);

    int updateByPrimaryKeySelective(ParticipationDO record);

    int updateByPrimaryKey(ParticipationDO record);
}