package com.example.listviewsamples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Extends BaseAdapter to create a ListView with category
 * 
 * @author john
 *
 */
public class CategoryListViewActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list_view);
		setListAdapter(new CategoryAdapter(createData()));
	}
	
	private class FakeObject {
		public String category;
		public String objectName;
		
		public FakeObject(String category, String objectName) {
			this.category = category;
			this.objectName = objectName;
		}
	}
	
	private List<FakeObject> createData() {
		List<FakeObject> fakeObjects = new ArrayList<FakeObject>();
		fakeObjects.add(new FakeObject("category 1", "John"));
		fakeObjects.add(new FakeObject("category 1", "Sky"));
		fakeObjects.add(new FakeObject("category 1", "Kevin"));
		fakeObjects.add(new FakeObject("category 2", "@@@@@"));
		fakeObjects.add(new FakeObject("category 3", "xxxx"));
		fakeObjects.add(new FakeObject("category 4", "Superman"));
		fakeObjects.add(new FakeObject("category 4", "Alpha wolf"));
		fakeObjects.add(new FakeObject("category 3", "xxxXX"));
		fakeObjects.add(new FakeObject("category 4", "Spider man"));
		fakeObjects.add(new FakeObject("category 4", "bat man"));
		fakeObjects.add(new FakeObject("category 4", "End"));
		return fakeObjects;
	}
	
	private class CategoryAdapter extends BaseAdapter {
		
		private List<CategoryFakeObject> list;
		
		private class CategoryFakeObject {
			public final static int HEADER = 0;
			public final static int ITEM = 1;
			
			public String categoryHeader;
			public List<FakeObject> data;
			
			public CategoryFakeObject(String category) {
				this.categoryHeader = category;
				this.data = new ArrayList<FakeObject>();
			}
			
			public boolean addFakeObject(FakeObject object) {
				return data.add(object);
			}
			
			public Object getFakeObject(int index) {
				if (index == 0) {
					return this;
				} 
				return data.get(index - 1);
			}
			
			public int getFakeObjectCount() {
				return data.size() + 1;
			}
		}
		
		private List<CategoryFakeObject> process(List<FakeObject> data) {
			
			// Sort data then process
			Collections.sort(data, new Comparator() {
				@Override
				public int compare(Object arg0, Object arg1) {
					FakeObject f0 = (FakeObject) arg0;
					FakeObject f1 = (FakeObject) arg1;
					return f0.category.compareTo(f1.category);
				}
			});
			
			List<CategoryFakeObject> result = new ArrayList<CategoryFakeObject>();
			if (data == null || data.size() == 0) return result;
			for (int i = 0; i < data.size(); i++) {
				FakeObject fakeObject = data.get(i);
				CategoryFakeObject categoryFakeObject = new CategoryFakeObject(fakeObject.category);
				categoryFakeObject.addFakeObject(fakeObject);
				
				int index = i;
				for (int j = index + 1; j < data.size(); j++) {
					FakeObject f = data.get(j);
					if (f.category.equals(fakeObject.category)) {
						categoryFakeObject.addFakeObject(f);
						i = j;
					} else {
						break;
					}
				} //for(j)
				result.add(categoryFakeObject);
			}
			return result;
		}
		
		public CategoryAdapter(List<FakeObject> data) {
			list = process(data);
		}

		@Override
		public int getCount() {
			int count = 0;
			for (CategoryFakeObject category: list) {
				count += category.getFakeObjectCount();
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			int firstIndex = 0;
			for (CategoryFakeObject category: list) {
				int size = category.getFakeObjectCount();
				int index = position - firstIndex;
				if (index < size) {
					return category.getFakeObject(index);
				}
				firstIndex += size;
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return getItemViewType(position) == CategoryFakeObject.ITEM;
		}
		
		@Override
		public int getItemViewType(int position) {
			int firstIndex = 0;
			for (CategoryFakeObject category: list) {
				int size = category.getFakeObjectCount();
				int index = position - firstIndex;
				if (index == 0) {
					return CategoryFakeObject.HEADER;
				}
				if (index < size) {
					return CategoryFakeObject.ITEM;
				}
				firstIndex += size;
			}
			return CategoryFakeObject.ITEM;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			if (type == CategoryFakeObject.HEADER) {
				if (convertView == null) {
					convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.header_category, null);
				}
				CategoryFakeObject categoryFakeObject = (CategoryFakeObject)getItem(position);
				
				TextView category = (TextView)convertView.findViewById(R.id.category);
				category.setText(categoryFakeObject.categoryHeader);
				TextView count = (TextView) convertView.findViewById(R.id.count);
				count.setText(String.valueOf(categoryFakeObject.getFakeObjectCount() - 1)); // Header need cut
			} else if (type == CategoryFakeObject.ITEM) {
				if (convertView == null) {
					convertView = LayoutInflater.from(getBaseContext()).inflate(android.R.layout.simple_list_item_1, null);
				}
				FakeObject fakeObject = (FakeObject) getItem(position);
				
				((TextView) convertView).setText(fakeObject.objectName);
			}
			return convertView;
		}
	}
}
