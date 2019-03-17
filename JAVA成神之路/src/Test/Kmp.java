package Test;
/*
Test 只是理论值比较好，实际效果不如朴素字符串
10次的平均值：将字符串转成byte[]能提高效率
 */

public class Kmp {
    public static void main(String[] args){
        // 准备数据
        String o = "aaaaaaaaaaaaaaaaaaaabcdefaabcdefa";
        String s = "ab";

        StringBuilder sb = new StringBuilder(s);
        for(int i=0;i<5;i++)      //子串至少要4次方能有优势
            sb.append(sb);
        s = new String(sb)+"c";

        //sb = new StringBuilder(s); //原串重复包含子串一部分
        for(int i=0;i<10;i++)
            sb.append(sb);
        o = new String(sb)+s;
        // 进行计时
        long time[] = new long[3];
        for(int i=0;i<100;i++) {
            long t = System.nanoTime();
            int a = Kmp.find(o, s);
            long t1 = System.nanoTime();
            int b = Kmp.find2(o, s);
            long t2 = System.nanoTime();
            int c = o.indexOf(s);
            long t3 = System.nanoTime();
            time[0] += t1-t;
            time[1] += t2-t1;
            time[2] += t3-t2;
            //System.out.println(a+"\t"+b+"\t"+c);
        }
        System.out.println("\n总时间：");
        System.out.println("kmp算法："+time[0]);
        System.out.println("朴素法 ："+time[1]);
        System.out.println("内置法 ："+time[2]);

    }
    public static int find(String o1,String s1){
        byte o[] = o1.getBytes();
        byte s[] = s1.getBytes();
        int pi[] = kmpmode(s);
        int p = 0;
        for(int i=0;i<o.length;i++){
            while (p>0&&s[p]!=o[i])
                p = pi[p-1];
            if(s[p]==o[i])
                p++;
            if(p==s.length)
                return i+1-s.length;
        }
        return -1;
    }
    public static int find2(String o1,String s1){
        byte o[] = o1.getBytes();
        byte s[] = s1.getBytes();
        int cnt = o.length-s.length+1;
        for(int i=0;i<cnt;i++){
            while (o[i]!=s[0]&&i<cnt)
                i++;
            int j=1;
            if(i<cnt) {
                while (j < s.length && o[i + j] == s[j])
                    j++;
            }
            if(j==s.length)
                return i;
        }
        return -1;
    }
    public static int[] kmpmode(byte[] s){
        int pi[] = new int[s.length];
        int p = 0;
        for(int i=2;i<=s.length;i++){
            while (p>0&&s[p]!=s[i-1])
                p = pi[p-1];
            if(s[p]==s[i-1])
                p++;
            pi[i-1] = p;
        }
        return pi;
    }
}
