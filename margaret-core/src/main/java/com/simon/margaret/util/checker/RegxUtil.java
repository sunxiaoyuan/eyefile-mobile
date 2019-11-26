package com.simon.margaret.util.checker;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.simon.margaret.app.Margaret;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 2019/11/22.
 */

public class RegxUtil {

	public static boolean checkIPWithEditText(EditText editText) {
		if (editText != null) {
			String string = editText.getText().toString().trim();
			/*正则表达式*/
			String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//限定输入格式
			Pattern p = Pattern.compile(ip);
			Matcher m = p.matcher(string);
			boolean b = m.matches();
			if ("".equals(string)) {
				Toast.makeText(Margaret.getApplicationContext(), "IP地址不可为空!", Toast.LENGTH_LONG).show();
				return false;
			} else {
				if (!b) {
					Toast.makeText(Margaret.getApplicationContext(), "IP格式输入错误！", Toast.LENGTH_LONG).show();
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}


}
