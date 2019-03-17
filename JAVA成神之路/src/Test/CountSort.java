package Test;
/*计数排序：数组中个取值为k=O（n）不大时，复杂度O（k+n）
 */
public class CountSort {
    public static void main(String[] args){
        int A[]=new int[]{2,5,3,2,3,3,4,6,3,5,1,2,7,8};
        PRINT.execute(CountSort.execute(A,8));
    }
    public static int[] execute(int A[],int k){
        int B[] = new int[A.length];
        int C[] = new int[k+1];
        //统计A[i]频率
        for(int i=0;i<A.length;i++)
            C[A[i]]++;
        //统计累计频率
        for(int i=1;i<=k;i++)
            C[i]+=C[i-1];
        //排序
        for (int i=A.length-1;i>=0;i--)
            B[--C[A[i]]] = A[i];
        return B;
    }
}
