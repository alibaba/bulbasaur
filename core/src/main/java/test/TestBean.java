package test;

/**
 * 用于core测试
 *
 * @author yunche.ch@taobao.com
 * @Description:
 * @date 2012-12-13 下午02:28:35
 */
@Deprecated
public class TestBean {

    public int testMethod(int i) {
        System.out.println(i);
        // 为了测试业务方报错
        // Map m = null ;
        // m.put("1", 1);
        return i = i + 1;
    }

    public void exceptionMethod() {
        // System.out.println("generate a exception...");
        throw new NullPointerException("a generated exception");
    }

    public void testNullParameter(Integer i) {// 因为是integer 才能传null
        System.out.println(i);
    }

}
