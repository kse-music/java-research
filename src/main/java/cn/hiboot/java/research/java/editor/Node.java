package cn.hiboot.java.research.java.editor;


import cn.hiboot.java.research.java.reflect.UserBean;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/1/4 14:00
 */
public class Node {

    private String nodeName;
    private UserBean userBean;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

}
