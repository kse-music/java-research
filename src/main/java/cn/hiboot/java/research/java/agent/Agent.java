package cn.hiboot.java.research.java.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/5/3 23:32
 */
public class Agent implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(className.equals("MainRun")){
            try {
                String loadName = className.replace("/",".");
                CtClass ctClass = ClassPool.getDefault().get(loadName);
                CtMethod ctMethod = ctClass.getDeclaredMethod("hello");
                ctMethod.addLocalVariable("_begin",CtClass.longType);
                ctMethod.insertBefore("_begin = System.nanoTime();");
                ctMethod.insertAfter("System.out.println(System.nanoTime() - _begin);");
                System.out.println(className);
                return ctClass.toBytecode();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }
}
