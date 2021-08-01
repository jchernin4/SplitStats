package SplitStats.Commands.Information;

import SplitStats.BotContainer;
import SplitStats.Commands.Abstract.BaseCommand;
import SplitStats.Commands.CommandContext;
import SplitStats.Commands.CommandManager;

import java.util.List;

public class Ping extends BaseCommand {
    @Override
    public void execute(CommandContext ctx) {
        ctx.getChannel().sendMessage(ctx.getAuthor().getAsMention() + ", pong! Gateway ping: `" + BotContainer.jda.getGatewayPing() + "`, rest ping: `" + BotContainer.jda.getRestPing().complete() + "`").queue();
    }

    @Override
    public CommandManager.CommandCategory getCategory() {
        return CommandManager.CommandCategory.INFORMATION;
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Checks gateway and rest ping.";
    }

    @Override
    public String getUsage() {
        return "ping";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
