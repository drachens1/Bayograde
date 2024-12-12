package org.drachens.fileManagement.databases;

public interface Entry {
    void load();
    void insert();
    String getIdentifier();
}
