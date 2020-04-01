package ntnk.sample.scheduleproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.sqlite.BoardDAO;
import yuku.ambilwarna.AmbilWarnaDialog;

import ntnk.sample.scheduleproject.R;

public class CreateBoardActivity extends AppCompatActivity {

    int mDefaultColor;
    BoardDAO boardDAO;
    BottomNavigationView bottomNavigation;
    Button btnColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        btnColorPicker = findViewById(R.id.btnColorPicker);

        boardDAO = BoardActivity.getBoardDatabase();

        mDefaultColor = ContextCompat.getColor(CreateBoardActivity.this, R.color.colorPrimary);
        btnColorPicker.setBackgroundColor(mDefaultColor);

        final TextView textName = (TextView) findViewById(R.id.plainTextName);
        Button btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        Button btnCreate = (Button) findViewById(R.id.btnEdit);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Board board = new Board(textName.getText().toString(), mDefaultColor);
                boardDAO.insert(board);
                setResult(200, intent);
                finish();
            }
        });

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                btnColorPicker.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(CreateBoardActivity.this, BoardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(CreateBoardActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(CreateBoardActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            };
}
