package org.drachens.fileManagement;

import dev.ng5m.CPlayer;
import org.drachens.fileManagement.databases.Entry;
import org.drachens.fileManagement.databases.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.drachens.util.Messages.getTime;

public class PlayerInfoEntry implements Entry {
    private final Table table;
    private final String uuid;
    private final String name;
    private Long playtime;
    private int gold;
    private List<String> permissions;
    private List<String> cosmetics;
    private List<String> achievements;

    public PlayerInfoEntry(CPlayer p, Table table){
        this.uuid= String.valueOf(p.getUuid());
        this.name=p.getUsername();
        this.table=table;
        insert();
    }

    public void setPlaytime(Long playtime){
        this.playtime=playtime;
        table.getColumn("playtime").onlySetThisValue(playtime,uuid);
    }

    public void setGold(int gold){
        this.gold=gold;
        table.getColumn("gold").onlySetThisValue(gold,uuid);
    }

    public void setPermissions(ArrayList<String> permissions){
        this.permissions=permissions;
        table.getColumn("permissions").onlySetThisValue(permissions,uuid);
    }

    public void setCosmetics(ArrayList<String> cosmetics){
        this.cosmetics=cosmetics;
        table.getColumn("cosmetics").onlySetThisValue(cosmetics,uuid);
    }

    public void setAchievements(ArrayList<String> achievements){
        this.achievements=achievements;
        table.getColumn("achievements").onlySetThisValue(achievements,uuid);
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public List<String> getCosmetics() {
        return cosmetics;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public int getGold() {
        return gold;
    }

    public Long getPlaytime() {
        return playtime;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public void insert() {
        String checkSql = "SELECT * FROM " + table.getTableName() + " WHERE uuid = \""+uuid+"\";";
        boolean insertRequired = false;
        System.out.println(checkSql);

        try (PreparedStatement stmt = table.getDatabase().getConnection().prepareStatement(checkSql)) {
            stmt.setString(1, uuid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    insertRequired = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (insertRequired) {
            String insertSql = "INSERT INTO " + table.getTableName() +
                    " (uuid, name, last_online, first_joined, playtime, gold, permissions, cosmetics, achievements) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = table.getDatabase().getConnection().prepareStatement(insertSql)) {
                insertStatement.setString(1, uuid);
                insertStatement.setString(2, name);
                insertStatement.setString(3, getTime());
                insertStatement.setString(4, getTime());
                insertStatement.setLong(5, 0);
                insertStatement.setInt(6, 0);
                insertStatement.setString(7, "");
                insertStatement.setString(8, "");
                insertStatement.setString(9, "");
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.gold = 0;
            this.playtime = 0L;
            this.cosmetics = new ArrayList<>();
            this.achievements = new ArrayList<>();
            this.permissions = new ArrayList<>();
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
                this.playtime = resultSet.getLong("playtime");
                this.gold = resultSet.getInt("gold");

                String permissionsString = resultSet.getString("permissions");
                this.permissions = new ArrayList<>(Arrays.asList(permissionsString.split(",")));

                String cosmeticsString = resultSet.getString("cosmetics");
                this.cosmetics = new ArrayList<>(Arrays.asList(cosmeticsString.split(",")));

                String achievementsString = resultSet.getString("achievements");
                this.achievements = new ArrayList<>(Arrays.asList(achievementsString.split(",")));

                table.getColumn("last_online").onlySetThisValue(getTime(),uuid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getIdentifier() {
        return uuid;
    }
}
