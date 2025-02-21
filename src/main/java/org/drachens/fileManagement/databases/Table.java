package org.drachens.fileManagement.databases;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Table {
    private final String tableName;
    private final String createStatement;
    private final String updateMsg;
    private final String primaryKeyName;
    private Database database;

    private Table(String tableName, String createStatement, String primaryKeyName) {
        this.tableName = tableName;
        this.createStatement = createStatement;
        this.primaryKeyName = primaryKeyName;
        updateMsg = "UPDATE " + tableName + " SET ";
    }

    public static class Create {
        private final StringBuilder createStatementBuilder;
        private final String tableName;
        private String primaryKey;

        public Create(String tableName) {
            this.tableName = tableName;
            this.createStatementBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        }

        public Create addColumn(String name, DataTypeEum dataType, boolean primaryKey, boolean notNull) {
            createStatementBuilder.append(name)
                    .append(' ')
                    .append(dataType.getName());

            if (primaryKey) {
                this.primaryKey = name;
            }
            if (notNull) {
                createStatementBuilder.append(" NOT NULL ");
            }

            createStatementBuilder.append(", ");
            return this;
        }

        public Create addColumn(String name, DataTypeEum dataType) {
            createStatementBuilder.append(name)
                    .append(' ')
                    .append(dataType.getName());

            createStatementBuilder.append(", ");
            return this;
        }


        public Table build() {
            int length = createStatementBuilder.length();
            if (null != this.primaryKey) {
                createStatementBuilder.append(" PRIMARY KEY (").append(primaryKey).append("));");
            } else {
                createStatementBuilder.replace(length - 2, length, ");");
            }
            return new Table(tableName, createStatementBuilder.toString(), primaryKey);
        }
    }
}
