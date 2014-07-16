package com.example.listviewsamples;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 1. Use ListView footer to create a dynamically loading list (Endless ListView)
 * 
 * @author john
 * 
 */
public class DynLoadingListViewActivity extends Activity {

	private ListView mListView;
	private ArrayAdapter<String> mAdapter;
	private View mFooterView;
	private boolean isLoading;
	private boolean isDone;
	
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_endless_list_view);
		initializeViews();
	}

	private void initializeViews() {
		mListView = (ListView) findViewById(R.id.list);
		isLoading = false;
		isDone = false;
		updateViews();
	}

	private void updateViews() {
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
		
		// ListView footer and header should be added before setAdapter() method
		if (mFooterView == null) {
			mFooterView = LayoutInflater.from(this).inflate(R.layout.footer_loading, null);
			mListView.addFooterView(mFooterView);
		}
		mListView.setAdapter(mAdapter);
		requestData();
		initializeListeners();
	}
	
	private void requestData() {
		
		if (mAdapter.getCount() == 60) {
			isDone = true;
			mListView.removeFooterView(mFooterView);
		}
		
		isLoading = true;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					mAdapter.add("Object " + mAdapter.getCount());
				}
				isLoading = false;
			}
		}, 5000);
	}
	
	private void initializeListeners() {
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (isLoading || isDone) return;
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					// Arrive the bottom
					requestData();
				}
			}
		});
	}
}
