package pl.com.adammistal.simpledb.Utils;


import pl.com.adammistal.simpledb.Builders.StringBuilders.TableStatementBuilder;

/**
 * Created by Adam Miśtal on 2015-09-06.
 * adam.mistal84@gmail.com
 */
public class QueryUtil {

    public static TableStatementBuilder createTable(String tableName) {
        TableStatementBuilder builder = new TableStatementBuilder("CREATE TABLE " + tableName + " ( ");
        return builder;
    }

}



