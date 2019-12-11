package com.android.downloadapk.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.util.Objects;

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
 * time :2019年12月11日18:31:56
 * 描述：常量
 */
public class Constans {
    public static String downApkName = "downapk.apk";
    public static String downApkPackageName = "com.liaomi.live2";
    public static String downApkUrl = "http://qq-1259512018.cos.ap-chengdu.myqcloud.com/youwu_1.1.9.apk";
    public static String apkPath = Utils.getApp().getExternalCacheDir().getAbsolutePath() + "_apk" + File.separator + Constans.downApkName;
}
