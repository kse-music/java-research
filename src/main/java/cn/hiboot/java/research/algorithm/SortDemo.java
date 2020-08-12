package cn.hiboot.java.research.algorithm;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2019/4/13 16:57
 */
@Slf4j
public class SortDemo {

    private static final int[] arr = {-6, -2, 11, -4, 13, -5, -3};
    private static int[] aux; // 用于排序的辅助数组

    @BeforeEach
    public void in() {
        log.info("before sort {}", arr);
    }

    @AfterEach
    public void out() {
        log.info("after sort {}", arr);
    }


    /**
     * 找到最小数就交换位置,  稳定
     */
    @Test
    public void bubble() {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    swap(arr, i, j);
                }
            }
        }
    }

    /**
     * 找到最小数的小标，最后交换一次位置,不稳定
     */
    @Test
    public void select() {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[min] > arr[j]) {
                    min = j;
                }
            }
            swap(arr, i, min);
        }

    }


    /**
     * 稳定
     */
    @Test
    public void insert() {
        int i, j, insertNote;// 要插入的数据
        for (i = 1; i < arr.length; i++) {// 从数组的第二个元素开始循环将数组中的元素插入
            insertNote = arr[i];// 设置数组中的第2个元素为第一次循环要插入的数据
            j = i - 1;
            while (j >= 0 && insertNote < arr[j]) {
                arr[j + 1] = arr[j];// 如果要插入的元素小于第j个元素,就将第j个元素向后移动
                j--;
            }
            arr[j + 1] = insertNote;// 直到要插入的元素不小于第j个元素,将insertNote插入到数组中
        }
    }

    /**
     * 不稳定 最坏的时间复杂度为O(n^2)，平均时间复杂度为O(nlogn)。
     */
    @Test
    public void quick() {
        quick(arr, 0, arr.length - 1);
    }

    /**
     * 过将两个有序的序列合并为一个大的有序的序列的方式来实现排序。合并排序是一种典型的分治算法
     * 稳定
     */
    @Test
    public void merge() {
//        aux = new int[arr.length];
//        sort(arr, 0,arr.length - 1);

        int[] ints = {1, 3, 5, 7, 9, 2, 4, 6, 8, 10};
        aux = new int[ints.length];
        merge(ints, 0, 4, 9);
        System.out.println(ints);
    }

    private void sort(int[] array, int lo, int hi) {
        if (lo >= hi) return; //如果下标大于上标，则返回
        int mid = lo + (hi - lo) / 2;//平分数组
        sort(array, lo, mid);//循环对左侧元素排序
        sort(array, mid + 1, hi);//循环对右侧元素排序
        merge(array, lo, mid, hi);//对左右排好的序列进行合并
    }

    private void merge(int[] arr,int lo,int mid , int hi) {
        int leftStartIndex = lo, rightStartIndex = mid + 1;
        //把元素拷贝到辅助数组中
        for (int k = lo; k <= hi; k++) {
            aux[k] = arr[k];
        }
        //然后按照规则将数据从辅助数组中拷贝回原始的array中
        for (int k = lo; k <= hi; k++) {
            if (leftStartIndex > mid) {//如果左边元素没了， 直接将右边的剩余元素都合并到到原数组中
                arr[k] = aux[rightStartIndex++];
            } else if (rightStartIndex > hi) {//如果右边元素没有了，直接将所有左边剩余元素都合并到原数组中
                arr[k] = aux[leftStartIndex++];
            } else if (aux[leftStartIndex] < aux[rightStartIndex]) {//如果左边比右边小，则将左边的元素拷贝到原数组中
                arr[k] = aux[leftStartIndex++];
            } else {
                arr[k] = aux[rightStartIndex++];
            }
        }
    }

    private void quick(int[] arr, int low, int high) {
        if (low < high) {
            int pos = low;
            int pivot = arr[pos];
            for (int i = low + 1; i <= high; i++) {
                if (arr[i] < pivot) {
                    swap(arr, i, ++pos);
                }
            }
            swap(arr, low, pos);
            // 分而治之
            quick(arr, low, pos - 1);// 排序左半部分
            quick(arr, pos + 1, high);// 排序右半部分
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
