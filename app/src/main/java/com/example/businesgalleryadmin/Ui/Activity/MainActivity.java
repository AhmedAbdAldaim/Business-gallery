package com.example.businesgalleryadmin.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.WindowManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Ui.Fragment.Home;
import com.example.businesgalleryadmin.Ui.Fragment.Profile;
import com.example.businesgalleryadmin.Ui.Fragment.Requestes;

public class MainActivity extends AppCompatActivity {
   public static MeowBottomNavigation meowBottomNavigatio;
    final static int ID_user=1;
    final static int ID_home=2;
    final static int ID_Requests=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meowBottomNavigatio= findViewById(R.id.meowbottomnavigation);
        meowBottomNavigatio.add(new MeowBottomNavigation.Model(1,R.drawable.ic_person_dashboard));
        meowBottomNavigatio.add(new MeowBottomNavigation.Model(2,R.drawable.ic_home));
        meowBottomNavigatio.add(new MeowBottomNavigation.Model(3,R.drawable.ic_request));

        //Disable scrennshot
      // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Home()).commit();

        meowBottomNavigatio.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {

                }
            }

        });

        meowBottomNavigatio.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item){
                Fragment fragment = null;
                switch (item.getId()){
                    case ID_Requests:
                        fragment= new Requestes();
                        break;
                    case ID_home:
                        fragment= new Home();
                        break;
                    case ID_user:
                        fragment= new Profile();
                        break;
                    default:

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();

            }
        }
   );

        meowBottomNavigatio.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {
                    case ID_Requests:
                        fragment= new Requestes();
                        break;
                    case ID_home:
                        fragment= new Home();
                        break;
                    case ID_user:
                        fragment= new Profile();
                        break;
                    default:
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();
            }

        });

        meowBottomNavigatio.show(2,false);
    }
}
