package SplitStats.Commands.Information;

import SplitStats.BotContainer;
import SplitStats.Commands.Abstract.BaseCommand;
import SplitStats.Commands.CommandContext;
import SplitStats.Commands.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class Help extends BaseCommand {
    @Override
    public void execute(CommandContext ctx) {
        EmbedBuilder[] embeds = BotContainer.commandManager.getHelpEmbeds();
        Message message = ctx.getAuthor().getUser().openPrivateChannel().complete().sendMessageEmbeds(embeds[0].build()).complete();
        message.addReaction("\u2b05").complete(); // Left arrow
        message.addReaction("\u27a1").complete(); // Right arrow

        ctx.getChannel().sendMessage(ctx.getAuthor().getAsMention() + ", check your dms").queue();
    }

    @Override
    public CommandManager.CommandCategory getCategory() {
        return CommandManager.CommandCategory.INFORMATION;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays information for each command.";
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands");
    }
}