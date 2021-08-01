package SplitStats.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandContext {
    private final Message message;
    private final String[] args;
    private final TextChannel channel;
    private final Member author;

    public CommandContext(Message message, String[] args, TextChannel channel, Member author) {
        this.message = message;
        this.args = args;
        this.channel = channel;
        this.author = author;
    }

    public Message getMessage() {
        return message;
    }

    public String[] getArgs() {
        return args;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Member getAuthor() {
        return author;
    }
}
