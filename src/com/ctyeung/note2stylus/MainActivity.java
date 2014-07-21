package com.ctyeung.note2stylus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.SPenTouchListener;


//http://developer.samsung.com/s-pen-sdk/technical-docs/S-Pen-SDK-2-3-Tutorial
//http://developer.samsung.com/forum/board/thread/view.do?boardName=SPenSDK&messageId=167254&startPage=9&curPage=11

public class MainActivity extends Activity {

	protected SCanvasView canvasView;
	protected RelativeLayout mCanvasContainer;
	protected int titleHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Context context  = this.getApplicationContext();
		
		try{
			mCanvasContainer = (RelativeLayout) findViewById(R.id.canvas_container);		
			canvasView = new SCanvasView(context);        
			mCanvasContainer.addView(canvasView);
			canvasView.setBackgroundColor(0xFF0000FF);
			canvasView.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
			canvasView.setSPenTouchListener(mSPenTouchListener);
			
			SettingStrokeInfo strokeInfo = canvasView.getSettingViewStrokeInfo();

			if(strokeInfo != null) 
			{
				int nColor = 0xFFFF0000;
				strokeInfo.setStrokeColor(nColor);	
				canvasView.setSettingViewStrokeInfo(strokeInfo);
				canvasView.setCanvasMode( SCanvasConstants.SCANVAS_MODE_INPUT_FILLING );
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// http://stackoverflow.com/questions/3355367/height-of-statusbar/3356263#3356263
	public int getStatusBarHeight() {
		
		Rect rectgle= new Rect();
		Window window= getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int StatusBarHeight= rectgle.top;
		int contentViewTop= 
		    window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int TitleBarHeight= contentViewTop - StatusBarHeight;
		
		return TitleBarHeight;
	}
	
	/**
	 * S Pen touch listener, which recognizes S Pen and finger touch events.
	 */
	private final SPenTouchListener mSPenTouchListener = new SPenTouchListener() {
		private int mDownX;
		private int mDownY;

		@Override
		public boolean onTouchFinger(View view, MotionEvent event) {
			onTouchPen(view, event);
			return true;
		}

		@Override
		public boolean onTouchPen(View view, MotionEvent event) {
			
			if(titleHeight==0)
				titleHeight = getStatusBarHeight();
			
			int action = event.getAction();
			canvasView.drawSAMMStrokePoint(action, 
	    			event.getRawX(), 
	    			event.getRawY() - titleHeight, 
	    			event.getPressure(), 
	    			event.getMetaState(), 
	    			event.getDownTime(), 
	    			event.getEventTime());
			/*
		    switch (action) {
			    case MotionEvent.ACTION_DOWN:
				      break;
				      
			    case MotionEvent.ACTION_MOVE: 
			    	break;
			    	
			    case MotionEvent.ACTION_UP:
			    	break;
			    	
			    case MotionEvent.ACTION_CANCEL:
				      break;
				      
				    default:
				      break;
			} */

			return true;
		}

		@Override
		public void onTouchButtonDown(View arg0, MotionEvent arg1) {
			// do nothing
		}

		@Override
		public void onTouchButtonUp(View arg0, MotionEvent arg1) {
			// do nothing
		}

		@Override
		public boolean onTouchPenEraser(View arg0, MotionEvent arg1) {
			// do nothing
			return false;
		}
	};
}

