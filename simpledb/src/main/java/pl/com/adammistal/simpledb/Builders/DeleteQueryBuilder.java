package pl.com.adammistal.simpledb.Builders;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.HashMap;

import pl.com.adammistal.simpledb.Interfaces.IDeleteCallback;


/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class DeleteQueryBuilder {
    private String tableName;
    private String whereText;
    private ContentValues cv;
    private SQLiteOpenHelper helper;
    private HashMap<String, AsyncTask> workers;


    public DeleteQueryBuilder(String tableName, SQLiteOpenHelper helper, HashMap<String, AsyncTask> workers) {
        this.tableName = tableName;
        this.helper = helper;
        this.workers = workers;
    }

    public void execute(final IDeleteCallback callback, final String TAG) {

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
                    int result = helper.getWritableDatabase().delete(table, whereT, null);
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
                if (result instanceof Integer) {
                    if (callback != null)
                        callback.onDeleteFinished((Integer) result);
                } else {
                    callback.onError(result);
                }

                workers = null;

            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                helper = null;
                if (workers != null)
                    workers.remove(TAG);
                workers = null;
                helper = null;
            }

            @Override
            protected void onCancelled(Object aLong) {
                super.onCancelled(aLong);
                helper = null;
                if (workers != null)
                    workers.remove(TAG);
                workers = null;
                helper = null;
            }
        };

        workers.put(TAG, worker);
        worker.execute(tableName, cv, whereText);

    }

    public DeleteQueryBuilder rowsWhere(String whereText) {
        this.whereText = whereText;
        return this;
    }


}
