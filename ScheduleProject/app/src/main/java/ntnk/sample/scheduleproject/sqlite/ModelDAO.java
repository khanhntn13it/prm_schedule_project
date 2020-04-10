package ntnk.sample.scheduleproject.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ModelDAO {
    protected SQLiteDatabase db;
    protected TaskDatabaseHelper dbHelper;
    private Context mContext;

    public ModelDAO(Context mContext) {
        this.mContext = mContext;
        TaskDatabaseHelper.getHelper(mContext);
        open();
    }
    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = TaskDatabaseHelper.getHelper(mContext);
        db = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
        db = null;
    }
}
