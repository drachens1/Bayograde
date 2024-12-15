package org.drachens.fileManagement.databases;

public enum DataTypeEum {
    LONG("bigint"),
    STRING("varchar(255)"),
    INTEGER("int");

    private final String name;

    DataTypeEum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
