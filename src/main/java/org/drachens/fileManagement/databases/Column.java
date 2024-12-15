package org.drachens.fileManagement.databases;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Column {
    private String msg;
    private final String name;
    private Table table;
    private int columnNumber;

    public Column(String name) {
        this.name = name;
    }

    public void setTable(Table table) {
        this.table = table;
        msg = table.getUpdateMsg() + name + " = ? WHERE " + table.getPrimaryKeyName() + " = \"";
    }

    public void setColumnNumber(int i) {
        this.columnNumber = i;
    }

    public void setValue(PreparedStatement ptsmt, Object object) throws SQLException {
        ptsmt.setObject(columnNumber, object);
    }

    public void onlySetThisValue(Object object, String identifier) {
        String newMsg = msg + identifier + "\"";
        try {
            PreparedStatement stmt = table.getDatabase().getConnection().prepareStatement(newMsg);
            stmt.setObject(1, object);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
