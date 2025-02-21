package org.drachens.fileManagement.databases;

import lombok.Getter;

@Getter
public enum DataTypeEum {
    LONG("bigint"),
    BIGSTRING("TEXT"),
    STRING("varchar(255)"),
    INTEGER("int");

    private final String name;

    DataTypeEum(String name) {
        this.name = name;
    }

}
