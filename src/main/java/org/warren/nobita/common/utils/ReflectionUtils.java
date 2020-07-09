package org.warren.nobita.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 初始化对外暴露服务接口的实现类
     * */
    public static Object loadClass( String implement_name){
        try {
            Class<?> clazz = Class.forName(implement_name);
            return ReflectionUtils.newInstance(clazz);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<Method> getPublicMethods(Class<T> clazz){
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method-> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toList());
    }

    public static Object invoke(Method method, Object obj, Object...parameter) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(obj, parameter);
    }

    public static Method getMethod(String className, String methodName, String[] paramsClassName){
        try {
            if (paramsClassName ==null){
                return Class.forName(className).getMethod(methodName);
            }
            else {
                Class<?>[] params = new Class[paramsClassName.length];
                for (int i = 0; i < params.length; i++) {
                    params[i] = Class.forName(paramsClassName[i]);
                }
                return Class.forName(className).getMethod(methodName,params);
            }

        }catch (ClassNotFoundException | NoSuchMethodException e){
            e.printStackTrace();
        }
        return null;
    }

}
