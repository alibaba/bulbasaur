package com.tmall.pokemon.bulbasaur.core.model;

import com.tmall.pokemon.bulbasaur.core.Result;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tmall.pokemon.bulbasaur.core.constants.XmlTagConstants.PATH_TAG;
import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * State的抽象类模型，含有变量 stateName
 * 会执行脚本
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 下午4:14:22
 */
public abstract class RunnableState implements StateLike {
    private final static Logger logger = LoggerFactory.getLogger(RunnableState.class);
    private String stateName;
    private String stateAlias;
    private String jumpTo;
    private String outGoing;
    private String bizId;
    private String definitionName;
    private List<PathLike> paths = new ArrayList<PathLike>();

    @Override
    public Result prepare(Map<String, Object> context) {
        // do nothing but return empty map
        // need override
        Result result = new Result();
        result.setContinue(true);
        return result;
    }

    @Override
    public Result execute(Map<String, Object> context) {
        // do nothing but return empty map
        // need override
        Result result = new Result();
        result.setContinue(true);
        return result;
    }

    @Override
    public String willGo(Map<String, Object> context) {
        // 遍历paths
        for (PathLike p : paths) {
            if (p.can(context)) {
                return p.to();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StateLike parse(Element elem) {
        require(elem.attributeValue("name") != null,
            "attribute 'name' in node is required");
        stateName = elem.attributeValue("name");
        stateAlias = elem.attributeValue("alias");
        // 得到所有该节点下的 paths 下的 path
        List<Element> childPaths = elem.selectNodes(PATH_TAG);
        for (Element node : childPaths) {
            require(node.attributeValue("to") != null, "attribute to in path node is required");

            String pathTo = node.attributeValue("to");
            String expr = null;
            if (node.attributeValue("expr") != null) {
                expr = node.attributeValue("expr");
            }
            // 新实例一个path,并加入到paths中
            Path pt = new Path(pathTo, expr);
            // 添加到path 列表中
            paths.add(pt);
        }
        return this;
    }

    @Override
    public String getBizId() {
        return bizId;
    }

    @Override
    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @Override
    public boolean isRunnable() {
        return true;
    }

    @Override
    public String getJumpTo() {
        return jumpTo;
    }

    @Override
    public void setJumpTo(String jumpTo) {
        this.jumpTo = jumpTo;
    }

    @Override
    public String getStateName() {
        return stateName;
    }

    @Override
    public String getStateAlias() {
        return stateAlias;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public void setDAOMap(Map<String, ?> map) {
    }

    @Override
    public String getOutGoing() {
        return outGoing;
    }

    @Override
    public void setOutGoing(String outGoing) {
        this.outGoing = outGoing;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    @Override
    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }
}
