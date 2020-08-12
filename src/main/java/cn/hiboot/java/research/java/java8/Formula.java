package cn.hiboot.java.research.java.java8;

public interface Formula {
	double calculate(int a);
	default double sqrt(int a) {
		return Math.sqrt(a);
	}
}
