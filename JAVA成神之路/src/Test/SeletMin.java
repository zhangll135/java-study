package Test;
/*
求数组中第i小元素
 */
public class SeletMin {
    public static void main(String[] argd){
        int A[]=new int[]{3,4,3,6,1,0,1};
        SeletMin.fastSort(A);
        PRINT.execute(A);

        System.out.println(SeletMin.selectNMin(null,0));
        System.out.println(SeletMin.selectNMin(A,0));
        System.out.println(SeletMin.selectNMin(A,A.length+1));
        A = new int[]{3,4,3,6,1,0,1};
        for(int i=0;i<A.length;i++)
            System.out.println(SeletMin.selectNMin(A,A.length-i));
    }
    public static void fastSort(int[] A){
        fastSort(A,0,A.length-1);
    }
    private static void fastSort(int[] A,int p,int q){
        int k = sortHelp(A,p,q);
        if(p+1<k)
            fastSort(A,p,k-1);
        if(k+1<q)
            fastSort(A,k+1,q);
    }
    public  static int selectNMin(int[]A,int k){
        if(A==null||k<=0||k>A.length||A.length==0)   //边界处理
            return -1;
        return selectNMin(A,0,A.length-1,k);
    }
    public    static int selectNMin(int[]A,int p,int q,int k){
        if(p<q&&k>0) {          //返回第A[p..q]中第k小数
            int m = sortHelp(A, p, q);
            int x = m - p + 1;
            if (k == x)
                return A[m];
            if(x>k)
                return selectNMin(A,p,m-1,k);
            return selectNMin(A,m+1,q,k-x);
        }
        if(p==q&&k==1)     //边界处理
            return A[p];
        return -1;        //逻辑错误：p>q，k超范围
    }
    private static int sortHelp(int A[],int p,int q){
        int i=p,j=q,x=A[i];
        while (i<j){
            while (i<j&&A[j]>=x)
                j--;
            if(i<j) {
                A[i++] = A[j];
                while (i < j && A[i] < x)
                    i++;
                A[j--] = A[i];
            }
        }
        A[i]=x;
        return i;
    }
}
