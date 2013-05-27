package com.studioirregular.dragsortgridview.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.studioirregular.dragsortgridview.DragSortGridView;

public class MainActivity extends Activity {

    private static final String TAG = "drag-sort-gridview-demo";
    
    private DragSortGridView gridView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        gridView = (DragSortGridView)findViewById(R.id.grid);
        
        int[] colors = new int[64];
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            colors[i] = Color.rgb(random.nextInt(0xff), random.nextInt(0xff), random.nextInt(0xff));
        }
        BaseAdapter adapter = new ColorAdapter(colors);
        
        gridView.setAdapter(adapter);
        
        gridView.setOnReorderingListener(dragSortListener);
    }
    
    private DragSortGridView.OnReorderingListener dragSortListener = new DragSortGridView.OnReorderingListener() {
        
        @Override
        public void onReordering(int fromPosition, int toPosition) {
            Log.w(TAG, "onReordering fromPosition:" + fromPosition + ",toPosition:" + toPosition);
            ((ColorAdapter)gridView.getAdapter()).reorder(fromPosition, toPosition);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class ColorAdapter extends BaseAdapter {

        private List<Integer> colors;
        private List<Integer> positions;
        
        public ColorAdapter(int[] colors) {
            this.colors = new ArrayList<Integer>();
            this.positions = new ArrayList<Integer>();
            
            for (int color : colors) {
                this.colors.add(color);
                this.positions.add(positions.size());
            }
        }
        
        public void reorder(int from, int to) {
            if (from != to) {
                int color = colors.remove(from);
                colors.add(to, color);
                
                int position = positions.remove(from);
                positions.add(to, position);
                
                notifyDataSetChanged();
            }
        }
        
        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Object getItem(int position) {
            return colors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView result = (TextView)convertView;
            
            if (result == null) {
                final Context context = MainActivity.this;
                
                result = new TextView(context);
                result.setMinHeight(160);
                result.setBackgroundColor(colors.get(position));
                result.setTextAppearance(context, android.R.style.TextAppearance_Large);
                result.setGravity(Gravity.CENTER);
                result.setText(Integer.toString(positions.get(position)));
            } else {
                result.setBackgroundColor(colors.get(position));
                result.setText(Integer.toString(positions.get(position)));
            }
            
            return result;
        }

    }

}
