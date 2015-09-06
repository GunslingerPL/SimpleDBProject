package pl.com.adammistal.simpledb.Builders.StringBuilders;

import android.util.Log;

/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class TableStatementBuilder {
    private StringBuilder sb;

    public TableStatementBuilder(String text) {
        sb = new StringBuilder(text);
    }

    public TableStatementBuilder addColumn(String coulmnName, String columnType) {
        sb.append(coulmnName + " " + columnType + " ,");
        return this;
    }

    public String build() {
        sb.deleteCharAt(sb.length() - 1).append(")");
        String result = sb.toString();
        sb.setLength(0);
        Log.d("Builder", result);
        return result;
    }
}
