package com.alibaba.json.bvt.parser;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.parser.Feature;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.TypeUtils;

import junit.framework.TestCase;

public class TypeUtilsTest_castToJavaBean extends TestCase {

    public void test_castToJavaBean_StackTraceElement() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("className", "java.lang.Object");
        map.put("methodName", "hashCode");
        StackTraceElement element = TypeUtils.castToJavaBean(map, StackTraceElement.class, null);
        Assert.assertEquals("java.lang.Object", element.getClassName());
        Assert.assertEquals("hashCode", element.getMethodName());
        Assert.assertEquals(null, element.getFileName());
    }

    public void test_castToJavaBean_StackTraceElement_1() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("className", "java.lang.Object");
        map.put("methodName", "hashCode");
        map.put("lineNumber", 12);
        StackTraceElement element = TypeUtils.castToJavaBean(map, StackTraceElement.class, null);
        Assert.assertEquals("java.lang.Object", element.getClassName());
        Assert.assertEquals("hashCode", element.getMethodName());
        Assert.assertEquals(null, element.getFileName());
        Assert.assertEquals(12, element.getLineNumber());
    }

    public void test_castToJavaBean_type() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("@type", "java.lang.StackTraceElement");
        map.put("className", "java.lang.Object");
        map.put("methodName", "hashCode");
        map.put("lineNumber", 12);
        int feautures = JSON.DEFAULT_PARSER_FEATURE |= Feature.SupportAutoType.mask;
        StackTraceElement element = (StackTraceElement) TypeUtils.castToJavaBean(map, Object.class, null, feautures);
        Assert.assertEquals("java.lang.Object", element.getClassName());
        Assert.assertEquals("hashCode", element.getMethodName());
        Assert.assertEquals(null, element.getFileName());
        Assert.assertEquals(12, element.getLineNumber());
    }

    public void test_error() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("@type", "xxx");
        Exception error = null;
        try {
            TypeUtils.castToJavaBean(map, Object.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("@type", "");
        Exception error = null;
        try {
            TypeUtils.castToJavaBean(map, Object.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_mapping() throws Exception {
        addClassMapping("my_xxx", VO.class);
        addClassMapping(null, VO.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("@type", "my_xxx");
        map.put("id", 123);
        VO vo = (VO) TypeUtils.castToJavaBean(map, Object.class);
        Assert.assertEquals(123, vo.getId());
        clearClassMapping();
    }

    public void test_interface() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        VO vo = TypeUtils.castToJavaBean(map, VO.class);
        Assert.assertEquals(123, vo.getId());
    }

    public void test_bean() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        Entity vo = TypeUtils.castToJavaBean(map, Entity.class);
        Assert.assertEquals(123, vo.getId());

        Assert.assertEquals("{\"id\":123}", JSON.toJSONString(vo));
    }

    public void test_loadClass() throws Exception {
        Assert.assertNull(TypeUtils.loadClass(null, null));
        Assert.assertNull(TypeUtils.loadClass("", null));
    }

    public void test_loadClass_1() throws Exception {
        clearClassMapping();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new TestLoader());
        try {
            Assert.assertEquals(VO.class,
                                TypeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_castToJavaBean$VO", null));
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public void test_loadClass_2() throws Exception {
        clearClassMapping();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new TestLoader());
        try {
            Assert.assertNull(TypeUtils.loadClass("xxx_xx", null));
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }
    
    public void test_bean_2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        PO vo = TypeUtils.castToJavaBean(map, PO.class);
        Assert.assertEquals(123, vo.id);
        
        SerializeWriter out = new SerializeWriter();

        try {
            SerializeConfig config = new SerializeConfig();
            JSONSerializer serializer = new JSONSerializer(out, config);
            config.put(PO.class, new JavaBeanSerializer(PO.class, PO.class.getModifiers(), Collections.singletonMap("id", "ID"), false, true, true, true, null));

            serializer.write(vo);

            Assert.assertEquals("{\"ID\":123}", out.toString());
        } finally {
            out.close();
        }

        
    }
    
    public void test_bean_3() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        PO vo = TypeUtils.castToJavaBean(map, PO.class);
        Assert.assertEquals(123, vo.id);
        
        SerializeWriter out = new SerializeWriter();

        try {
            SerializeConfig config = new SerializeConfig();
            JSONSerializer serializer = new JSONSerializer(out, config);
            config.put(PO.class, new JavaBeanSerializer(PO.class, PO.class.getModifiers(), Collections.singletonMap("id", (String) null), false, true, true, true, null));

            serializer.write(vo);

            Assert.assertEquals("{}", out.toString());
        } finally {
            out.close();
        }

        
    }

    public static interface VO {

        void setId(int value);

        int getId();

        ClassLoader getClassLoader();
    }

    public static class Entity {

        private int    id;
        protected String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        protected String getName() {
            return name;
        }

        protected void setName(String name) {
            this.name = name;
        }

        public ClassLoader getClassLoader() {
            return Entity.class.getClassLoader();
        }
    }
    
    private static class PO {
        public int id;
    }

    public static class TestLoader extends ClassLoader {

        public TestLoader(){
            super(null);
        }

        public URL getResource(String name) {
            return null;
        }

        public Class<?> loadClass(String name) throws ClassNotFoundException {
            throw new ClassNotFoundException();
        }
    }
    
    public static void addBaseClassMappings() throws Exception {
        Field field = TypeUtils.class.getDeclaredField("mappings");
        field.setAccessible(true);
        
        ConcurrentMap<String, Class<?>> mappings = (ConcurrentMap<String, Class<?>>) field.get(null);
        mappings.put("byte", byte.class);
        mappings.put("short", short.class);
        mappings.put("int", int.class);
        mappings.put("long", long.class);
        mappings.put("float", float.class);
        mappings.put("double", double.class);
        mappings.put("boolean", boolean.class);
        mappings.put("char", char.class);

        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);

        mappings.put(HashMap.class.getName(), HashMap.class);
    }
    
    public static void clearClassMapping() throws Exception {
        Field field = TypeUtils.class.getDeclaredField("mappings");
        field.setAccessible(true);
        
        ConcurrentMap<String, Class<?>> mappings = (ConcurrentMap<String, Class<?>>) field.get(null);
        
        mappings.clear();
        addBaseClassMappings();
    }
    
    public static void addClassMapping(String className, Class<?> clazz) throws Exception {
        if (className == null) {
            className = clazz.getName();
        }
        
        Field field = TypeUtils.class.getDeclaredField("mappings");
        field.setAccessible(true);
        
        ConcurrentMap<String, Class<?>> mappings = (ConcurrentMap<String, Class<?>>) field.get(null);
        

        mappings.put(className, clazz);
    }
}
