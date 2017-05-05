package com.example.android.testapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
        RequestQueue queue;
        TextView product;

        public MyDataViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            product = (TextView) itemView.findViewById(R.id.product);
        }

        @Override
        public void onClick(View view) {
            String query = list.get(getAdapterPosition());
            queue = Volley.newRequestQueue(context);
            String url = "https://adurcupexamplerequest.firebaseio.com/Round_2/Default_Schema/Product_Details/" + query + ".json";
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    buildAlert(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(objectRequest);

        }

        public void buildAlert(JSONObject object){
            
        }

    }

}
