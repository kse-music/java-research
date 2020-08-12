package cn.hiboot.java.research.algorithm;


import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 最大子序列
 *
 * @author DingHao
 * @since 2019/4/19 23:51
 */
public class MaxSubSum {

    private static final int[] arr = {-2,11,-4,13,-5,-2};

    @Test
    public void maxSubSum4(){
        int maxSum = arr[0];
        int start = 0,end = 0,sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if(sum > 0){
                sum += arr[i];
            }else{
                sum = arr[i];
                start = i;
            }
            if(maxSum < sum){
                end = i;
                maxSum = sum;
            }
        }
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr,start,end+1)));
        System.out.println(maxSum);
    }

    @Test
    public void maxSubSum3(){

    }

    @Test
    public void maxSubSum2(){
        int maxSum = 0;
        int firstIndex = 0;
        int lastIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            int thisSum = 0;
            for (int j = i; j < arr.length; j++) {
                thisSum += arr[j];
                if(thisSum > maxSum){
                    maxSum = thisSum;
                    firstIndex= i;
                    lastIndex= j;
                }
            }
        }
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr,firstIndex,lastIndex+1)));
        System.out.println(maxSum);
    }


    @Test
    public void maxSubSum1(){
        int maxSum = 0;
        int firstIndex = 0;
        int lastIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                int thisSum = 0;
                for (int k = i; k <= j; k++) {
                    thisSum += arr[k];
                    if(thisSum > maxSum){
                        maxSum = thisSum;
                        firstIndex= i;
                        lastIndex= j;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr,firstIndex,lastIndex+1)));
        System.out.println(maxSum);
    }


}
