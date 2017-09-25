package com.example.droodsunny.memorybook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.litepal.crud.DataSupport;

import java.util.List;

import static com.example.droodsunny.memorybook.R.id.monthRecycler;

public class MonthActivity extends AppCompatActivity {

    public static MonthActivity monthActivity;
    private RecyclerView recyclerView;
    public List<Note> noteList = null;
    private String year;
    public TextView textView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        monthActivity = this;
        textView = (TextView) findViewById(R.id.year);


        recyclerView = (RecyclerView) findViewById(monthRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        year = getIntent().getStringExtra("year");
        textView.setText(year);
        textView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MonthActivity.this, YearActivity.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//关掉所要到的界面的activity
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MonthActivity.this,recyclerView,"sharedView").toBundle());
                }*/
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    public static void actionStart(Context context, Note note) {
        Intent intent = new Intent(context, MonthActivity.class);
        intent.putExtra("year", note.getYear());
        context.startActivity(intent);
    }
    private void ListTitle() {
        noteList = DataSupport.where("year = ?", year).find(Note.class);
        for ( int i = 0 ; i < noteList.size() - 1 ; i ++ ) {
            for ( int j = noteList.size() - 1 ; j > i; j -- ) {
                if (noteList.get(j).getMonth().equals(noteList.get(i).getMonth())) {
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Month Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
