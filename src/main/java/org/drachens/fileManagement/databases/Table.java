package org.drachens.fileManagement.databases;

import java.util.HashMap;

public class Table {
    private Database database;
    private final String tableName;
    private final String createStatement;
    private final HashMap<String, Column> columns;
    private final String updateMsg;
    private final String primaryKeyName;

    private Table(String tableName, String createStatement, HashMap<String, Column> columns, String primaryKeyName) {
        this.tableName = tableName;
        this.createStatement = createStatement;
        this.columns = columns;
        this.primaryKeyName = primaryKeyName;
        updateMsg = "UPDATE " + tableName + " SET ";
        columns.forEach((key, value) -> {
            value.setTable(this);
            value.setColumnNumber(createStatement.indexOf(key));
        });
    }

    public Column getColumn(String identifier) {
        if (!columns.containsKey(identifier)) System.err.println(identifier + " not in columns array");
        return columns.get(identifier);
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getCreateStatement() {
        return createStatement;
    }

    public String getTableName() {
        return tableName;
    }

    public Database getDatabase() {
        return database;
    }

    public String getUpdateMsg() {
        return updateMsg;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public static class Create {
        private String primaryKey = null;
        private final StringBuilder createStatementBuilder;
        private final String tableName;
        private final HashMap<String, Column> columns = new HashMap<>();

        public Create(String tableName) {
            this.tableName = tableName;
            this.createStatementBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        }

        public Create addColumn(String name, DataTypeEum dataType, boolean primaryKey, boolean notNull) {
            createStatementBuilder.append(name)
                    .append(" ")
                    .append(dataType.getName());

            if (primaryKey) {
                this.primaryKey = name;
            }
            if (notNull) {
                createStatementBuilder.append(" NOT NULL ");
            }

            columns.put(name, new Column(name));
            createStatementBuilder.append(", ");
            return this;
        }

        public Create addColumn(String name, DataTypeEum dataType) {
            createStatementBuilder.append(name)
                    .append(" ")
                    .append(dataType.getName());

            columns.put(name, new Column(name));
            createStatementBuilder.append(", ");
            return this;
        }


        public Table build() {
            int length = createStatementBuilder.length();
            if (primaryKey != null) {
                createStatementBuilder.append(" PRIMARY KEY (").append(primaryKey).append("));");
            } else {
                createStatementBuilder.replace(length - 2, length, ");");
            }
            return new Table(tableName, createStatementBuilder.toString(), columns, primaryKey);
        }
    }
}
