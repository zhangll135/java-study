package Test;

import java.util.Stack;
/*
后序遍历：实现非递归排序
 */
public class PostOrder {
    public static void main(String[] args){
        int A[] = new int[]{1,2,8,0,3,2,1};
        new Sort().noRecursion(A);
        PRINT.execute(A);
    }

}
class Sort{
    Stack<Integer>stack = new Stack<>();
    Stack<Integer>post = new Stack<>();
    public int[] recursion(int A[]){
        return recursion(A,0,A.length-1,new int[A.length]);
    }
    private int[] recursion(int A[],int p,int q,int B[]){
        if(p<q) {
            int mid = (p + q) / 2;
            if (p < mid)
                recursion(A, p, mid, B);
            if (mid + 1 < q)
                recursion(A, mid + 1, q, B);
            sor(A, B, p, q, mid);
        }
        return A;
    }


    public int[] noRecursion(int A[]){
        return noRecursion(A,0,A.length-1,new int[A.length]);
    }
    private int[] noRecursion(int A[],int p,int q,int B[]){
        int mid;
        stack.push(q);
        stack.push(p);
        while (!stack.isEmpty()) {
            //访问根节点
            p = stack.pop();
            q = stack.pop();
            post.push(q);
            post.push(p);

            mid = (p + q) / 2;
            //先遍历右树、再遍历左
            if(p<mid){
                stack.push(mid);
                stack.push(p);
            }
            if(mid+1<q) {
                stack.push(q);
                stack.push(mid + 1);
            }
        }
        while (!post.isEmpty()){
            p = post.pop();
            q = post.pop();
            sor(A,B,p,q,(p+q)/2);
        }
        return A;
    }

    private void sor(int A[],int B[],int p,int q,int mid){
        int i=p,j=mid+1;
        for(int k=p;k<=q;k++){
            if(i<=mid) {
                try {
                    if (j <= q && A[i] > A[j])
                        B[k] = A[j++];
                    else
                        B[k] = A[i++];
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else
                B[k] = A[j++];
        }
        for(i=p;i<=q;i++)
            A[i] = B[i];
    }
}
