package com.jiavan.leapinglight;

import android.app.Application;

public class BleData extends Application{
	public BluetoothLeClass mBle;

	public BluetoothLeClass getmBle() {
		return mBle;
	}

	public void setmBle(BluetoothLeClass mBle) {
		this.mBle = mBle;
	}
}