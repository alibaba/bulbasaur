package com.alibaba.pokemon.bulbasaur.persist.mybatis;

import com.alibaba.pokemon.bulbasaur.persist.PersistModule;
import org.mybatis.spring.SqlSessionFactoryBean;

import java.util.Properties;

/**
 * 适配业务创建的表名规则
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/3/8
 * Time: 下午1:51
 */
public class SqlSessionFactory extends SqlSessionFactoryBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        prepare();
        super.afterPropertiesSet();
    }

    protected void prepare() {
        this.setDataSource(PersistModule.getInstance().getDataSource());
        Properties props = new Properties();
        props.setProperty("bulbasaur_p", PersistModule.getInstance().getTableNameP());
        props.setProperty("bulbasaur_s", PersistModule.getInstance().getTableNameS());
        props.setProperty("bulbasaur_d", PersistModule.getInstance().getTableNameD());
        props.setProperty("bulbasaur_j", PersistModule.getInstance().getTableNameJ());
        props.setProperty("bulbasaur_t", PersistModule.getInstance().getTableNameT());
        props.setProperty("bulbasaur_ptp", PersistModule.getInstance().getTableNamePtp());
        this.setConfigurationProperties(props);
    }
}
