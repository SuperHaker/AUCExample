package com.example.android.testapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.android.testapp.MyDataAdapter;
import com.example.android.testapp.MyViewPager;
import com.example.android.testapp.R;
import com.example.android.testapp.adapters.MainPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.android.volley.Request.Method.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultDataFragment extends Fragment {

    public static final String TAG = "VolleyRequest";
    MyViewPager pager;
    RequestQueue queue;
    MainPagerAdapter pagerAdapter = null;
    TabLayout tabLayout;
    MyDataAdapter adapter;
    ArrayList<String> list = new ArrayList<>();


    public DefaultDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerAdapter = new MainPagerAdapter();
        queue = Volley.newRequestQueue(getContext());
        String url = "https://adurcupexamplerequest.firebaseio.com/Round_2/Products_Image.json";
        String url2 = "https://adurcupexamplerequest.firebaseio.com/Round_2/Default_Schema/Products.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET, url, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i = 0; i < response.length(); i++){
                        FrameLayout v0 = (FrameLayout) getActivity().getLayoutInflater().inflate (R.layout.viewpager_image_layout, null);
                        ImageView imageView = (ImageView) v0.findViewById(R.id.imageView);
                        Glide.with(getContext()).load(response.get(i)).into(imageView);
                        pagerAdapter.addView(v0, i);
                        pagerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
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
                }            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        jsonArrayRequest.setTag(TAG);
        arrayRequest.setTag(TAG);
        queue.add(jsonArrayRequest);
        queue.add(arrayRequest);
        adapter = new MyDataAdapter(getContext(), list);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_default_data, container, false);
        pager = (MyViewPager) v.findViewById(R.id.horizontal_images_viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(TAG);
        }
    }
}
