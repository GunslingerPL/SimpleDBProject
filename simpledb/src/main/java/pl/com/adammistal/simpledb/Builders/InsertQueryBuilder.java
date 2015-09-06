package pl.com.adammistal.simpledb.Builders;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.HashMap;

import pl.com.adammistal.simpledb.Interfaces.IInsertCallback;


/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class InsertQueryBuilder {
    private String tableName;
    private ContentValues cv;
    private SQLiteOpenHelper helper;
    private HashMap<String, AsyncTask> workers;


    public InsertQueryBuilder(ContentValues cv, SQLiteOpenHelper helper) {
        this.cv = cv;
        this.helper = helper;
    }

    public InsertQueryBuilder toTable(String tableName) {
        this.tableName = tableName;
        return InsertQueryBuilder.this;
    }


    public void execute(final IInsertCallback callback, final String TAG) {

        AsyncTask worker = workers.get(TAG);
        if (worker != null)
            return;


        worker = new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    String table = (String) objects[0];
                    ContentValues cv = (ContentValues) objects[1];
                    long result = helper.getWritableDatabase().insert(table, null, cv);
                    helper.getReadableDatabase().close();
                    helper = null;


                    return result;
                } catch (Exception e) {
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                if (workers != null)
                    workers.remove(TAG);
                if (result instanceof Long) {
                    if (callback != null)
                        callback.onInsertFinished((Long) result);
                } else {
                    callback.onError(result);
                }

            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if (workers != null)
                    workers.remove(TAG);
                workers = null;
                helper = null;
            }

            @Override
            protected void onCancelled(Object aLong) {
                super.onCancelled(aLong);
                if (workers != null)
                    workers.remove(TAG);
                workers = null;
                helper = null;
            }
        };
        workers.put(TAG, worker);
        worker.execute(tableName, cv);

    }
}
