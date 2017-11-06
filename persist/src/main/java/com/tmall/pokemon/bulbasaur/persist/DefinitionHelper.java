package com.tmall.pokemon.bulbasaur.persist;

import java.util.Date;
import java.util.List;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.persist.domain.DefinitionDO;
import com.tmall.pokemon.bulbasaur.persist.domain.DefinitionDOExample;
import com.tmall.pokemon.bulbasaur.persist.mapper.DefinitionDOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tmall.pokemon.bulbasaur.core.Bulbasaur;
import com.tmall.pokemon.bulbasaur.persist.tx.TransactionRun;

/**
 * 提供插入Porcess定义（xml）到DB的接口
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-20 上午11:52:34
 */
public class DefinitionHelper {
    private final static Logger logger = LoggerFactory.getLogger(DefinitionHelper.class);

    @Autowired
    private DefinitionDOMapper definitionDOMapper;
    @Autowired
    private PersistHelper persistHelper;

    /**
     * 在Bulbasaur使用非spring方式加载时提供另一种访问接口的方式
     *
     * @return 返回DefinitionHelper在spring中的实例
     */
    public static DefinitionHelper getInstance() {
        return Bulbasaur.getInnerApplicationContext().getBean("definitionHelper", DefinitionHelper.class);
    }

    /**
     * deploy a process definition to database with a generated version
     *
     * @param name       process name
     * @param alias      alias
     * @param content    definition content
     * @param setDefault is deploy as default version
     * @return definition itself
     */
    public DefinitionDO deployDefinition(String name, String alias, String content, boolean setDefault) {

        DefinitionDOExample definitionDOExample = new DefinitionDOExample();
        definitionDOExample.createCriteria().andDefinitionNameEqualTo(name).andOwnSignEqualTo(
            CoreModule.getInstance().getOwnSign());

        List<DefinitionDO> defList = definitionDOMapper.selectByExampleWithBLOBs(definitionDOExample);
        int lastVersion;
        if (defList == null || defList.isEmpty()) {
            lastVersion = 1;
        } else {
            // biggest version +1
            int bigVersion = 0;
            for (DefinitionDO definitionDO : defList) {
                if (definitionDO.getDefinitionVersion() > bigVersion) {
                    bigVersion = definitionDO.getDefinitionVersion();
                }
            }
            lastVersion = bigVersion + 1;
        }
        return deployDefinition(name, alias, lastVersion, content, setDefault);

    }

    /**
     * deploy a process definition to database with a given version
     * <hr/>
     * if the given version already exist, it will be overwritten
     *
     * @param name       process name
     * @param version    process version
     * @param content    definition content
     * @param setDefault is deploy as default version
     * @return definition itself
     */
    public DefinitionDO deployDefinition(final String name, final String alias, final int version, String content,
                                         final boolean setDefault) {

        DefinitionDOExample definitionDOExample = new DefinitionDOExample();
        definitionDOExample.createCriteria().andDefinitionNameEqualTo(name).andDefinitionVersionEqualTo(version)
            .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());

        List<DefinitionDO> defList = definitionDOMapper.selectByExampleWithBLOBs(definitionDOExample);

        DefinitionDO oldDefinitionDO = defList != null && !defList.isEmpty() ? defList.get(0) : null;
        final boolean isNew = oldDefinitionDO == null;
        final DefinitionDO definitionDO;
        if (isNew) {
            definitionDO = new DefinitionDO();
            definitionDO.setDefinitionName(name);
            definitionDO.setDefinitionAlias(alias);
            definitionDO.setDefinitionVersion(version);
            definitionDO.setGmtCreate(new Date());
        } else {
            definitionDO = oldDefinitionDO;
        }
        definitionDO.setContent(content);
        definitionDO.setStatus(setDefault);
        definitionDO.setGmtModified(new Date());
        definitionDO.setOwnSign(CoreModule.getInstance().getOwnSign());

        persistHelper.tx(new TransactionRun<Object>() {
            @Override
            public Object run() {
                if (setDefault) {

                    DefinitionDOExample definitionDOExample = new DefinitionDOExample();
                    definitionDOExample.createCriteria().andOwnSignEqualTo(CoreModule.getInstance().getOwnSign())
                        .andDefinitionNameEqualTo(name)
                        .andDefinitionVersionEqualTo(version);
                    DefinitionDO de = new DefinitionDO();
                    de.setStatus(false);
                    definitionDOMapper.updateByExampleSelective(de, definitionDOExample);
                }
                if (isNew) {
                    definitionDOMapper.insert(definitionDO);
                } else {
                    definitionDOMapper.updateByPrimaryKeyWithBLOBs(definitionDO);
                }
                return null;
            }
        });

        logger.info(String.format("deploy definition [ %s@%s ,isDefault: %s] ", name, version, setDefault));
        logger.info("definition content:\n" + content);

        return definitionDO;
    }
}
