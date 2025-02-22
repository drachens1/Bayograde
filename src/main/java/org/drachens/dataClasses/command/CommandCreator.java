package org.drachens.dataClasses.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.condition.CommandCondition;
import org.jetbrains.annotations.NotNull;

public class CommandCreator {
    private final Command command;
    public CommandCreator(@NotNull String name) {
        command = new Command(name);
    }
    public CommandCreator(@NotNull String name, @NotNull String... alias) {
        command = new Command(name,alias);
    }
    public CommandCreator setCondition(CommandCondition condition){
        command.setCondition(condition);
        return this;
    }
    public CommandCreator addSyntax(@NotNull CommandExecutor executor, Argument<?>... args){
        command.addSyntax(executor,args);
        return this;
    }
    public CommandCreator setDefaultExecutor(@NotNull CommandExecutor executor){
        command.setDefaultExecutor(executor);
        return this;
    }
    public CommandCreator addSubCommand(Command command){
        command.addSubcommand(command);
        return this;
    }
    public Command build(){
        return command;
    }
}
