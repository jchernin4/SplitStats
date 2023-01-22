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
                builder.setTitle(username + "'s Stats");

                builder.addField("",
                        "**Points:** `" + ((JSONObject) lifetimeStats.get("points")).get("displayValue") +
                                "`\n**Kills:** `" + ((JSONObject) lifetimeStats.get("kills")).get("displayValue") +
                                "`\n**Assists:** `" + ((JSONObject) lifetimeStats.get("assists")).get("displayValue") +
                                "`\n**Deaths:** `" + ((JSONObject) lifetimeStats.get("deaths")).get("displayValue") +
                                "`\n**Suicides:** `" + ((JSONObject) lifetimeStats.get("suicides")).get("displayValue") +
                                "`", true);

                builder.addField("",
                        "**K/D:** `" + ((JSONObject) lifetimeStats.get("kd")).get("displayValue") +
                                "`\n**K/A/D:** `" + ((JSONObject) lifetimeStats.get("kad")).get("displayValue") +
                                "`\n**Kills Per Min:** `" + ((JSONObject) lifetimeStats.get("killsPerMinute")).get("displayValue") +
                                "`\n**Kills Per Match:** `" + ((JSONObject) lifetimeStats.get("killsPerMatch")).get("displayValue") +
                                "`", true);

                builder.addField("",
                        "**Damage Dealt:** `" + ((JSONObject) lifetimeStats.get("damageDealt")).get("displayValue") +
                                "`\n**Headshots:** `" + ((JSONObject) lifetimeStats.get("headshotsLanded")).get("displayValue") +
                                "`\n**Headshot Kills:** `" + ((JSONObject) lifetimeStats.get("headshotKills")).get("displayValue") +
                                "`\n**Headshot Accuracy:** `" + ((JSONObject) lifetimeStats.get("headshotAccuracy")).get("displayValue") +
                                "`\n**Collaterals:** `" + ((JSONObject) lifetimeStats.get("collaterals")).get("displayValue") +
                                "`", true);


                builder.addField("",
                        "**Time Played:** `" + ((JSONObject) lifetimeStats.get("timePlayed")).get("displayValue") +
                                "`\n**Matches Played:** `" + ((JSONObject) lifetimeStats.get("matchesPlayed")).get("displayValue") +
                                "`\n**Wins:** `" + ((JSONObject) lifetimeStats.get("wins")).get("displayValue") +
                                "`\n**Losses:** `" + ((JSONObject) lifetimeStats.get("losses")).get("displayValue") +
                                "`\n**Win Loss %:** `" + ((JSONObject) lifetimeStats.get("wlPercentage")).get("displayValue") +
                                "`", true);

                builder.addField("",
                        "**Melee Kills:** `" + ((JSONObject) lifetimeStats.get("meleeKills")).get("displayValue") +
                                "`\n**Portal Kills:** `" + ((JSONObject) lifetimeStats.get("portalKills")).get("displayValue") +
                                "`\n**Kills Through Portal:** `" + ((JSONObject) lifetimeStats.get("killsThruPortal")).get("displayValue") +
                                "`\n**Shots Fired:** `" + ((JSONObject) lifetimeStats.get("shotsFired")).get("displayValue") +
                                "`\n**Shots Landed:** `" + ((JSONObject) lifetimeStats.get("shotsLanded")).get("displayValue") +
                                "`\n**Shots Accuracy:** `" + ((JSONObject) lifetimeStats.get("shotsAccuracy")).get("displayValue") +
                                "`", true);

                builder.addField("",
                        "**Rank Level:** `" + ((JSONObject) lifetimeStats.get("rankLevel")).get("displayValue") +
                                "`\n**Rank XP:** `" + ((JSONObject) lifetimeStats.get("rankXp")).get("displayValue") +
                                "`\n**Progression Level:** `" + ((JSONObject) lifetimeStats.get("progressionLevel")).get("displayValue") +
                                "`\n**Progression XP:** `" + ((JSONObject) lifetimeStats.get("progressionXp")).get("displayValue") +
                                "`", true);

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
