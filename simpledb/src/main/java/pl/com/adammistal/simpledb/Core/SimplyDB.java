package pl.com.adammistal.simpledb.Core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import pl.com.adammistal.simpledb.Builders.DeleteQueryBuilder;
import pl.com.adammistal.simpledb.Builders.InsertQueryBuilder;
import pl.com.adammistal.simpledb.Builders.SelectQueryBuilder;
import pl.com.adammistal.simpledb.Builders.UpdateQueryBuilder;
import pl.com.adammistal.simpledb.Interfaces.ISQLiteOpenHelper;


/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class SimplyDB {
    private static SimplyDB instance;
    private final String TAG = "SimplyDB";
    private final SQLiteOpenHelper helper;
    private final HashMap<String, AsyncTask> workers;

    private SimplyDB(Context context, String dbName, int versionDB, final ISQLiteOpenHelper callback) {
        synchronized (SimplyDB.this) {
            workers = new HashMap<>();
            helper = new SQLiteOpenHelper(context, dbName, null, versionDB) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    Log.d(TAG, "onCreate");
                    callback.onCreate(db);
                }

                @Override
                public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                    Log.d(TAG, "onUpgrade");
                    callback.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
                }
            };
        }

    }


    public static void init(Context context, String dbName, int versionDB, final ISQLiteOpenHelper callback) {
        if (instance == null) {
            instance = new SimplyDB(context, dbName, versionDB, callback);
        }
    }

    public static SimplyDB getInstance() {
        return instance;
    }

    public InsertQueryBuilder insert(ContentValues cv) {
        return new InsertQueryBuilder(cv, helper);
    }

    public SelectQueryBuilder select(String query) {
        return new SelectQueryBuilder(query, helper, workers);
    }

    public UpdateQueryBuilder update(String tableName) {
        return new UpdateQueryBuilder(tableName, helper, workers);
    }

    public DeleteQueryBuilder deleteFrom(String tableName) {
        return new DeleteQueryBuilder(tableName, helper, workers);
    }

    public void cancelExecution(String TAG) {
        AsyncTask toremove = null;
        for (Map.Entry<String, AsyncTask> task : workers.entrySet()) {
            if (task.getKey().equals(TAG)) {
                toremove = task.getValue();
            }
        }
        if (toremove != null) {
            toremove.cancel(true);
            workers.remove(toremove);
            Log.d(TAG, "cancelExecution " + TAG);
        }


    }

}
