package pl.com.adammistal.simpledb.Interfaces;

import android.database.Cursor;

/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public interface ICursorCallback<T> {
    T onParseCursor(Cursor cursor);

    void onExecuted(T object);

    void onCanceled();

    void onError(Throwable object);
}
