package me.mimic.lib.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Command {

    //TODO: look up annotations instead of builder

    private CommandGroup group;
    private String name;
    private boolean playerOnly;
    private ArrayList<CommandParameter> parameters;
    private BiConsumer<CommandSender, Object[]> function;
    private BiFunction<CommandSender, String[], List<String>> completionFunction;

    public Command(CommandGroup group, String name) {
        this.group = group;
        this.name = name;
        this.parameters = new ArrayList<>();
        this.playerOnly = false;
        this.function = (sender, objects) -> {};
        this.completionFunction = (sender, strings) -> { return Collections.<String>emptyList(); };
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        String params = this.parameters.stream()
                .map(param -> param.getLabel())
                .collect(Collectors.joining("> <"));
        return "/" + group.getName() + " " + this.name + " <" + params + ">";
    }

    public boolean execute(CommandSender sender, String[] strings) {
        if(!(sender instanceof Player) && this.playerOnly) {
            sender.sendMessage("§cError, this command can only be used by players!");
            return false;
        }
        Object[] objects = new Object[this.parameters.size()];
        for(int i = 0; i < this.parameters.size(); i++) {
            if(i >= strings.length && !this.parameters.get(i).hasDefault()) { // Missing a parameter that is not optional.
                sender.sendMessage("§cInvalid syntax! " + getUsage());
                return false;
            }
            CommandParameter param = this.parameters.get(i);
            if(i < strings.length && !param.getType().isValid(strings[i])) {
                sender.sendMessage("§cError, parameter " + param.getLabel() + " is incorrect!");
                return false;
            }
            objects[i] = (i >= strings.length) ? param.getDefault() : param.getType().parse(strings[i]); // Add parsed or default value.
        }
        this.function.accept(sender, objects);
        return true;
    }

    public List<String> complete(CommandSender sender, String[] strings) {
        return this.completionFunction.apply(sender, strings);
    }

    public Command playerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return this;
    }

    public Command param(CommandParameter parameter) {
        this.parameters.add(parameter);
        return this;
    }

    public Command execute(BiConsumer<CommandSender, Object[]> function) {
        this.function = function;
        return this;
    }

    public Command complete(BiFunction<CommandSender, String[], List<String>> function) {
        this.completionFunction = function;
        return this;
    }
}
