package com.github.seraphain.examples.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbyExample {

    public static void main(String[] args) {
        String jdbcDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "target/example";

        // load JDBC driver
        try {
            Class.forName(jdbcDriver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        Statement statement = null;
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement updatePreparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:" + dbName + ";create=true");
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            // statement.execute("drop table EXAMPLE");
            statement.execute("create table EXAMPLE(ID int, NAME varchar(32))");
            System.out.println("Table 'EXAMPLE' created.");

            insertPreparedStatement = connection.prepareStatement("insert into EXAMPLE values (?, ?)");
            insertPreparedStatement.setInt(1, 1);
            insertPreparedStatement.setString(2, "test1");
            insertPreparedStatement.executeUpdate();
            System.out.println("{1, 'test1'} inserted .");

            insertPreparedStatement.setInt(1, 2);
            insertPreparedStatement.setString(2, "test2");
            insertPreparedStatement.executeUpdate();
            System.out.println("{2, 'test2'} inserted.");

            updatePreparedStatement = connection.prepareStatement("update EXAMPLE set ID=?, NAME=? where ID=?");
            updatePreparedStatement.setInt(1, 3);
            updatePreparedStatement.setString(2, "test3");
            updatePreparedStatement.setInt(3, 1);
            updatePreparedStatement.executeUpdate();
            System.out.println("{1, 'test1'} updated to {3, 'test3'}.");

            resultSet = statement.executeQuery("select ID, NAME from EXAMPLE order by ID");

            System.out.println("Query result:");
            System.out.println("ID\tNAME");
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String name = resultSet.getString("NAME");
                System.out.println(id + "\t" + name);
            }

            statement.execute("drop table EXAMPLE");
            System.out.println("Table 'EXAMPLE' dropped.");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeQuietly(resultSet);
            closeQuietly(statement);
            closeQuietly(updatePreparedStatement);
            closeQuietly(insertPreparedStatement);
            closeQuietly(connection);
        }
        // shutdown derby
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if (((e.getErrorCode() == 50000) && ("XJ015".equals(e.getSQLState())))) {
                System.out.println("Derby shut down normally");
            } else {
                System.err.println("Derby did not shut down normally");
                e.printStackTrace();
            }
        }
    }

    private static void closeQuietly(AutoCloseable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
