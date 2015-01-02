package com.jiavan.leapinglight;

import java.util.List;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

public class Constant {
	public static final String UUID_WRITE = "0000fff1-0000-1000-8000-00805f9b34fb";
	public static final int LIGHT_SCALE = 3;
	public static final long SCAN_PERIOD = 10000;
	public static final byte[] LIGHT_ON = {0x00};
	public static final byte[] LIGHT_OFF = {0x01};
	public static final byte[] LIGHT_I = {0x02};
	public static final byte[] LIGHT_II = {0x03};
	public static final byte[] LIGHT_III = {0x04};
	public static final byte[] LIGHT_COMMON = {0x05};
	public static final byte[] LIGHT_RED = {0x07};
	public static final byte[] LIGHT_GREEN = {0x08};
	public static final byte[] LIGHT_BLUE = {0x09};
	public static final byte[] LIGHT_WHITE = {0x0A};
	public static final byte[] LIGHT_SHADE = {0x0B};
	public static final byte[] LIGHT_FLASH = {0x0C};
	public static final byte[] LIGHT_BREATH = {0x0E};
	public static final byte[] LIGHT_MUSIC = {0x0D};
	
	//discovery the services
	public static void gattWriteServices(List<BluetoothGattService> gattServices, byte[] data, BluetoothLeClass mBle) {
        if (gattServices == null) return;
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
        		if(gattCharacteristic.getUuid().toString().equals(Constant.UUID_WRITE)){        			
        			//接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
        			mBle.setCharacteristicNotification(gattCharacteristic, true);
        			//设置数据内容
        			gattCharacteristic.setValue(data);
        			//往蓝牙模块写入数据
        			mBle.writeCharacteristic(gattCharacteristic);
        		}
            }
        }
    }
}
