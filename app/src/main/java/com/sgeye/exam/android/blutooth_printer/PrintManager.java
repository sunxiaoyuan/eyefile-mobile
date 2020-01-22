package com.sgeye.exam.android.blutooth_printer;

import android.annotation.SuppressLint;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.event.bean.PrintEventBean;
import com.simon.margaret.app.Margaret;

import java.util.ArrayList;
import java.util.Vector;

public class PrintManager {

    private static PrintManager mSingleton = null;

    private PrintManager() {
    }

    public static PrintManager getInstance() {
        if (mSingleton == null) {
            synchronized (PrintManager.class) {
                if (mSingleton == null) {
                    mSingleton = new PrintManager();
                }
            }
        }
        return mSingleton;
    }


    private int counts;
    ArrayList<String> per = new ArrayList<>();
    private UsbManager usbManager;

    // 判断打印机所使用指令是否是ESC指令
    /**
     * ESC查询打印机实时状态指令
     */
    private byte[] esc = {0x10, 0x04, 0x02};

    /**
     * CPCL查询打印机实时状态指令
     */
    private byte[] cpcl = {0x1b, 0x68};

    /**
     * TSC查询打印机状态指令
     */
    private byte[] tsc = {0x1b, '!', '?'};

    private static final int CONN_MOST_DEVICES = 0x11;
    private static final int CONN_PRINTER = 0x12;
    private int printcount = 0;
    private boolean continuityprint = false;
    private ThreadPool threadPool;
    private static final int CONN_STATE_DISCONN = 0x007;
    private static final int PRINTER_COMMAND_ERROR = 0x008;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] != null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getConnState()) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].closePort(0);
                        ToastUtils.showShort(Margaret.getApplicationContext().getString(R.string.str_disconnect_success));
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    ToastUtils.showShort(Margaret.getApplicationContext().getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    ToastUtils.showShort(Margaret.getApplicationContext().getString(R.string.str_cann_printer));
                    break;
            }
        }
    };

    // 重新连接回收上次连接的对象，避免内存泄漏
    private void closeport() {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] != null && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].mPort != null) {
//            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].reader.cancel();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].mPort.closePort();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].mPort = null;
        }
    }


    // 打印机状态查询
    public void btnPrinterState() {
        /* 打印机状态查询 */
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getConnState()) {
            ToastUtils.showShort(Margaret.getApplicationContext().getString(R.string.str_cann_printer));
            return;
        }
        DeviceConnFactoryManager.whichFlag = true;
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                Vector<Byte> data = new Vector<>(esc.length);
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                    for (int i = 0; i < esc.length; i++) {
                        data.add(esc[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(data);
                } else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    for (int i = 0; i < tsc.length; i++) {
                        data.add(tsc[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(data);
                } else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getCurrentPrinterCommand() == PrinterCommand.CPCL) {
                    for (int i = 0; i < cpcl.length; i++) {
                        data.add(cpcl[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(data);
                }
            }
        });
    }

    public void printRecept(boolean haveParent, PrintEventBean bean) {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getConnState()) {
            ToastUtils.showShort(Margaret.getApplicationContext().getString(R.string.str_cann_printer));
            return;
        }
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                    print(haveParent, bean);
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }


    private void print(boolean haveParent, PrintEventBean bean) {

        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);

        /* 设置打印居中 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addText("【" + bean.getHospitalName() + "】");
        esc.addPrintAndLineFeed();
        esc.addText("视力筛查结果通知单");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        /* 取消倍高倍宽 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addText("筛查日期：【" + bean.getCheckDate() + "】");
        esc.addPrintAndLineFeed();

        /* 设置打印左对齐 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("------------------------------------------------");
        esc.addPrintAndLineFeed();
        esc.addText("姓名：【" + bean.getName() + "】     家长手机：【" + bean.getPhone() + "】");
        esc.addPrintAndLineFeed();
        esc.addText("学校：【" + bean.getSchoolName() + "】     班级：【" + bean.getGrade() + "级" + bean.getClassName() + "班】");
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
        esc.addStoreQRCodeData(bean.getQrcode());
        esc.addPrintQRCode(); /* 打印QRCode */
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("温馨提示：第一次查询需要绑定筛查时预留手机号。");
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addPrintAndFeedLines((byte) 2);
        esc.addText("本次筛查中的验光为非睫状肌麻痹验光，结果不具有诊断意义。若初筛结果提示“视力不良”或”异常：，请及时复查，确认是否为“近视”、“斜视”、“弱视”等眼病，及时采取正确的治疗措施。");
        esc.addPrintAndFeedLines((byte) 2);

        esc.addText("筛查机构：【" + bean.getHospitalName() + "】");
        esc.addPrintAndLineFeed();
        esc.addText("联系电话：【" + bean.getMobile() + "】");
        esc.addPrintAndLineFeed();
        esc.addPrintAndFeedLines((byte) 2);

        if (haveParent) {
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
            esc.addText("-----------------------");
            esc.addPrintAndFeedLines((byte) 2);

            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
            /* 设置打印居中 */
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
            esc.addText("【" + bean.getHospitalName() + "】");
            esc.addPrintAndLineFeed();
            esc.addText("视力筛查家长回执单");
            esc.addPrintAndLineFeed();

            /* 打印文字 */
            /* 取消倍高倍宽 */
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
            esc.addText("筛查日期：【" + bean.getCheckDate() + "】");
            esc.addPrintAndLineFeed();
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
            esc.addText("------------------------------------------------");
            esc.addPrintAndLineFeed();
            esc.addText("姓名：【" + bean.getName() + "】     家长手机：【" + bean.getPhone() + "】");
            esc.addPrintAndLineFeed();
            esc.addText("学校：【" + bean.getSchoolName() + "】     班级：【" + bean.getGrade() + "级" + bean.getClassName() + "班】");
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
