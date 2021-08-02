package SplitStats.Commands.Stats;

import SplitStats.Commands.Abstract.BaseCommand;
import SplitStats.Commands.CommandContext;
import SplitStats.Commands.CommandManager;
import SplitStats.Settings;
import net.dv8tion.jda.api.EmbedBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Stats extends BaseCommand {
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

                JSONObject lifetimeSeg = (JSONObject) segments.get(0);
                JSONObject lifetimeStats = (JSONObject) lifetimeSeg.get("stats");

                String username = platformInfo.get("platformUserHandle").toString();
                builder.addField(username + "'s Stats", "", false);

                builder.addField("Points", ((JSONObject) lifetimeStats.get("points")).get("value").toString(), true);
                builder.addField("Kills", ((JSONObject) lifetimeStats.get("kills")).get("value").toString(), true);
                builder.addField("Assists", ((JSONObject) lifetimeStats.get("assists")).get("value").toString(), true);
                builder.addField("Deaths", ((JSONObject) lifetimeStats.get("deaths")).get("value").toString(), true);
                builder.addField("K/D", ((JSONObject) lifetimeStats.get("kd")).get("value").toString(), true);
                builder.addField("K/A/D", ((JSONObject) lifetimeStats.get("kad")).get("value").toString(), true);
                builder.addField("Suicides", ((JSONObject) lifetimeStats.get("suicides")).get("value").toString(), true);
                builder.addField("Melee Kills", ((JSONObject) lifetimeStats.get("meleeKills")).get("value").toString(), true);
                builder.addField("Portal Kills", ((JSONObject) lifetimeStats.get("portalKills")).get("value").toString(), true);
                builder.addField("Kills Through Portal", ((JSONObject) lifetimeStats.get("killsThruPortal")).get("value").toString(), true);
                builder.addField("Damage Dealt", ((JSONObject) lifetimeStats.get("damageDealt")).get("value").toString(), true);
                builder.addField("Matches Played", ((JSONObject) lifetimeStats.get("matchesPlayed")).get("value").toString(), true);
                builder.addField("Wins", ((JSONObject) lifetimeStats.get("wins")).get("value").toString(), true);
                builder.addField("Losses", ((JSONObject) lifetimeStats.get("losses")).get("value").toString(), true);
                builder.addField("Win Loss %", ((JSONObject) lifetimeStats.get("wlPercentage")).get("value").toString(), true);
                builder.addField("Time Played", ((JSONObject) lifetimeStats.get("timePlayed")).get("displayValue").toString(), true);
                builder.addField("Headshots", ((JSONObject) lifetimeStats.get("headshotsLanded")).get("value").toString(), true);
                builder.addField("Headshot Kills", ((JSONObject) lifetimeStats.get("headshotKills")).get("value").toString(), true);
                builder.addField("Headshot Accuracy", ((JSONObject) lifetimeStats.get("headshotAccuracy")).get("value").toString(), true);
                builder.addField("Collaterals", ((JSONObject) lifetimeStats.get("collaterals")).get("value").toString(), true);
                builder.addField("Shots Fired", ((JSONObject) lifetimeStats.get("shotsFired")).get("value").toString(), true);
                builder.addField("Shots Landed", ((JSONObject) lifetimeStats.get("shotsLanded")).get("value").toString(), true);
                builder.addField("Shots Accuracy", ((JSONObject) lifetimeStats.get("shotsAccuracy")).get("value").toString(), true);
                builder.addField("Kills Per Min", ((JSONObject) lifetimeStats.get("killsPerMinute")).get("value").toString(), true);
                builder.addField("Kills Per Match", ((JSONObject) lifetimeStats.get("killsPerMatch")).get("value").toString(), true);
                builder.addField("Rank Level", String.valueOf(((JSONObject) lifetimeStats.get("rankLevel")).get("value")), true);
                builder.addField("Rank XP", String.valueOf(((JSONObject) lifetimeStats.get("rankXp")).get("value")), true);
                builder.addField("Progression Level", String.valueOf(((JSONObject) lifetimeStats.get("progressionLevel")).get("value")), true);
                builder.addField("Progression XP", String.valueOf(((JSONObject) lifetimeStats.get("progressionXp")).get("value")), true);
                
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
        return CommandManager.CommandCategory.STATS;
    }

    @Override
    public String getName() {
        return "stats";
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
