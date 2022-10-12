package com.example.businesgalleryadmin.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.AddCommentResponse;
import com.example.businesgalleryadmin.Model.LoveandCommentsResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Ui.Adapter.GetAllComments_adapter;
import com.example.businesgalleryadmin.Utility.Utility;

public class Comments extends AppCompatActivity {
    EditText ed_add_comment;
    TextView btn_add_comment;
    TextView tv_connect, tv_empty;
    Button btn_connect;
    String comment,work_id;
    LocalSession localSession;
    //<---
    RecyclerView recyclerView;
    GetAllComments_adapter getAllComments_adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG_error = "TAG_error";
    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        work_id = intent.getStringExtra("work_id");

        //Inisallization
        ed_add_comment = findViewById(R.id.ed_add_comment);
        btn_add_comment = findViewById(R.id.btn_add_comment);
        localSession = new LocalSession(this);
        //<--
        tv_connect = findViewById(R.id.connection);
        btn_connect = findViewById(R.id.btnconnection);
        swipeRefreshLayout = findViewById(R.id.swipe);
        tv_empty = findViewById(R.id.empty);
        recyclerView = findViewById(R.id.rec);


        //<---EditText When Text Change-->
        ed_add_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    btn_add_comment.setVisibility(View.VISIBLE);
                }else {
                    btn_add_comment.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //  <-- Onclick EditProfile Button-->
        btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = ed_add_comment.getText().toString().trim();
                ed_add_comment.setText("");
                 AddComment(comment);
                //<--Hidden Keyboard
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isAcceptingText())
                {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });



        //<--
        // <-- Refresh -->
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (recyclerView.getVisibility() == View.INVISIBLE) {
                    tv_connect.setVisibility(View.INVISIBLE);
                    btn_connect.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    getAllComments();
                }
                tv_connect.setVisibility(View.INVISIBLE);
                btn_connect.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                getAllComments();
            }
        });

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAllComments();

    }


    // <-- Send Data TO request And Git Response Status
    public void AddComment(String comment){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<AddCommentResponse> call= requestInterface.SendComment(work_id,comment,"Bearer " + localSession.getToken());
        call.enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        loading.dismiss();
                        Toast.makeText(Comments.this, getString(R.string.comment_done)+"", Toast.LENGTH_SHORT).show();
                        getAllComments();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Comments.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),Comments.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Comments.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }



    public void getAllComments() {
        ProgressDialog loading = ProgressDialog.show(Comments.this, null, getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoveandCommentsResponse> call = requestInterface.GetallComments(work_id,"Bearer " + localSession.getToken());
        call.enqueue(new Callback<LoveandCommentsResponse>() {
            @Override
            public void onResponse(Call<LoveandCommentsResponse> call, Response<LoveandCommentsResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        getAllComments_adapter = new GetAllComments_adapter(response.body().getTotalLoveModel().getCommentsModel(),Comments.this);
                        if (getAllComments_adapter.getItemCount() == 0) {
                            loading.dismiss();
                            tv_empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        } else if (getAllComments_adapter.getItemCount() > 0) {
                            loading.dismiss();
                            tv_empty.setVisibility(View.INVISIBLE);
                            getAllComments_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(getAllComments_adapter);

                        }
                    } else {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), Comments.this);
                    }

                } else {
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Comments.this);
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoveandCommentsResponse> call, Throwable t) {
                loading.dismiss();
                Utility.printLog(Tag_failure, t.getMessage());
                tv_connect.setText(R.string.connect_internet_slow);
                tv_connect.setVisibility(View.VISIBLE);
                btn_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.INVISIBLE);
                btn_connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Comments.this, t.getMessage()+"", Toast.LENGTH_SHORT).show();
                        tv_connect.setVisibility(View.INVISIBLE);
                        btn_connect.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        getAllComments();
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
