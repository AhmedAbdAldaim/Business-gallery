package com.example.businesgalleryadmin.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.EditProfileResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Utility.Utility;

public class EditProfile extends AppCompatActivity {

    EditText ed_name,ed_email;
    Button btn_editprofile,btn_editprofile_transparent;
    String name,email;
    TextView tv_editpassword;
    LocalSession localSession;

    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_editprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inisallization
        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        btn_editprofile = findViewById(R.id.btn_editprofile);
        btn_editprofile_transparent = findViewById(R.id.btn_edit_transparent);
        localSession = new LocalSession(EditProfile.this);


        // <-- Get Local Date TO EditText -->
        ed_name.setText(localSession.getName());
        ed_email.setText(localSession.getEmail());


        //<---EditText When Text Change-->
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().equals(localSession.getName()))
                {
                    btn_editprofile_transparent.setVisibility(View.INVISIBLE);
                    btn_editprofile.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn_editprofile_transparent.setVisibility(View.VISIBLE);
                    btn_editprofile.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //<---EditText When Text Change-->
        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().equals(localSession.getEmail()))
                {
                    btn_editprofile_transparent.setVisibility(View.INVISIBLE);
                    btn_editprofile.setVisibility(View.VISIBLE);
                }
                else
                {
                    btn_editprofile_transparent.setVisibility(View.VISIBLE);
                    btn_editprofile.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_email.getId()) {
                    ed_email.setCursorVisible(true);
                    ed_email.requestFocus();
                }
            }
        });
        ed_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_email.setCursorVisible(false);
                ed_email.clearFocus();
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_email.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        //  <-- Onclick EditProfile Button-->
        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ed_name.getText().toString().trim();
                email = ed_email.getText().toString().trim();

                if (Validation(name,email))
                {
                   EditProfile(name,email);
                }

            }
        });


    }

    // <-- Check Fields Function -->
    public Boolean Validation(String name,String email) {
        if (name.isEmpty()) {
            ed_name.setError(getResources().getString(R.string.editprofile_name_empty));
            ed_name.requestFocus();
            return false;
        }else if(name.length()>33){
            ed_name.setError(getResources().getString(R.string.editprofile_name_check));
            ed_name.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            ed_email.setError(getResources().getString(R.string.editprofile_email_empty));
            ed_email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError(getString(R.string.editprofile_email_valid));
            ed_email.requestFocus();
            return false;
        }
        return true;
    }


    // <-- Send Data TO request And Git Response Status
    public void EditProfile(String name,String phone){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<EditProfileResponse> call= requestInterface.EditProfile(name,email,localSession.getPassword(),"Bearer " + localSession.getToken());
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
                                name,
                                phone,
                                LocalSession.getPassword(),
                                response.body().getEditprofileModel().getRole());
                        Toast.makeText(EditProfile.this, getString(R.string.editprofile_successfully)+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), EditProfile.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditProfile.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),EditProfile.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }

    // <-- Intent To EditPassword Page
    public void EditPassword_btn(View view) {
        startActivity(new Intent(EditProfile.this,EditPassword.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
