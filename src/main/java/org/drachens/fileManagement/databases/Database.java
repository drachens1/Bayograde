package org.drachens.fileManagement.databases;

import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Database {
    private final String database;
    private final HashMap<String, Table> tableHashMap = new HashMap<>();
    private Connection connection;

    public Database(String database, String host, int port, String user, String password) {
        this.database=database;
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+database+"?user="+user+"&password="+password);
        } catch (Exception e) {
            System.err.println("Error connecting to the database "+e.getMessage());
        }
    }

    public Table getTable(String s){
        return tableHashMap.get(s);
    }

    public void addTable(Table table){
        table.setDatabase(this);
        tableHashMap.put(table.getTableName(), table);
    }

    public void createTable(Table table) {
        try {
            String createStatement = table.getCreateStatement();
            System.out.println(createStatement);
            org.mariadb.jdbc.Statement stmt = getConnection().createStatement();
            stmt.execute(createStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addTable(table);
    }

    public String getName(){
        return database;
    }

    public Connection getConnection(){
        return connection;
    }
}
