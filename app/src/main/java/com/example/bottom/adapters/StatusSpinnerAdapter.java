package com.example.bottom.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bottom.R;
import com.example.bottom.models.ApartmentStatus;

import java.util.List;

public class StatusSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<ApartmentStatus> data; // Замените на свою модель данных

    public StatusSpinnerAdapter(Context context, List<ApartmentStatus> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.status_spinner_item, parent, false);
            holder = new ViewHolder();
            holder.shape = view.findViewById(R.id.statusShape);
            holder.textTextView = view.findViewById(R.id.statusTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ApartmentStatus item = data.get(position);

        int id = item.getId();
        switch (id){
            case 1:{
                holder.shape.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(225,225,0)));
            }break;
            case 2:{
                holder.shape.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(225,165,0)));
            } break;
            case 3:{
                holder.shape.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,225,0)));
            }break;
            case 4:{
                holder.shape.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(225,0,0)));
            }break;
        }
        holder.textTextView.setText(item.getName());

        return view;
    }

    private static class ViewHolder {
        View shape;
        TextView textTextView;
    }
}
