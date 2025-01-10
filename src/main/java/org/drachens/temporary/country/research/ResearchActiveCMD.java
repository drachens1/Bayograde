package org.drachens.temporary.country.research;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.temporary.research.ResearchCountry;

public class ResearchActiveCMD extends Command {
    public ResearchActiveCMD() {
        super("currently-active");

        setCondition((sender,s)->!notCountry(sender));
        setDefaultExecutor((sender,context)->{
           if (notCountry(sender))return;
           CPlayer p = (CPlayer) sender;
           ResearchCountry country = (ResearchCountry) p.getCountry();
           p.sendMessage(country.getCurrentResearch().getName());
        });
    }

    private boolean notCountry(CommandSender sender){
        if (sender instanceof CPlayer p){
            return p.getCountry()==null;
        }
        return true;
    }
}
