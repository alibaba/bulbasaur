package com.tmall.pokemon.bulbasaur.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;

/**
 * mvel工具类
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-12 下午4:00:45
 */
public class MvelUtils {

    public static boolean evalToBoolean(String expr, Map<String, Object> vars) {
        return MVEL.evalToBoolean(expr, vars);
    }

    /**
     * 脚本和上下文对比。eg:脚本里面 goto==2 ，上下文里面 map("goto",2)
     *
     * @return void
     * @author: yunche.ch@taobao.com
     * @since 2012-12-13 下午02:15:49
     */
    public static Object eval(String script, Map<String, Object> context) {
        return MVEL.eval(script, context);

    }

    public static Throwable unboxingException(Throwable ex) {
        if (ex instanceof PropertyAccessException) {
            Throwable innerEx = getCauseOver(ex);
            if (innerEx instanceof InvocationTargetException) {
                return ((InvocationTargetException)innerEx).getTargetException();
            } else {
                return innerEx;
            }
        } else {
            return ex;
        }

    }

    public static Throwable getCauseOver(Throwable pae) {
        if (pae.getCause() == null) {
            return pae;
        } else if ((pae.getCause() instanceof PropertyAccessException) && !(pae.getCause() == pae)) {
            return getCauseOver(pae.getCause());
        } else {
            return pae.getCause();
        }
    }

}
