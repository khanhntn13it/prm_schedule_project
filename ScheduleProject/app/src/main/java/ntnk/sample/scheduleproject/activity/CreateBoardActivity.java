package ntnk.sample.scheduleproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.sqlite.BoardDAO;
import yuku.ambilwarna.AmbilWarnaDialog;

import ntnk.sample.scheduleproject.R;

public class CreateBoardActivity extends AppCompatActivity {

    int mDefaultColor;
    BoardDAO boardDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        boardDAO = BoardActivity.getBoardDatabase();

        mDefaultColor = ContextCompat.getColor(CreateBoardActivity.this, R.color.colorPrimary);

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
                Board board = new Board(BoardActivity.getBoardList().size() + 2, textName.getText().toString(), mDefaultColor);
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
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
            }
        });
        colorPicker.show();
    }
}
