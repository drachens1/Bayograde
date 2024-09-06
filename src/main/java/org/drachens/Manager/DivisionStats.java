package org.drachens.Manager;

import org.drachens.dataClasses.Armys.DivisionType;

import java.util.HashMap;

public class DivisionStats {
    private HashMap<String,DivisionType> divisionDesignHashMap = new HashMap<>();
    public void registerDivision(DivisionType divisionDesign){
        divisionDesignHashMap.put(divisionDesign.getName(),divisionDesign);
    }
    public void unregisterDivision(DivisionType divisionDesign){
        divisionDesignHashMap.remove(divisionDesign.getName(),divisionDesign);
    }
    public DivisionType getDevision(String name){
        return divisionDesignHashMap.get(name);
    }
}
