package Test;
/*
快排不是稳定排序，可以实现非递归。
因为是原址排序，所以递归也快
 */

import java.util.Stack;

public class FastSort {
    public static void main(String[] args){
        int[] temp = new int[]{3,4,6,2,1,6,7,3,2,7,9,0};
        PRINT.execute(temp);

        System.out.println("--------快速排序（稳定）---------------");
        PRINT.execute(FastSort.execute(temp));
        PRINT.execute(temp);
        System.out.println("--------快速排序（不稳定）---------------");
        temp = new int[]{3,4,6,2,1,6,7,3,2,7,9,0};
        PRINT.execute(FastSort.execute1(temp,1,temp.length,new int[0]));
        PRINT.execute(temp);
        System.out.println("--------快速排序（非递归）---------------");
        temp = new int[]{3,4,6,2,1,6,7,3,2,7,9,0};
        PRINT.execute(FastSort.noRecursion(temp));
        PRINT.execute(temp);
    }
    public static int[] execute(int[] o){
        int index[] = new int[o.length];
        for (int i = 0; i < index.length; i++)
            index[i] = i + 1;
        return sortHelp(o,0,o.length-1,index);
    }
    public static int[] noRecursion(int[] o){
        Stack<Integer> stack=new Stack<>();
        int p=0;
        int q=o.length-1;
        if(p<q) {
            stack.push(q);
            stack.push(p);
        }
        int index[] = new int[o.length];
        for (int i = 0; i < index.length; i++)
            index[i] = i + 1;
        while (!stack.empty()){    //先序遍历（根-左树-右树）
            int i=stack.pop();
            int j=stack.pop();
            int k = noRecursionHelp(o,i,j,index);
            if(k+1<j){
                stack.push(j);
                stack.push(k+1);
            }
            if(i+1<k){
                stack.push(k-1);
                stack.push(i);
            }
        }
        return index;
    }
    private static int noRecursionHelp(int[] o,int star,int end,int[] index){
        if(star<end) {
            int x = o[end];
            int i = star - 1;
            for (int j = star; j < end; j++) {
                if (o[j] <= x) {
                    i++;
                    swap(o, i, j);
                    swap(index,i,j);
                }
            }
            swap(o, end, i+1);
            swap(index,end,i+1);
            return i+1;
        }
        return star;
    }
    private static int[] sortHelp(int[] o,int star,int end,int[] index){
        if(star<end){
            int x = o[end];
            int i=star-1;
            for(int j=star;j<end;j++) {
                if (o[j] <= x) {
                    i++;
                    swap(o, i, j);
                    swap(index,i,j);
                }
            }
            int p=i+1;
            swap(o,end,p);
            swap(index,end,p);
            sortHelp(o,star,p-1,index);
            sortHelp(o,p+1,end,index);
        }
        return index;
    }
    private static void swap(int[] o,int i,int j){
        int temp = o[i];
        o[i] = o[j];
        o[j] = temp;
    }
    public static int[] execute1(int[] o,int p,int q,int[] index){
        if(p==1&&q==o.length) {
            index = new int[o.length];
            for (int i = 0; i < index.length; i++)
                index[i] = i + 1;
        }
        if(p<q) {
            int i = p, j = q;
            int x = o[i - 1];
            int y = index[i-1];
            while (i < j) {
                while (i < j && o[j - 1] >= x)
                    j--;
                if(i<j) {
                    o[(i++) - 1] = o[j - 1];
                    index[i-1-1] = index[j-1];
                    while (i < j && o[i - 1] < x)
                        i++;
                    if (i < j) {
                        o[(j--) - 1] = o[i - 1];
                        index[j+1-1] = index[i-1];
                    }
                }
            }
            o[i - 1] = x;
            index[i-1] = y;
            execute1(o, p, i - 1,index);
            execute1(o, i + 1, q,index);
        }
        return index;
    }
    private static int sort(int[] A,int p,int q){
        int i=p,j=q,x=A[i];
        while (i<j){
            while (i<j&&A[j]>=x)
                j--;
            if(i<j)
                A[i++] = A[j];
            while (i<j&&A[i]<x)
                i++;
            if(i<j)
                A[j--] = A[i];
        }
        A[i] = x;
        return i;
    }
}
