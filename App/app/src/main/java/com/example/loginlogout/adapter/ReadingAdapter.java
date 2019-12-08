package com.example.loginlogout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginlogout.R;
import com.example.loginlogout.model.ReadingList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReadingAdapter extends BaseAdapter {
    List<ReadingList> arr;
    Context context;
    int myLayout;

    public ReadingAdapter(List<ReadingList> arr, Context context, int myLayout) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(myLayout,null);
        TextView nameTest = convertView.findViewById(R.id.read_name_test);
        TextView number = convertView.findViewById(R.id.number);
        nameTest.setText(arr.get(position).name);
        number.setText(arr.get(position).number+" Câu hỏi");
        return convertView;
    }
}
