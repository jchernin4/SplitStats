package SplitStats;

import SplitStats.Commands.CommandManager;
import SplitStats.Listeners.MessageListener;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;

public class Driver {
    public static void main(String[] args) throws LoginException {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);

        Logger anotherLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        anotherLogger.setLevel(Level.INFO);

        BotContainer.commandManager = new CommandManager();
        BotContainer.mongoManager = MongoManager.getInstance();

        JDABuilder builder = JDABuilder.createDefault(Settings.TOKEN);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        BotContainer.jda = builder.build();
        BotContainer.jda.addEventListener(new MessageListener());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BotContainer.jda.getPresence().setActivity(Activity.watching(Utils.getNumMembers() + " members in " + BotContainer.jda.getGuildCache().size() + " guilds"));
            }
        }, 1000, 60 * 1000 * 5);
    }
}
