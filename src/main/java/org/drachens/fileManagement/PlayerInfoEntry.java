package org.drachens.fileManagement;

import dev.ng5m.CPlayer;
import org.drachens.fileManagement.databases.Entry;
import org.drachens.fileManagement.databases.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.drachens.util.Messages.getTime;

public class PlayerInfoEntry implements Entry {
    private final CPlayer p;
    private final Table table;
    private final String uuid;
    private final String name;
    private final HashMap<String, Integer> eventAchievementTrigger = new HashMap<>();
    private Long playtime;
    private int gold;
    private List<String> permissions;
    private List<String> cosmetics;

    public PlayerInfoEntry(CPlayer p, Table table) {
        this.p = p;
        p.setPlayerDataFile(this);
        this.uuid = String.valueOf(p.getUuid());
        this.name = p.getUsername();
        this.table = table;
        insert();
    }

    public void addCosmetic(String toAdd) {
        cosmetics.add(toAdd);
    }

    public void removeCosmetic(String toAdd) {
        cosmetics.remove(toAdd);
    }

    public HashMap<String, Integer> getEventAchievementTrigger() {
        return eventAchievementTrigger;
    }

    public void addAchievementEventTriggered(String identifier, int count) {
        eventAchievementTrigger.put(identifier, count);
    }

    public List<String> getCosmetics() {
        return cosmetics;
    }

    public void setCosmetics(ArrayList<String> cosmetics) {
        this.cosmetics = cosmetics;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public Long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Long playtime) {
        this.playtime = playtime;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
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
                    " (uuid, name, last_online, first_joined, playtime, gold, permissions, cosmetics, event_count) " +
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
                insertStatement.setString(10, "");
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting the info " + e.getMessage());
            }

            this.gold = 0;
            this.playtime = 0L;
            this.cosmetics = new ArrayList<>();
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
                p.setPlayTime(playtime);
                this.gold = resultSet.getInt("gold");
                p.setGold(gold);

                String permissionsString = resultSet.getString("permissions");
                this.permissions = new ArrayList<>(Arrays.asList(permissionsString.split(",")));
                permissions.forEach(permission -> p.addPermission(permission));


                String cosmeticsString = resultSet.getString("cosmetics");
                this.cosmetics = new ArrayList<>(Arrays.asList(cosmeticsString.split(",")));
                cosmetics.forEach(p::addCosmetic);

                String achievementEventString = resultSet.getString("event_count");
                if (achievementEventString != null) {
                    String[] events = achievementEventString.split(",");
                    for (String event : events) {
                        String[] parts = event.split(":");
                        if (parts.length == 2) {
                            eventAchievementTrigger.put(parts[0], Integer.parseInt(parts[1]));
                        }
                    }
                }


                table.getColumn("last_online").onlySetThisValue(getTime(), uuid);
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
                " SET playtime = ?, gold = ?, permissions = ?, cosmetics = ? , event_count = ?" +
                "WHERE uuid = ?";

        try (PreparedStatement updateStatement = table.getDatabase().getConnection().prepareStatement(updateSql)) {
            updateStatement.setLong(1, playtime);
            updateStatement.setInt(2, gold);
            updateStatement.setString(3, String.join(",", permissions));
            updateStatement.setString(4, String.join(",", cosmetics));

            StringBuilder eventCountBuilder = new StringBuilder();
            for (Map.Entry<String, Integer> entry : eventAchievementTrigger.entrySet()) {
                eventCountBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            }
            String eventCountString = eventCountBuilder.toString();
            if (eventCountString.endsWith(",")) {
                eventCountString = eventCountString.substring(0, eventCountString.length() - 1);
            }
            updateStatement.setString(5, eventCountString);
            updateStatement.setString(6, uuid);

            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error applying changes: " + e.getMessage());
        }
    }
}
