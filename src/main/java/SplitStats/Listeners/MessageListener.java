package SplitStats.Listeners;

import SplitStats.BotContainer;
import SplitStats.Commands.Abstract.BaseCommand;
import SplitStats.Commands.CommandContext;
import SplitStats.Utils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) {
            return;
        }

        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("You cannot use this command in a private channel.").queue();
            return;
        }

        if (event.getMessage().getContentRaw().equals("<@!834814951213760543>")) {
            event.getChannel().sendMessage("My prefix is " + BotContainer.mongoManager.findOrCreateGuildSettings(event.getGuild()).get("prefix") + ".").queue();
            return;
        }

        Document guildSettings = BotContainer.mongoManager.findOrCreateGuildSettings(event.getGuild());
        
        Message message = event.getMessage();
        if (message.getContentRaw().startsWith((String) guildSettings.get("prefix"))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
            String formattedDate = formatter.format(now);
            LOGGER.info(String.format("%s - [%s (%s)] [%s (%s)] -  %s (%s) : %s\n", formattedDate, event.getGuild().getName(), event.getGuild().getId(), event.getChannel().getName(), event.getChannel().getId(), event.getAuthor().getAsTag(), event.getAuthor().getId(), event.getMessage().getContentRaw()));

            String[] splitContent = message.getContentRaw().split(" ");
            String command = splitContent[0];
            String[] args = new String[splitContent.length - 1];

            System.arraycopy(splitContent, 1, args, 0, splitContent.length - 1);

            command = command.substring(((String) guildSettings.get("prefix")).length());

            BaseCommand commandObj = BotContainer.commandManager.searchForCommand(command);

            if (commandObj == null) {
                event.getChannel().sendMessage("Command `" + command + "` does not exist. Did you mean `" + Utils.getClosestCommand(command) + "`? Use the help command to view a list of available commands.").queue();
                return;
            }

            commandObj.execute(new CommandContext(message, args, event.getTextChannel(), event.getMember()));
        }
    }
}
