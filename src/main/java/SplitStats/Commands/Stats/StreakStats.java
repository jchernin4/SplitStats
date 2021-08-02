package SplitStats.Commands.Stats;

import SplitStats.Commands.Abstract.BaseCommand;
import SplitStats.Commands.CommandContext;
import SplitStats.Commands.CommandManager;
import SplitStats.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class StreakStats extends BaseCommand {
    @Override
    public void execute(CommandContext ctx) {
        try {
            URL obj = new URL("https://public-api.tracker.gg/v2/splitgate/standard/profile/steam/" + ctx.getArgs()[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0");
            httpURLConnection.setRequestProperty("TRN-Api-Key", Settings.TRN_API_KEY);
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jo = (JSONObject) new JSONParser().parse(response.toString());
                JSONObject data = (JSONObject) jo.get("data");
                JSONObject platformInfo = (JSONObject) data.get("platformInfo");
                JSONArray segments = (JSONArray) data.get("segments");

                EmbedBuilder builder = new EmbedBuilder();
                String username = platformInfo.get("platformUserHandle").toString();
                
                JSONObject lifetimeSeg = (JSONObject) segments.get(0);
                JSONObject lifetimeStats = (JSONObject) lifetimeSeg.get("stats");
                
                builder.addField(username + "'s Streak Stats", "", false);
                
                builder.addField("Kills Per Min", ((JSONObject) lifetimeStats.get("killsPerMinute")).get("value").toString(), true);
                builder.addField("Kills Per Match", ((JSONObject) lifetimeStats.get("killsPerMatch")).get("value").toString(), true);
                builder.addField("Double Kills", ((JSONObject) lifetimeStats.get("medalDoubleKills")).get("value").toString(), true);
                builder.addField("Triple Kills", ((JSONObject) lifetimeStats.get("medalTripleKills")).get("value").toString(), true);
                builder.addField("Quad Kills", ((JSONObject) lifetimeStats.get("medalQuadKills")).get("value").toString(), true);
                builder.addField("Quint Kills", ((JSONObject) lifetimeStats.get("medalQuintKills")).get("value").toString(), true);
                builder.addField("Sext Kills", ((JSONObject) lifetimeStats.get("medalSexKills")).get("value").toString(), true);
                builder.addField("5 Killstreak", ((JSONObject) lifetimeStats.get("medalKillstreak1")).get("value").toString(), true);
                builder.addField("10 Killstreak", ((JSONObject) lifetimeStats.get("medalKillstreak2")).get("value").toString(), true);
                builder.addField("15 Killstreak", ((JSONObject) lifetimeStats.get("medalKillstreak3")).get("value").toString(), true);
                builder.addField("20 Killstreak", ((JSONObject) lifetimeStats.get("medalKillstreak4")).get("value").toString(), true);
                builder.addField("25 Killstreak", ((JSONObject) lifetimeStats.get("medalKillstreak5")).get("value").toString(), true);
                builder.addField("50 Killstreak", ((JSONObject) lifetimeStats.get("medalKillstreak6")).get("value").toString(), true);
                builder.addField("Highest Consecutive Kills", String.valueOf(((JSONObject) lifetimeStats.get("highestConsecutiveKills")).get("value")), true);
                
                ctx.getChannel().sendMessageEmbeds(builder.build()).queue();

            } else {
                ctx.getChannel().sendMessage(ctx.getAuthor().getAsMention() + ", player " + ctx.getArgs()[0] + " not found.").queue();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommandManager.CommandCategory getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "streakstats";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
