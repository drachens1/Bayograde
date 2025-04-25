package org.drachens.fileManagement.customTypes.player;

import net.minestom.server.network.player.GameProfile;
import org.drachens.fileManagement.databases.Entry;
import org.drachens.fileManagement.databases.Table;
import org.drachens.player_types.CPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfoEntry implements Entry {
    private final GameProfile gameProfile;
    private final String uuid;
    private final Table table;
    private PlayerJson playerJson;

    public PlayerInfoEntry(GameProfile gameProfile, Table table) {
        this.gameProfile = gameProfile;
        this.uuid = gameProfile.uuid().toString();
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
            System.out.println("Created a new playerJson 1");
            playerJson = new PlayerJson("", gameProfile);
        } else {
            load();
        }
    }


    @Override
    public void load() {
        String sql = "SELECT * FROM " + table.getTableName() + " WHERE uuid = ?";

        try (PreparedStatement stmt = table.getDatabase().getConnection().prepareStatement(sql)) {
            stmt.setString(1, uuid);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String jsonData = resultSet.getString(2);

                if (null == jsonData || jsonData.isEmpty()) {
                    System.out.println("Created a new playerJson 2");
                    playerJson = new PlayerJson("", gameProfile);
                } else {
                    playerJson = new PlayerJson(jsonData, gameProfile);
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

    public void setPlayer(CPlayer p){
        p.setPlayerDataFile(this);
        playerJson.setPlayer(p);
    }
}
