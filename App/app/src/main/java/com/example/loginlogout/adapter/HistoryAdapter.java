package com.example.loginlogout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.loginlogout.R;
import com.example.loginlogout.model.history;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<history> arr;

    public HistoryAdapter(Context context, ArrayList<history> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_item, null);
            viewHolder = new ViewHolder();
            viewHolder.namehistory = convertView.findViewById(R.id.hsnametest);
            viewHolder.scorehistory = convertView.findViewById(R.id.hsscore);
            viewHolder.timehistory = convertView.findViewById(R.id.hstime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.namehistory.setText(arr.get(position).getName());
        viewHolder.scorehistory.setText(arr.get(position).getScore());
        viewHolder.timehistory.setText(arr.get(position).getTime());

        return convertView;
    }
    class ViewHolder {
        private TextView namehistory;
        private TextView scorehistory;
        private TextView timehistory;
    }
}
