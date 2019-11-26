package com.simon.margaret.delegates;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.simon.margaret.observer.ObserverManager;
import com.simon.margaret.ui.camera.CameraImageBean;
import com.simon.margaret.ui.camera.MargaretCamera;
import com.simon.margaret.ui.camera.RequestCodes;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;
import com.yalantis.ucrop.UCrop;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by apple on 2019/8/22.
 */


public abstract class PermissionCheckerDelegate extends BaseDelegate {

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case RequestCodes.TAKE_PHOTO:
					final Uri resultUri = CameraImageBean.getInstance().getPath();
					if (getContext() != null) {
						UCrop.of(resultUri, resultUri)
								.withMaxResultSize(400, 400)
								.start(getContext(), this);
					}
					break;
				case RequestCodes.PICK_PHOTO:
					if (data != null) {
						final Uri pickPath = data.getData();
						//从相册选择后需要有个路径存放剪裁过的图片
						final String pickCropPath = MargaretCamera.createCropFile().getPath();
						if (pickPath != null && getContext() != null) {
							UCrop.of(pickPath, Uri.parse(pickCropPath))
									.withMaxResultSize(400, 400)
									.start(getContext(), this);
						}
					}
					break;
				case RequestCodes.CROP_PHOTO:
					final Uri cropUri = UCrop.getOutput(data);
					// 拿到剪裁后的数据进行处理
					@SuppressWarnings("unchecked") final IGlobalCallback<Uri> callback = CallbackManager
							.getInstance()
							.getCallback(CallbackType.ON_CROP);
					if (callback != null) {
						callback.executeCallback(cropUri);
					}
					break;
				case RequestCodes.CROP_ERROR:
					Toast.makeText(getContext(), "剪裁出错", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	}


}

