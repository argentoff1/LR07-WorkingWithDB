package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";
    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etFullName, etRecordingTime, etID;
    DBHelper dbHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnUpd = (Button) findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(this);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etRecordingTime = (EditText) findViewById(R.id.etRecordingTime);
        etID = (EditText) findViewById(R.id.etID);

        dbHelper = new DBHelper(this);
    }
    @Override
    public void onClick(View v) {

        ContentValues cv = new ContentValues();

        String name = etFullName.getText().toString();
        String email = etRecordingTime.getText().toString();
        String id = etID.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                cv.put("name", name);
                cv.put("email", email);

                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.btnRead:
                Log.d(LOG_TAG, "--- Rows in mytable: ---");

                Cursor c = db.query("mytable", null, null, null, null,
                        null, null);

                if (c.moveToFirst()) {

                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");
                    do {

                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) + ", name = "
                                        + c.getString(nameColIndex) + ", email = "
                                        + c.getString(emailColIndex));


                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");

                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
            case R.id.btnUpd:
                if (id.equalsIgnoreCase("")) {
                    break;
                }
                Log.d(LOG_TAG, "--- Update mytable: ---");

                cv.put("name", name);
                cv.put("email", email);

                int updCount = db.update("mytable", cv, "id = ?",
                        new String[] { id });
                Log.d(LOG_TAG, "updated rows count = " + updCount);
                break;
            case R.id.btnDel:
                if (id.equalsIgnoreCase("")) {
                    break;
                }
                Log.d(LOG_TAG, "--- Delete from mytable: ---");

                int delCount = db.delete("mytable", "id = " + id,
                        null);
                Log.d(LOG_TAG, "deleted rows count = " + delCount);
                break;
        }

        dbHelper.close();
    }
    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {

            super(context, "myDB", null, 1);
        }
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int
                newVersion) {
        }
    }
}