package com.simon.margaret.app;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.delegates.web.event.EventManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * Created by apple on 2019/8/22.
 */

public class Configurator {

	private static final HashMap<Object, Object> PEANUT_CONFIGS = new HashMap<>();
	private static final Handler HANDLER = new Handler();
	private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

	private Configurator() {
		PEANUT_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
		PEANUT_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
	}


	public static Configurator getInstance() {
		return Holder.INSTANCE;
	}

	final HashMap<Object, Object> getPeanutConfigs() {
		return PEANUT_CONFIGS;
	}

	private static class Holder {
		private static final Configurator INSTANCE = new Configurator();
	}

	public final void configure() {
		PEANUT_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
		Utils.init(Margaret.getApplicationContext());
	}

	public final Configurator withInterceptor(Interceptor interceptor) {
		INTERCEPTORS.add(interceptor);
		PEANUT_CONFIGS.put(ConfigKeys.INTERCEPTOR.name(), INTERCEPTORS);
		return this;
	}

	public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
		INTERCEPTORS.addAll(interceptors);
		PEANUT_CONFIGS.put(ConfigKeys.INTERCEPTOR.name(), INTERCEPTORS);
		return this;
	}

	public final Configurator withApiHost(String host) {
		PEANUT_CONFIGS.put(ConfigKeys.API_HOST, host);
		return this;
	}

	public final Configurator withImageUploadUrl(String url) {
		PEANUT_CONFIGS.put(ConfigKeys.IMAGE_UPLOAD_URL, url);
		return this;
	}

	public Configurator withJavascriptInterface(@NonNull String name) {
		PEANUT_CONFIGS.put(ConfigKeys.JAVASCRIPT_INTERFACE, name);
		return this;
	}

	public Configurator withWebHost(@NonNull String host) {
		PEANUT_CONFIGS.put(ConfigKeys.WEB_HOST, host);
		return this;
	}

	public Configurator withWebEvent(@NonNull String name, @NonNull Event event) {
		final EventManager manager = EventManager.getInstance();
		manager.addEvent(name, event);
		return this;
	}

	public Configurator withActivity(Activity activity) {
		PEANUT_CONFIGS.put(ConfigKeys.Activity, activity);
		return this;
	}


	private void checkConfiguration() {
		final boolean isReady = (boolean) PEANUT_CONFIGS.get(ConfigKeys.CONFIG_READY);
		if (!isReady) {
			throw new RuntimeException("Configuration is not ready, call configure()");
		}
	}

	@SuppressWarnings("unchecked")
	final <T> T getConfiguration(Object key) {
		checkConfiguration();
		final Object value = PEANUT_CONFIGS.get(key);
		if (value == null) {
			throw new NullPointerException(key.toString() + " IS NULL");
		}
		return (T) PEANUT_CONFIGS.get(key);

	}


}
