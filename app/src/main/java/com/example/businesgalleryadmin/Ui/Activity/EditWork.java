package com.example.businesgalleryadmin.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.AddWorkResponse;
import com.example.businesgalleryadmin.Model.EditWorkResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditWork extends AppCompatActivity {
    EditText ed_img_name,ed_img_details,ed_img_price;
    ImageView image;
    TextView img_empty;
    Button btn_editwork;
    String image_path="";
    String id;
    LocalSession localSession;
    MultipartBody.Part partt;
    MultipartBody.Part name;
    MultipartBody.Part details;
    MultipartBody.Part price;
    String name_,details_,price_;

    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_work);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_edit_work);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inisallization
        ed_img_name = findViewById(R.id.ed_img_name);
        ed_img_details = findViewById(R.id.ed_img_details);
        ed_img_price = findViewById(R.id.ed_img_price);
        image = findViewById(R.id.image);
        img_empty = findViewById(R.id.image_empty);
        btn_editwork = findViewById(R.id.btn_editwork);
        localSession = new LocalSession(EditWork.this);


        //GetIntenet
        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");
        String img_name = intent.getStringExtra("img_name");
        String img_details = intent.getStringExtra("img_details");
        String img_price = intent.getStringExtra("img_price");
        String img = intent.getStringExtra("image");
        Picasso.with(EditWork.this).load(img).into(image);

        //image_path = im;
        ed_img_name.setText(img_name);
        ed_img_details.setText(img_details);
        ed_img_price.setText(img_price);


        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_img_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_img_price.getId()) {
                    ed_img_price.setCursorVisible(true);
                    ed_img_price.requestFocus();
                }
            }
        });
        ed_img_price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_img_price.setCursorVisible(false);
                ed_img_price.clearFocus();
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_img_price.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        //  <-- Onclick EditProfile Button-->
        btn_editwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Part
                name = MultipartBody.Part.createFormData("name", ed_img_name.getText().toString().trim());
                details = MultipartBody.Part.createFormData("details", ed_img_details.getText().toString().trim());
                price = MultipartBody.Part.createFormData("price", ed_img_price.getText().toString().trim());

                //EditTEXT
                name_ = ed_img_name.getText().toString().trim();
                details_ = ed_img_details.getText().toString().trim();
                price_ = ed_img_price.getText().toString().trim();

                //getImage
                File file = new File(image_path);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                partt = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
                if (Validation(name_, details_, price_)) {
                    if (!image_path.isEmpty()) {
                        EditWork(name, details, price, partt);
                    } else {
                        EditWork(name, details, price, null);
                    }

                }

            }
        });
    }

    // <-- Check Fields Function -->
    public Boolean Validation(String name,String details,String price) {
        if (name.isEmpty()) {
            ed_img_name.setError(getResources().getString(R.string.addwork_name_empty));
            ed_img_name.requestFocus();
            return false;
        }
        if (price.isEmpty()) {
            ed_img_price.setError(getResources().getString(R.string.addwork_price_empty));
            ed_img_price.requestFocus();
            return false;
        }
        if (details.isEmpty()) {
            ed_img_details.setError(getResources().getString(R.string.addwork_detailes_empty));
            ed_img_details.requestFocus();
            return false;
        }
        return true;
    }

//    // <--- On Click Image -->
    public void img_select(View view) {
        int p = ActivityCompat.checkSelfPermission(EditWork.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(p != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditWork.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1 );
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        }
    }

    //<--Get Url For Image -->
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100&&resultCode==RESULT_OK)
        {
                Uri uri = data.getData();
                image_path = getReadPhatFromUri(uri);
                image.setImageURI(uri);
                img_empty.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getReadPhatFromUri(Uri uri){
        String prog[]= {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),uri,prog,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int coulm_indx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String res =cursor.getString(coulm_indx);
        cursor.close();
        return res;
    }


    // <-- Send Data TO request And Git Response Status
    public void EditWork(MultipartBody.Part name,MultipartBody.Part details,MultipartBody.Part price,MultipartBody.Part image){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.custom_progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<EditWorkResponse> call= requestInterface.EditWorks(id,name,details,price,image,"Bearer " + localSession.getToken());
        call.enqueue(new Callback<EditWorkResponse>() {
            @Override
            public void onResponse(Call<EditWorkResponse> call, Response<EditWorkResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        Toast.makeText(EditWork.this, getString(R.string.edit_work_successfully)+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditWork.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        loading.dismiss();
                    }
                    else
                    {
                        loading.dismiss();
                        Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), EditWork.this);
                    }
                }
                else
                {
                    loading.dismiss();
                    Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),EditWork.this);
                    Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EditWorkResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow)+t.getMessage(),EditWork.this);
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
