package com.tmall.pokemon.bulbasaur.persist.mapper;

import com.tmall.pokemon.bulbasaur.persist.domain.DefinitionDO;
import com.tmall.pokemon.bulbasaur.persist.domain.DefinitionDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DefinitionDOMapper {
    int countByExample(DefinitionDOExample example);

    int deleteByExample(DefinitionDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DefinitionDO record);

    int insertSelective(DefinitionDO record);

    List<DefinitionDO> selectByExampleWithBLOBs(DefinitionDOExample example);

    List<DefinitionDO> selectByExample(DefinitionDOExample example);

    DefinitionDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DefinitionDO record, @Param("example") DefinitionDOExample example);

    int updateByExampleWithBLOBs(@Param("record") DefinitionDO record, @Param("example") DefinitionDOExample example);

    int updateByExample(@Param("record") DefinitionDO record, @Param("example") DefinitionDOExample example);

    int updateByPrimaryKeySelective(DefinitionDO record);

    int updateByPrimaryKeyWithBLOBs(DefinitionDO record);

    int updateByPrimaryKey(DefinitionDO record);
}