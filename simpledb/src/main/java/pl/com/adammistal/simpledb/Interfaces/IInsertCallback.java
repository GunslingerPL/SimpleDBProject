package pl.com.adammistal.simpledb.Interfaces;

/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public interface IInsertCallback {
    void onInsertFinished(long rowId);

    void onError(Object obj);
}
