package fzw.demo01;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author fzw
 * @description
 **/
public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.test();
    }

    public void test() {
        try {
            MyClassLoader loader = new MyClassLoader();
            Class<?> aClass = loader.loadClass("Hello", "src/main/resources/Hello.xlass");
            Object instance = aClass.getDeclaredConstructor().newInstance();
            Method method = aClass.getMethod("hello");
            method.invoke(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
