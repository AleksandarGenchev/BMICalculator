package com.example.bmicalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public abstract class DBActivity extends AppCompatActivity {

    protected void initDB() {

        ExecSQL(
                "CREATE TABLE if not exists INFORMATION(" +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Weight real not null, " +
                        "Height real not null, " +
                        "Result real not null" +
                        ");",
                null,
                ()->{
                    Toast.makeText(this, "Init db successful", Toast.LENGTH_LONG).show();
                },
                (error)->{
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
        );
    }

    protected void SelectSQL(String SelectQuery, String[] args, OnSelectSuccess success)
            throws Exception{

        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/BMI.db",
                        null);
        db.beginTransaction();
        Cursor cursor = db.rawQuery(SelectQuery, args);
        while (cursor.moveToNext()){
            String ID = cursor.getString(cursor.getColumnIndex("ID"));
            String Weight = cursor.getString(cursor.getColumnIndex("Weight"));
            String Height = cursor.getString(cursor.getColumnIndex("Height"));
            String Result = cursor.getString(cursor.getColumnIndex("Result"));
            success.OnElementSelected(ID, Weight, Height, Result);
        }
        db.endTransaction();
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success, OnQueryError err)
    {
        SQLiteDatabase db = null;
        try {
            db =
                    SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/BMI.db",
                            null
                    );
            if(args == null) {
                db.execSQL(SQL);
            }else{
                db.execSQL(SQL, args);
            }
            success.OnSuccess();

        }catch (Exception e){
            err.OnError(e.getMessage().toString());
        }finally {
            if (db != null)
                db.close();
        }
    }

    protected interface OnQuerySuccess{
        public void OnSuccess();
    }
    protected interface OnQueryError {
        public void OnError(String error);
    }

    protected interface OnSelectSuccess{
        public void OnElementSelected(String ID, String Weight, String Height, String Result);
    }
}
