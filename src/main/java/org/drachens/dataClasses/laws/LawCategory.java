package org.drachens.dataClasses.laws;

import com.google.gson.JsonElement;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LawCategory implements Saveable {
    private final String identifier;
    private final List<Law> laws;
    private final HashMap<String, Law> lawMap;
    private final Country country;
    private Law current;

    private LawCategory(Create create) {
        this.lawMap = create.lawMap;
        this.laws = create.laws;
        this.current = lawMap.get(create.defaultLaw);
        this.identifier = create.identifier;
        country = null;
    }

    public LawCategory(LawCategory lawCategory, Country country) {
        this.lawMap = lawCategory.getLawMap();
        this.laws = lawCategory.laws;
        this.country = country;
        this.current = lawCategory.getCurrent();
        this.identifier = lawCategory.getIdentifier();
    }

    public List<Law> getLawsStuff() {
        return laws;
    }

    public HashMap<String, Law> getLawMap() {
        return lawMap;
    }

    public Set<String> getLaws() {
        return lawMap.keySet();
    }

    public boolean canApply(String identifier) {
        Law law = lawMap.get(identifier);
        return law.isAvailable() == null || law.isAvailable().apply(country);
    }

    public Law getCurrent() {
        return current;
    }

    public void setCurrent(String identifier) {
        setCurrent(lawMap.get(identifier));
    }

    public void setCurrent(Law law) {
        if (!(law.isAvailable() == null || law.isAvailable().apply(country))) return;
        country.removeModifier(current.modifier());
        country.addModifier(law.modifier());
        this.current = law;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Law getLaw(String name) {
        return lawMap.get(name);
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    public static class Create {
        private final List<Law> laws = new ArrayList<>();
        private final HashMap<String, Law> lawMap = new HashMap<>();
        private final String identifier;
        private String defaultLaw;

        public Create(String identifier) {
            this.identifier = identifier;
        }

        public Create addLaw(Law law) {
            lawMap.put(law.identifier(), law);
            laws.add(law);
            return this;
        }

        public Create setDefault(String law) {
            defaultLaw = law;
            return this;
        }

        public LawCategory build() {
            return new LawCategory(this);
        }
    }
}
