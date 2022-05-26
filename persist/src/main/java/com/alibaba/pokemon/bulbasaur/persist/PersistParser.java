package com.alibaba.pokemon.bulbasaur.persist;

import static com.alibaba.pokemon.bulbasaur.util.SimpleUtils.require;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.persist.domain.DefinitionDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.DefinitionDOExample;
import com.alibaba.pokemon.bulbasaur.persist.mapper.DefinitionDOMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.pokemon.bulbasaur.core.Parser;
import com.alibaba.pokemon.bulbasaur.core.model.Definition;

import java.util.List;

/**
 * 可读取DB中的流程模板
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-20 上午10:48:30
 */
public class PersistParser extends Parser {
    private final static Logger logger = LoggerFactory.getLogger(PersistParser.class);

    @Autowired
    private DefinitionDOMapper definitionDOMapper;

    /**
     * refresh if default definition's version in DB not same as local one
     *
     * @param processName
     * @param processVersion
     * @param oldDefinition
     * @return
     */
    @Override
    public boolean needRefresh(String processName, int processVersion, Definition oldDefinition) {

        DefinitionDOExample definitionDOExample = new DefinitionDOExample();
        definitionDOExample.createCriteria().andDefinitionNameEqualTo(processName).andOwnSignEqualTo(
            CoreModule.getInstance().getOwnSign());
        List<DefinitionDO> definitionDOList = definitionDOMapper.selectByExample(definitionDOExample);

        DefinitionDO defaultD = definitionDOList.get(0);

        return (processVersion == 0) && (defaultD.getDefinitionVersion() != oldDefinition.getVersion());

    }

    @Override
    public Definition parse(String processName, int processVersion) {
        DefinitionDO definitionDO;
        if (processVersion == 0) {
            // 直接查默认的 status = true
            DefinitionDOExample definitionDOExample = new DefinitionDOExample();
            definitionDOExample.createCriteria().andDefinitionNameEqualTo(processName)
                .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign())
                .andStatusEqualTo(true);
            List<DefinitionDO> definitionDOList = definitionDOMapper.selectByExampleWithBLOBs(definitionDOExample);

            definitionDO = definitionDOList != null && !definitionDOList.isEmpty() ? definitionDOList.get(0) : null;
        } else {
            // version 不为0 ，说明需要使用指定版本的定义，不关心是否是默认
            DefinitionDOExample definitionDOExample = new DefinitionDOExample();
            definitionDOExample.createCriteria().andDefinitionNameEqualTo(processName).andDefinitionVersionEqualTo(
                processVersion).andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());
            List<DefinitionDO> definitionDOList = definitionDOMapper.selectByExampleWithBLOBs(definitionDOExample);
            definitionDO = definitionDOList != null && !definitionDOList.isEmpty() ? definitionDOList.get(0) : null;
        }
        require(definitionDO != null,
            String.format("definition [%s:%s ] not found in DB", processName, processVersion));
        // 将string 转成 xml
        Document processXML;
        try {
            processXML = DocumentHelper.parseText(definitionDO.getContent());
        } catch (DocumentException e) {
            logger.error("parse definition content error:" + e.getMessage(), e);
            logger.error("error content:\n" + definitionDO.getContent());
            throw new IllegalArgumentException("parse definition content error", e);
        }
        return super.parser0(processName, definitionDO.getDefinitionVersion(), processXML, definitionDO.getStatus());
    }

}
