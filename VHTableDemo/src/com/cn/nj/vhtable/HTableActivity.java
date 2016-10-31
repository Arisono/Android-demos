package com.cn.nj.vhtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
/**
 * 
 * 带滑动表头与固定列的ListView
 */
public class HTableActivity extends Activity{
	 private ListView mListView;
	 
	 public HorizontalScrollView mTouchView;
	 //装入所有的HScrollView
	 protected List<CHTableScrollView> mHScrollViews =new ArrayList<CHTableScrollView>();
	 HashMap<String,EditText> mColumnControls = new HashMap<String,EditText>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_htable);
		initViews();
		
	}
	
	private void initViews() {
		
		//Column
		String[] cols = {
				"title","data_1","data_2","data_3",
				"data_4","data_5","data_6",
				"data_7","data_8","data_9",};
		
		//Table Title
		LinearLayout titleLinearLayout = (LinearLayout)this.findViewById(R.id.scrollLinearLayout);
		for(int i=0 ;i<cols.length;i++){
			if(i!=0){
				View linearLay = newView(R.layout.row_title_edit_view,"Title_"+i);
				EditText et = (EditText)linearLay.findViewById(R.id.tevEditView);
				et.setText("Date"+i);
				
				titleLinearLayout.addView(linearLay);
			}
		}
		
		List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
		Map<String, String> data = null;
		CHTableScrollView headerScroll = (CHTableScrollView) findViewById(R.id.item_scroll_title);
		//添加头滑动事件 
		mHScrollViews.add(headerScroll);
		mListView = (ListView) findViewById(R.id.scroll_list);
		for(int i = 0; i < 100; i++) {
			data = new HashMap<String, String>();

			data.put(cols[0], "Title_" + i);
			data.put(cols[1], "Date_" + 1 + "_" +i );
			data.put(cols[2], "Date_" + 2 + "_" +i );
			data.put(cols[3], "Date_" + 3 + "_" +i );
			data.put(cols[4], "Date_" + 4 + "_" +i );
			data.put(cols[5], "Date_" + 5 + "_" +i );
			data.put(cols[6], "Date_" + 6 + "_" +i );
			data.put(cols[7], "Date_" + 7 + "_" +i );
			data.put(cols[8], "Date_" + 8 + "_" +i );
			data.put(cols[9], "Date_" + 9 + "_" +i );
			
			datas.add(data);
		}
		

		mColumnControls.clear();
		for(int i=0;i<cols.length;i++){
			//预留第一列
			if(i!=0){
				EditText etItem1 = new EditText(HTableActivity.this);
				etItem1.setWidth(50);
				etItem1.setTextColor(Color.DKGRAY);
				etItem1.setGravity(Gravity.CENTER);
				//
				mColumnControls.put(cols[i], etItem1);
			}
		}
		
		BaseAdapter adapter = new ScrollAdapter2(this, datas, R.layout.row_item_edit, cols);
		mListView.setAdapter(adapter);
	}
	
	public void addHViews(final CHTableScrollView hScrollView) {
		if(!mHScrollViews.isEmpty()) {
			int size = mHScrollViews.size();
			CHTableScrollView scrollView = mHScrollViews.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			//第一次满屏后，向下滑动，有一条数据在开始时未加入
			if(scrollX != 0) {
				mListView.post(new Runnable() {
					@Override
					public void run() {
						//当listView刷新完成之后，把该条移动到最终位置
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		mHScrollViews.add(hScrollView);
	}
	
	public void onScrollChanged(int l, int t, int oldl, int oldt){
		for(CHTableScrollView scrollView : mHScrollViews) {
			//防止重复滑动
			if(mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}
	class ScrollAdapter2 extends BaseAdapter {
		private List<? extends Map<String, ?>> datas;
		private int res;
		private String[] from;
		private Context context;
		public ScrollAdapter2(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from) {
			this.context = context;
			this.datas = data;
			this.res = resource;
			this.from = from;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			if(v == null) {
				v = LayoutInflater.from(context).inflate(res, null);
				//第一次初始化的时候装进来
				
				mColumnControls.put("title", (EditText)v.findViewById(R.id.item_title));
				View chsv = v.findViewById(R.id.item_scroll);
				LinearLayout ll = (LinearLayout)chsv.findViewById(R.id.item_scroll_layout);
				View[] views = new View[from.length];
				
				for(int i=0 ;i < from.length;i++){
					if(i==0){
						views[0] = v.findViewById(R.id.item_title);
						continue;
					}

					View linearLay = newView(R.layout.row_item_edit_view,from[i]);
					EditText td = (EditText)linearLay.findViewById(R.id.ievEditView);
					
					td.setOnClickListener(clickListener);
					ll.addView(linearLay);
					
					views[i] = td;
				}
				//
				v.setTag(views);
				
				addHViews((CHTableScrollView)chsv);
			}
			
			View[] holders = (View[]) v.getTag();
			int len = holders.length;
			for(int i = 0 ; i < len; i++) {
				((EditText)holders[i]).setText(this.datas.get(position).get(from[i]).toString());
			}
			
			return v;
		}
	}
	
	private View newView(int res_id,String tag_name){
		
		View itemView  = LayoutInflater.from(HTableActivity.this).inflate(res_id, null);
		itemView.setTag(tag_name);
		
		return itemView;
	}
	
	class ScrollAdapter extends SimpleAdapter {

		private List<? extends Map<String, ?>> datas;
		private int res;
		private String[] from;
		private int[] to;
		private Context context;
		public ScrollAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.context = context;
			this.datas = data;
			this.res = resource;
			this.from = from;
			this.to = to;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
				v = LayoutInflater.from(context).inflate(res, null);
				//第一次初始化的时候装进来
				addHViews((CHTableScrollView) v.findViewById(R.id.item_scroll));
				View[] views = new View[to.length];
				//
				for(int i = 0; i < to.length; i++) {
					View tv = v.findViewById(to[i]);
					tv.setOnClickListener(clickListener);
					views[i] = tv;
				}
				//
				v.setTag(views);
			}
			View[] holders = (View[]) v.getTag();
			int len = holders.length;
			for(int i = 0 ; i < len; i++) {
				((EditText)holders[i]).setText(this.datas.get(position).get(from[i]).toString());
			}
			return v;
		}
	}
	
	//测试点击的事件 
	protected View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(HTableActivity.this, ((EditText)v).getText(), Toast.LENGTH_SHORT).show();
		}
	};
}
