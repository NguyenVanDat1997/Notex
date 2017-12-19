package com.example.anonymus.notex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import static android.content.ContentValues.TAG;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    Toolbar toolbar;
    ListView listView;
    ArrayList<Notex> arrData = new ArrayList<>();
    CustomArrayAdapter adapter;
    SharedPreferences presf;
    String orderby;
    String NameApp;
    TextView textToolBar;

    public static final int MY_REQUEST_CODE = 1997;
    public static final int MY_RESULT_TEXT1 = 1999;
    public static final int MY_RESULT_TEXT2 = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textToolBar = (TextView) findViewById(R.id.textToolBar);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomArrayAdapter(this,R.layout.custom_listview,arrData);
        listView.setAdapter(adapter);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        presf = PreferenceManager.getDefaultSharedPreferences(this);
        orderby = setOption();
        presf.registerOnSharedPreferenceChangeListener(prefListener);

        NameApp = getNameApps();
        textToolBar.setText(NameApp);

        databaseHelper = new DatabaseHelper(this);
        arrData.addAll(databaseHelper.getAllNote(orderby));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title =  ((CustomArrayAdapter)(adapterView.getAdapter())).getItem(i).getTitle();
                String content =  ((CustomArrayAdapter)(adapterView.getAdapter())).getItem(i).getContent();
                int id = ((CustomArrayAdapter)(adapterView.getAdapter())).getItem(i).getId();
                Intent intent = new Intent(MainActivity.this,EditDatabase.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",title);
                bundle.putString("content",content);
                bundle.putInt("id",id);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,MY_REQUEST_CODE);



            }
        });

        //đăng ký context menu cho listview
        registerForContextMenu(listView);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Notex selectedNote = (Notex) listView.getItemAtPosition(info.position);
        switch (item.getItemId()){
            case R.id.delete:
                databaseHelper.Delete(selectedNote);
                arrData.clear();
                arrData.addAll(databaseHelper.getAllNote(orderby));
                adapter.notifyDataSetChanged();
                return true;
            default:return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.action,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add:
                Bundle bundle =new Bundle();
                bundle.putInt("id",-1);
                Intent intent = new Intent(MainActivity.this,EditDatabase.class);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,MY_REQUEST_CODE);
                return true;
            case android.R.id.home:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
            case R.id.prefs:
                Intent intent_pre = new Intent(this,EditPreferences.class);
                startActivity(intent_pre);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == MY_REQUEST_CODE){
            switch (resultCode){
                case MY_RESULT_TEXT1:
                    Notex notex = new Notex();
                    //lay bundle ra roi xu ly
                    Bundle bundlemain = data.getBundleExtra("bundle");

                    String title = bundlemain.getString("title");
                    String content = bundlemain.getString("content");Log.d(TAG,"ti : "+title);
                    if (!title.equals("")&& !content.equals("")){
                        notex.setTitle(title);
                        notex.setContent(content);
                        databaseHelper.addNote(notex);
                        arrData.add(notex);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case MY_RESULT_TEXT2:
                    Notex notex1 = new Notex();
                    Bundle bundle = data.getBundleExtra("bundle");

                    String title1 = bundle.getString("title");
                    String content1 = bundle.getString("content");
                    int id = bundle.getInt("id");

                    notex1.setId(id);notex1.setTitle(title1);notex1.setContent(content1);
                    databaseHelper.Update(notex1);
                    arrData.clear();
                    arrData.addAll(databaseHelper.getAllNote(orderby));
                    adapter.notifyDataSetChanged();

                    break;
                default:
                    orderby = setOption();
                    arrData.clear();
                    arrData.addAll(databaseHelper.getAllNote(orderby));
                    break;
            }

        }

    }
    public String setOption(){
       String order = presf.getString("sort_order","NULL");
       return  order;
    }
    public String getNameApps(){
        String string = presf.getString("NameBar","Nguyễn Đạt");
        return string;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener=
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
                                                      String key) {
                    if (key.equals("sort_order")) {
                        updateList();
                    }
                    if (key.equals("NameBar")){
                        updateNameApps();
                    }

                }
            };

    private void updateList() {
        orderby = setOption();
        arrData.clear();
        arrData.addAll(databaseHelper.getAllNote(orderby));
        adapter.notifyDataSetChanged();
    }
    private void updateNameApps(){
        NameApp = getNameApps();
        textToolBar.setText(NameApp);
    }


}
