package com.example.SqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.object.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.R.attr.id;
import static com.example.SqlHelper.Table_jz.Uid;
import static com.example.mytools.staticTools.url;

/**
 * Created by wangqiang on 2016/11/21.
 */

public class jz_helpe extends SQLiteOpenHelper {
    private String TAG = "jz_helpe";
    private static final String DBName = "jz";
    private static final int version = 1;
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public jz_helpe(Context context){
        super(context,DBName,null,version);
    }
    public jz_helpe(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("sql","sql is created");
        if(db!=null){
            Log.e("sql","sql is not null");
            db.execSQL("CREATE TABLE IF NOT EXISTS "+ Table_jz.tableName+" ( "
                    + Table_jz.Jid+" INT unique, "
                    + Table_jz.Uid+" INT, "
                    + Table_jz.ImageUrl+" VARCHAR , "
                    + Table_jz.Title+" VARCHAR , "
                    + Table_jz.Salary+" VARCHAR, "
                    + Table_jz.Time + " VARCHAR,"
                    + Table_jz.Place+" VARCHAR );" );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+ Table_jz.tableName);
        db.close();
    }
    public ArrayList<HashMap<String,String>> selectAll(String uid){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Cursor cursor = getWritableDatabase()
                .rawQuery("SELECT * FROM "+
                Table_jz.tableName + " where "
                +Table_jz.Uid+" = "+uid,null);
        while(cursor.moveToNext()){
            HashMap<String,String> map = new HashMap<>();
            map.put(Table_jz.Jid,cursor.getString(0));
            map.put(Table_jz.Uid,cursor.getString(1));
            map.put(Table_jz.ImageUrl,cursor.getString(2));
            map.put(Table_jz.Title,cursor.getString(3));
            map.put(Table_jz.Salary,cursor.getString(4));
            map.put(Table_jz.Time,cursor.getString(5));
            map.put(Table_jz.Place,cursor.getString(6));
            list.add(map);
        }
        try{
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 判断是否收藏兼职
     */
    public boolean hasjid(String jid,String  uid){
        Cursor cursor = getWritableDatabase().rawQuery("select * from "+
                Table_jz.tableName +" where "+ Table_jz.Jid +" =? "+" and "+Table_jz.Uid+" =?",
                new String[]{jid,uid});
        Boolean has = cursor.moveToFirst();
        try{
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return has;
    }
    /**
     * 获取兼职数量
     */
    public String getNumber(String  uid){
        int n = 0;
        Cursor cursor = getWritableDatabase().rawQuery("select * from "+
                        Table_jz.tableName +" where "+Table_jz.Uid +" =?",
                new String[]{uid});
        while(cursor.moveToNext()){
             n++;
        }
        return n+"";
    }
    public void deletejid(String jid,String uid){
        String where = Table_jz.Jid+"=? and "+ Table_jz.Uid +" =?";
        String[] value = {jid,uid};
        getWritableDatabase().delete(Table_jz.tableName,where,value);
        Log.e(TAG,"delete success");
    }
    public void insert(Job job,String uid){
        ContentValues cv = new ContentValues();
        cv.put(Table_jz.Jid,job.getJid());
        cv.put(Table_jz.Uid,uid);
        cv.put(Table_jz.ImageUrl,job.getJurl());
        cv.put(Table_jz.Title,job.getTitle());
        cv.put(Table_jz.Salary,job.getSalary());
        cv.put(Table_jz.Time,job.getPubtime());
        cv.put(Table_jz.Place,job.getArea());
        getWritableDatabase().insert(Table_jz.tableName, Table_jz.Jid,cv);

    }
}
