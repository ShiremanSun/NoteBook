package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    SQLiteDatabase db=YearActivity.db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

    }


    public static void actionStart(Context context,Note note){
        Intent intent= new Intent(context,EditActivity.class);
        intent.putExtra("title",note.getTitle());
        context.startActivity(intent);
    }


}
