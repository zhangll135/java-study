package ConcTest.MyAnnotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestInstaller {
    public static void process(Class<?> c1){
        try{
            Object o = c1.newInstance();
            Map<Integer,Method> map = new HashMap<>();
            for(Method m : c1.getDeclaredMethods()){
                Test a = m.getAnnotation(Test.class);
                if(a!=null&&a.value()>0){
                    map.put(a.value(),m);
                }
            }
            for(Map.Entry<Integer,Method>m:map.entrySet()){
                while(Thread.activeCount()>2)
                    Thread.yield();
                System.out.println("------------"+m.getKey()+" "+m.getValue().getName()+"------------");
                m.getValue().invoke(o);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
