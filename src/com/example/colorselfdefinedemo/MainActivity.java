package com.example.colorselfdefinedemo;

import com.example.colorselfdefinedemo.RectangleColorSelectorView.OnColorChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements OnColorChangedListener {

    Context context;
    private RectangleColorSelectorView colorPicker;
    private TextView tvText;
    
    private RectangleColorSelectorView View;
    
    String  TAG = "ColorPickerView";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    /**
     * 初始化UI
     */
    private void initViews() {
    	colorPicker = (RectangleColorSelectorView) findViewById(R.id.self_view);
    	colorPicker.setOnColorChangedListener(this);
    	tvText = (TextView) findViewById(R.id.tv_text);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
//    	int width = colorPicker.getMeasuredWidth();
//    	int height = colorPicker.getMeasuredHeight();
//    	
//    	DisplayMetrics metrics = getResources().getDisplayMetrics();
//    	int screenWidth = metrics.widthPixels;
//    	int screenHeight = metrics.heightPixels;
    }
    
	@Override
	public void onColorChanged(String color) {
		Log.e(TAG, "最后获得的颜色是" + color);
		tvText.setBackgroundColor(Color.parseColor(color));
	}
}
