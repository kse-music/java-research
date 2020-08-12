package cn.hiboot.java.research.java.simple;

/**
 * 位图
 *
 * 　可以运用在快速查找、去重、排序、压缩数据等。
 *
 * @author DingHao
 * @since 2019/10/8 14:07
 */
public class BitMap {

    private int[] bigArray;

    public BitMap(long  size){
        bigArray = new int[(int) (size/ 32 + 1)];
    }

    public void add(int  num){
        //确定数组 index
        int arrayIndex = num >> 5;
        //确定bit index
        int bitIndex = num & 31;
        //设置0
        bigArray[arrayIndex] |= 1 << bitIndex;
    }

    public boolean exist(int  num){
        //确定数组 index
        int arrayIndex = num >> 5;
        //确定bit index
        int bitIndex = num & 31;

        //判断是否存在
        return (bigArray[arrayIndex] & ((1 << bitIndex))) != 0;
    }

}
