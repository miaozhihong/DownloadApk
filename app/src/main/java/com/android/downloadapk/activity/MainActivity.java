package com.android.downloadapk.activity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.android.downloadapk.callback.CheckAppVsersion;
import com.android.downloadapk.utils.Constans;
import com.android.downloadapk.utils.FileUtils;
import com.android.downloadapk.R;
import com.android.downloadapk.utils.RetrofitUtils;
import com.android.downloadapk.callback.DownLoadListner;
import com.android.downloadapk.utils.RootUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private boolean isFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 下载apk
     */
    public void downApk(View view) {
        downloadApk(Constans.downApkUrl);
    }

    /**
     * 安装apk
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void installAPK(View view) {
        if (RootUtils.checkRoot()) {
            boolean install = RootUtils.installAPK(Constans.apkPath);
            if (install) {
                ToastUtils.showLong("安装成功");
                startActivity(getPackageManager().getLaunchIntentForPackage(Constans.downApkPackageName));
                FileUtils.deleteFiles(Constans.apkPath);
            } else {
                ToastUtils.showLong("安装失败");
            }
        } else {
            ToastUtils.showLong("该设备未Root");
        }
    }

    private void downloadApk(String url) {
        RetrofitUtils.instanceApi()
                .downloaApk(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showDialog();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        FileUtils.saveFile(responseBody, new DownLoadListner() {
                            @Override
                            public void onPregress(long fileSize, long fileSizeDownloaded) {

                            }

                            @Override
                            public void onErro(String msg) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("下载失败" + e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        ToastUtils.showLong("下载成功");
                        progressDialog.dismiss();
                    }
                });
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("软件更新中");
        progressDialog.setMessage("软件正在更新中,请稍后...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    /**
     *检查其他版本号
     */
    public void checkApk(View view) {
        getItems(this, new CheckAppVsersion() {
            @Override
            public void onSuccess(PackageInfo packageInfo) {
                ToastUtils.showShort( packageInfo.packageName + "----" + packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + "----" + packageInfo.versionName + "----" + packageInfo.versionCode);
            }

            @Override
            public void onErro() {
                ToastUtils.showShort("未找到对应应用");
            }
        });
    }

    /**
     * app的信息
     */
    public void getItems(final Context context, final CheckAppVsersion checkAppVsersion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager pckMan = context.getPackageManager();
                List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);
                for (PackageInfo pInfo : packageInfo) {
                    if (pInfo.packageName.equals(Constans.downApkPackageName)) {
                        isFind = true;
                        checkAppVsersion.onSuccess(pInfo);
                    }
                }
                if (!isFind) {
                    checkAppVsersion.onErro();
                }
            }
        }).start();
    }

}
