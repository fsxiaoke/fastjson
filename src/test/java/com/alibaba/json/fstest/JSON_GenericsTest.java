package com.alibaba.json.fstest;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.fstest.beans.*;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSON_GenericsTest extends TestCase {

    public void test_create() throws Exception {

    }

    /**
     * 优先走JSONCreator构造方法
     * @throws Exception
     */
    public void test_create_2() throws Exception {
        A a=JSON.parseObject("{\"m\":0}",A.class);
        Assert.assertTrue(a.isM());
    }


    /**
     * fastjson走jsoncreater的话，无法反序列化父类的属性
     * @throws Exception
     */
    public void test_create_3() throws Exception {
        Child child=JSON.parseObject("{\"name\":\"lili\",\"age\":18}",Child.class);
        Assert.assertNotNull(child);
    }


    /**
     * @JSONCreator构造方法中，基本类型为空时，修复默认为空异常
     *  @JSONCreator不匹配的属性不是基本类型解析出错问题
     * @throws Exception
     */
    public void test_create_4() throws Exception {
        FieldEmpty empty=JSON.parseObject("{\"age\":18,\"bean\":{\"color\":\"yellow\"}}",FieldEmpty.class);
        Assert.assertNotNull(empty);
    }


    /**
     *  修改fastjson属性为final依然可以赋值
     *
     * @throws Exception
     */
    public void test_create_5() throws Exception {
        FinalField field=JSON.parseObject("{\"value\":\"去玩儿\"}",FinalField.class);
        Assert.assertNotNull(field.value);
    }

    /**
     *  修改属性deserialize 失效问题
     *
     * @throws Exception
     */
    public void test_create_6() throws Exception {
        DeserializeBean field=JSON.parseObject("{\"value\":\"去玩儿\",\"index\":999}",DeserializeBean.class);
        String result = JSON.toJSONString(field);
        Assert.assertNotNull(result);
    }


    /**
     *  JSONCreator构造方法基本类型为null问题
     *
     * @throws Exception
     */
    public void test_create_7() throws Exception {
        FieldEmpty empty=JSON.parseObject("{\"age\":null,\"a\":null}",FieldEmpty.class);
        Assert.assertNotNull(empty);
    }

    /**
     *  当进行toJSONString的时候，默认如果重用对象的话，会使用引用的方式进行引用对象。
     *
     * @throws Exception
     */
    public void test_create_8() throws Exception {
        Child c1 = new Child(99);
        Child c2 = new Child(66);
        FieldRef ref = new FieldRef();
        FieldRef2 ref2 = new FieldRef2();
        ref.ref2=ref2;
        ref.child1=c1;
        ref.child2=c1;
        ref2.child=c1;
        String result = JSON.toJSONString(ref);
        Assert.assertNotNull(result);
    }

    /**
     * 修改只有public属性可以反序列化
     * @throws Exception
     */
    public void test_create_9() throws Exception {
        Child parent=JSON.parseObject("{\"name\":\"lili\"}",Child.class);
        Assert.assertNotNull(parent);
    }
}
