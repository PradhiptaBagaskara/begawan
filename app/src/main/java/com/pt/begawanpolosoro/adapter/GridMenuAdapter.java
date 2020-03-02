package com.pt.begawanpolosoro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pt.begawanpolosoro.R;

public class GridMenuAdapter extends BaseAdapter {
    Context context;
    int logos[];
    String name[];
    LayoutInflater inflater;
    public GridMenuAdapter(Context mContext, int[] logo, String[] Mname){
        this.context = mContext;
        this.logos = logo;
        this.name = Mname;
        inflater = (LayoutInflater.from(mContext));
    }
    @Override
    public int getCount() {
        return logos.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public String getMenuName(int position){
        return name[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.grid_menu_item, null);
        ImageView icon = convertView.findViewById(R.id.GridmenuImg);
        TextView textView = convertView.findViewById(R.id.GridmenuName);
        icon.setImageResource(logos[position]);
        textView.setText(name[position]);
        return convertView;
    }
}
