package com.android.downloadapk.utils;
import android.content.Context;
import com.android.downloadapk.callback.DownLoadListner;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import okhttp3.ResponseBody;

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
 * time :2019年11月24日14:43:58
 * 描述：文件类
 */
public class FileUtils {
    /**
     * 创建文件
     * @param filePath 文件地址
     * @param fileName 文件名
     */
    public static boolean createFile(String filePath, String fileName) {
        String strFilePath = filePath + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File subfile = new File(strFilePath);
        if (!subfile.exists()) {
            try {
                boolean b = subfile.createNewFile();
                return b;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 下载文件
     */
    public static File saveFile(ResponseBody body, DownLoadListner downLoadListner) {
       String filePath= FileUtils.getSaveFile(ActivityUtils.getTopActivity(), "_apk").getPath() + File.separator + Constans.downApkName;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            if (filePath == null) {
                return null;
            }
            file = new File(filePath);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            byte[] fileReader = new byte[4096];

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                downLoadListner.onPregress(fileSize, fileSizeDownloaded);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            downLoadListner.onErro(e.toString());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 创建文件夹
     */
    public static File createFiles(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    /**
     * 删除文件
     * @param filePath 文件地址
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            LogUtils.i("install" + "删除了文件:" + Constans.apkPath);
            file.delete();
        } else {
            LogUtils.i("install" + "文件不存在:" + Constans.apkPath);
        }
    }

    /**
     * 向文件中添加内容
     * @param strcontent 内容
     * @param filePath   地址
     * @param fileName   文件名
     */
    public static void writeToFile(String strcontent, String filePath, String fileName) {
        String strFilePath = filePath + fileName;
        File subfile = new File(strFilePath);
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(subfile, "rw");
            raf.seek(subfile.length());
            raf.write(strcontent.getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改文件内容（覆盖或者添加）
     * @param path    文件地址
     * @param content 覆盖内容
     * @param append  指定了写入的方式，是覆盖写还是追加写(true=追加)(false=覆盖)
     */
    public static void modifyFile(String path, String content, boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(path, append);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 地址
     * @param filename 名称
     * @return 返回内容
     */
    public static String getString(String filePath, String filename) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath + filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 重命名文件
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        oleFile.renameTo(newFile);
    }


    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     */
    private static void copy(String fromFile, String toFile) {
        File[] currentFiles;
        File root = new File(fromFile);
        if (!root.exists()) {
            return;
        }
        currentFiles = root.listFiles();
        File targetDir = new File(toFile);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        for (File currentFile : currentFiles) {
            if (currentFile.isDirectory()) {
                copy(currentFile.getPath() + "/", toFile + currentFile.getName() + "/");
            } else {
                CopySdcardFile(currentFile.getPath(), toFile + currentFile.getName());
            }
        }
    }

    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    private static void CopySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ignored) {
        }
    }

    /**
     * 创建保存文件的路径
     */
    public static File getSaveFile(Context mContext, String path) {
        String absolutePath = mContext.getExternalCacheDir().getAbsolutePath() + path;
        File file = new File(absolutePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
