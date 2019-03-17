package leetcode;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class KthSmallerstPath {
    public static void main(String[] args) {
        int A[] = new int[]{2,2,0,1,1,0,0,1,2,0};
        Solution s = new Solution();
        System.out.println(s.smallestDistancePair(A,2));
        System.out.println(s.findDuplicate(new int[]{1,3,4,2,2}));
        System.out.println(s.splitArray(new int[]{7,2,5,10,8},2));
        System.out.println(s.myPow(2,10));
        System.out.println(s.myPow(2,-2));
        System.out.println(s.myPow(-0.00001,2147483647));
        System.out.println(s.myPow(-0.99999,221620));
        System.out.println((Math.sqrt(Integer.MAX_VALUE)));
        System.out.println(s.isPerfectSquare(2147395600));
        System.out.println(s.nextGreatestLetter(new char[]{'c','f','g'},'z'));
    }

}

class Solution {
    public char nextGreatestLetter(char[] letters, char target) {
        char hashtab[]=new char[256];
        for(int i=0;i<letters.length;i++)
            hashtab[letters[i]] = 1;
        for(int i=target+1;i<hashtab.length;i++)
            if(hashtab[i]==1)
                return (char)i;
        for(int i=0;i<hashtab.length;i++)
            if(hashtab[i]==1)
                return (char)i;
        return letters[0];
    }
    public boolean isPerfectSquare(int num) {
        if(num==0||num==1)
            return true;
        int l=2,r = 46340;
        while (l<r){
            int mid = (l+r)/2;
            int temp=mid*mid;
            if(temp==num)
                return true;
            if(temp<num)
                l = mid+1;
            else
                r = mid-1;
        }
        if(l*l==num)
            return true;
        return false;
    }
    public double myPow(double x, int n) {
        if(n<0)
            return n==Integer.MIN_VALUE? 1 / myPowHelp(x, -(n+2)):1 / myPowHelp(x, -n);
        return myPowHelp(x,n);
    }
    private double myPowHelp(double x,int n) {
        double res=1;
        int stack[] = new int[32];
        int p=0;
        while (n > 0) {
            stack[p++] = n&1;
            n=n>>1;
        }
        while (p-->0){
            if(stack[p]!=0)
                res=res*res*x;
            else
                res*=res;
        }
        return res;
    }
    public int splitArray(int[] nums, int m) {
        long l = nums[0];
        long r = nums[0];
        long mid;
        for(int i=1;i<nums.length;i++){
            if(nums[i]>l)
                l = nums[i];
            r+=nums[i];
        }
        while (l<r){
            mid = (l+r)/2;
            if(splitArrayHelp(nums,m,mid))
                r = mid;
            else
                l = mid+1;
        }
        return (int)l;
    }
    private boolean splitArrayHelp(int[] nums,int m,long magic){
        long acc=0;
        for(int i=0;i<nums.length;){
            if(acc+nums[i]<=magic)
                acc+=nums[i++];
            else{
                acc=0;
                if(--m==0)
                    return false;
            }
        }
        return true;
    }
    public int findDuplicate(int[] nums) {
        int hashtab[] = new int[nums.length+1];
        for(int i=0;i<nums.length;i++)
            if(hashtab[nums[i]]>0)
                return nums[i];
            else
                hashtab[nums[i]]=1;
        return -1;
    }
    public int smallestDistancePair(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int l=0;
        int r=nums[n-1]-nums[0];
        for(int cnt=0;l<r;cnt=0){
            int mid = (l+r)/2;
            for(int i=0;i<n;i++) {
                int j = i+1;
                while (j<n&&nums[j]-nums[i]<=mid)
                    j++;
                cnt+=j-i-1;
            }
            if(cnt<k)
                l = mid+1;
            else
                r = mid;
        }
        return l;
    }
    private int distanceserchHelp(int nums[],int p,int q,int d){
        //找到nums第一个小于等于距离d的位置
        while (p+1<q){
            int mid = (p+q)/2;
            if(nums[mid]>d)
                q=mid;
            else if (nums[mid + 1] > d)
                return mid;
            else
                p = mid;
        }
        if(nums[q]<=d)
            return q;
        if(nums[p]<=d)
            return p;
        return -1;
    }
    public int search(int[] nums, int target) {
        return serchHelp(nums,0,nums.length-1,target);
    }
    private int serchHelp(int nums[],int p,int q,int target){
        while (p<q){
            int mid = (p+q)/2;
            if(target==nums[mid])
                return mid;
            if(target>nums[mid])
                p=mid+1;
            else
                q = mid-1;
        }
        if(nums[p]==target)
            return p;
        return -1;
    }
}

class Solution1 {
    private final AtomicLong count = new AtomicLong(0);

}