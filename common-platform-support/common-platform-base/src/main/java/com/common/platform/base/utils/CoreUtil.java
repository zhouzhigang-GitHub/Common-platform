package com.common.platform.base.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.common.platform.base.config.app.AppNameProperties;
import com.common.platform.base.config.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

public class CoreUtil extends ValidateUtil{

    /**
     * 默认密码盐长度
     */
    public static final int SALT_LENGTH = 6;

    /**
     * 获取随机字符,自定义长度
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * md5加密(加盐)
     */
    public static String md5Hex(String password, String salt) {
        return md5Hex(password + salt);
    }

    public static String md5Hex(String str) {
        return MD5Util.encrypt(str);
    }

    /**
     * 过滤掉字符串中的空白
     * @param value
     * @return
     */
    public static String removeWhiteSpace(String value) {
        if (isEmpty(value)) {
            return "";
        } else {
            return value.replaceAll("\\s*","");
        }
    }

    /**
     * 获取某个时间间隔以前的时间，时间格式： yyyy-MM-dd HH:mm:ss
     * @param seconds
     * @return
     */
    public static String getCreateTimeBefore(int seconds) {
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTimeInMillis-seconds*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取异常的具体信息
     */
    public static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();
        try {
            e.printStackTrace(new PrintWriter(sw));
        } finally {
            try {
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sw.getBuffer().toString().replaceAll("\\$", "T");
    }

    /**
     * 获取应用名称
     */
    public static String getApplicationName() {
        try {
            AppNameProperties appNameProperties =
                    SpringContextHolder.getBean(AppNameProperties.class);
            return appNameProperties.getName();
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(CoreUtil.class);
            logger.error("获取应用名称错误！", e);
            return "";
        }
    }

    public static String getIP() {
        try {
            StringBuilder IFCONFIG = new StringBuilder();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        IFCONFIG.append(inetAddress.getHostAddress().toString()).append("\n");
                    }
                }
            }
            return IFCONFIG.toString();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拷贝属性，为null的不拷贝
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true).ignoreError());
    }

    /**
     * 判断是否是windows操作系统
     */
    public static Boolean isWinOs() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }

    /**
     * 获取临时目录
     */
    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 把一个数字转换成int
     * @param val
     * @return
     */
    public static Integer toInt(Object val) {
        if (val instanceof Double) {
            BigDecimal bigDecimal = BigDecimal.valueOf((Double) val);
            return bigDecimal.intValue();
        } else {
            return Integer.valueOf(val.toString());
        }
    }

    /**
     * 判断是否为数字
     * @param obj
     * @return
     */
    public static boolean isNum(Object obj) {
        try {
            Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取项目路径
     */
    public static String getWebRootPath(String filePath) {
        try {
            String path = Objects.requireNonNull(CoreUtil.class.getClassLoader().getResource("")).toURI().getPath();
            path = path.replace("/WEB-INF/classes/", "");
            path = path.replace("/target/classes/", "");
            path = path.replace("file:/", "");
            if (isEmpty(filePath)) {
                return path;
            } else {
                return path + "/" + filePath;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件后缀名，不包含点
     * @param fileWholeName
     * @return
     */
    public static String getFileSuffix(String fileWholeName) {
        if (isEmpty(fileWholeName)) {
            return "none";
        }
        int lastIndexOf = fileWholeName.lastIndexOf(".");
        return fileWholeName.substring(lastIndexOf+1);
    }

    /**
     * 判断一个对象是否是时间类型
     * @param obj
     * @return
     */
    public static String dateType(Object obj) {
        if (obj instanceof Date) {
            return DateUtil.formatDate((Date) obj);
        } else {
            return obj.toString();
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String currentTime() {
        return DateUtil.formatDateTime(new Date());
    }
}
