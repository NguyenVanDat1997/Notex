package com.example.anonymus.notex;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.database.Cursor;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME ="note_list";
    private static final String TABLE_NAME ="note";
    private static final String ID ="id";
    private static final String TITLE ="title";
    private static final String CONTENT ="content";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, 1);
        Log.d("DBManager", "DBManager: ");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                ID +" integer primary key, "+
                TITLE + " TEXT, "+
                CONTENT +" TEXT)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }



    public void hello(){
        Toast.makeText(context, "Xin chao ban", Toast.LENGTH_SHORT).show();
    }

    //phải thêm chỉnh sửa xóa trong MainActivity nếu không làm như vậy thì chả có gì xảy ra cả
    //Add new a student
    public void addNote(Notex notex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, notex.getTitle());
        values.put(CONTENT, notex.getContent());
        //Neu de null thi khi value bang null thi loi

        db.insert(TABLE_NAME,null,values);

        db.close();
    }

    //Lấy toàn bộ sinh viên ra
    public ArrayList<Notex> getAllNote(String orderby){
        ArrayList<Notex> arrayList = new ArrayList<>();

        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+orderby;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){

            do {
                Notex notex = new Notex();
                notex.setId(cursor.getInt(0));
                notex.setTitle(cursor.getString(1));
                notex.setContent(cursor.getString(2));
                arrayList.add(notex);
            }while (cursor.moveToNext());

         }

         cursor.close();
         db.close();
         return arrayList;

    }

    public int Update(Notex notex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",notex.getTitle());
        values.put("content",notex.getContent());

        return db.update(TABLE_NAME,values,ID +"=?",new String[]{String.valueOf(notex.getId())});

    }

    public void Delete(Notex notex){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ID +"=?",new String[]{String.valueOf(notex.getId())});
        db.close();
    }


}