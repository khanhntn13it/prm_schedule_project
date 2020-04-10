package ntnk.sample.scheduleproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.BoardActivity;
import ntnk.sample.scheduleproject.activity.EditBoardActivity;
import ntnk.sample.scheduleproject.entity.Board;

public class BoardAdapter extends BaseAdapter {

    private List<Board> list;
    private Activity activity;

    public BoardAdapter(List<Board> list){
        this.list = list;
    }

    public BoardAdapter(List<Board> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.board_layout, null);
        }

        TextView textViewName = view.findViewById(R.id.plainTextName);
        TextView textViewColor = view.findViewById(R.id.textViewColor);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        final Board board = list.get(position);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditBoardActivity.class);
                intent.putExtra("board", (Serializable) board);
                activity.startActivityForResult(intent, 100);
            }
        });

        textViewName.setText(board.getName());
        Drawable drawable = activity.getResources().getDrawable(R.drawable.hexagon_white);
        drawable.setColorFilter(board.getColor(), PorterDuff.Mode.MULTIPLY);
        textViewColor.setBackground(drawable);

        return view;
    }
}
