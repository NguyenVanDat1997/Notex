package com.example.anonymus.notex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EditDatabase extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    ImageView done;
    EditText title,content;
    TextView textBar;
    int id=0;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        textBar  = (TextView) findViewById(R.id.textBar);
        done = (ImageView) findViewById(R.id.done);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        textBar.setText(sharedPreferences.getString("NameBar","Nguyễn Đạt"));


        if (getAdapter()==1){
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    senToMain(MainActivity.MY_RESULT_TEXT2);
                }
            });

        }else if (getAdapter() == -1){
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    senToMain(MainActivity.MY_RESULT_TEXT1);
                }
            });

        }


    }

    private void senToMain(int myResultText1) {
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        String titlevalue = title.getText().toString();
        String contentvalue = content.getText().toString();

        //gan tất cả vào bundle rồi gửi bundle đi

        bundle.putString("title",titlevalue);
        bundle.putString("content",contentvalue);
        if(id!=-1){
            bundle.putInt("id",id);
        }

        intent.putExtra("bundle",bundle);
        setResult(myResultText1,intent);
        finish();
    }
    private int getAdapter(){
        Bundle bundle =getIntent().getBundleExtra("bundle");
        id = bundle.getInt("id");
        if (id!=-1){
            title.setText(bundle.getString("title"));
            content.setText(bundle.getString("content"));
            bundle.clone();
            return 1;
        }
        return -1;
    }

}