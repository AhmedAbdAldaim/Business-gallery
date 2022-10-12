package com.example.businesgalleryadmin.Ui.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.GetAllOrdersModel;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Ui.Adapter.AllOrders_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Requestes extends Fragment {
    RecyclerView recyclerView;
    TextView tv_total_all_orders,tv_connect;
    RequestQueue requestQueue;
    LinearLayout liner_emptydate;
    Button btn_connect;
    AllOrders_adapter allOrders_adapter;
    ArrayList<GetAllOrdersModel> arrayList = new ArrayList<GetAllOrdersModel>();
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";
    private static final String TAG_error  = "TAG_error";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.requestes_fragment, container, false);

        tv_connect = root.findViewById(R.id.connection);
        btn_connect = root.findViewById(R.id.btnconnection);
        tv_total_all_orders = root.findViewById(R.id.total_all_orders);
        liner_emptydate = root.findViewById(R.id.liner_empty_date);

        recyclerView = root.findViewById(R.id.rec);
        localSession = new LocalSession(getContext());


        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        getAllOrders();

        return root;
    }


    public void getAllOrders(){
        ProgressDialog loading = ProgressDialog.show(getContext(),null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        String url = "http://192.168.144.199:8000/api/user_order";
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("orders");
                    Boolean error = jsonObject.getBoolean("error");
                    if(error.equals(false)){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String order_id = object.getString("id");
                            String status = object.getString("status");

                            JSONObject jsonObjec11 = object.getJSONObject("user");
                            String name_order = jsonObjec11.getString("name");
                            String phone_order = jsonObjec11.getString("bio");

                            JSONObject jsonObjec12 = object.getJSONObject("work");
                            int user_id = jsonObjec12.getInt("user_id");
                            String name_img = jsonObjec12.getString("name");
                            String price = jsonObjec12.getString("price");
                            String photo = jsonObjec12.getString("photo");
                            if(Integer.parseInt(LocalSession.getId())==user_id){
                                arrayList.add(new GetAllOrdersModel(order_id,status,name_order, phone_order,name_img, price, photo));
                                allOrders_adapter = new AllOrders_adapter(arrayList, getContext());
                                recyclerView.setAdapter(allOrders_adapter);
                                tv_total_all_orders.setText(arrayList.size() + "");
                                loading.dismiss();
                            }else{
                                loading.dismiss();
                                tv_total_all_orders.setText("0");
                                liner_emptydate.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else{
                        loading.dismiss();
                        tv_total_all_orders.setText(jsonArray.length() + "");
                        liner_emptydate.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Log.i(TAG_server, e.getMessage());
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization","Bearer "+ localSession.getToken());
                return hashMap;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
