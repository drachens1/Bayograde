package org.drachens.fileManagement.customTypes;

import org.drachens.fileManagement.YamlFileType;

public class PermissionsFile extends YamlFileType {
    public PermissionsFile(String name, String path) {
        super(name, path);
    }

    @Override
    protected void initialLoad() {

    }

    @Override
    protected void setDefaults() {

    }
}
