package org.drachens.fileManagement.customTypes.player;

import org.drachens.fileManagement.filetypes.GsonStringMaker;

public class SaveFile extends GsonStringMaker {
    public SaveFile(String json) {
        super(json);
    }

    @Override
    protected void initialLoad() {

    }

    @Override
    protected void setDefaults() {

    }
}
