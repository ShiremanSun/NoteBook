package com.example.droodsunny.memorybook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.droodsunny.memorybook.TextUtil.SetAppTypeface;

import java.util.List;

/**
 * Created by DroodSunny on 2017/9/23.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Note> noteList;
    int i;
   private Context context;
    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView textView;
        View view;
        ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            textView=(TextView)itemView.findViewById(R.id.text);
        }
    }
    NoteAdapter(List<Note> note){

        noteList=note;
        i=noteList.size();
    }
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        final ViewHolder viewHolder= new ViewHolder(view);
        //匿名内部类
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Note note=noteList.get(position);
               context=v.getContext();
               //获取当前的Activity
               Activity activity= SetAppTypeface.getActivity(context);
               if(activity==SetAppTypeface.getActivityByClass(YearActivity.class))
               {
                  MonthActivity.actionStart(context,note);
                  activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

               }else if(activity==SetAppTypeface.getActivityByClass(MonthActivity.class)){
                    DayActivity.actionStart(context,note);
                    activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
               }else if(activity==SetAppTypeface.getActivityByClass(MainActivity.class)){
                    LookActivity.actionStart(context,note);
                     activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
               }else if(activity==SetAppTypeface.getActivityByClass(DayActivity.class)){
                   MainActivity.actionStart(context,note);
                  activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
               }

            }
        });
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note=noteList.get(position);
       context=holder.view.getContext();
       Activity activity=SetAppTypeface.getActivity(context);

        if("无笔记".equals(note.getYear())){
            holder.textView.setText("请添加一个笔记");
        } else  if(activity==SetAppTypeface.getActivityByClass(YearActivity.class)) {
            holder.textView.setText(note.getYear());
        }else if(activity==SetAppTypeface.getActivityByClass(MonthActivity.class)){
            holder.textView.setText(note.getMonth());
        }else if(activity==SetAppTypeface.getActivityByClass(DayActivity.class)){
            holder.textView.setText(note.getDay());
        }else if(activity==SetAppTypeface.getActivityByClass(MainActivity.class)){
            holder.textView.setText(note.getTitle());
        }
        }
    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
