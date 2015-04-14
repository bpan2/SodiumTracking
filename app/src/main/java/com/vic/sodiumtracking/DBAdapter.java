package com.vic.sodiumtracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class DBAdapter {
    static final String DATABASE_PATH = "/data/data/com.vic.sodiumtracking/databases/";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "sodium.db";
    static final String TABLE_SODVAL = "sodVal";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_VALUE = "value";
    static final String CREATE_SODVAL_TABLE = "create table sodVal (_id integer primary key, name text, value numeric);";

    Context myContext;
    MyDBHandler DBHelper = null;
    SQLiteDatabase db = null;

    public DBAdapter(Context cxt){
        this.myContext = cxt;
        //http://stackoverflow.com/questions/18147354/sqlite-connection-leaked-although-everything-closed/18148718#18148718
        DBHelper = new MyDBHandler(myContext);

    }

    public class MyDBHandler extends SQLiteOpenHelper {

        private MyDBHandler sInstance;

        public  synchronized MyDBHandler getInstance(Context context) {
            if (sInstance == null) {
                sInstance = new MyDBHandler(context.getApplicationContext());
            }
            return sInstance;
        }
        private MyDBHandler(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_SODVAL_TABLE);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IS EXISTS " + TABLE_SODVAL);
            onCreate(db);
        }


        public void createDatabse() {
            createDB();
        }

        private void createDB() {
            boolean dbExist = DBExists();

            if (!dbExist) {
                this.getReadableDatabase();
                copyDBFromResource();
            }
        }

        private boolean DBExists() {
            try {
                String dbsPath = DATABASE_PATH + DATABASE_NAME;
                SQLiteDatabase checkdb = null;
                checkdb = SQLiteDatabase.openDatabase(dbsPath, null, SQLiteDatabase.OPEN_READWRITE);
                checkdb.setLocale(Locale.getDefault());
                checkdb.setLockingEnabled(true);
                checkdb.setVersion(1);
            } catch (SQLException e) {
                Log.e("myDBHandler", "database not found");
            }
            return db != null ? true : false;
        }


        private void copyDBFromResource() {
            InputStream is = null;
            OutputStream os = null;
            String dbFilePath = DATABASE_PATH + DATABASE_NAME;

            try {
                is = myContext.getAssets().open(DATABASE_NAME);
                os = new FileOutputStream(dbFilePath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                throw new Error("Problem at copying dbs from res file");
            }
        }
    }


    public DBAdapter open() throws SQLException {
        DBHelper.getInstance(myContext);
        db = DBHelper.getWritableDatabase();
        return this;
    }



    public Cursor getAllItems() {
        return db.query(TABLE_SODVAL, new String[]{COLUMN_NAME, COLUMN_VALUE}, null, null, null, null, null);

    }


}
