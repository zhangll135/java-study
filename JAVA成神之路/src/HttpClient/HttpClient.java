package HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;
import java.util.Map;

public class HttpClient {
    public static void execute(String ...s){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<s[0].length();i++){
            if(s[0].charAt(i)<256)
                sb.append(s[0].charAt(i));
            else{
                try {
                    byte temp[] = s[0].substring(i, i + 1).getBytes("UTF-8");
                    for(int j=0;j<temp.length;j++){
                        sb.append("%"+Integer.toString(temp[j]&0xff,16));
                    }
                }catch (Exception e){}
            }
        }
        s[0] = sb.toString();
        try {
            Socket socket = new Socket();
            if(s.length<3){
                s = new String[]{s[0],"45.32.12.136","8089"};
            }
            socket.connect(new InetSocketAddress(s[1],Integer.parseInt(s[2])));

            socket.getOutputStream().write(("GET /"+s[0]+" HTTP/1.1\r\nContent-type:text/html\r\n\r\n").getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            String line;
            while((line = in.readLine()) != null){
                System.out.println(line);
            }
            System.out.println();
            socket.close();
        }catch (IOException e){
            System.out.println("----------------------------------end---------------------------------------");
        }
    }
    public static String sendGet(String url,String param){
        String result="";
        String urlName = url+"?"+param;
        try{
            URLConnection conn = new URL(urlName).openConnection();
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("connection","keep-alive");
            conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36" +
                    " (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.setRequestProperty("content-type","text/html");
            conn.setRequestProperty("content-length","0");
            conn.connect();

            Map<String, List<String>>map = conn.getHeaderFields();
            for(String name:map.keySet())
                System.out.println(name+"-->"+map.get(name));

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while((line = in.readLine()) != null){
                System.out.println(line);
                result +="\n" + line;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String sendPost(String url,String param){
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.setRequestProperty("content-type","text/html");
            conn.setRequestProperty("content-length",""+param.length());
            //post设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while((line = in.readLine()) != null){
                System.out.println(line);
                result +="\n" + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
