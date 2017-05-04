package com.example.android.testapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MyDataAdapter extends RecyclerView.Adapter<MyDataAdapter.MyDataViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> list = new ArrayList<>();

    public MyDataAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_data_list_layout, parent, false);
        return new MyDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyDataViewHolder holder, int position) {
        holder.product.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class MyDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView product;

        public MyDataViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            product = (TextView) itemView.findViewById(R.id.product);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
