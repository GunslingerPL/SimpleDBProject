package pl.com.adammistal.simpledb.Builders;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

import pl.com.adammistal.simpledb.Interfaces.ICursorCallback;


/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class SelectQueryBuilder {
    private String tableName;
    private String query;
    private SQLiteOpenHelper helper;
    private HashMap<String, AsyncTask> workers;
    private String whereText;

    public SelectQueryBuilder(String query, SQLiteOpenHelper helper, HashMap<String, AsyncTask> workers) {
        this.query = query;
        this.helper = helper;
        this.workers = workers;
    }

    public SelectQueryBuilder fromTable(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SelectQueryBuilder where(String whereText) {
        this.whereText = whereText;
        return this;
    }

    public void execute(final ICursorCallback callback, final String TAG) {

        AsyncTask worker = workers.get(TAG);
        if (worker != null)
            return;

        worker = new AsyncTask<String, Void, Object>() {
            private boolean dispachCancelation;

            @Override
            protected Object doInBackground(String... objects) {
                try {
                    String query = objects[0];
                    Cursor result = helper.getReadableDatabase().rawQuery(query, null);
                    Object r = callback.onParseCursor(result);
                    result.close();
                    helper.getReadableDatabase().close();
                    return r;
                } catch (Throwable e) {
                    return e;
                }

            }

            @Override
            protected void onPostExecute(Object aVoid) {
                super.onPostExecute(aVoid);
                if (workers != null)
                    workers.remove(TAG);


                Log.d("Builder", "SelectQueryBuilder rows count is " + aVoid + "");
                if (callback != null) {
                    if (aVoid instanceof Throwable) {
                        callback.onError((Throwable) aVoid);

                    } else {
                        callback.onExecuted(aVoid);
                    }
                }


            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                Log.d("Builder", "onCancelled");
                if (workers != null)
                    workers.remove(TAG);
                workers = null;

                callback.onCanceled();
                dispachCancelation = true;
            }

            @Override
            protected void onCancelled(Object aLong) {
                super.onCancelled(aLong);
                Log.d("Builder", "onCancelled long= " + aLong);
                if (workers != null)
                    workers.remove(TAG);
                workers = null;
                if (!dispachCancelation)
                    callback.onCanceled();
            }
        };
        workers.put(TAG, worker);

        String queryText = "SELECT " + query + " FROM " + tableName + " ";
        if (whereText != null)
            queryText += queryText;
        worker.execute(queryText);

    }

}
