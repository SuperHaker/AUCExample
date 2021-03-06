package com.example.android.testapp.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.testapp.adapters.MyDataAdapter;
import com.example.android.testapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDataFragment extends Fragment {

    MyDataAdapter adapter;
    FloatingActionButton fab;
    RequestQueue queue = null;
    public static final String TAG = "VolleyRequest";
    ArrayList<String> list = new ArrayList<>();


    public MyDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getContext());
        String url = "https://adurcupexamplerequest.firebaseio.com/Round_2/Products/Products.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                list.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        list.add((String) response.get(i));
                        adapter.notifyDataSetChanged();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        jsonArrayRequest.setTag(TAG);
        queue.add(jsonArrayRequest);
        adapter = new MyDataAdapter(getContext(), list);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_data, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View v = inflater.inflate(R.layout.fab_dialog, null);
                builder.setView(v)
                        .setPositiveButton("Patch", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                String token = sharedPref.getString("user_token", "");
                                String key = ((EditText) v.findViewById(R.id.edittext_key)).getText().toString();
                                String val = ((EditText) v.findViewById(R.id.edittext_value)).getText().toString();
                                patchRequest(token, key, val);


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                builder.create();
                builder.show();


            }
        });

        return v;
    }


    public void patchRequest(String token, String key, String val){
        String url = "https://adurcupexamplerequest.firebaseio.com/Round_2/Products/Product_Details.json/?auth=" + token;

        HashMap<String, String> map = new HashMap<>();
        map.put(key, val);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, new JSONObject(map), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error patching", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        request.setTag(TAG);
        queue.add(request);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(TAG);
        }
    }
}
