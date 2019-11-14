package com.example.loginlogout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.loginlogout.R;
import com.example.loginlogout.model.ListeningModal;

import java.util.List;

public class ListeningAdapter extends BaseAdapter {
    List<ListeningModal> arr;
    Context context;
    int myLayout;

    public ListeningAdapter(List<ListeningModal> arr, Context context, int myLayout) {
        this.arr = arr;
        this.context = context;
        this.myLayout = myLayout;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(myLayout,null);

        TextView lbltitle= convertView.findViewById(R.id.lbl_title);
        TextView lblnumber = convertView.findViewById(R.id.lbl_number);
        TextView lbltime = convertView.findViewById(R.id.lbl_time);

        lbltitle.setText(arr.get(position).title);
        lblnumber.setText(arr.get(position).number);
        lbltime.setText(arr.get(position).time);
        return convertView;
    }
}
