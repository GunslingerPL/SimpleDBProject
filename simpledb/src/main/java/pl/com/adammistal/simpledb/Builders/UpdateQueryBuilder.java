package pl.com.adammistal.simpledb.Builders;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.HashMap;

import pl.com.adammistal.simpledb.Interfaces.IUpdateCallback;


/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class UpdateQueryBuilder {
    private String tableName;
    private String whereText;
    private ContentValues cv;
    private SQLiteOpenHelper helper;
    private HashMap<String, AsyncTask> workers;


    public UpdateQueryBuilder(String tableName, SQLiteOpenHelper helper, HashMap<String, AsyncTask> workers) {
        this.tableName = tableName;
        this.helper = helper;
        this.workers = workers;
    }

    public UpdateQueryBuilder whith(ContentValues cv) {
        this.cv = cv;
        return this;
    }

    public UpdateQueryBuilder where(String whereText) {
        this.whereText = whereText;
        return this;
    }


    public void execute(final IUpdateCallback callback, final String TAG) {

        AsyncTask worker = workers.get(TAG);
        if (worker != null)
            return;


        worker = new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    String table = (String) objects[0];
                    ContentValues cv = (ContentValues) objects[1];
                    String whereT = (String) objects[2];
                    long result = helper.getWritableDatabase().update(table, cv, whereT, null);
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
                        callback.onUpdateFinished((Integer) result);
                } else {
                    callback.onError(result);
                }

            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                helper = null;
                if (workers != null)
                    workers.remove(TAG);
            }

            @Override
            protected void onCancelled(Object aLong) {
                super.onCancelled(aLong);
                helper = null;
                if (workers != null)
                    workers.remove(TAG);
            }
        };
        workers.put(TAG, worker);
        worker.execute(tableName, cv, whereText);

    }
}
