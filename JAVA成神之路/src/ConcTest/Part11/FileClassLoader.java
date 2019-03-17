package ConcTest.Part11;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class FileClassLoader extends  ClassLoader{
    private String rootDir;

    public FileClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }
    // 编写获取类的字节码并创建class对象的逻辑
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //...省略逻辑代码
        byte[] data = null;
        try{
            data = getClassData(name);
            if(data!=null)
                return defineClass(name,data,0,data.length);
            else
                return  Class.forName(name);
        }catch (IOException e){
            return Class.forName(name);
        }
    }
    //编写读取字节流的方法
    private byte[] getClassData(String className) throws IOException{
        // 读取类文件的字节
        //省略代码....
        File file = new File(rootDir+File.separatorChar+className.replace('.','/')+".class");
        byte[] result=null;
        // 当前路径文件存在，则读取
        if(file.exists()){
            result = new byte[(int)file.length()];
            InputStream io = new FileInputStream(file);
            io.read(result);
        }
        //返回null
        return  result;
    }
}
