package ConcTest.Part11;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class MainTest {
    public static void main(String[] args){
        TestInstaller.process(MainTest.class);
    }

    @Test(-1)
    public void FileClassLoaderTest()throws Exception{
        Class Hello =  new FileClassLoader(".").findClass("ConcTest.Part11.Hello");
        Object hello = Hello.newInstance();
        Hello.getMethod("show").invoke(hello);

        Class Hello1 =  new FileClassLoader("src").findClass("ConcTest.Part11.Hello");
        Object hello1 = Hello1.newInstance();
        Hello1.getMethod("show").invoke(hello1);


        URL[] urls= new URL[]{new File(".").toURI().toURL()};
        URLClassLoader loader = new URLClassLoader(urls,null);
        Class Hello2 = loader.loadClass("ConcTest.Part11.Hello");
        Object hello2 = Hello2.newInstance();
        Hello2.getMethod("show").invoke(hello2);
    }
    @Test(-2)
    public void fileTest()throws Exception{
        String rootDir=".";
        //创建自定义文件类加载器
        FileClassLoader loader = new FileClassLoader(rootDir);
        FileClassLoader loader2 = new FileClassLoader(rootDir);

        try {
            //加载指定的class文件,调用loadClass()
            Class<?> object1=loader.loadClass("ConcTest.Part11.Hello");
            Class<?> object2=loader2.loadClass("ConcTest.Part11.Hello");

            System.out.println("loadClass->obj1:"+object1.hashCode());
            System.out.println("loadClass->obj2:"+object2.hashCode());

            //加载指定的class文件,直接调用findClass(),绕过检测机制，创建不同class对象。
            Class<?> object3=loader.findClass("ConcTest.Part11.Hello");
            Class<?> object4=loader2.findClass("ConcTest.Part11.Hello");

            System.out.println("loadClass->obj3:"+object3.hashCode());
            System.out.println("loadClass->obj4:"+object4.hashCode());

            /**
             * 输出结果:
             * loadClass->obj1:644117698
             loadClass->obj2:644117698
             findClass->obj3:723074861
             findClass->obj4:895328852
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test(3)
    public void HelloTest()throws Exception{
        Hello hello = new Hello();
        hello.show();
    }
}
