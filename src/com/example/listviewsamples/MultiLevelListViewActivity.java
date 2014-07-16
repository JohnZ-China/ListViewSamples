package com.example.listviewsamples;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Extends BaseAdapter and use recursion to create a multi-level ListView
 * 
 * @author john
 *
 */
public class MultiLevelListViewActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_level_list_view);
		MultiLevelAdapter multiLevelAdapter = createAdapter();
		setListAdapter(multiLevelAdapter);
	}
	
	private MultiLevelAdapter createAdapter() {
		MultiLevelAdapter adapter = new MultiLevelAdapter();
		
		Level level1 = new Level("Level 1");
		Level level2 = new Level("Level 2");
		adapter.add(level1);
		adapter.add(level2);
		
		Level level11 = new Level("Level 1.1");
		level1.addSub(level11);
		level1.addSub(new Level("Level 1.2"));
		level1.addSub(new Level("Level 1.3"));
		
		level2.addSub(new Level("Level 2.1"));
		Level level22 = new Level("Level 2.2");
		level2.addSub(level22);
		
		Level level221 = new Level("Level 2.2.1");
		level221.addSub(new Level("Level 2.2.1.1"));
		level22.addSub(level221);
		
		level11.addSub(new Level("Level 1.1.1"));
		
		return adapter;
	}

	private class MultiLevelAdapter extends BaseAdapter {
		
		private Level root;
		
		public Level add(Level level) {
			root.addSub(level);
			return level;
		}
		
		public MultiLevelAdapter() {
			root = new Level();
		}
		
		@Override
		public int getCount() {
			return root.getCount();
		}

		@Override
		public Object getItem(int position) {
			return root.getLevel(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				Object object = getItem(position);
				if (object instanceof Level) {
					Level level = (Level) object;
					convertView = LayoutInflater.from(getBaseContext()).inflate(android.R.layout.simple_list_item_1, null);
					TextView tag = (TextView) convertView;
					tag.setPadding(getDipFromPixel(50 * level.getDepth()) , 0, 0, 0);
					tag.setText(level.tag);
				}
			}
			return convertView;
		}
		
		private int getDipFromPixel(float pixels) {
			float scale = getResources().getDisplayMetrics().density;
			return (int) (pixels * scale + 0.5f);
		}
	}
	
	private class Level {
		public String tag;
		public Level parent;
		public List<Level> subLevels;
		
		public Level() {
			this.tag = "Root";
			this.subLevels = new ArrayList<Level>();
		}
		
		public Level(String tag) {
			this.tag = tag;
			this.subLevels = new ArrayList<Level>();
		}
		
		public boolean addSub(Level subLevel) {
			subLevel.parent = this;
			return subLevels.add(subLevel);
		}
		
		public int getCount() {
			int count = subLevels.size();
			for (Level l: subLevels) {
				count += l.getCount();
			}
			return count;
		}
		
		public Object getLevel(int position) {
			Object object = getLevel(position + 1, this);
			if (object instanceof Level) {
				return object;
			} else {
				return null;
			}
		}
		
		private Object getLevel(int position, Level level) {
			if (position == 0) {
				return level;
			}
			for (Level le: level.subLevels) {
				Object object = getLevel(--position, le);
				if (object instanceof Level) {
					return object;
				}
				position = (Integer) object;
			}
			return position;
		}
		
		public int getDepth() {
			if (parent != null) {
				return parent.getDepth() + 1;
			}
			return -1;
		}
		
		// We can use dfs to get the specific item.
		// But tmp cannot be declared like this.
		// So I used JL's method
		/*public Level getLevel(int position) {
			TreeDFS(position);
			return tmp.get(position + 1);
		}
		
		private void TreeDFS(int position) {
			if (position + 2 == tmp.size()) return;
			tmp.add(this);
			for (Level level: subLevels) level.TreeDFS(position);
		}*/
	}
}
