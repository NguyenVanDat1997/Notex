package com.example.anonymus.notex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Notex> {
    private Context context;
    private int resource;
    private ArrayList<Notex> objects;



    public CustomArrayAdapter(Context context, int resource, ArrayList<Notex> objects) {
        super(context ,resource ,objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        //Lần đầu khởi tạo
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title =  convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Notex notex = objects.get(position);
        viewHolder.title.setText(notex.getTitle());

        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditDatabase.class);
                Bundle bundle = new Bundle();
                bundle.putString("adtitle",notex.getTitle());
                bundle.putString("adcontent",notex.getContent());
                intent.putExtra("adbundle",bundle);
                context.startActivity(intent);


            }
        });
*/
        return convertView;
    }

    public class ViewHolder{
        TextView title;
    }
}
