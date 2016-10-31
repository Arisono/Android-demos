package com.cn.nj.vhtable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CHTableScrollView extends HorizontalScrollView {
	
	HTableActivity activity;
	
	public CHTableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (HTableActivity) context;
	}

	
	public CHTableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (HTableActivity) context;
	}

	public CHTableScrollView(Context context) {
		super(context);
		activity = (HTableActivity) context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//进行触摸赋值
		activity.mTouchView = this;
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//当当前的CHSCrollView被触摸时，滑动其它
		if(activity.mTouchView == this) {
			activity.onScrollChanged(l, t, oldl, oldt);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
