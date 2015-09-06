package pl.com.adammistal.simpledb.Interfaces;

import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public interface ISQLiteOpenHelper {

    void onCreate(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion);
}
