package com.example.businesgalleryadmin.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.LoginResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Utility.Utility;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText ed_email,ed_password;
    Button btn_login;
    String email,password;
    ImageView imageView_visibiltyoff,imageView_visibilty;
    FrameLayout frameLayout;
    LocalSession localSession;

    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inisallization
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        imageView_visibiltyoff = findViewById(R.id.visibiltyoff);
        imageView_visibilty = findViewById(R.id.visibilty);
        frameLayout = findViewById(R.id.framelayout);
        localSession = new LocalSession(Login.this);


        // <-- Show Visibilty or Invisibilty Icon When OnTextChange -->
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                frameLayout.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty())
                {
                    frameLayout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // <--Click Icon Visibilty and Invisibilty Password -->
        imageView_visibiltyoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visibiltyoff.setVisibility(View.GONE);
                imageView_visibilty.setVisibility(View.VISIBLE);
                ed_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_visibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visibilty.setVisibility(View.GONE);
                imageView_visibiltyoff.setVisibility(View.VISIBLE);
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });


        //<---Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_password.getId()) {
                    ed_password.setCursorVisible(true);
                    ed_password.requestFocus();
                }
            }
        });
        ed_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_password.setCursorVisible(false);
                ed_password.clearFocus();
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_password.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick Login Button-->
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = ed_email.getText().toString().trim();
                password = ed_password.getText().toString().trim();

                if (Validation(email, password))
                {
                    Login(email, password);
                }
            }
        });

    }

    // <-- Check Fields Function -->
    public Boolean Validation(String phone,String password) {
        if (phone.isEmpty()) {
            ed_email.setError(getResources().getString(R.string.email_empty));
            ed_email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(phone).matches()) {
            ed_email.setError(getString(R.string.email_valid));
            ed_email.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            ed_password.setError(getResources().getString(R.string.password_empty));
            ed_password.requestFocus();
            return false;
        }
        return true;
    }



    // <-- Send Data TO request And Git Response Status
    public void Login(String email,String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoginResponse> call= requestInterface.Login(email,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        loading.dismiss();
                        localSession.createSession(
                                response.body().getToken(),
                                response.body().getLoginModel().getId(),
                                response.body().getLoginModel().getName(),
                                response.body().getLoginModel().getEmail(),
                                ed_password.getText().toString().trim(),
                                response.body().getLoginModel().getRole());

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Login.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),Login.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Login.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }
}
