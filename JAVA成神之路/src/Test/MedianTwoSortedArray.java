package Test;

public class MedianTwoSortedArray {
    public static void main(String[] args) {
        int num1[] = new int[]{1, 3};
        int num2[] = new int[]{2};
        MedianTwoSortedArray a = new MedianTwoSortedArray();
        System.out.println(a.findMedianSortedArrays(num1, num2));

        int nums1[] = new int[]{1, 2};
        int nums2[] = new int[]{3, 4};
        System.out.println(a.findMedianSortedArrays(nums1, nums2));

        Logm logm = new Logm();
        System.out.println(logm.findMedianSortedArrays(num1, num2));
        System.out.println(logm.findMedianSortedArrays(nums1, nums2));
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int k = (nums1.length + nums2.length);
        int n = k / 2 + 1;
        int data[] = new int[n];
        // copy the first n nums(in nums1 and nums2) to data Array
        for (int i = 0, j = 0, x = 0; x < n; x++) {
            if (i < nums1.length)
                if (j < nums2.length && nums2[j] < nums1[i])
                    data[x] = nums2[j++];
                else
                    data[x] = nums1[i++];
            else
                data[x] = nums2[j++];
        }
        //return the last num(average of last two num) in data
        if (k % 2 == 0)
            return (double) (data[n-1] + data[n - 2]) / 2;
        return data[n-1];
    }
}
class Logm{
    public double findMedianSortedArrays(int num1[],int num2[]){
        int k = num1.length+num2.length;
        if(k%2==0)
            return (getKth(num1,num2,0,0,k/2)+getKth(num1,num2,0,0,k/2-1))/2;
        return getKth(num1,num2,0,0,k/2);
    }
    private double getKth(int nums1[],int nums2[],int i,int j,int k){
        if(i==nums1.length)
            return nums2[j+k];
        if(j==nums2.length)
            return nums1[i+k];
        if(k==0)
            return Math.min(nums1[i],nums2[j]);
        int mid1 = Math.min(nums1.length-i,(k+1)/2);
        int mid2 = Math.min(nums2.length-j,(k+1)/2);
        if(nums1[i+mid1-1]<nums2[j+mid2-1])
            return getKth(nums1,nums2,i+mid1,j,k-mid1);
        return getKth(nums1,nums2,i,j+mid2,k-mid2);
    }
}
