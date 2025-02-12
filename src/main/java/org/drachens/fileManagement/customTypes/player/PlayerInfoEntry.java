package org.drachens.fileManagement.customTypes.player;

import org.drachens.fileManagement.databases.Entry;
import org.drachens.fileManagement.databases.Table;
import org.drachens.player_types.CPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfoEntry implements Entry {
    private final CPlayer p;
    private final String uuid;
    private final Table table;
    private PlayerJson playerJson;

    public PlayerInfoEntry(CPlayer p, Table table) {
        this.p = p;
        this.uuid= p.getUuid().toString();
        p.setPlayerDataFile(this);
        this.table = table;
        insert();
    }

    @Override
    public void insert() {
        String checkSql = "SELECT * FROM " + table.getTableName() + " WHERE uuid = \"" + uuid + "\";";
        boolean insertRequired = false;

        try (PreparedStatement stmt = table.getDatabase().getConnection().prepareStatement(checkSql)) {
            stmt.setString(1, uuid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    insertRequired = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting 1: " + e.getMessage());
            return;
        }

        if (insertRequired) {
            String insertSql = "INSERT INTO " + table.getTableName() +
                    " (uuid, data) " +
                    "VALUES (?, ?)";
            try (PreparedStatement insertStatement = table.getDatabase().getConnection().prepareStatement(insertSql)) {
                insertStatement.setString(1, uuid);
                insertStatement.setString(2,"");
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting the info " + e.getMessage());
            }
            playerJson = new PlayerJson("",p);
        } else {
            load();
        }
    }


    public void load() {
        String sql = "SELECT * FROM " + table.getTableName() + " WHERE uuid = ?";

        try (PreparedStatement stmt = table.getDatabase().getConnection().prepareStatement(sql)) {
            stmt.setString(1, uuid);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String jsonData = resultSet.getString(2);

                if (jsonData==null||jsonData.isEmpty()){
                    playerJson = new PlayerJson("",p);
                }else {
                    playerJson = new PlayerJson(jsonData,p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading from the database: " + e.getMessage());
        }
    }


    @Override
    public String getIdentifier() {
        return uuid;
    }

    @Override
    public void applyChanges() {
        String updateSql = "UPDATE " + table.getTableName() +
                " SET data = ? WHERE uuid = ?";

        try (PreparedStatement updateStatement = table.getDatabase().getConnection().prepareStatement(updateSql)) {
            updateStatement.setString(1, playerJson.saveToJson());
            updateStatement.setString(2, uuid);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error applying changes: " + e.getMessage());
        }
    }
}
