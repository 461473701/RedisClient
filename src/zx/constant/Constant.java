package zx.constant;

import javafx.scene.image.Image;
import zx.design.Main;
import zx.property.PropertyUtil;

import java.io.File;

/**
 * 功能描述：
 * 时间：2016/3/27 12:17
 *
 * @author ：zhaokuiqiang
 */
public class Constant {

    /**
     * 配置文件路径
     */
    public final static String PROPERTYPATH = System.getProperty("user.dir") + File.separator + "runtime" + File.separator + "Redis.properties";
//    public final static String PROPERTYPATH = PropertyUtil.class.getResource("").getPath() + "Redis.properties";

    public final static String NAME = "name";
    public final static String PASSWORD = "password";
    public final static String PORT = "port";
    public final static String IP = "ip";

    public final static String MAXID = "maxid";

    public final static String RESOURCE = "file:"+System.getProperty("user.dir") +
            File.separator + "runtime"+File.separator+"resource" + File.separator;

    /**
     * 系统icon
     */
    public final static Image ICONIMG = new Image(RESOURCE + "icon.png");

    /**
     * tree显示的db
     */
    public final static String DB = "db";

    //-------------redis返回常量-----------------
    public final static String REDIS_OK = "OK";
    //-------------redis类型常量-----------------
    public final static String REDIS_STRING = "string";
    public final static String REDIS_HASH = "hash";
    public final static String REDIS_LIST = "list";

    /**
     * key和field的拆分
     */
    public final static String SEPARATE = ";";

}
