package cn.hiboot.java.research.java.editor;


import cn.hiboot.java.research.java.reflect.UserBean;
import org.junit.jupiter.api.Test;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/1/4 14:04
 */
public class PropertyEditorDemo {

    @Test
    public void editor() throws Exception {
        Map<String, String> parameters = new HashMap<String, String>(){
            {
                //这里的key要和Node里面的属性名一致
                put("nodeName", "迈克");
                put("userBean", "mike|标题|11|内容|搜索|不搜索|2019-01-04 14:08:00");
            }
        };
        Node convert = convert(parameters);
        System.out.println(convert.getNodeName());
        System.out.println(convert.getUserBean());
    }

    /**
     * 进行转换
     */
    public static Node convert(Map<String, String> parameters)throws Exception {
        //注册bean的编辑器,放到一个WeakHashMap中
        PropertyEditorManager.registerEditor(UserBean.class, UserBeanEditor.class);
        Node node = new Node();
        BeanInfo bi = Introspector.getBeanInfo(Node.class);
        //获取所有的属性
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        for(PropertyDescriptor pd : pds){
            Class<?> propertyType = pd.getPropertyType();
            Method writeMethod = pd.getWriteMethod();
            if(propertyType == Class.class){
                //ignore
            }else if(propertyType == String.class){
                writeMethod.invoke(node, parameters.get(pd.getName()));
            }else{
                //根据类型查找bean的编辑器,获取到了UserEditor的实例
                PropertyEditor editor = PropertyEditorManager.findEditor(propertyType);
                if(editor != null){
                    editor.setAsText(parameters.get(pd.getName()));
                    writeMethod.invoke(node, editor.getValue());
                }else{
                    System.out.println("no editor for:"+pd.getName());
                }
            }
        }
        return node;
    }

}
