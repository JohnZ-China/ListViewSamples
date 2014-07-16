package com.example.listviewsamples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * MainActivity is a SimpleAdapter sample navigate to other samples
 * 
 * @author john
 *
 */
public class MainActivity extends Activity {

    private final static String KEY_TITLE       = "title";
    private final static String KEY_DESCRIPTION = "description";

    private ListView            mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeListeners();
        updateViews();
    }

    private void initializeViews() {
        mListView = (ListView) findViewById(R.id.list);
    }

    private void initializeListeners() {
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                case 0:
                    openActivity(ArrayAdapterActivity.class);
                    break;
                case 1:
                    openActivity(BaseAdapterActivity.class);
                    break;
                case 2:
                    openActivity(DynLoadingListViewActivity.class);
                    break;
                case 3:
                    openActivity(CategoryListViewActivity.class);
                    break;
                case 4:
                    openActivity(MultiLevelListViewActivity.class);
                    break;
                default:
                    break;
                }
            }
        });
    }

    private void updateViews() {
        /**
         * data     is a List<Map<String, Object>>;
         * resource is row view resId ;
         * from is String[] {keys of Map};
         * to is the id of view in resource
         */
        SimpleAdapter adapter = new SimpleAdapter(this, createData(), 
                R.layout.row_simple_adapter, 
                new String[] {KEY_TITLE, KEY_DESCRIPTION }, 
                new int[] { R.id.title, R.id.description });

        mListView.setAdapter(adapter);

    }

    private List<Map<String, Object>> createData() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        result.add(createMapItem("ArrayAdapter", "Highlight selected item, change listSelector and change dividerHeight"));
        result.add(createMapItem("BaseAdapter", "Filter, CheckBox in ListView"));
        result.add(createMapItem("EndlessListView", "Listview footer"));
        result.add(createMapItem("CategoryListView", "Category ListView with category header"));
        result.add(createMapItem("MultiLevelListView", "ListView with Multi-Level"));

        return result;
    }

    private Map<String, Object> createMapItem(String title, String description) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(KEY_TITLE, title);
        result.put(KEY_DESCRIPTION, description);
        return result;
    }

    private void openActivity(Class<? extends Activity> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
}
