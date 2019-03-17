package JavaBasic;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.Introspector;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Part10 {
    public static void main(String[] args){
        //运行时，利用反射注解
        new ButtonFrame().setVisible(true);
    }
}
class ButtonFrame extends JFrame{
    private JPanel jPanel;
    private JButton yellowButton;
    private JButton blueButton;
    private JButton redButton;
    public ButtonFrame(){
        setSize(400,300);
        jPanel = new JPanel();
        add(jPanel);

        yellowButton = new JButton("yellow");
        blueButton = new JButton("blue");
        redButton = new JButton("red");
        jPanel.add(yellowButton);
        jPanel.add(blueButton);
        jPanel.add(redButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //注解解释工具
        ActionListenerInstaller.processAnnotation(this);
    }
    @ActionListennerFor(source = "yellowButton")
    public void yellowBackground(){
        jPanel.setBackground(Color.YELLOW);
    }
    @ActionListennerFor(source = "blueButton")
    public void blueBackground(){
        jPanel.setBackground(Color.BLUE);
    }
    @ActionListennerFor(source = "redButton")
    public void redBackground(){
        jPanel.setBackground(Color.RED);
    }
}
class ActionListenerInstaller{
    public static void processAnnotation(Object o){
        try{
            Class<?> c = o.getClass();
            for(Method m:c.getDeclaredMethods()){
                ActionListennerFor a = m.getAnnotation(ActionListennerFor.class);
                if(a!=null){
                    Field f = c.getDeclaredField(a.source());
                    f.setAccessible(true);
                    addListenner(f.get(o),o,m);
                }
            }
        }catch (ReflectiveOperationException e){
            e.printStackTrace();
        }
    }
    public static void addListenner(Object o, final Object parent, final Method m)throws ReflectiveOperationException {
        //代理ActionListener类行为
        Object listener = Proxy.newProxyInstance(null, new Class[]{java.awt.event.ActionListener.class},
                (Object proxy, Method method, Object[] args) -> m.invoke(parent));
        o.getClass().getMethod("addActionListener", ActionListener.class).invoke(o, listener);
    }
}
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface  ActionListennerFor{
    String source();
}
//-------------------------------------------------------------------------------------------------

@SupportedAnnotationTypes("JavaBasic.Property")
class BeanInfoAnnotationProcessor extends AbstractProcessor {
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        for(TypeElement t:annotations){
            Map<String,Property> map = new LinkedHashMap<>();
            String beanClassName = null;
            for(Element e:roundEnv.getElementsAnnotatedWith(t)){
                String mname = e.getSimpleName().toString();
                String[] prifixes = new String[]{"set","get","is"};
                boolean found = false;
                for(int i=0;!found&&i<prifixes.length;i++){
                    if(mname.startsWith(prifixes[i])){
                        found=true;
                        map.put(Introspector.decapitalize(mname.substring(prifixes.length)), e.getAnnotation(Property.class));
                    }
                }
                if(!found)
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,"@Property must applied to getX,setX,isX");
                else if(beanClassName==null)
                    beanClassName=((TypeElement) e.getEnclosingElement()).getQualifiedName().toString();
            }
            try{
                if(beanClassName!=null)
                    writeBeanInfoFile(beanClassName,map);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return true;
    }
    private void writeBeanInfoFile(String beanClassName,Map<String,Property>map)throws IOException{
        JavaFileObject file = processingEnv.getFiler().createSourceFile(beanClassName+"BeanInfo");
        PrintWriter out = new PrintWriter(file.openWriter());
        int i=beanClassName.indexOf(".");
        if(i>0){
            out.print("package ");
            out.print(beanClassName.substring(0,i));
            out.println(";");
        }
        out.print("public class ");
        out.print(beanClassName.substring(i+1));
        out.println("BeanInfo extends java.beans.SimpleBeanInfo{");
        out.println("    public java.beans.PropertyDescriptor[] getPropertyDescriptors(){");
        out.println("        try{");
        for(Map.Entry<String,Property>e:map.entrySet()){
            out.print("        java.beans.PropertyDescriptor ");
            out.print(e.getKey());
            out.println("Descriptor");
            out.print("            =new java.beans.PropertyDescriptor(\"");
            out.print(e.getKey());
            out.print("\",");
            out.print(beanClassName);
            out.println(".class);");
            String ed = e.getValue().editor().toString();
            if(!ed.equals("")){
                out.print("        ");
                out.print(e.getKey());
                out.print("Descriptor.setPropertyEditorClass(");
                out.print(ed);
                out.println(".class);");
            }
        }
        out.print("        return new java.beans.PropertyDescriptor[]{");
        boolean first = true;
        for(String p:map.keySet()){
            if(first)
                first=false;
            else
                out.print(",");
            out.println();
            out.print("            ");
            out.print(p);
            out.print("Descriptor");
        }
        out.println();
        out.println("             };");
        out.println("        }");
        out.println("        catch(java.beans.IntrospectionException e){");
        out.println("              e.printStackTrace();");
        out.println("              return null;");
        out.println("        }");
        out.println("    }");
        out.println("}");
        out.close();
    }
}
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@interface Property{
    String editor() default "";
}

class sorceAnnotation{
    private ButtonFrame name;
    private int numer;

    @Property(editor = "ButtonFrame")
    public void setName(ButtonFrame name) {
        this.name = name;
    }

    @Property
    public int getNumer() {
        return numer;
    }
}

