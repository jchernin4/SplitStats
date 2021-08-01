package SplitStats.Commands.Abstract;

import SplitStats.Commands.CommandContext;
import SplitStats.Commands.CommandManager;

import java.util.List;

public abstract class BaseCommand {
    public abstract void execute(CommandContext ctx);

    public abstract CommandManager.CommandCategory getCategory();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getUsage();

    public abstract List<String> getAliases();
}
