package com.jiavan.leapinglight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class AboutActivity extends Activity{

	private int startX;
	private int endX;
	private int screenWidth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        this.screenWidth = width;
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
				intent.setClass(AboutActivity.this, DevicesListActivity.class);
				AboutActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
				finish();
			}
		}
		
		return super.onTouchEvent(event);
	}
}
