package ConcTest.Part11;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Hello {
    //补充修改路径，为null表示当前路径下的类；
    private String path=".";

    private Class<?> className=null;
    private Object object=null;
    public Hello()throws Exception{
        //路径不空，则加载指定类
        if (path!=null){
            URL[] urls = new URL[]{new File(path).toURI().toURL()};
            URLClassLoader loader = new URLClassLoader(urls, null);
            className = loader.loadClass(this.getClass().getCanonicalName());
            object = className.newInstance();
        }
    }
    public void show(Object... args)throws Exception{
        if(className==null){
            //此处为补充代码...
            System.out.println("hello");
        }else {
            className.getMethod("show").invoke(object,args);
        }
    }
}
