package com.sgeye.exam.android.blutooth_printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.R2;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class BluetoothDeviceListDelegate extends BottomItemDelegate {

    /**
     * Debugging
     */
    private static final String DEBUG_TAG = "DeviceListActivity";
    public static LinearLayout deviceNamelinearLayout;

    /**
     * Member fields
     */

    @BindView(R2.id.lvPairedDevices)
    ListView lvPairedDevice = null;
    //   private TextView tvPairedDevice = null, tvNewDevice = null;
    @BindView(R2.id.btBluetoothScan)
    Button btDeviceScan = null;

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mDevicesArrayAdapter;

    //   private ArrayAdapter<String> mNewDevicesArrayAdapter;
    public static final String EXTRA_DEVICE_ADDRESS = "address";
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 3;



    @Override
    public Object setLayout() {
        return R.layout.delegate_bluetooth_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
//        _mActivity.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        btDeviceScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.setVisibility(View.GONE);
                discoveryDevice();
            }
        });

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        _mActivity.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        _mActivity.registerReceiver(mFindBlueToothReceiver, filter);
        initBluetooth();
    }


    private void initBluetooth() {
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastUtils.showShort("Bluetooth is not supported by the device");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,
                        REQUEST_ENABLE_BT);
            } else {
                getDeviceList();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                // bluetooth is opened
                getDeviceList();
            } else {
                // bluetooth is not open
                ToastUtils.showShort("bluetooth is not enabled");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        if (mFindBlueToothReceiver != null) {
            _mActivity.unregisterReceiver(mFindBlueToothReceiver);
        }
    }

    protected void getDeviceList() {
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mDevicesArrayAdapter = new ArrayAdapter<String>(_mActivity,
                R.layout.bluetooth_device_name_item);
//        mNewDevicesArrayAdapter = new ArrayAdapter<>(this,
//                R.layout.bluetooth_device_name_item);
        lvPairedDevice.setAdapter(mDevicesArrayAdapter);
        lvPairedDevice.setOnItemClickListener(mDeviceClickListener);
//        lvNewDevice.setAdapter(mNewDevicesArrayAdapter);
//        lvNewDevice.setOnItemClickListener(mDeviceClickListener);
//        // Get the local Bluetooth adapter
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        mDevicesArrayAdapter.add("已连接");
        if (pairedDevices.size() > 0) {
            //  tvPairedDevice.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
        } else {
            String noDevices = "none paired";
            mDevicesArrayAdapter.add(noDevices);
        }
    }

    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mDevicesArrayAdapter.add(device.getName() + "\n"
                            + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                _mActivity.setProgressBarIndeterminateVisibility(false);
                _mActivity.setTitle("select bluetooth device");
                Log.i("tag", "finish discovery" + (mDevicesArrayAdapter.getCount() - 2));
                if (mDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = "none bluetooth device found";
                    mDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };


    private void discoveryDevice() {
        // Indicate scanning in the title
        _mActivity.setProgressBarIndeterminateVisibility(true);
        _mActivity.setTitle("scanning");
        // Turn on sub-title for new devices
        mDevicesArrayAdapter.add("新发现");
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String noDevices = "none paired";
            String noNewDevice = "none bluetooth device found";
            Log.i("tag", info);
            if (!info.equals(noDevices) && !info.equals(noNewDevice) && !info.equals("新发现") && !info.equals("已连接")) {
                mBluetoothAdapter.cancelDiscovery();
                String address = info.substring(info.length() - 17);

                @SuppressWarnings("unchecked")
                final IGlobalCallback<String> callback = CallbackManager
                        .getInstance()
                        .getCallback(CallbackType.ON_CLICK_BLUTOOTH_DEVICE);
                if (callback != null) {
                    callback.executeCallback(address);
                }

                getSupportDelegate().pop();
            }
        }
    };

    // 返回按钮
    @OnClick(R2.id.btn_blutooth_list_back)
    public void back() {
        getSupportDelegate().pop();
    }

}
