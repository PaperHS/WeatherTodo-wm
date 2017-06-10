package com.anbillon.weathertodo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxttt on 2017/5/21.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RcViewHoler>{

    List<ThingTodo> todoList = new ArrayList<>();
    Context context  ;
    RecyclerAdapter(List<ThingTodo> todoList ,Context context ){
        this.todoList = todoList;
        this.context = context;

    }

    public AdapterView.OnItemClickListener listener;

    public void setListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RcViewHoler onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_noti,parent,false);
        final RcViewHoler holer = new RcViewHoler(v);
        holer.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(null,v,(int)holer.tv1.getTag(),0);
                }
            }
        });
        return holer;
    }

    @Override
    public void onBindViewHolder(RcViewHoler holder, int position) {
        holder.tv1.setTag(position);
        ThingTodo todo = todoList.get(position);
        holder.tv1.setText(todo.thing);
        holder.tv2.setText("time:"+todo.time);
    }

    @Override
    public int getItemCount() {
        return todoList == null ?0:todoList.size();
    }


    public class RcViewHoler extends RecyclerView.ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public RcViewHoler(View itemView) {
            super(itemView);
            tv1 = (TextView)itemView.findViewById(R.id.tv_content);
            tv2 = (TextView)itemView.findViewById(R.id.tv_time);
        }
    }

}
