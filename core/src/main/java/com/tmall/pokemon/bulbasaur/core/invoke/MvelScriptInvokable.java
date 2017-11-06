package com.tmall.pokemon.bulbasaur.core.invoke;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

import com.tmall.pokemon.bulbasaur.exception.BizException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.mvel2.PropertyAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.tmall.pokemon.bulbasaur.core.Bulbasaur;
import com.tmall.pokemon.bulbasaur.core.annotation.InvokableMeta;
import com.tmall.pokemon.bulbasaur.util.MvelUtils;

import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * Mvel调用，执行脚本
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午06:41:02
 */
@InvokableMeta(t = "script")
public class MvelScriptInvokable extends Invokable {
	private final static Logger logger = LoggerFactory.getLogger(MvelScriptInvokable.class);

	private String expr = null;
	private String script = null;
	private Map<String, Object> beans;
	private Map<String, Object> pojos;

	@Override
	public Invokable init(Element elem) {
		super.init(elem);
		// expr
		if (elem.attributeValue("expr") != null) {
			this.expr = elem.attributeValue("expr");
		} else {
			this.expr = "";
		}

		if (elem.attributeValue("script") != null) {
			this.script = elem.attributeValue("script");
		} else {
			require(StringUtils.isNotBlank(elem.getText()),
					"script text can't be blank in script tag");
			this.script = elem.getTextTrim();
		}

		// async
		if (elem.attributeValue("async") != null) {
			// 直接改变父类的async
			async = Boolean.valueOf(elem.attributeValue("async"));
			// 这样用 只要字符串不是true就全是false
		}

		// beans
		this.beans = new HashMap<String, Object>();
		if (elem.attributeValue("beans") != null) {
			// 拿CoreModule 的spring 容器
			ApplicationContext ctx = Bulbasaur.getApplicationContext();
			if (ctx == null) {
				throw new NullPointerException("can't get Spring bean because can't find Spring context");
			}
			String tmp = elem.attributeValue("beans");
			String[] beansStr = tmp.split(",");
			for (String beanName : beansStr) {
				beans.put(beanName.trim(), ctx.getBean(beanName.trim()));
			}
		}

		// pojos
		this.pojos = new HashMap<String, Object>();
		if (elem.attributeValue("pojos") != null) {
			String tmp = elem.attributeValue("pojos");
			String[] pojosStr = tmp.split(",");
			for (String pojo : pojosStr) {
				// eg: test.TestBean -> test
				String[] resolve = pojo.split("->");
				String path = resolve[0].trim();// test.TestBean 这里是全路径名：包名+类名
				String key = resolve[1].trim();// test
				try {
					pojos.put(key, Class.forName(path).newInstance());
				} catch (RuntimeException re) {
					throw re;
				} catch (Throwable e) {
					throw new UndeclaredThrowableException(e, "error happened when newInstance pojo:" + path);
				}
			}
		}
		return this;

	}// 构造初始化

	@Override
	public Object invoke0(Map<String, Object> context) {
		context.putAll(beans);
		context.putAll(pojos);
		if (StringUtils.isNotBlank(expr) && !MvelUtils.evalToBoolean(expr, context)) {
			return null;
		}
		try {
			return MvelUtils.eval(script, context);
		} catch (PropertyAccessException e) {
			logger.error("mvel script execute error:" + e.getMessage(), e);
			Throwable sourceE = MvelUtils.unboxingException(e);
			if (sourceE instanceof RuntimeException) {
				throw (BizException) sourceE;
			} else {
				throw new UndeclaredThrowableException(sourceE);
			}
		}
	}

	@Override
	public String toString() {
		return "MvelScriptInvokable{" + "script='" + script + '\'' + ", expr='" + expr + '\'' + ", returnKey='" + returnKey + '\'' + '}';
	}
}
