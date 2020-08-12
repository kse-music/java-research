package cn.hiboot.java.research.java.java8;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 流的操作
 * 当把一个数据结构包装成 Stream 后，就要开始对里面的元素进行各类操作了。常见的操作可以归类如下。
 * <p>
 * Intermediate：
 * map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
 * <p>
 * Terminal：
 * forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 * <p>
 * Short-circuiting：
 * anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
 *
 * @author DingHao
 * @since 2019/1/14 21:20
 */
public class Java8Demo {

    //一、接口的默认方法
    @Test
    public void defaultMethod() {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };
        System.out.println(formula.calculate(100));// 100.0
        System.out.println(formula.sqrt(16));// 4.0

        Formula formula2 = (a) -> (double) (a + 12);
        System.out.println(formula2.calculate(4));

        Formula formula3 = Double::valueOf;
        System.out.println(formula3.calculate(4));
    }

    //二、Lambda 表达式
    @Test
    public void lambda() {
//        Stream.iterate(0, n -> n + 3).limit(10).forEach(x -> System.out.print(x + " "));

        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        Collections.sort(names, new Comparator<String>() {//传统
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        //style1
        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        //style2
        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        //style3,Java编译器可以自动推导出参数类型
        Collections.sort(names, (a, b) -> b.compareTo(a));

        //style4
        Collections.sort(names, Comparator.comparing(String::valueOf).reversed());

        names.stream().sorted().forEach(System.out::println);
        names.stream().sorted(Comparator.comparing(String::valueOf).reversed()).forEach(System.out::println);

    }


    //三、函数式接口:是指仅仅只包含一个抽象方法的接口，每一个该类型的lambda表达式都会被匹配到这个抽象方法
    @Test
    public void functionInterface() {
        Converter<String, Integer> converter = Integer::valueOf;
        Integer converted = converter.convert("123");
        System.out.println(converted);    // 123
    }

    //四、方法与构造函数引用
    @Test
    public void methodConstructor() {
        Converter<String, Integer> converter = Integer::valueOf;
        Integer converted = converter.convert("123");
        System.out.println(converted);   // 123

        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Peter", "Parker");
        System.out.println(person);
    }

    //五、Lambda 作用域,在lambda表达式中访问外层作用域和老版本的匿名对象中的方式很相似。你可以直接访问标记了final的外层局部变量，
    // 或者实例的字段以及静态变量
    //六、访问局部变量
    //七、访问对象字段与静态变量
    //和本地变量不同的是，lambda内部对于实例的字段以及静态变量是即可读又可写。该行为和匿名对象是一致的
    @Test
    public void localVar() {
        int num = 1;//隐性的具有final的语义
        Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
        System.out.println(stringConverter.convert(2));     // 3
    }

    //八、访问接口的默认方法
    @Test
    public void comprehensive() {
        //接口Formula定义了一个默认方法sqrt可以直接被formula的实例包括匿名对象访问到，但是在lambda表达式中这个是不行的
        //Lambda表达式中是无法访问到默认方法的
//		Formula formula = (a) -> sqrt( a * 100);

        //Predicate接口
        Predicate<String> predicate = (s) -> s.length() > 0;
        predicate.test("foo");              // true
        predicate.negate().test("foo");     // false
        Predicate<Boolean> nonNull = Objects::nonNull;
        nonNull.test(true);
        Predicate<Boolean> isNull = Objects::isNull;
        isNull.test(false);
        Predicate<String> isEmpty = String::isEmpty;
        isEmpty.test("");
        Predicate<String> isNotEmpty = isEmpty.negate();
        isNotEmpty.test("");

        //Function 接口,具有输入和输出
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        backToString.apply("123");     // "123"

        //Supplier接口,只有输出
        Supplier<Person> personSupplier = Person::new;
        personSupplier.get();   // new Person

        //Consumer接口,只有输入
        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.getFirstName());
        greeter.accept(new Person("Luke", "Skywalker"));

        //Comparator 接口
        Comparator<Person> comparator = (p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName());
        comparator = Comparator.comparing(Person::getFirstName).reversed();
        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");
        System.out.println(comparator.compare(p1, p2));             // > 0
        System.out.println(comparator.reversed().compare(p1, p2));  // < 0

        //Optional接口,不是函数是接口,用来防止NullPointerException异常的辅助类型，
        Optional<String> optional = Optional.of("bam");
        optional.isPresent();           // true
        optional.get();                 // "bam"
        optional.orElse("fallback");    // "bam"
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"

        //Stream 接口,Stream 操作分为中间操作或者最终操作两种，最终操作返回一特定类型的计算结果，而中间操作返回Stream本身
        List<String> stringCollection = Lists.newArrayList("ddd2", "aaa2", "bbb1", "aaa1", "bbb3", "ccc", "bbb2", "ddd1");
        //--串行Streams
        //--Filter 过滤
        stringCollection.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);
        Map<String, Long> result = stringCollection.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(result);

        //--Sort 排序,排序只创建了一个排列好后的Stream，而不会影响原有的数据源
        stringCollection.stream().sorted().filter(s -> s.startsWith("a")).forEach(System.out::println);
        stringCollection.stream().sorted((a, b) -> b.compareTo(a)).filter(s -> s.startsWith("a")).forEach(System.out::println);
        stringCollection.stream().sorted(Comparator.comparing(String::valueOf).reversed()).filter(s -> s.startsWith("a")).forEach(System.out::println);

        //--Map 映射,将元素根据指定的Function接口来依次将元素转成另外的对象
        stringCollection.stream().map(String::toUpperCase).sorted(Comparator.comparing(String::valueOf).reversed()).forEach(System.out::println);

        //--Match 匹配,所有的匹配操作都是最终操作
        boolean anyStartsWithA = stringCollection.stream().anyMatch((s) -> s.startsWith("a"));
        System.out.println(anyStartsWithA);      // true
        boolean allStartsWithA = stringCollection.stream().allMatch((s) -> s.startsWith("a"));
        System.out.println(allStartsWithA);      // false
        boolean noneStartsWithZ = stringCollection.stream().noneMatch((s) -> s.startsWith("z"));
        System.out.println(noneStartsWithZ);      // true

        //--Count 计数是一个最终操作
        long startsWithB = stringCollection.stream().filter((s) -> s.startsWith("b")).count();
        System.out.println(startsWithB);    // 3

        //--Reduce 规约是一个最终操作
        Optional<String> reduced = stringCollection.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);

        //--并行Streams
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        long t0 = System.nanoTime();
        long count = values.parallelStream().sorted().count();
        System.out.println(count);
        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis));

        //Map类型不支持stream，不过Map提供了一些新的有用的方法来处理一些日常任务。
        Map<Integer, String> map = Maps.newHashMap();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }
        map.forEach((id, val) -> System.out.println(val));

        map.computeIfPresent(3, (num, val) -> val + num);
        map.get(3);             // val33
        map.computeIfPresent(9, (num, val) -> null);
        map.containsKey(9);     // false
        map.computeIfAbsent(23, num -> "val" + num);
        map.containsKey(23);    // true
        map.computeIfAbsent(3, num -> "bam");
        map.get(3);             // val33

        map.remove(3, "val3");
        map.get(3);             // val33
        map.remove(3, "val33");
        map.get(3);             // null

        map.getOrDefault(42, "not found");  // not found

        map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
        map.get(9);             // val9
        map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
        map.get(9);             // val9concat
    }

    //九、Date API,新的日期API和开源的Joda-Time库差不多
    @Test
    public void time() {
        /**
         * Instant 它代表的是时间戳，比如2014-01-14T02:20:13.592Z，这可以从java.time.Clock类中获取，像这样： Instant current = Clock.system(ZoneId.of(“Asia/Tokyo”)).instant();
         LocalDate 它表示的是不带时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
         LocalTime – 它表示的是不带日期的时间
         LocalDateTime – 它包含了时间与日期，不过没有带时区的偏移量
         ZonedDateTime – 这是一个带时区的完整时间，它根据UTC/格林威治时间来进行时区调整
         */

        //Clock 时钟
        //第一个是Clock类，它通过指定一个时区，然后就可以获取到当前的时刻，日期与时间。Clock可以替换System.currentTimeMillis()与TimeZone.getDefault()。
        // Get the system clock as UTC offset
        final Clock clock = Clock.systemDefaultZone();
        System.out.println(clock.instant());
        System.out.println(clock.millis());
        System.out.println(Clock.systemUTC().millis());
        long millis = clock.millis();
        System.out.println(millis);
        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant);   // legacy java.util.Date
        System.out.println(legacyDate);

        //Timezones 时区
        System.out.println(ZoneId.getAvailableZoneIds());    // prints all available timezone ids
        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        ZoneId zone3 = ZoneId.of("Asia/Chongqing");
        System.out.println(zone1.getRules());    // ZoneRules[currentStandardOffset=+01:00]
        System.out.println(zone2.getRules()); // ZoneRules[currentStandardOffset=-03:00]
        System.out.println(zone3.getRules());

        //LocalTime 本地时间

        //LocalDate 本地日期
        Instant timestamp = Instant.now();
        System.out.println(timestamp);

        ZoneId currentZone = ZoneId.systemDefault();
        System.out.println(currentZone);
        System.out.println("Local date: " + LocalDateTime.ofInstant(timestamp,currentZone));

        final LocalDateTime datetime = LocalDateTime.now().withNano(0);
        final LocalDateTime datetimeFromClock = LocalDateTime.now( clock );

        System.out.println(datetime);
        System.out.println(datetimeFromClock );

        final LocalDate date = LocalDate.of(2020,2,3);
        System.out.println(date.isLeapYear());

        LocalDate today = LocalDate.now();
        // 取本月第1天：
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println(firstDayOfThisMonth);
        // 取本月第2天：
        LocalDate secondDayOfThisMonth = today.withDayOfMonth(2);
        System.out.println(secondDayOfThisMonth);

        // 取本月最后一天，再也不用计算是28，29，30还是31：
        LocalDate lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(lastDayOfThisMonth);

        // 取下一天：
        LocalDate firstDayOf2018 = lastDayOfThisMonth.plusDays(1);
        System.out.println(firstDayOf2018);

        // 取2018年1月第一个周一，这个计算用Calendar要死掉很多脑细胞：
        LocalDate firstMondayOf2018 = LocalDate.parse("2018-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)); // 2018-01-01
        System.out.println(firstMondayOf2018);

        //LocalDateTime 本地日期时间


    }


    static class Lambda4 {
        static int outerStaticNum;
        int outerNum;
        void testScopes() {
            Converter<Integer, String> stringConverter1 = (from) -> {
                outerNum = 23;
                return String.valueOf(from);
            };
            stringConverter1.convert(1);
            Converter<Integer, String> stringConverter2 = (from) -> {
                outerStaticNum = 72;
                return String.valueOf(from);
            };
            stringConverter2.convert(1);
        }
    }


}
