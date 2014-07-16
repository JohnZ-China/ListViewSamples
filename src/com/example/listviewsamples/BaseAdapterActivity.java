package com.example.listviewsamples;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listviewsamples.util.ToastHelper;

/**
 * 1. Use BaseAdapter to display ListView
 * 
 * 2. Filter feature 
 * 
 * 3. Check box in ListView, fix CheckBox status saved bug when adapter reuse view
 * 
 * 4. Enable list item click (CheckBox android:focusable="false")
 * (By default, item click will disable when there is a check box or other views in one item)
 * 
 * @author john
 *
 */
public class BaseAdapterActivity extends Activity {
	
	private ListView mListView;
	
	private TextView mSearchBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_adapter);
		initializeViews();
	}

	private void initializeViews() {
		mListView = (ListView) findViewById(R.id.list);
		mSearchBox = (TextView) findViewById(R.id.search_box);
		updateViews();
	}
	
	private void updateViews() {
		mListView.setAdapter(new SampleAdapter(createData()));
		initializeListeners();
	}
	
	private void initializeListeners() {
		mSearchBox.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				((SampleAdapter) mListView.getAdapter()).getFilter().filter(s);
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FakeObject fakeObject = (FakeObject) mListView.getItemAtPosition(position);
				
				ToastHelper.show(getBaseContext(), "Clicked item: " + fakeObject.objectName);
			}
		});
	}

	private List<FakeObject> createData() {
		List<FakeObject> result = new ArrayList<FakeObject>();
		for (int i = 0; i < 30; i++) {
			FakeObject fakeObject = new FakeObject(i + " Checkbox and Filter sample");
			result.add(fakeObject);
		}
		return result;
	}
	
	private class FakeObject {
		public FakeObject(String name) {
			objectName = name;
			isSelected = false;
		}
		public String objectName;
		public boolean isSelected;
	}

	private class SampleAdapter extends BaseAdapter implements Filterable {
		
		private List<FakeObject> list;
		private SampleFilter filter;
		
		public SampleAdapter(List<FakeObject> list) {
			this.list = list;
		}

		public void updateData(List<FakeObject> list) {
			this.list = list;
			this.notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.row_base_adapter, null);
				viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				
				viewHolder.check.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int position = (Integer) v.getTag();
						FakeObject fakeObject = (FakeObject) getItem(position);
						fakeObject.isSelected = ((CheckBox) v).isChecked();
						
						ToastHelper.show(getBaseContext(), "CheckBox focusable should be false if you wanna enable item click");
					}
				});
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			bindingData(viewHolder, position);
			return convertView;
		}
		
		private void bindingData(ViewHolder viewHolder, int position) {
			FakeObject fakeObject = (FakeObject) getItem(position);
			viewHolder.check.setTag(position); // Used in check listener, because the position when click is incorrect
			viewHolder.check.setChecked(fakeObject.isSelected);
			viewHolder.name.setText(fakeObject.objectName);
		}

		private class ViewHolder {
			CheckBox check;
			TextView name;
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new SampleFilter(list);
			}
			return filter;
		}
		
		private class SampleFilter extends Filter {
			private List<FakeObject> list;
			
			public SampleFilter(List<FakeObject> list) {
				this.list = list;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				if (constraint == null || constraint.length() == 0) {
					results.values = list;
					results.count = list.size();
				} else {
					List<FakeObject> tmp = new ArrayList<FakeObject>();
					for (FakeObject fakeObject: list) {
						if (fakeObject.objectName.toUpperCase().contains(constraint.toString().toUpperCase())) {
							tmp.add(fakeObject);
						}
					}
					results.values = tmp;
					results.count = tmp.size();
				}
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				updateData((List<FakeObject>)results.values);
			}
		}
	}
}
