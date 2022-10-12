package com.example.businesgalleryadmin.Ui.Fragment;
import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Ui.Activity.EditProfile;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Ui.Activity.Login;
import com.example.businesgalleryadmin.Ui.Activity.MainActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Profile extends Fragment {
    CardView EditProfileCardView,logoutCardView;
    TextView tv_name,tv_type,tv_email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);

        //ininsallization
        EditProfileCardView = root.findViewById(R.id.editprofile_cardview);
        logoutCardView = root.findViewById(R.id.logout_cardview);
        tv_name = root.findViewById(R.id.name);
        tv_type = root.findViewById(R.id.type);
        tv_email = root.findViewById(R.id.email);

        tv_name.setText(LocalSession.getName());
        if(LocalSession.getRole().equals("2")){
            tv_type.setText("مصمم");
        }else if(LocalSession.getRole().equals("3")){
            tv_type.setText("مصور فوتوغرافي");
        }else if(LocalSession.getRole().equals("4")){
            tv_type.setText("رسام");
        }
        tv_email.setText(LocalSession.getEmail());


        // <-- Go TO EditProfile Page -->
        EditProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfile.class));
            }
        });

        // <-- logut -->
        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = getLayoutInflater().inflate(R.layout.custom_logout_massage, null);
                ImageView image_cancle = view1.findViewById(R.id.cancle);
                Button logout_confirm = view1.findViewById(R.id.confirm_btn);
                Button logout_cancle = view1.findViewById(R.id.cancle_btn);
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                dialog.getWindow().setBackgroundDrawable(insetDrawable);

                image_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                logout_confirm.setOnClickListener(v ->
                {
                    LocalSession.clearSession();
                    Intent intent1 = new Intent(getActivity(), Login.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    getActivity().finish();
                });
                logout_cancle.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            }
        });

        return root;
    }
}
