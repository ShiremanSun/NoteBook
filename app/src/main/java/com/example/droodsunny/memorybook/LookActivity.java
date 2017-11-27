package com.example.droodsunny.memorybook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.droodsunny.memorybook.Util.TextViewVertical;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LookActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static LookActivity sLookActivity;
    private TextView date_text;
    private TextView location_text;
    private TextViewVertical content_text;
    private TextView title_text;
    private ImageButton image_update;
    private ImageButton image_delete;

    private int id;


    private int count = 0;
    // 第一次点击的时间 long型
    private long firstClick = 0;
    // 最后一次点击的时间
    private long lastClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        sLookActivity=this;
        date_text=(TextView)findViewById(R.id.date);
        location_text=(TextView)findViewById(R.id.location);
        content_text=(TextViewVertical)findViewById(R.id.content);
        title_text=(TextView)findViewById(R.id.titlel);
        image_update=(ImageButton)findViewById(R.id.update);
        image_delete=(ImageButton)findViewById(R.id.delete);

        content_text.setTextSize(60);

        content_text.setLineWidth(100);

        final String date=getIntent().getStringExtra("year")+getIntent().getStringExtra("month")+getIntent().getStringExtra("day");
        final String location=getIntent().getStringExtra("location");
        final String content=getIntent().getStringExtra("content");
        final String title=getIntent().getStringExtra("title");

        List<Note> NoteId= DataSupport.select("id").where("year = ? and month = ? and day = ? and title = ?",
                getIntent().getStringExtra("year"),getIntent().getStringExtra("month"),getIntent().getStringExtra("day"),getIntent().getStringExtra("title")).find(Note.class);
         id=NoteId.get(0).getId();
        date_text.setText(date);
        location_text.setText(location);
        content_text.setText(content);
        title_text.setText(title);


        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Note.class,"year = ? and month = ? and day = ? and title = ? ",
                        getIntent().getStringExtra("year"),getIntent().getStringExtra("month"),getIntent().getStringExtra("day"),getIntent().getStringExtra("title"));
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        image_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LookActivity.this,UpdateActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("date",date);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                intent.putExtra("location",location);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }
    public static void actionStart(Context context, Note note){
        Intent intent= new Intent(context,LookActivity.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("year",note.getYear());
        intent.putExtra("month",note.getMonth());
        intent.putExtra("day",note.getDay());
        intent.putExtra("content",note.getContent());
        intent.putExtra("location",note.getLocation());
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

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
    /*双击返回*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (firstClick != 0 && System.currentTimeMillis() - firstClick > 500) {
                count = 0;
            }
            count++;
            if (count == 1) {
                firstClick = System.currentTimeMillis();
            } else if (count == 2) {
                lastClick = System.currentTimeMillis();
                // 两次点击小于500ms 也就是连续点击
                if (lastClick - firstClick < 500) {
                    //Log.v("Double", "Double");
                    count = 0;
                    firstClick = 0;
                    lastClick = 0;
                    finish();
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }

        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("LookActivity","onActivity");
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String title=data.getStringExtra("return_title");
                    String content=data.getStringExtra("return_content");
                    String location=data.getStringExtra("return_location");
                    location_text.setText(location);
                    content_text.setText(content);
                    title_text.setText(title);
                }
        }
    }
    @Override
    protected void onResume() {
        Log.d("LookActivity","onResume");

        super.onResume();
    }
}