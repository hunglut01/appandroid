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
import com.example.loginlogout.model.GrammarModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GrammarAdapter extends BaseAdapter {
    List<GrammarModel> arr;
    Context context;
    int myLayout;
    public  GrammarAdapter(Context context, int layout,List<GrammarModel> arr)
    {
         this.context=context;
         this.myLayout=layout;
         this.arr=arr;

    }
    @Override
    public int getCount() {//trả ra số item sẽ được hiển thị trên listView
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

        ImageView imageView = convertView.findViewById(R.id.pictureGrammar);
        TextView lblName= convertView.findViewById(R.id.lbl_grammar);
        TextView noteGrammar = convertView.findViewById(R.id.noteGrammar);

        Picasso.get().load(arr.get(position).image).into(imageView);
        lblName.setText(arr.get(position).topic);
        noteGrammar.setText(arr.get(position).name);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);
        return convertView;
    }
}
