package com.acc.common.util;

import android.content.Context;

import com.acc.common.util.annotation.DictField;
import com.data.BaseDicValueDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanUtils {
    /**
     * 通过属性名拷贝并生成一个泛型对象
     *
     * @param object ： 源对象
     * @param clazz  ： 目标对象
     * @param <T>    ：  目标类型
     * @return ：返回一个拷贝对象
     */
    public static <T> T copyBeanToBean(Object object, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            List<Field> fields = getAllFields(clazz);// clazz.getDeclaredFields();
            for (Field field : fields) {
                if (hashField(object, field)) {// 查看对象属性是否存在
                    Object beanValue = Reflections.getFieldValue(object, field.getName());
                    if (beanValue != null) {
                        try {
                            Reflections.setFieldValue(obj, field.getName(), beanValue);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 把一个对象属性名拷贝到另外一个对象
     *
     * @param object  ： 源对象
     * @param object2 ： 目标对象
     */
    public static void copyBeanToBean(Object object, Object object2) {
        if (object == null || object2 == null)
            return;
        Field[] declaredFields = object2.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (hashField(object, field)) {// 查看对象属性是否存在
                Object beanValue = Reflections.getFieldValue(object, field.getName());
                if (beanValue != null)
                    Reflections.setFieldValue(object2, field.getName(), beanValue);
            }
        }
    }

    /**
     * 判断对象是否存在同名属性
     *
     * @param object
     * @param field
     * @return
     */
    private static boolean hashField(Object object, Field field) {
        if (object == null || field == null)
            return false;
        List<Field> declaredFields = getAllFields(object.getClass());
        for (Field field2 : declaredFields) {
            //属性名称相同并且属性类型相同
            if (field2.getName().equals(field.getName()) && field.getType() == field2.getType() && !field.getName().equals("serialVersionUID") && !field2.getName().equals("serialVersionUID"))
                return true;
        }
        return false;
    }

    /**
     * 获得一个对象的全部属性，包括父类属性
     *
     * @param classz
     * @return
     */
    private static List<Field> getAllFields(Class classz) {
        List<Field> fieldList = new ArrayList<>();
        while (classz != null) {
            fieldList.addAll(Arrays.asList(classz.getDeclaredFields()));
            classz = classz.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 批量转化List 数据字典
     *
     * @param list
     */
    public static void translateListBeanDict(List list, Context context) {
        if (list != null && list.size() > 0) {
            for (Object object : list)
                translateBeanDict(object, context);
        }
    }

    /**
     * 国情 Bean对象数据字典
     *
     * @param object
     */
    public static void translateBeanDict(Object object, Context context) {
        if (object == null)
            return;
        BaseDicValueDao baseDicValueDao = new BaseDicValueDao(context);
        List<Field> fildList = getAllFields(object.getClass());
        for (Field field : fildList) {
            DictField annotation = field.getAnnotation(DictField.class);
            if (annotation != null) {
                Object dictValue = Reflections.getFieldValue(object, field.getName());
                if (dictValue != null) {
                    String label = baseDicValueDao.queryLableBydictTypeAndValue(dictValue.toString(), annotation.dictType(), dictValue.toString());
                    Reflections.setFieldValue(object, field.getName(), label);
                }
            }
        }
    }
}
