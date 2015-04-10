package com.example.colorselfdefinedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RectangleColorSelectorView extends View {
	private float rectLeft;// 渐变方块左x坐标
	private float rectTop;// 渐变方块右x坐标
	private float rectRight;// 渐变方块上y坐标
	private float rectBottom;// 渐变方块下y坐标

	private final String TAG = "ColorPicker";
	private OnColorChangedListener mListener;
	private String colorValue = null;
	
	boolean inRec = false;
	boolean moveRec = false;
	private DisplayMetrics metrics;
	private Paint mPaint;// 渐变色环画笔
	private Shader s;

	public interface OnColorChangedListener {
		public void onColorChanged(String color);
	}

	public RectangleColorSelectorView(Context context) {
		super(context);
	}

	public RectangleColorSelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		metrics = getContext().getResources().getDisplayMetrics();
		setInitData();
	}

	public void setInitData() {		
		rectLeft = 0;
		rectTop = metrics.heightPixels * (float)0.8;
		rectRight = metrics.widthPixels;
		rectBottom = metrics.heightPixels;
		s = new LinearGradient(rectLeft, rectTop, rectRight, rectBottom,generateStreechColorArray(), null, TileMode.CLAMP);		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	private int[] generateStreechColorArray() {

		int[] colorArray = new int[361];

		int count = 0;
		for (int i = colorArray.length - 1; i >= 0; i--, count++) {
			colorArray[count] = Color.HSVToColor(new float[] { i, 1f, 1f });
		}
		return colorArray;
	}

	@Override
	protected void onDraw(Canvas canvas) {		
		mPaint.setShader(s);
		mPaint.setStyle(Paint.Style.STROKE);		
		mPaint.setStrokeWidth(canvas.getMaximumBitmapWidth());
		
		canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mPaint);
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			float x = event.getX();
			float y = event.getY();
			inRec = inColorRec(x, y);
			if (inRec) {// down按在渐变色环内, 且move也在渐变色环内
				colorValue = interpRectColor(x);
			}
		case MotionEvent.ACTION_MOVE:
            float rawX = event.getRawX();
            float rawY = event.getRawY();
            moveRec = inColorRec(rawX, rawY);
			if (moveRec) {// down按在渐变色环内, 且move也在渐变色环内
				colorValue = interpRectColor(rawX);
				if(colorValue != null){
					mListener.onColorChanged(colorValue);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (inRec || moveRec) {
				if (mListener != null && colorValue != null) {
					mListener.onColorChanged(colorValue);
					inRec = false;
					moveRec = false;
				}
			}
//			if (downInRect) {
//				downInRect = false;
//			}
			break;
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
//		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//	    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//	    this.setMeasuredDimension(parentWidth, parentHeight);	    
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private float pointToStrechColor(float x) {

		float width = rectRight - rectLeft;

		if (x < rectLeft) {
			x = 0f;
		} else if (x > rectRight) {
			x = width;
		} else {
			x = x - rectLeft;
		}

		return 360f - (x * 360f / width);
	}

	/**
	 * 坐标是否在长方形上
	 * 
	 * @param x 坐标
	 * @param y 坐标
	 * @return
	 */
	private boolean inColorRec(float x, float y) {
		if ((x >= rectLeft && x <= rectRight)&& (y <= rectBottom && y >= rectTop)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取渐变块上颜色
	 * @param x
	 * @return
	 */
	private String interpRectColor(float x) {
		Log.e(TAG, "\n x " + x);
		Log.e(TAG,"\ncolor " + Color.HSVToColor(new float[] { pointToStrechColor(x),1f, 1f }));
		int currentColor = Color.HSVToColor(new float[] {pointToStrechColor(x), 1f, 1f });
		return convertToARGB(currentColor);
	}

	/**
	 * 转化为ARGB格式字符串 For custom purposes. Not used by ColorPickerPreferrence
	 * 
	 * @param color
	 * @author Unknown
	 */
	public String convertToARGB(int color) {
		String alpha = Integer.toHexString(Color.alpha(color));
		String red = Integer.toHexString(Color.red(color));
		String green = Integer.toHexString(Color.green(color));
		String blue = Integer.toHexString(Color.blue(color));

		if (alpha.length() == 1) {
			alpha = "0" + alpha;
		}

		if (red.length() == 1) {
			red = "0" + red;
		}

		if (green.length() == 1) {
			green = "0" + green;
		}

		if (blue.length() == 1) {
			blue = "0" + blue;
		}

		return "#" + red + green + blue;
	}

	public OnColorChangedListener getmListener() {
		return mListener;
	}

	public void setOnColorChangedListener(OnColorChangedListener mListener) {
		this.mListener = mListener;
	}
}
