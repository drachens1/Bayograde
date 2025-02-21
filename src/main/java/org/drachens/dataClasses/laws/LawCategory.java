package org.drachens.dataClasses.laws;

import com.google.gson.JsonElement;
import lombok.Getter;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Getter
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
        this.lawMap = lawCategory.lawMap;
        this.laws = lawCategory.laws;
        this.country = country;
        this.current = lawCategory.current;
        this.identifier = lawCategory.identifier;
    }

    public List<Law> getLawsStuff() {
        return laws;
    }

    public Set<String> getLaws() {
        return lawMap.keySet();
    }

    public boolean canApply(String identifier) {
        Law law = lawMap.get(identifier);
        return null == law.isAvailable() || law.isAvailable().apply(country);
    }

    public void setCurrent(String identifier) {
        setCurrent(lawMap.get(identifier));
    }

    public void setCurrent(Law law) {
        if (!(null == law.isAvailable() || law.isAvailable().apply(country))) return;
        country.removeModifier(current.modifier());
        country.addModifier(law.modifier());
        this.current = law;
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
