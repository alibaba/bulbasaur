package test;

import java.util.HashMap;
import java.util.Map;

import com.tmall.pokemon.bulbasaur.exception.BizException;

/**
 * 用于core测试
 *
 * @author yunche.ch@taobao.com
 * @Description:
 * @date 2012-12-13 下午02:30:13
 */
@Deprecated
public class TestBean2 {
    public boolean judge() {
        return true;
    }

    public int testMethod(int i) {
        System.out.println(i);
        //throw new BizException("a generated exception");
        return i + 2;
    }

    public String getUsers(String abc) {
        return "00001:测试人员";

    }

    public int testState4(int i) {
        System.out.println("state4");
        return i = i + 2;
    }

    public Map<String, Object> getTimeOutStra(String abc) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("outGoing", "state3");
        map.put("period", "2minute");
        return map;

    }
}
