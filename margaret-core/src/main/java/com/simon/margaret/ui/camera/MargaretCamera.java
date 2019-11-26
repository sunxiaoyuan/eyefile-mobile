package com.simon.margaret.ui.camera;

import android.net.Uri;

import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.util.file.FileUtil;


/**
 * Created by simon
 * 照相机调用类
 */

public class MargaretCamera {

    public static Uri createCropFile() {
        return Uri.parse
                (FileUtil.createFile("crop_image",
                        FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    public static void start(MargaretDelegate fragment) {
        new CameraHandler(fragment).beginCameraDialog();
    }
}
