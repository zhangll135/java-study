package ConcTest.Part9;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;

public class MainTest {
    public static void main(String[] args){
        TestInstaller.process(MainTest.class);
    }
    @Test(-1)
    public void LongTimeFrameTest(){
        new LongTimeFram();
    }
    @Test(2)
    public void LongTimeFrameTest2(){
        new LongTimeFram2();
    }
}
