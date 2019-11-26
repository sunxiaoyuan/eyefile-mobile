package com.sgeye.exam.android.modules.scanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.ui.scanner.ScanView;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by simon
 */

public class ScannerDelegate extends MargaretDelegate implements ZBarScannerView.ResultHandler {

	private ScanView mScanView = null;
	private CallbackType callbackType = null;

	public void setCallbackType(CallbackType callbackType) {
		this.callbackType = callbackType;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mScanView == null) {
			mScanView = new ScanView(getContext());
		}
		mScanView.setAutoFocus(true);
		mScanView.setResultHandler(this);
	}

	@Override
	public Object setLayout() {
		return mScanView;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mScanView != null) {
			mScanView.startCamera();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mScanView != null) {
			mScanView.stopCameraPreview();
			mScanView.stopCamera();
		}
	}

	@Override
	public void handleResult(Result result) {

		getSupportDelegate().pop();

		@SuppressWarnings("unchecked")
		final IGlobalCallback<String> callback = CallbackManager
				.getInstance()
				.getCallback(this.callbackType);
		if (callback != null) {
			callback.executeCallback(result.getContents());
		}
	}
}
