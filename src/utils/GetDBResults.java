package utils;

import java.sql.ResultSet;

@FunctionalInterface
public interface GetDBResults {
    ResultSet getResults(String query);
}
