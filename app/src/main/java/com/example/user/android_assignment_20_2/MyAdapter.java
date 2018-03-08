package com.example.user.android_assignment_20_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 12-02-2018.
 */

public class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    private ListView listView;
    private ArrayList<String> al_contactName,al_contactNumber;
    Context context;

    MyAdapter(Context context, ArrayList<String> al_contactName, ArrayList<String> al_contactNumber) {
        this.context = context;
        this.al_contactName = al_contactName;
        this.al_contactNumber = al_contactNumber;
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public int getCount() {
        return al_contactName.size();
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public Object getItem(int position) {
        return null;
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public long getItemId(int position) {
        return 0;
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.name_cont,parent,false);
            holder = new ViewHolder();
            holder.bindView(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.contactName.setText(al_contactName.get(position));
        holder.contactNumber.setText(al_contactNumber.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView contactName, contactNumber;

        void bindView(View convertView) {
            contactName = (TextView) convertView.findViewById(R.id.contact_name);
            contactNumber = (TextView) convertView.findViewById(R.id.contact_number);
        }
    }


}

