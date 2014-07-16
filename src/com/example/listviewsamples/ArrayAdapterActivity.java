package com.example.listviewsamples;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 1. Use the smallest ArrayAdapter to display ListView
 * 
 * 2. Show how to highlight one item when selected
 * 
 * 3. Increase height between list items. (android:dividerHeight="20dp")
 * 
 * 4. Highlight clicked item by changing the selector color. (android:listSelector="@android:color/darker_gray")
 * 
 * @author john
 *
 */
public class ArrayAdapterActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_array_adapter);
		
		SampleAdapter adapter = new SampleAdapter(this);
		
		for (int i = 0; i < 10; i++) {
			adapter.add(new MenuItem(android.R.drawable.ic_delete, "Click me", android.R.drawable.ic_media_next));
		}
		
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    SampleAdapter sampleAdapter = (SampleAdapter) l.getAdapter();
	    sampleAdapter.setSelectedPosition(position);
	}
	
	private class MenuItem {
		public int iconRes;
		public String tag;
		public int arrowRes;
		
		public MenuItem(int iconRes, String tag, int arrowRes) {
			this.iconRes = iconRes;
			this.tag = tag;
			this.arrowRes = arrowRes;
		}
	}
	
	private class SampleAdapter extends ArrayAdapter<MenuItem> {
	    
	    private int selectedIndex = -1;
	    
		public SampleAdapter(Context context) {
			super(context, 0);
		}
		
		public void setSelectedPosition(int position) {
            selectedIndex = position;
            notifyDataSetChanged();
        }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_array_adapter, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView tag = (TextView) convertView.findViewById(R.id.tag);
			tag.setText(getItem(position).tag);
			ImageView arrow = (ImageView) convertView.findViewById(R.id.arrow);
			arrow.setImageResource(getItem(position).arrowRes);
			if (selectedIndex != -1 && selectedIndex == position) {
                convertView.setBackgroundResource(android.R.color.black);
            } else {
                convertView.setBackgroundResource(android.R.color.transparent);
            }
			return convertView;
		}
	}
}
