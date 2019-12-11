package com.android.downloadapk.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .   ' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * .............................................
 * 佛祖保佑             永无BUG
 *
 * @author :created by mzh
 * time :2019年12月11日14:26:22
 * 描述：root
 */
public class RootUtils {
    private static final String[] SU_BINARY_DIRS = {
            "/system/bin",
            "/system/sbin",
            "/system/xbin",
            "/vendor/bin",
            "/sbin"
    };

    /**
     * 检查设备是否root
     */
    public static boolean checkRoot() {
        boolean isRoot = false;
        try {
            for (String dir : SU_BINARY_DIRS) {
                File su = new File(dir, "su");
                if (su.exists()) {
                    isRoot = true;
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        return isRoot;
    }

    private static void closeIO(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 运行root命令
     */
    @SuppressLint("LogUtilsNotUsed")
    private static boolean runRootCmd(String cmd) {
        boolean grandted;
        DataOutputStream outputStream = null;
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(cmd + "\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            process.waitFor();
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            grandted = true;

            String msg = reader.readLine();
            if (msg != null) {
                LogUtils.i(RootUtils.class.getSimpleName(), msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            grandted = false;

            closeIO(outputStream);
            closeIO(reader);
        }
        return grandted;
    }
    /**
     * 安装apk
     */
    public static boolean installAPK(String apkPath) {
        return runRootCmd("pm install -i "+ Utils.getApp().getPackageName() +" --user 0 " + apkPath);
    }

    /**
     * 为app申请root权限
     */
    public static boolean grantRoot(Context context) {
        return runRootCmd("chmod 777 " + context.getPackageCodePath());
    }
}
