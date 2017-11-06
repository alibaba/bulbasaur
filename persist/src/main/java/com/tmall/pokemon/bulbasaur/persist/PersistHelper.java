package com.tmall.pokemon.bulbasaur.persist;

import java.lang.reflect.UndeclaredThrowableException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tmall.pokemon.bulbasaur.persist.tx.TransactionRun;

/**
 * 注入SqlMapClient，初始化DAO
 * 
 * @author yunche.ch@taobao.com
 * @since 2012-12-18 上午10:16:25
 * 
 */
public class PersistHelper {
	private final static Logger logger = LoggerFactory.getLogger(PersistHelper.class);

	private static PlatformTransactionManager transactionManager;

	@PostConstruct
	protected void init() {
		transactionManager = new DataSourceTransactionManager(PersistModule.getInstance().getDataSource());
	}

	private TransactionStatus getTransactionStatus() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		return transactionManager.getTransaction(definition);
	}

	public <T> T tx(TransactionRun<T> run) {
		TransactionStatus txStatus = getTransactionStatus();
		T result;
		try {
			result = run.run();
		} catch (RuntimeException re) {
			transactionManager.rollback(txStatus);
			throw re;
		} catch (Error err) {
			transactionManager.rollback(txStatus);
			throw err;
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			throw new UndeclaredThrowableException(e, "undeclared error happened in transaction");
		}
		transactionManager.commit(txStatus);
		return result;
	}

	public static PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

}
