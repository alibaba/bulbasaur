package com.tmall.pokemon.bulbasaur.core.model;

import com.tmall.pokemon.bulbasaur.core.annotation.StateMeta;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.tmall.pokemon.bulbasaur.core.constants.XmlTagConstants.INFO_TAG;
import static com.tmall.pokemon.bulbasaur.core.constants.XmlTagConstants.KEY_TAG;
import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * 业务信息，在引擎中流转
 * Created by IntelliJ IDEA.
 *
 * @author : guichen - anson
 * @since : 13-1-7
 */
@StateMeta(t = "bizInfo")
public class BizInfo extends NotRunnableState {

    private Map<String, List<BizInfoElement>> bizInfoList = new HashMap<String, List<BizInfoElement>>();

    @SuppressWarnings("unchecked")
    @Override
    public StateLike parse(Element elem) {
        List<Element> infoElemList = elem.selectNodes(INFO_TAG);
        for (Element infoElem : infoElemList) {
            BizInfoElement bizInfoElement = new BizInfoElement();
            for (Iterator<Attribute> it = infoElem.attributeIterator(); it.hasNext(); ) {
                Attribute attribute = it.next();
                bizInfoElement.attribute.put(attribute.getName(), attribute.getValue());
            }
            String key = bizInfoElement.attribute.get(KEY_TAG);
            require(StringUtils.hasText(key), "attribute '" + KEY_TAG + "' in node bizInfo/info is required");
            if (bizInfoList.get(key) == null) {
                bizInfoList.put(key, new ArrayList<BizInfoElement>());
            }
            bizInfoList.get(key).add(bizInfoElement);
        }
        return this;
    }

    public List<BizInfoElement> getBizInfoList(String key) {
        return bizInfoList.get(key);
    }

    public static class BizInfoElement {
        private Map<String, String> attribute = new HashMap<String, String>();

        public String getAttribute(String key) {
            return attribute.get(key);
        }
    }

    /* (non-Javadoc)
     * @see com.tmall.pokemon.bulbasaur.core.model.StateLike#getJumpTo()
     */
    @Override
    public String getJumpTo() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.tmall.pokemon.bulbasaur.core.model.StateLike#setBizId(java.lang.String)
     */
    @Override
    public void setBizId(String bizId) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOutGoing() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOutGoing(String outGoing) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /* (non-Javadoc)
     * @see com.tmall.pokemon.bulbasaur.core.model.StateLike#setJumpTo(java.lang.String)
     */
    @Override
    public void setJumpTo(String jumpTo) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.tmall.pokemon.bulbasaur.core.model.StateLike#setDefinitionName(java.lang.String)
     */
    @Override
    public void setDefinitionName(String definitionName) {
        // TODO Auto-generated method stub

    }

}
