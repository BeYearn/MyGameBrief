package com.emagroup.briefsdk;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UCommUtil {

    private static final String TAG = "UCommUtil";


    public static String readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Log.e(TAG, "file is not exist");
            return null;
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }


    public static void writeFile(String filePath, String str) {
        FileWriter fileWriter = null;
        try {
            //将string写入目的地
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            fileWriter = new FileWriter(file);
            fileWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 用于打印map
     *
     * @param map
     */
    public static void logMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.e(entry.getKey() + "", "::" + entry.getValue());
        }
    }

    /**
     * 拼接url
     *
     * @param url
     * @param map
     * @return
     */
    public static String buildUrl(String url, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        int i = 0;
        for (String key : map.keySet()) {
            if (i == 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append(key).append("=").append(map.get(key));
            i++;
        }
        Log.e(TAG, "test_url___:" + sb.toString());
        return sb.toString();
    }

    /**
     * 获取指定长度的随机字符串(个数不超过32位)
     *
     * @return
     */
    public static String getRandomStr(int length) {
        Random random = new Random(System.currentTimeMillis());
        String str = MD5(random.toString());
        if (length >= 32)
            length = 32;
        if (str.length() >= length)
            str = str.substring(0, length);
        return str;
    }

    /**
     * 获取32位的随机字符串
     *
     * @return
     */
    public static String getRandomStr() {
        return getRandomStr(32);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断字符是否是中文
     *
     * @param str
     * @return
     */
    public static boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (bytes.length == str.length()) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 过滤输入密码的特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String loginPasswStringFilter(String str)
            throws PatternSyntaxException {
        String regEx = "[ ()\"']";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }


    /**
     * 获取签名
     */
    public static String getSign(String appid, String sid, String uuid,
                                 String appkey) {
        long stamp = getTimeStamp();
        String sign = appid + sid + uuid + stamp + appkey;
        sign = MD5(sign);
        return sign;
    }

    /**
     * 获取当前时间（精确到二十分钟内），作为验证
     *
     * @return
     */
    public static long getTimeStamp() {
        long stamp = (int) (System.currentTimeMillis() / 1000);
        stamp = stamp - stamp % 1200;
        return stamp;
    }


    /**
     * 判断是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        String mode = "";
        int position = str.indexOf('@');
        if (position == 1)
            mode = "^[a-z0-9A-Z]+\\@[a-z0-9A-Z]+[.]{1}[a-z0-9A-Z]+\\w*[.]*\\w*[a-zA-Z]+$";
        else
            mode = "^[a-z0-9A-Z]+[-+._a-z0-9A-Z]*[a-z0-9A-Z]+\\@[a-z0-9A-Z]+[.]{1}[a-z0-9A-Z]+\\w*[.]*\\w*[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(mode);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 判断是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^1\\d{10}$");
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        }
    }

    /**
     * 返回md5加密后的字符串
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte bytes[] = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++)
                if (Integer.toHexString(0xff & bytes[i]).length() == 1)
                    sb.append("0").append(Integer.toHexString(0xff & bytes[i]));
                else
                    sb.append(Integer.toHexString(0xff & bytes[i]));
            return sb.toString().toUpperCase();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 返回格式化后的时间格式
     *
     * @param content
     * @return
     */
    public static String DateFormat(String content) {
        return DateFormat(Long.valueOf(content));
    }

    /**
     * 返回格式化后的时间格式
     *
     * @return
     */
    public static String DateFormat(Date date) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.US);
        return sfd.format(date);
    }

    /**
     * 返回格式化后的时间格式
     *
     * @param content
     * @return
     */
    public static String DateFormat(Long content) {
        String time = content.toString();
        if (time.length() == 10) {
            time += "000";
        }
        if (time.length() > 13) {
            time = time.substring(0, 13);
        }
        content = Long.valueOf(time);
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.US);
        return sfd.format(new Date(content));
    }

    /**
     * 返回指定格式的时间
     *
     * @param content
     * @param format
     * @return
     */
    public static String DateFormat(String content, String format) {
        return DateFormat(Long.valueOf(content), format);
    }

    /**
     * 返回指定格式的时间
     *
     * @param content
     * @param format
     * @return
     */
    public static String DateFormat(Long content, String format) {
        String time = content.toString();
        if (time.length() == 10) {
            time += "000";
        }
        if (time.length() > 13) {
            time = time.substring(0, 13);
        }
        content = Long.valueOf(time);
        SimpleDateFormat sfd = new SimpleDateFormat(format, Locale.US);
        return sfd.format(new Date(content));
    }


    //读取指定目录下的TXT文件的第一行内容
    protected static String getFileContent(File file) {
        String content = "";
        if (file.getName().endsWith(".txt")) {//文件格式为txt文件
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader
                            = new InputStreamReader(instream, "GBK");
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line = "";
                    //分行读取
                       /* while (( line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }*/
                    content = buffreader.readLine();
                    instream.close();        //关闭输入流
                }
            } catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static String getTopApp(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        android.app.ActivityManager mActivityManager;
        mActivityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        String packageName;
        if (Build.VERSION.SDK_INT > 20) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);

            long ts = System.currentTimeMillis();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);

            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {
                if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                    recentStats = usageStats;
                }
            }
            packageName = recentStats != null ? recentStats.getPackageName() : null;
        } else {
            // 5.0之前
            // 获取正在运行的任务栈(一个应用程序占用一个任务栈) 最近使用的任务栈会在最前面
            // 1表示给集合设置的最大容量 List<RunningTaskInfo> infos = am.getRunningTasks(1);
            // 获取最近运行的任务栈中的栈顶Activity(即用户当前操作的activity)的包名
            packageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            //Log.i(TAG,packageName);
        }
        Log.i(TAG, "getTopApp  packageName==" + packageName);
        return packageName;
    }

}
