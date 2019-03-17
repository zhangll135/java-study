package Test;

public class ShiftK {
    public static void main(String[] args){
        int A[]=new int[]{1,2,3,4,5,6,7,8};
        new ShiftK().shift(A,3);
        PRINT.execute(A);
    }
    public void shift(int[] o,int k){
        if(k<=0||o==null||o.length<0)
            return;
        k = k%o.length;
        if(k>o.length/2) {//转化为左移动
            shiftleft(o, o.length - k);
            return;
        }
        //右移k位：1保存最后k位
        int temp[] = new int[k];
        for(int i=0,j=o.length-k;i<k;i++)
            temp[i] =o[j++];
        //填充末尾k位
        for(int i=0;i<k;i++){
            for (int j=o.length-i-1-k;j>=0;j-=k)
                o[j+k] = o[j];
        }
        // 填充开头k位
        for(int i=0;i<k;i++)
            o[i]=temp[i];
    }
    public void shiftleft(int[] o,int k){
        // 保存开头k位
        int temp[] = new int[k];
        for(int i=0;i<k;i++)
            temp[i] =o[i];
        //填充开头k位
        for(int i=0;i<k;i++){
            for (int j=i+k;j<o.length;j+=k)
                o[j-k] = o[j];
        }
        // 填充末尾k位
        for(int i=0,j=o.length-k;i<k;i++)
            o[j++]=temp[i];
    }
}
