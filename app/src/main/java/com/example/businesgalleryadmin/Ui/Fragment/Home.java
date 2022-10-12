package com.example.businesgalleryadmin.Ui.Fragment;
import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.GetAllWorksByUserIDResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.Ui.Activity.Add_Work;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Ui.Activity.MainActivity;
import com.example.businesgalleryadmin.Ui.Adapter.GetAllWorksByUserID_adapter;
import com.example.businesgalleryadmin.Utility.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {
    public static FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    GetAllWorksByUserID_adapter getAllWorksByUserID_adapter;
    TextView tv_total_course, tv_connect, tv_empty;
    Button btn_connect;
    EditText ed_Search;
    ImageView img_clear_search;
    SwipeRefreshLayout swipeRefreshLayout;
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";
    private static final String TAG_error = "TAG_error";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment, container, false);

        //inisalization
        floatingActionButton = root.findViewById(R.id.floatingActionButton);
        ed_Search = root.findViewById(R.id.search);
        tv_connect = root.findViewById(R.id.connection);
        btn_connect = root.findViewById(R.id.btnconnection);
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        img_clear_search = root.findViewById(R.id.img_clear);
        //tv_total_course = root.findViewById(R.id.total);
        tv_empty = root.findViewById(R.id.empty);
        recyclerView = root.findViewById(R.id.rec);

        ConnectivityManager connectivityManager = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        localSession = new LocalSession(getContext());

        // <-- Onclick floatingActionButton -->
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Add_Work.class));
            }
        });

        //<-- Search -->
        ed_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    getAllWorksByUserID_adapter.getFilter().filter(charSequence);
                } catch (Exception e) {
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    getAllWorksByUserID_adapter.getFilter().filter(charSequence);
                    img_clear_search.setVisibility(View.VISIBLE);
                    img_clear_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ed_Search.setText("");
                           img_clear_search.setVisibility(View.INVISIBLE);
                        }
                    });
                    if(ed_Search.getText().toString().isEmpty()){
                        img_clear_search.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //<--- EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_Search.getId()) {
                    ed_Search.setCursorVisible(true);
                }
            }
        });
        ed_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_Search.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_Search.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        // <-- Refresh -->
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (recyclerView.getVisibility() == View.INVISIBLE) {
                    tv_connect.setVisibility(View.INVISIBLE);
                    btn_connect.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    getAllWorks();
                }
                tv_connect.setVisibility(View.INVISIBLE);
                btn_connect.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                getAllWorks();
            }
        });

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAllWorks();

        return root;
    }


    public void getAllWorks() {
        ProgressDialog loading = ProgressDialog.show(getContext(), null, getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<GetAllWorksByUserIDResponse> call = requestInterface.GetAllWorksByUserID(localSession.getId(),"Bearer " + localSession.getToken());
        call.enqueue(new Callback<GetAllWorksByUserIDResponse>() {
            @Override
            public void onResponse(Call<GetAllWorksByUserIDResponse> call, Response<GetAllWorksByUserIDResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        getAllWorksByUserID_adapter = new GetAllWorksByUserID_adapter(response.body().getGetAllWorksByUserIDModel().getGetAllWorksByUserIDModel2(), getContext());
                        if (getAllWorksByUserID_adapter.getItemCount() == 0) {
                            loading.dismiss();
                            tv_empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
//                        total_rec.setVisibility(View.VISIBLE);
//                        total_rec.setText(clinics_adapter.getItemCount() + "");
                            //liner_empty_date.setText(R.string.empty);
                            return;
                        } else if (getAllWorksByUserID_adapter.getItemCount() > 0) {
                            loading.dismiss();
                            tv_empty.setVisibility(View.INVISIBLE);
                            //total_rec.setVisibility(View.VISIBLE);
                            //  total_rec.setText(clinics_adapter.getItemCount() + "");
                            getAllWorksByUserID_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(getAllWorksByUserID_adapter);
                        }
                    } else {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), getContext());
                    }

                } else {
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), getContext());
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetAllWorksByUserIDResponse> call, Throwable t) {
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
                        tv_connect.setVisibility(View.INVISIBLE);
                        btn_connect.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        getAllWorks();
                    }
                });
            }
        });
    }
}

