package com.android.downloadapk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.android.downloadapk.utils.Constans;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

import java.io.File;
import java.util.Objects;
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
 * time :2019-12-11 14:28:49
 * 描述：安装apk广播
 */
public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            LogUtils.i("install" + "安装了完成:" + "包名的程序");
        }
        //接收安装广播
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getDataString();
            LogUtils.i("install" + "安装了:" + packageName + "包名的程序");

        }
        //接收卸载广播
        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            String packageName = intent.getDataString();
            LogUtils.i("install" + "卸载了:" + packageName + "包名的程序");
        }
    }
}
