package ConcTest;

import java.io.IOException;
import java.io.InputStream;

public class SocketReader {//带缓存的行读取器（字符串和文本量）
    private InputStream inputStream;
    private int maxLength = Integer.MAX_VALUE;//maxLength==最大值，单字节读取行；maxlength=const，最大读取长度。
    final int buffsize = 1024;
    private byte buff[]=new byte[buffsize];
    private int datalen=0;
    private int bufflength=0;
    //-------------------------------------------------------------------------
    public void setInputStream(InputStream in) {
        this.inputStream = in;
    }
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        datalen=bufflength=0;
    }
    public int getDatalength(){
        return datalen==0?maxLength:datalen;
    }
    public int readLine(byte[] outbuff)throws IOException {
        if(maxLength==Integer.MAX_VALUE)//强行读取一行
            return readLine(inputStream,outbuff);
        //清除旧值
        for(int i=0,j=datalen;j<bufflength;)
            buff[i++]=buff[j++];
        //读取缓存值
        int cnt=0;
        if (maxLength > 0)
            cnt = inputStream.read(buff, bufflength - datalen, Math.min(maxLength, buffsize - bufflength + datalen));
        bufflength = bufflength-datalen;
        if(cnt!=-1) {
            bufflength += cnt;
            maxLength-=cnt;
        }
        if(bufflength<=0){
            bufflength=datalen=0;
            return 0;
        }
        //返回参数
        for(int i=0;i<bufflength;i++) {
            if(buff[i]=='\r'){
                if(i==bufflength-1) {
                    return (datalen=i);//持有最后字符
                }
                else if(buff[i+1]=='\n'){
                    outbuff[i]=buff[i];
                    outbuff[i+1]=buff[i+1];
                    return (datalen=i+2);
                }
            }
            outbuff[i]=buff[i];
        }
        return (datalen=bufflength);
    }
    public String readLine()throws IOException{//  \r\n结尾
        byte buff[] = new byte[buffsize];
        int cnt = readLine(buff);
        if(cnt>1&&buff[cnt-1]=='\n'&&buff[cnt-2]=='\r')
            return new String(buff, 0, cnt - 2,"UTF-8");
        return new String(buff, 0, cnt,"UTF-8");
    }
    public String str2utf8(String str)throws IOException{
        byte buff[] = new byte[str.length()];
        int cnt=0;
        for(int i=0;i<str.length();){
            if(str.charAt(i)!='%')
                buff[cnt++] = (byte) str.charAt(i++);
            else{
                buff[cnt++] = (byte) Integer.parseInt(str.substring(i+1,i+3),16);
                i+=3;
            }
        }
        return new String(buff,0,cnt,"UTF-8");
    }
    private int readLine(InputStream in,byte[] buff)throws IOException{
        final int buffsize = 1024;
        //读第一个字符:前一次持有的回车符
        if(buff[buffsize-1]=='\r') {
            buff[0] = '\r';
            buff[buffsize-1]=0;
        }
        else
            in.read(buff,0,1);
        //读余下1023字符
        for(int cnt=1;cnt<buffsize;){
            in.read(buff, cnt++, 1);
            if(buff[cnt-1]=='\n'&&buff[cnt-2]=='\r')//回车换行结束符,未满1024字节
                return cnt;
        }
        //读满1024字节，分两种情况决定是否持有第1024字节
        if(buff[buffsize-1]=='\r')    //读满1024字节且最后一字节\r，只返回1023（保留一个）
            return buffsize-1;
        return buffsize;               //返回所读字节数1024
    }
}
