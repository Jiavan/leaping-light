package com.jiavan.leapinglight;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity{

	private String deviceAdress;
	private BluetoothLeClass mBLE;
	private BleData bleObj;
	private ImageButton ibPower;
	private int LIGHT_STATE = 0;
	private ImageButton ibPowerI;
	private ImageButton ibPowerII;
	private ImageButton ibPowerIII;
	private int currentLightPower;
	private boolean lightSwitch;
	private int screenWidth;
	private int startX;
	private int endX;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initData();
		initViews();
		
		Intent intent = new Intent();
		intent = this.getIntent();
		
		this.bleObj = (BleData)this.getApplication();
        bleObj.setmBle(mBLE);
		mBLE.connect(deviceAdress);
		//light on/off
		ibPower.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				++LIGHT_STATE;
				if(LIGHT_STATE == 2)
					LIGHT_STATE = 0;
				
				if(LIGHT_STATE == 0){
					Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_OFF, mBLE);
					ibPower.setBackgroundResource(R.drawable.light_off);
					lightSwitch = false;
				}else{
					Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_ON, mBLE);
					ibPower.setBackgroundResource(R.drawable.light_on);
					lightSwitch = true;
				}
			}
		});
		//light powerI
		ibPowerI.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_I, mBLE);
				currentLightPower = 1;
				lightSwitch = true;
				setCurrentState(currentLightPower, lightSwitch);
			}
		});
		ibPowerII.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_II, mBLE);
				currentLightPower = 2;
				lightSwitch = true;
				setCurrentState(currentLightPower, lightSwitch);
			}
		});
		ibPowerIII.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_III, mBLE);
				currentLightPower = 3;
				lightSwitch = true;
				setCurrentState(currentLightPower, lightSwitch);
			}
		});
	}
	
	private void initData(){
		Intent intent = this.getIntent();
		deviceAdress = intent.getStringExtra("adress");
		
		mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
        	Toast.makeText(this, "init data error!", Toast.LENGTH_LONG).show();
            finish();
        }
        
        
        
        this.startX = 0;
        this.endX = 0;
	}
	
	private void initViews(){
		ibPower = (ImageButton)findViewById(R.id.ib_power);
        ibPowerI = (ImageButton)findViewById(R.id.ib_power_I);
        ibPowerII = (ImageButton)findViewById(R.id.ib_power_II);
        ibPowerIII = (ImageButton)findViewById(R.id.ib_power_III);
        ibPower.setBackgroundResource(R.drawable.light_off);
        
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        this.screenWidth = width;
        android.view.ViewGroup.LayoutParams lp = ibPowerI.getLayoutParams();
        lp.width = width / Constant.LIGHT_SCALE;
        lp.height = width / Constant.LIGHT_SCALE;
        ibPowerI.setLayoutParams(lp);
        ibPowerII.setLayoutParams(lp);
        ibPowerIII.setLayoutParams(lp);
        ibPowerI.setBackgroundResource(R.drawable.light_power_off);
        ibPowerII.setBackgroundResource(R.drawable.light_power_off);
        ibPowerIII.setBackgroundResource(R.drawable.light_power_off);
	}
	
	private void setCurrentState(int lightPower, boolean lightSwitch){
		if(lightPower == 1){
			ibPowerI.setBackgroundResource(R.drawable.light_power_on);
			ibPowerII.setBackgroundResource(R.drawable.light_power_off);
			ibPowerIII.setBackgroundResource(R.drawable.light_power_off);
		}else if(lightPower == 2){
			ibPowerI.setBackgroundResource(R.drawable.light_power_on);
			ibPowerII.setBackgroundResource(R.drawable.light_power_on);
			ibPowerIII.setBackgroundResource(R.drawable.light_power_off);
		}else if(lightPower == 3){
			ibPowerI.setBackgroundResource(R.drawable.light_power_on);
			ibPowerII.setBackgroundResource(R.drawable.light_power_on);
			ibPowerIII.setBackgroundResource(R.drawable.light_power_on);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int)event.getX();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			startX = x;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			endX = x;
			if((startX - endX) > (this.screenWidth / 4)){
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, StageActivity.class);
				intent.putExtra("adress", deviceAdress);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);  
				/*LayoutInflater inflator = this.getLayoutInflater();
				View viewIn = inflator.inflate(R.layout.activity_stage, null, false);
				View viewOut = inflator.inflate(R.layout.activity_main, null, false);
				viewOut.startAnimation(AnimationUtils.loadAnimation(this, R.anim.out_to_left));
				viewIn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_right));
				setContentView(viewIn);*/
			}
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub				intent.putE
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
}
