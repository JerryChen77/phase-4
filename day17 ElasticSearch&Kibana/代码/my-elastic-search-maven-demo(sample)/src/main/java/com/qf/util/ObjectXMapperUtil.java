package com.qf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectXMapperUtil {

  /***
   * 将对象转换为map对象
   * @param thisObj 对象
   * @return
   */
  public static Map<String, Object> objectToMap(Object thisObj) {
    Map<String, Object> map = new HashMap<String, Object>();
    @SuppressWarnings("rawtypes")
    Class c;
    try {
      c = Class.forName(thisObj.getClass().getName());
      //获取所有的方法
      Method[] m = c.getMethods();
      for (int i = 0; i < m.length; i++) {   //获取方法名
        String method = m[i].getName();
        //获取get开始的方法名
        if (method.startsWith("get") && !method.contains("getClass")) {
          try {
            //获取对应对应get方法的value值: Method.invoke(thisObj) book.getId()  book.getName() :  "name":List [0,1]==>"0 | 1"
            Object value = m[i].invoke(thisObj);
            if (value != null) {
              //截取get方法除get意外的字符 如getUserName-->UserName
              String key = method.substring(3);
              //将属性的第一个值转为小写
              key = key.substring(0, 1).toLowerCase() + key.substring(1);
              //将属性key,value放入对象
              if (value.getClass().isArray()) {
                String values = "";
                for (Object o : (Object[]) value) {
                  values += o.toString() + "|";
                }
                map.put(key, values);//"name":"0 | 1"
              } else {
                map.put(key, value);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            System.out.println("error:" + method);
          }
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return map;
  }


  /**
   * 将Map对象通过反射机制转换成Bean对象
   *
   * @param map   存放数据的map对象
   * @param clazz 待转换的class
   * @return 转换后的Bean对象
   * @throws Exception 异常
   */
  public static Object mapToObject(Map<String, Object> map, Class<?> clazz) throws Exception {
    Object obj = clazz.newInstance();
    if (map != null && map.size() > 0) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String propertyName = entry.getKey();       //属性名
        Object value = entry.getValue();
        String setMethodName = "set"
          + propertyName.substring(0, 1).toUpperCase()
          + propertyName.substring(1);
        //获取属性对应的对象字段
        Field field = getClassField(clazz, propertyName);
        if (field == null)
          continue;
        //获取字段类型
        Class<?> fieldTypeClass = field.getType();
        //根据字段类型进行值的转换
        value = convertValType(value, fieldTypeClass);
        try {
          //调用对象对应的set方法
          clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
      }
    }
    return obj;
  }

  /**
   * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
   *
   * @param clazz     指定的class
   * @param fieldName 字段名称
   * @return Field对象
   */
  private static Field getClassField(Class<?> clazz, String fieldName) {
    if (Object.class.getName().equals(clazz.getName())) {
      return null;
    }
    Field[] declaredFields = clazz.getDeclaredFields();
    for (Field field : declaredFields) {
      if (field.getName().equals(fieldName)) {
        return field;
      }
    }

    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null) {// 简单的递归一下
      return getClassField(superClass, fieldName);
    }
    return null;
  }

  /**
   * 将Object类型的值，转换成bean对象属性里对应的类型值
   *
   * @param value          Object对象值
   * @param fieldTypeClass 属性的类型
   * @return 转换后的值
   */
  private static Object convertValType(Object value, Class<?> fieldTypeClass) {
    Object retVal = null;
    if (Long.class.getName().equals(fieldTypeClass.getName())
      || long.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Long.parseLong(value.toString());
    } else if (Integer.class.getName().equals(fieldTypeClass.getName())
      || int.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Integer.parseInt(value.toString());
    } else if (Float.class.getName().equals(fieldTypeClass.getName())
      || float.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Float.parseFloat(value.toString());
    } else if (Double.class.getName().equals(fieldTypeClass.getName())
      || double.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Double.parseDouble(value.toString());
    } else {
      retVal = value;
    }
    return retVal;
  }

//    public static void main(String... args) throws Exception{
//        BankVo bv = new BankVo();
//        bv.setBankCard("11111111111111");
//        bv.setBankImgUrl("222222222222");
//        String[] aa = new String[]{"1","2"};
//        bv.setAa(aa);
//        //对象转map
//        Map<String, Object> map = objectToMap(bv);
//        System.out.println(getPlanText(map));
//        for (Map.Entry entry : map.entrySet()) {
//            System.out.println(entry);
//            System.out.println(entry.getKey() +":" + entry.getValue());
//        }
  //map转对象
//        BankVo newUser = (BankVo) mapToObject(map,BankVo.class);
//        System.out.println(newUser.getBankCard());
//        System.out.println(newUser.getBankImgUrl());
//        System.out.println(newUser.getAa());
//
//        }


  /**
   * sign 参数排序去空
   *
   * @return 生成sign 明文串
   */
  public static String getPlanText(Map<String, Object> map) {
//        Map<String, String> map = new TreeMap<String, String>();


    //这里将map.entrySet()转换成list
    List<Map.Entry<String, Object>> list;
    list = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
    //然后通过比较器来实现排序
    Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
      //升序排序
      public int compare(Map.Entry<String, Object> o1,
                         Map.Entry<String, Object> o2) {
        return o1.getKey().compareTo(o2.getKey());
      }
    });
    StringBuffer s = new StringBuffer();
    for (Map.Entry<String, Object> m : list) {
      s.append(m.getKey()).append("=").append(m.getValue()).append("&");
    }
    if (s.length() > 1) s.setLength(s.length() - 1);
    String planText = s.toString();
    System.out.println("request planText [" + planText + "]");
    return planText;
  }

}
