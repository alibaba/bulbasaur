package com.alibaba.pokemon.bulbasaur.core.model;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.pokemon.bulbasaur.exception.CoreModuleException;
import com.alibaba.pokemon.bulbasaur.util.MvelUtils;

/**
 * 能达到的路径
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午06:42:04
 */
public class Path implements PathLike {

    public String initTo;
    public String expr;

    public Path(String initTo, String expr) {
        this.initTo = initTo;
        this.expr = expr;

    }

    @Override
    public boolean can(Map<String, Object> vars) throws CoreModuleException {
        if (StringUtils.isBlank(expr)) {
            return true;
        } else {
            try {
                return MvelUtils.evalToBoolean(expr, vars);
                // catch 不同的exception
            } catch (Exception e) {
                throw new CoreModuleException("未传入context中相应的表达式(expr)!\n" + e.getMessage(), e);
            }
        }
    }

    @Override
    public String to() {
        return initTo;
    }

}
