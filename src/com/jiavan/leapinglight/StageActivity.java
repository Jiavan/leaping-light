package com.jiavan.leapinglight;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StageActivity extends Activity{

	private int startX;
	private int endX;
	private int screenWidth;
	private int screenHeight;
	private int circleW;
	private int circleH;
	private int smallCircleR;
	private int headH;
	private int[][] circlePoint;
	private int paddingLeft;
	private BluetoothLeClass mBLE;
	private BleData bleObj;
	private Button btShade;
	private Button btFlash;
	private Button btBreath;
	private Button btMusic;
	private ImageView imHead;
	private String deviceAdress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stage);
		initData();
		mBLE.connect(deviceAdress);
		
		btShade = (Button)findViewById(R.id.bt_shade);
		btFlash = (Button)findViewById(R.id.bt_flash);
		btBreath = (Button)findViewById(R.id.bt_breath);
		btMusic = (Button)findViewById(R.id.bt_music);
		imHead = (ImageView)findViewById(R.id.im_head);
		btShade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Autothis.screenHeight-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_SHADE, mBLE);
				//setButtonBG();
				btMusic.setBackgroundColor(Color.DKGRAY);
				btBreath.setBackgroundColor(Color.DKGRAY);
				btFlash.setBackgroundColor(Color.DKGRAY);
				btShade.setBackgroundColor(0x666666);
				
			}
		});
		btFlash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_FLASH, mBLE);
				//setButtonBG();
				btMusic.setBackgroundColor(Color.DKGRAY);
				btBreath.setBackgroundColor(Color.DKGRAY);
				btShade.setBackgroundColor(Color.DKGRAY);
				btFlash.setBackgroundColor(0x999999);
			}
		});
		btBreath.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_BREATH, mBLE);
				btMusic.setBackgroundColor(Color.DKGRAY);
				btFlash.setBackgroundColor(Color.DKGRAY);
				btShade.setBackgroundColor(Color.DKGRAY);
				btBreath.setBackgroundColor(0x999999);
			}
		});
		btMusic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_MUSIC, mBLE);
				btShade.setBackgroundColor(Color.DKGRAY);
				btFlash.setBackgroundColor(Color.DKGRAY);
				btBreath.setBackgroundColor(Color.DKGRAY);
				btMusic.setBackgroundColor(0x999999);
			}
		});
	}
	
	private void initData(){
		this.startX = 0;
		this.endX = 0;
		WindowManager wm = this.getWindowManager();
		this.screenWidth = wm.getDefaultDisplay().getWidth();
		this.screenHeight = wm.getDefaultDisplay().getHeight();
		this.headH = (int)(this.screenHeight *(0.25f));
		this.paddingLeft = (int)(0.1f * this.screenWidth);
		
		bleObj = (BleData)this.getApplication();
		this.mBLE = bleObj.getmBle();
		
		if (!mBLE.initialize()) {
        	Toast.makeText(this, "init data error!", Toast.LENGTH_LONG).show();
            finish();
        }
		
		ImageView stageCircle = (ImageView)findViewById(R.id.stage_circle);
		LayoutParams lp = stageCircle.getLayoutParams();
		lp.width = (int)(this.screenWidth * (4.0f / 5));
		lp.height = lp.width;
		stageCircle.setLayoutParams(lp);
		this.circleW = lp.width;
		this.circleH = lp.height;
		this.smallCircleR = (int)(0.15f * this.circleW);
		
		this.circlePoint = new int[4][4];
		this.circlePoint[0][0] = paddingLeft + smallCircleR;
		this.circlePoint[0][1] = headH + (int)(0.5f * circleH);
		this.circlePoint[1][0] = paddingLeft + (int)(0.5f * circleW);
		this.circlePoint[1][1] = headH + smallCircleR;
		this.circlePoint[2][0] = paddingLeft + circleW - smallCircleR;
		this.circlePoint[2][1] = headH + (int)(0.5f * circleH);
		this.circlePoint[3][0] = paddingLeft + (int)(0.5f * circleW);
		this.circlePoint[3][1] = headH + circleH - smallCircleR;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int)event.getX();
		int y = (int)event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			this.startX = x;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			this.endX = x;
			if((endX - startX) > (this.screenWidth / 4)){
				finish();
				overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
			}else if(x >= circlePoint[0][0] - smallCircleR  && x <= circlePoint[0][0] + smallCircleR 
					&& y >= circlePoint[0][1] - smallCircleR && y <= circlePoint[0][1] + smallCircleR){
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_GREEN, mBLE);
				imHead.setBackgroundColor(Color.GREEN);
			}else if(x >= circlePoint[1][0] - smallCircleR  && x <= circlePoint[1][0] + smallCircleR 
					&& y >= circlePoint[1][1] - smallCircleR && y <= circlePoint[1][1] + smallCircleR){
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_RED, mBLE);
				imHead.setBackgroundColor(Color.RED);
			}else if(x >= circlePoint[2][0] - smallCircleR  && x <= circlePoint[2][0] + smallCircleR 
					&& y >= circlePoint[2][1] - smallCircleR && y <= circlePoint[2][1] + smallCircleR){
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_BLUE, mBLE);
				imHead.setBackgroundColor(Color.BLUE);
			}else if(x >= circlePoint[3][0] - smallCircleR  && x <= circlePoint[3][0] + smallCircleR 
					&& y >= circlePoint[3][1] - smallCircleR && y <= circlePoint[3][1] + smallCircleR){
				Constant.gattWriteServices(mBLE.getSupportedGattServices(), Constant.LIGHT_WHITE, mBLE);
				imHead.setBackgroundColor(Color.WHITE);
			}
		}
		return super.onTouchEvent(event);
	}
}
