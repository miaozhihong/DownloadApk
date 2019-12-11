package com.android.downloadapk.activity;

import java.io.File;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.android.downloadapk.utils.Constans;
import com.android.downloadapk.utils.FileUtils;
import com.android.downloadapk.R;
import com.android.downloadapk.utils.RetrofitUtils;
import com.android.downloadapk.callback.DownLoadListner;
import com.android.downloadapk.utils.RootUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //下载apk
    public void downApk(View view) {
        downloadApk(Constans.downApkUrl);
    }

    //安装apk
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
}
