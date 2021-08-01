package SplitStats.Commands;

import SplitStats.BotContainer;
import SplitStats.Commands.Abstract.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<BaseCommand> commands;

    public CommandManager() {
        commands = new ArrayList<>();

        //commands.add(new Ping());
    }

    public enum CommandCategory {
        CONFIGURATION, INFORMATION, ECONOMY, MODERATION, FUN
    }

    public EmbedBuilder[] getHelpEmbeds() {
        EmbedBuilder[] embeds = new EmbedBuilder[CommandManager.CommandCategory.values().length];
        for (BaseCommand c : BotContainer.commandManager.getCommands()) {
            if (embeds[c.getCategory().ordinal()] == null) {
                embeds[c.getCategory().ordinal()] = new EmbedBuilder();
                String categoryTitleCase = c.getCategory().name().toLowerCase().substring(0, 1).toUpperCase() + c.getCategory().name().toLowerCase().substring(1);
                embeds[c.getCategory().ordinal()].setTitle("Help - " + categoryTitleCase);
                embeds[c.getCategory().ordinal()].setFooter("Page " + (c.getCategory().ordinal() + 1) + " / " + CommandManager.CommandCategory.values().length);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("- `%s`: %s\nUsage: `%s`", c.getClass().getSimpleName(), c.getDescription(), c.getUsage()));

            sb.append("\nAliases:");
            if (c.getAliases() != null) {
                for (String s : c.getAliases()) {
                    sb.append("\n`").append(s).append("`");
                }

            } else {
                sb.append("\nNone");
            }

            embeds[c.getCategory().ordinal()].appendDescription(sb + "\n\n");
        }

        return embeds;
    }

    /**
     * Searches for a command with the given name or alias (case insensitive)
     * @param name name to search for
     * @return BaseCommand object for name or null
     */
    public BaseCommand searchForCommand(String name) {
        String nameLower = name.toLowerCase();
        for (BaseCommand cmd : commands) {
            if (nameLower.equals(cmd.getName()) || (cmd.getAliases() != null && cmd.getAliases().contains(nameLower))) {
                return cmd;
            }
        }

        return null;
    }

    public List<BaseCommand> getCommands() {
        return commands;
    }
}
