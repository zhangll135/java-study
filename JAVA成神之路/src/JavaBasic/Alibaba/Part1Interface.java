package JavaBasic.Alibaba;

import java.nio.ByteBuffer;

public interface Part1Interface {
    ByteBuffer data=ByteBuffer.allocate(1024);
    default void read(){
        System.out.println(data);
    }
    default void init(String data1){
        data.put(data1.getBytes());
        data.flip();
        data.flip();
        System.out.println(data.limit());
    }
}
