package com.tmall.pokemon.bulbasaur.persist.mapper;

import com.tmall.pokemon.bulbasaur.persist.domain.StateDO;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOExample;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StateDOMapper {
    int countByExample(StateDOExample example);

    int deleteByExample(StateDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StateDOWithBLOBs record);

    int insertSelective(StateDOWithBLOBs record);

    List<StateDOWithBLOBs> selectByExampleWithBLOBs(StateDOExample example);

    List<StateDO> selectByExample(StateDOExample example);

    StateDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StateDOWithBLOBs record, @Param("example") StateDOExample example);

    int updateByExampleWithBLOBs(@Param("record") StateDOWithBLOBs record, @Param("example") StateDOExample example);

    int updateByExample(@Param("record") StateDO record, @Param("example") StateDOExample example);

    int updateByPrimaryKeySelective(StateDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(StateDOWithBLOBs record);

    int updateByPrimaryKey(StateDO record);
}