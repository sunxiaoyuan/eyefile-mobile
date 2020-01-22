package com.sgeye.exam.android.blutooth_printer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gprinter.command.CpclCommand;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.sgeye.exam.android.R;
import com.simon.margaret.app.Margaret;

import java.util.Vector;

public class CheckPrinter {

	public static void receiptPrint (boolean haveParent){

		EscCommand esc = new EscCommand();
		esc.addInitializePrinter();
		esc.addPrintAndFeedLines((byte) 3);

		/* 设置打印居中 */
		esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
		esc.addText("【北京美和眼科】");
		esc.addPrintAndLineFeed();
		esc.addText("视力筛查结果通知单");
		esc.addPrintAndLineFeed();

		/* 打印文字 */
		/* 取消倍高倍宽 */
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
		esc.addText("筛查日期：【2019-11-11】");
		esc.addPrintAndLineFeed();

		/* 设置打印左对齐 */
		esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
		esc.addText("------------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("姓名：【张三】     家长手机：【18739393933】");
		esc.addPrintAndLineFeed();
		esc.addText("学校：【龙岗路小学】     班级：【2016级1班】");
		esc.addPrintAndLineFeed();
		esc.addText("------------------------------------------------");
		esc.addPrintAndLineFeed();

		esc.addText("尊敬的家长：");
		esc.addPrintAndLineFeed();
		esc.addText("请您微信扫描下方二维码，关注“青少年眼健康”公众号。点击“家长入口”-“档案查询”，查看视力筛查结果。\n");
		esc.addPrintAndLineFeed();

		esc.addSelectSizeOfModuleForQRCode((byte) 10);
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
		esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
		esc.addStoreQRCodeData("www.smarnet.cc");
		esc.addPrintQRCode(); /* 打印QRCode */
		esc.addPrintAndLineFeed();

		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
		esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
		esc.addText("温馨提示：第一次查询需要绑定筛查时预留手机号。");
		esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
		esc.addPrintAndFeedLines((byte) 2);
		esc.addText("本次筛查中的验光为非睫状肌麻痹验光，结果不具有诊断意义。若初筛结果提示“视力不良”或”异常：，请及时复查，确认是否为“近视”、“斜视”、“弱视”等眼病，及时采取正确的治疗措施。");
		esc.addPrintAndFeedLines((byte) 2);

		esc.addText("筛查机构：【海淀区妇幼保健院】");
		esc.addPrintAndLineFeed();
		esc.addText("联系电话：【010-88888888】");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 2);

		if (haveParent){
			esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
			esc.addText("-----------------------");
			esc.addPrintAndFeedLines((byte) 2);

			esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
			/* 设置打印居中 */
			esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
			esc.addText("【北京美和眼科】");
			esc.addPrintAndLineFeed();
			esc.addText("视力筛查家长回执单");
			esc.addPrintAndLineFeed();

			/* 打印文字 */
			/* 取消倍高倍宽 */
			esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
			esc.addText("筛查日期：【2019-11-11】");
			esc.addPrintAndLineFeed();
			esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
			esc.addText("------------------------------------------------");
			esc.addPrintAndLineFeed();
			esc.addText("姓名：【张三】     家长手机：【18739393933】");
			esc.addPrintAndLineFeed();
			esc.addText("学校：【龙岗路小学】     班级：【2016级1班】");
			esc.addPrintAndFeedLines((byte) 2);

			esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
			esc.addText("我已经查看并了解了学生的视力筛查结果");
			esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
			esc.addPrintAndFeedLines((byte) 2);

			esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
			esc.addText("右眼：_______________");
			esc.addPrintAndFeedLines((byte) 2);
			esc.addText("左眼：_______________");
			esc.addPrintAndFeedLines((byte) 2);

			esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
			esc.addText("家长签字：____________    日期：_____________");
			esc.addPrintAndLineFeed();
		}


		/* 开钱箱 */
		esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
		esc.addPrintAndFeedLines((byte) 8);
		esc.addSound((byte) 1, (byte) 3);
		byte[] bytes = {29, 114, 1};
		esc.addUserCommand(bytes);
		Vector<Byte> datas = esc.getCommand();
		/* 发送数据 */
		DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(datas);


	}


}
