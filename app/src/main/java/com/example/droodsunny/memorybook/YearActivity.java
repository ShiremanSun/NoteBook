package com.example.droodsunny.memorybook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


public class YearActivity extends AppCompatActivity {

    public List<Note> noteList=null;
    private SharedPreferences preferences;
    private RecyclerView recyclerView=null;
    public  static SQLiteDatabase db=null;
    public static YearActivity yearActivity;
    public String city=null;
    private TextView textView;
    private ImageButton imageButton;
    private LinearLayout linearLayout;
    private long firstTime = 0;
    private long exitTime = 0;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        yearActivity=this;
        db= LitePal.getDatabase();



       /* mLocationClient = new LocationClient(this);
        if(Build.VERSION.SDK_INT>=23) {
            if (YearActivity.this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);

            }
        }*/


        textView=(TextView)findViewById(R.id.text);
        imageButton=(ImageButton)findViewById(R.id.addImg);

        recyclerView=(RecyclerView)findViewById(R.id.yearRecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(YearActivity.this,AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });



    }


    /*去除noteList年份重复的元素*/
    private void ListTitle(){
        noteList= DataSupport.findAll(Note.class);
        for ( int i = 0 ; i < noteList.size() - 1 ; i ++ ) {
            for ( int j = noteList.size() - 1 ; j > i; j -- ) {
                if (noteList.get(j).getYear().equals(noteList.get(i).getYear())) {
                    noteList.remove(j);
                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ListTitle();
        if(noteList.size()==0){
            Note note = new Note("无笔记","无笔记","无笔记","无笔记","无笔记","无笔记");
            noteList.add(note);
            NoteAdapter noteAdapter;
            noteAdapter = new NoteAdapter(noteList);
            recyclerView.setAdapter(noteAdapter);
        }
        else {
            NoteAdapter noteAdapter;
            noteAdapter = new NoteAdapter(noteList);
            recyclerView.setAdapter(noteAdapter);
        }
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




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(YearActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        yearActivity=null;
    }
}
