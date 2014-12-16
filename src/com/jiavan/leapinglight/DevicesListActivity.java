package com.jiavan.leapinglight;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DevicesListActivity extends Activity {

	private ListView lvDevices;
	private Handler mHandler;
	private boolean mScanning;
	private BluetoothAdapter mBluetoothAdapter;
	private List<String> listDevicesInfo;
	private List<BluetoothDevice> listDevices;
	private ArrayAdapter<String> mBTAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        initData();
        openBluetooth();
    }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		scanLeDevice(true);
		
		lvDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				BluetoothDevice device = listDevices.get(position);
				if (device == null) return;
		        if (mScanning) {
		            mBluetoothAdapter.stopLeScan(mLeScanCallback);
		            mScanning = false;
		        }
		        
				String adress = device.getAddress();
				Intent intent = new Intent();
				intent.setClass(DevicesListActivity.this, MainActivity.class);
				intent.putExtra("adress", adress);
				DevicesListActivity.this.startActivity(intent);
			}
		});
	}

	//init the data
    private void initData(){
    	mHandler = new Handler();
    	listDevices = new ArrayList<BluetoothDevice>();
    	listDevicesInfo = new ArrayList<String>();
    	lvDevices = (ListView)findViewById(R.id.lv_devices);
    	mBTAdapter = new ArrayAdapter<String>(DevicesListActivity.this, android.R.layout.simple_expandable_list_item_1, listDevicesInfo);
    	lvDevices.setAdapter(mBTAdapter);
    }
    //open the bluetooth
    private boolean openBluetooth(){
    	// Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "error_bluetooth_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        //open the bluetooth
        if(mBluetoothAdapter.isEnabled()){
        	return true;
        }else return false;
    }
    
    //scan the devices
    private void scanLeDevice(final boolean enable){
    	if(enable){
    		mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, Constant.SCAN_PERIOD);
    		mScanning = true;
    		mBluetoothAdapter.startLeScan(mLeScanCallback);
    	}else{
    		mScanning = false;
    		mBluetoothAdapter.startLeScan(mLeScanCallback);
    	}
    }
    
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			// TODO Auto-generated method stub
			listDevicesInfo.add(device.getName() + "\n" + device.getAddress());
			listDevices.add(device);
			mBTAdapter.notifyDataSetChanged();
		}
	};
}
