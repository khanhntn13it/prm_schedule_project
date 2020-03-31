package ntnk.sample.scheduleproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import ntnk.sample.scheduleproject.R;

public class AboutUsActivity extends AppCompatActivity {

    View aboutPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
//                }
//                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });
        aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.app_icon)
                .setDescription("This is out project in subject PRM391 - Mobile Programing - IS1301")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(new Element().setTitle("Advertise here"))
                .addGroup("Connect with us")
                .addEmail("scheduleprojectttt@gmail.com")
                .addWebsite("https://github.com/khanhntn13it/prm_schedule_project")
                .addFacebook("hfs.eurus")
                .addTwitter("scheduleproject")
                .addYoutube("UClyA28-01x4z60eWQ2kiNbA")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("khanhntn13it")
                .addInstagram("kwonnara_com")
                .addItem(new Element().setTitle(
                        String.format("Copyright %d by Schedule Project",
                                Calendar.getInstance().get(Calendar.YEAR))))
                .create();
        setContentView(aboutPage);

//        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
//        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

//    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.navigation_home:
//                            Intent intent = new Intent(AboutUsActivity.this, BoardActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(0,0);
//                            return true;
//                        case R.id.navigation_task:
//                            intent = new Intent(AboutUsActivity.this, TodayTaskActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(0,0);
//                            return true;
//                        case R.id.navigation_aboutus:
//                            intent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(0,0);
//                            return true;
//                    }
//                    return false;
//                }
//            };
}