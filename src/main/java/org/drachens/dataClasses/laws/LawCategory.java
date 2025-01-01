package org.drachens.dataClasses.laws;

import org.drachens.dataClasses.Countries.Country;

import java.util.HashMap;
import java.util.Set;

public class LawCategory {
    private final String identifier;
    private final HashMap<String, Law> lawMap;
    private Law current;
    private final Country country;
    private LawCategory(Create create){
        this.lawMap=create.lawMap;
        this.current=lawMap.get(create.defaultLaw);
        this.identifier= create.identifier;
        country=null;
    }
    public LawCategory(LawCategory lawCategory, Country country){
        this.lawMap=lawCategory.getLawMap();
        this.country=country;
        this.current=lawCategory.getCurrent();
        this.identifier=lawCategory.getIdentifier();
    }
    public HashMap<String,Law> getLawMap(){
        return lawMap;
    }
    public Set<String> getLaws(){
        return lawMap.keySet();
    }
    public boolean canApply(String identifier){
        Law law = lawMap.get(identifier);
        return law.isAvailable()==null||law.isAvailable().apply(country);
    }
    public Law getCurrent(){
        return current;
    }
    public void setCurrent(String identifier){
        Law law = lawMap.get(identifier);
        if (!(law.isAvailable()==null||law.isAvailable().apply(country)))return;
        country.removeModifier(current.modifier());
        country.addModifier(law.modifier());
        this.current=law;
    }
    public String getIdentifier(){
        return identifier;
    }

    public static class Create {
        private final HashMap<String, Law> lawMap = new HashMap<>();
        private final String identifier;
        private String defaultLaw;
        public Create(String identifier){
            this.identifier=identifier;
        }
        public Create addLaw(Law law){
            lawMap.put(law.identifier(),law);
            return this;
        }
        public Create setDefault(String law){
            defaultLaw=law;
            return this;
        }
        public LawCategory build(){
            return new LawCategory(this);
        }
    }
}
