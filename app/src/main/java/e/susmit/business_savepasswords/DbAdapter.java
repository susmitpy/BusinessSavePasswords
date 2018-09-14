package e.susmit.business_savepasswords;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
    public static int dbversion = 1;
    public static String dbname = "StorageDB";
    public static String pT = "passTable";

    private static class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context){
            super(context, dbname, null, dbversion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + pT + " (PASS TEXT, SECKEY TEXT) ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + pT);
            onCreate(db);
        }
    }

    private final Context c;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlDb;

    public DbAdapter(Context context) {
        this.c = context;
    }
    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(c);
        sqlDb = dbHelper.getWritableDatabase();
        return this;
    }

    public void insert(String pin, String secKey) {
            sqlDb.execSQL("DELETE FROM " + pT);
            sqlDb.execSQL("INSERT INTO " + pT + " (PASS, SECKEY) VALUES('" + pin + "', '" +  secKey + "')");
    }


    public Cursor GetConf(String s)
    { Cursor cursor;

    if (s.equals("pass")) {

        String query = "SELECT PASS FROM " + pT;
        cursor = sqlDb.rawQuery(query, null);
    }else if (s.equals("seckey")){
        String query = "SELECT SECKEY FROM " + pT;
        cursor = sqlDb.rawQuery(query, null);
    }else{
        cursor = null;
    }
       return cursor;
    }
}
