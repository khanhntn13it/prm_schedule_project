package ntnk.sample.scheduleproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.adapter.BoardAdapter;
import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.sqlite.BoardDAO;

public class BoardActivity extends AppCompatActivity {

    private static List<Board> list;
    public static ListView listView;
    public static BoardDAO boardDAO;
    public static BoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpForFirstRun();
        setContentView(R.layout.activity_board);
        list = new ArrayList<>();
        boardDAO = new BoardDAO(this);
        list = boardDAO.getListBoards();
        //Toast.makeText(getApplicationContext(), "Init ---" + String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
        adapter = new BoardAdapter(list, this);
        listView = (ListView) findViewById(R.id.listViewBoard);
        listView.setAdapter(adapter);

        Button btnCreateBoard = (Button)findViewById(R.id.btnCreateBoard);
        btnCreateBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, CreateBoardActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        "Redirect to list tasks of board in position " + String.valueOf(position)
//                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardActivity.this, MainActivity.class);
                intent.putExtra("boardId", list.get(position).getId());
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == 200){
            list = boardDAO.getListBoards();
            adapter = new BoardAdapter(list, this);
            listView = (ListView) findViewById(R.id.listViewBoard);
            listView.setAdapter(adapter);
            ((BoardAdapter)listView.getAdapter()).notifyDataSetChanged();
        }
    }

    public static List<Board> getBoardList(){
        return list;
    }
    public static BoardDAO getBoardDatabase(){return boardDAO;};
    public static BoardAdapter getAdapter(){return ((BoardAdapter)listView.getAdapter());};
    public void setUpForFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if(isFirstRun) {
            startActivity(new Intent(new Intent(this, IntroActivity.class)));
            Toast.makeText(this, "First run", Toast.LENGTH_SHORT);
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(BoardActivity.this, BoardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(BoardActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(BoardActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            };
}
