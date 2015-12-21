package com.example.admin.wearnotification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.SoftReference;

/**
 * Created by Admin on 12/10/2015.
 */

public class WearableAdapter extends WearableListView.Adapter {
    String[] listValues;
    Context context;
    LayoutInflater inflater;

    public WearableAdapter(Context context,String[] listValues) {
        this.context = context;
        this.listValues = listValues;
        inflater=LayoutInflater.from(context);
    }
    public class MyViewHolder extends WearableListView.ViewHolder{
        TextView listText;
        public MyViewHolder(View itemView) {
            super(itemView);
            listText =(TextView) itemView.findViewById(R.id.list_text);
        }
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wearable_list_items,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder =(MyViewHolder) holder;
        myViewHolder.listText.setText(listValues[position]);
      ((MyViewHolder) holder).itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return listValues.length;
    }
}
