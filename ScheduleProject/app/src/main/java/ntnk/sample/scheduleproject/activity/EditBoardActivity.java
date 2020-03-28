package ntnk.sample.scheduleproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.sqlite.BoardDAO;
import yuku.ambilwarna.AmbilWarnaDialog;

public class EditBoardActivity extends AppCompatActivity {

    int mDefaultColor;
    BoardDAO boardDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board);

        final Board board = (Board)getIntent().getSerializableExtra("board");

        boardDAO = BoardActivity.getBoardDatabase();
        mDefaultColor = ContextCompat.getColor(EditBoardActivity.this, R.color.colorPrimary);

        final TextView textName = (TextView) findViewById(R.id.plainTextName);
        textName.setText(board.getName());
        TextView tvCurrentColor = (TextView)findViewById(R.id.tvCurrentColor);
        tvCurrentColor.setBackgroundColor(board.getColor());

        Button btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                board.setColor(mDefaultColor);
                board.setName(textName.getText().toString());
                boardDAO.update(board);
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

        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                boardDAO.deleteBoardById(board);
                setResult(200, intent);
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
