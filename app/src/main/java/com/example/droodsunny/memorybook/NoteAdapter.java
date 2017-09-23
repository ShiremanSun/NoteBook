package com.example.droodsunny.memorybook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DroodSunny on 2017/9/23.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Note> noteList;

   private Context context;

    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView textView;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            textView=(TextView)itemView.findViewById(R.id.text);
        }
    }
    public NoteAdapter(List<Note> note){
        noteList=note;
    }
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        final ViewHolder viewHolder= new ViewHolder(view);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Note note=noteList.get(position);
               context=v.getContext();
               if(context==YearActivity.yearActivity)
               {
                   MonthActivity.actionStart(context,note);
               }else if(context==MonthActivity.monthActivity){
                   DayActivity.actionStart(context,note);
               }else if(context==MainActivity.mainActivity){
                   EditActivity.actionStart(context,note);
               }else if(context==DayActivity.dayActivity){
                   MainActivity.actionStart(context,note);
               }
            }
        });
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note=noteList.get(position);
       context=holder.view.getContext();
        if(noteList.isEmpty()){
            holder.textView.setText("无笔记");
        } else  if(context==YearActivity.yearActivity) {
            holder.textView.setText(note.getYear());
        }else if(context==MonthActivity.monthActivity){
            holder.textView.setText(note.getMonth());
        }else if(context==DayActivity.dayActivity){
            holder.textView.setText(note.getDay());
        }else if(context==MainActivity.mainActivity){
            holder.textView.setText(note.getTitle());
        }



        }
    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
