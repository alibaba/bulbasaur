package com.tmall.pokemon.bulbasaur.persist;

import javax.sql.DataSource;

import com.tmall.pokemon.bulbasaur.core.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.tmall.pokemon.bulbasaur.core.Bulbasaur;
import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Module;

/**
 * 持久化Module，带有DB的读写
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-6 下午1:16:23
 */
public class PersistModule extends Module implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(PersistModule.class);

    @Override
    public Module[] require() {
        return new Module[] {CoreModule.getInstance()};
    }

    protected PersistModule() {}

    private static PersistModule self = null;

    public synchronized static PersistModule getInstance() {
        if (self == null) {
            self = new PersistModule();
        }
        return self;
    }

    private String tableNameP = "bulbasaur_p";
    private String tableNameS = "bulbasaur_s";
    private String tableNameD = "bulbasaur_d";
    private String tableNameJ = "bulbasaur_j";
    private String tableNameT = "bulbasaur_t";
    private String tableNamePtp = "bulbasaur_ptp";

    private DataSource dataSource;
    /**
     * 从db中读取
     */
    private boolean usePersistParser = true;

    public boolean isUsePersistParser() {
        return usePersistParser;
    }

    public void setUsePersistParser(boolean usePersistParser) {
        this.usePersistParser = usePersistParser;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getTableNameP() {
        return tableNameP;
    }

    public void setTableNameP(String tableNameP) {
        this.tableNameP = tableNameP;
    }

    public String getTableNameS() {
        return tableNameS;
    }

    public void setTableNameS(String tableNameS) {
        this.tableNameS = tableNameS;
    }

    public String getTableNameD() {
        return tableNameD;
    }

    public void setTableNameD(String tableNameD) {
        this.tableNameD = tableNameD;
    }

    public String getTableNameJ() {
        return tableNameJ;
    }

    public void setTableNameJ(String tableNameJ) {
        this.tableNameJ = tableNameJ;
    }

    public String getTableNameT() {
        return tableNameT;
    }

    public void setTableNameT(String tableNameT) {
        this.tableNameT = tableNameT;
    }

    public String getTableNamePtp() {
        return tableNamePtp;
    }

    public void setTableNamePtp(String tableNamePtp) {
        this.tableNamePtp = tableNamePtp;
    }

    @Override
    public void afterInit() {
        if (usePersistParser) {
            CoreModule.getInstance().setParser(
                Bulbasaur.getInnerApplicationContext().getBean("persistParser", PersistParser.class));
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        self = this;
    }

    @Override
    public String toString() {
        return "PersistModule{" +
            "tableNameP='" + tableNameP + '\'' +
            ", tableNameS='" + tableNameS + '\'' +
            ", tableNameD='" + tableNameD + '\'' +
            ", tableNameJ='" + tableNameJ + '\'' +
            ", tableNameT='" + tableNameT + '\'' +
            ", tableNamePtp='" + tableNamePtp + '\'' +
            ", dataSource=" + dataSource +
            ", usePersistParser=" + usePersistParser +
            '}';
    }
}
