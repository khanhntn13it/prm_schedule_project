package ntnk.sample.scheduleproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.adapter.BoardAdapter;
import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.sqlite.BoardDatabaseHelper;

public class BoardActivity extends AppCompatActivity {

    private static List<Board> list;
    public static ListView listView;
    public static BoardDatabaseHelper bdh;
    public static BoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        list = new ArrayList<>();
        bdh = new BoardDatabaseHelper(this);
        list = bdh.getListBoards();
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
                Toast.makeText(getApplicationContext(),
                        "Redirect to list tasks of board in position " + String.valueOf(position)
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == 200){
            list = bdh.getListBoards();
            adapter = new BoardAdapter(list, this);
            listView = (ListView) findViewById(R.id.listViewBoard);
            listView.setAdapter(adapter);
            ((BoardAdapter)listView.getAdapter()).notifyDataSetChanged();
        }
    }

    public static List<Board> getBoardList(){
        return list;
    }
    public static BoardDatabaseHelper getBoardDatabase(){return bdh;};
    public static BoardAdapter getAdapter(){return ((BoardAdapter)listView.getAdapter());};
}
