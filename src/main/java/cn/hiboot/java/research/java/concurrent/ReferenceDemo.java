package cn.hiboot.java.research.java.concurrent;


import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

/**
 * 强引用
 * 软引用:普通的GC并不会回收软引用，只有在即将OOM的时候(也就是最后一次Full GC)如果被引用的对象只有SoftReference指向的引用，才会回收。
 * 弱引用:当发生GC时，如果被引用的对象只有WeakReference指向的引用，就会被回收。
 * 虚引用
 *
 * @author DingHao
 * @since 2019/1/24 11:05
 *
 */
public class ReferenceDemo {

    @Test
    public void reference(){

        House house = new House();

        //强引用
        House buyer1 = house;  //最常用

        //软引用:在即将OOM之前，垃圾回收器会把这些软引用指向的对象加入回收范围，以便获得更多的内存空间，让程序能够继续健康的运行。
        SoftReference<House> buyer2 = new SoftReference<>(house);

        //弱引用：如果弱应用指向的对象只存在弱应用这一条线路，则在下一次YGC时会被回收。
        //在垃圾回收器线程扫描它 所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存
        WeakReference<House> buyer3 = new WeakReference<>(house);

        //虚引用：必须配合引用队列使用
        PhantomReference<House> buyer4 = new PhantomReference<>(house,null); //几乎很难用得到

        house = null;

    }

    @Test
    public void weakHashMap(){
        House seller1 = new House("1 号卖家房源");
        SellerInfo sellerInfo1 = new SellerInfo();

        House seller2 = new House("2 号卖家房源");
        SellerInfo sellerInfo2 = new SellerInfo();

        WeakHashMap<House,SellerInfo> weakHashMap = new WeakHashMap<>();
//        Map<House,SellerInfo> weakHashMap = new HashMap<>();
        weakHashMap.put(seller1,sellerInfo1);
        weakHashMap.put(seller2,sellerInfo2);

        System.out.println("WeakHashMap before null , size = " +  weakHashMap.size());

        seller1 = null;

        System.gc();//建议gc回收
        System.runFinalization();//强制调用已失去引用对象的finalize()

        System.out.println("WeakHashMap after null , size = " +  weakHashMap.size());

        System.out.println(weakHashMap);

    }

    @Test
    public void rf() {
        //-Xms5m -Xmx5m
//        List<House> houses = Lists.newArrayList();//517个左右
//        List<SoftReference> houses = Lists.newArrayList();//74006个左右
        List<WeakReference> houses = Lists.newArrayList();//99991个左右
        int i = 0;
        while (true){
//            houses.add(new House());
//            houses.add(new SoftReference<>(new House()));
            houses.add(new WeakReference<>(new House()));
            System.out.println("i = " + (++i));
        }

    }


    static class House{
        private static final Integer DOOR_NUMBER = 2000;
        public Door[] doors = new Door[DOOR_NUMBER];
        private String name;
        House(){}
        House(String name){
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        class Door{

        }
    }

    static class SellerInfo{

    }

}
