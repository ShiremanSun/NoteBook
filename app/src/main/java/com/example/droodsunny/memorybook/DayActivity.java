package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static DayActivity dayActivity;
    public List<Note> noteList=null;
    private String month;
    private String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        dayActivity=this;
        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");

        recyclerView=(RecyclerView)findViewById(R.id.dayRecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void ListTitle() throws Exception{

        noteList= DataSupport.where("year = ? and month = ?",year,month).find(Note.class);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            ListTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NoteAdapter noteAdapter=new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
    }
       /*意图跳转*/
    public static void actionStart(Context context, Note note){
        Intent intent= new Intent(context,DayActivity.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("year",note.getYear());
        intent.putExtra("month",note.getMonth());
        intent.putExtra("day",note.getDay());
        intent.putExtra("content",note.getContent());
        intent.putExtra("location",note.getLocation());
        context.startActivity(intent);
    }
    /*沉浸式状态栏*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
