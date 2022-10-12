package com.example.businesgalleryadmin.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.businesgalleryadmin.Model.EditProfileResponse;
import com.example.businesgalleryadmin.Model.LoginResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Utility.Utility;

import java.util.Locale;

public class EditPassword extends AppCompatActivity {

    EditText ed_oldpassword,ed_newpassword,ed_confirmpassword;
    Button btn_editPassword;
    String oldpassword,newpassword,confirmpassword;
    ImageView imageView_visible_oldpassword,imageView_invisible_oldpassword,imageView_visible_newpassword,imageView_invisible_newpassword,imageView_visible_confirmpassword,imageView_invisible_confirmpassword;
    FrameLayout frameLayout_oldpassword,frameLayout_newpassword,frameLayout_confirmpassword;
    LocalSession localSession;

    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_editpassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inisallization
        ed_oldpassword = findViewById(R.id.ed_oldpassword);
        ed_newpassword = findViewById(R.id.ed_newpassword);
        ed_confirmpassword = findViewById(R.id.ed_new_confirm_password);
        btn_editPassword = findViewById(R.id.btn_editpassword);
        imageView_visible_oldpassword = findViewById(R.id.visibiltyoff_oldpassword);
        imageView_invisible_oldpassword = findViewById(R.id.visibilty_oldpassword);
        imageView_visible_newpassword = findViewById(R.id.visibiltyoff_newpassword);
        imageView_invisible_newpassword = findViewById(R.id.visibilty_newpassword);
        imageView_visible_confirmpassword = findViewById(R.id.visibiltyoff_new_password_confirm);
        imageView_invisible_confirmpassword = findViewById(R.id.visibilty_new_password_confirm);
        frameLayout_oldpassword = findViewById(R.id.framelayout_oldpassword);
        frameLayout_newpassword = findViewById(R.id.framelayout_newpassword);
        frameLayout_confirmpassword = findViewById(R.id.framelayout_new_password_confirm);
        localSession = new LocalSession(EditPassword.this);


        // <-- Show Visibilty or Invisibilty Icon When OnTextChange 1 -->
        ed_oldpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout_oldpassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) {
                    frameLayout_oldpassword.setVisibility(View.INVISIBLE);
                }
                else {
                    frameLayout_oldpassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // <-- Visibilty and Invisibilty Password 1 -->
        imageView_visible_oldpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visible_oldpassword.setVisibility(View.GONE);
                imageView_invisible_oldpassword.setVisibility(View.VISIBLE);
                ed_oldpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
        imageView_invisible_oldpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisible_oldpassword.setVisibility(View.GONE);
                imageView_visible_oldpassword.setVisibility(View.VISIBLE);
                ed_oldpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });



        // <-- Show Visibilty or Invisibilty Icon When OnTextChange 2 -->
        ed_newpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout_newpassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) {
                    frameLayout_newpassword.setVisibility(View.INVISIBLE);
                }
                else {
                    frameLayout_newpassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // <-- Visibilty and Invisibilty Password 2-->
        imageView_visible_newpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visible_newpassword.setVisibility(View.GONE);
                imageView_invisible_newpassword.setVisibility(View.VISIBLE);
                ed_newpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
        imageView_invisible_newpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisible_newpassword.setVisibility(View.GONE);
                imageView_visible_newpassword.setVisibility(View.VISIBLE);
                ed_newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });



        // <-- Show Visibilty or Invisibilty Icon When OnTextChange 3 -->
        ed_confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                frameLayout_confirmpassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) {
                    frameLayout_confirmpassword.setVisibility(View.INVISIBLE);
                }
                else {
                    frameLayout_confirmpassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // <-- Visibilty and Invisibilty Password -->
        imageView_visible_confirmpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visible_confirmpassword.setVisibility(View.GONE);
                imageView_invisible_confirmpassword.setVisibility(View.VISIBLE);
                ed_confirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
        imageView_invisible_confirmpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisible_confirmpassword.setVisibility(View.GONE);
                imageView_visible_confirmpassword.setVisibility(View.VISIBLE);
                ed_confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });



        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_confirmpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_confirmpassword.getId()) {
                    ed_confirmpassword.setCursorVisible(true);
                    ed_confirmpassword.requestFocus();
                }
            }
        });
        ed_confirmpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_confirmpassword.setCursorVisible(false);
                ed_confirmpassword.clearFocus();
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_confirmpassword.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });



        //  <-- Onclick Login Button-->
        btn_editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldpassword = ed_oldpassword.getText().toString().trim();
                newpassword = ed_newpassword.getText().toString().trim();
                confirmpassword = ed_confirmpassword.getText().toString().trim();

                if (Validation(oldpassword, newpassword ,confirmpassword))
                {
                    Login(oldpassword);
                }
            }
        });
    }



    // <-- Check Fields Function -->
    public Boolean Validation(String password,String newPassword,String newpasswordconfirm){
        if (password.isEmpty())
        {
            ed_oldpassword.setError(getResources().getString(R.string.editpassword_old_password_empty));
            ed_oldpassword.requestFocus();
            return false;
        }

        if (newPassword.isEmpty())
        {
            ed_newpassword.setError(getResources().getString(R.string.editpassword_new_password_empty));
            ed_newpassword.requestFocus();
            return false;
        }
        else if(newPassword.length() <8)
        {
            ed_newpassword.setError(getResources().getString(R.string.editprofile_password_check));
            ed_newpassword.requestFocus();
            return false;
        }

        if (newpasswordconfirm.isEmpty())
        {
            ed_confirmpassword.setError(getResources().getString(R.string.editpassword_new_password_confirm_empty));
            ed_confirmpassword.requestFocus();
            return false;
        }
        else if(!newPassword.isEmpty()&&!newpasswordconfirm.equals(newPassword))
        {
            ed_confirmpassword.setError(getResources().getString(R.string.editpassword_password_similarity));
            ed_confirmpassword.requestFocus();
            return false;
        }

        return true;
    }


    // <-- Send Data TO request And Git Response Status
    public void Login(String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);



        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoginResponse> call= requestInterface.Login(localSession.getEmail(),password);
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
                                response.body().getLoginModel(). getEmail(),
                                localSession.getPassword(),
                                response.body().getLoginModel().getRole());

                        EditPassword(newpassword);
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.editpassword_wrong_password), EditPassword.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditPassword.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),EditPassword.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }



    // <-- Send Data TO request And Git Response Status
    public void EditPassword(String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);



        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<EditProfileResponse> call= requestInterface.EditProfilePassword(localSession.getName(),localSession.getEmail(),password,"Bearer " + localSession.getToken());
        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        loading.dismiss();
                        localSession.createSession(
                                LocalSession.getToken(),
                                response.body().getEditprofileModel().getId(),
                                response.body().getEditprofileModel().getName(),
                                response.body().getEditprofileModel().getEmail(),
                                password,
                                response.body().getEditprofileModel().getRole());

                        Toast.makeText(EditPassword.this, getString(R.string.editprofile_successfully)+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPassword.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), EditPassword.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditPassword.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),EditPassword.this);
                Utility.printLog(Tag_failure, t.getMessage());
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
