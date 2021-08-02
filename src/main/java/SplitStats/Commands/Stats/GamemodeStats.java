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

public class GamemodeStats extends BaseCommand {
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
                StringBuilder response = new StringBuilder();

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

                builder.addField(username + "'s Gamemode Stats", "", false);
                
                builder.addField("Kills On Hill", String.valueOf(((JSONObject) lifetimeStats.get("killsOnHill")).get("displayValue")), true);
                builder.addField("Enemy Kills On Hill", String.valueOf(((JSONObject) lifetimeStats.get("enemyKillsOnHill")).get("displayValue")), true);
                builder.addField("Kills As VIP", String.valueOf(((JSONObject) lifetimeStats.get("killsAsVIP")).get("displayValue")), true);
                builder.addField("Hills Neutralized", String.valueOf(((JSONObject) lifetimeStats.get("hillsNeutralized")).get("displayValue")), true);
                builder.addField("Hills Captured", String.valueOf(((JSONObject) lifetimeStats.get("hillsCaptured")).get("displayValue")), true);
                builder.addField("Flags Returned", String.valueOf(((JSONObject) lifetimeStats.get("flagsReturned")).get("displayValue")), true);
                builder.addField("Flags Picked Up", String.valueOf(((JSONObject) lifetimeStats.get("flagsPickedUp")).get("displayValue")), true);
                builder.addField("Flags Kills", String.valueOf(((JSONObject) lifetimeStats.get("flagKills")).get("displayValue")), true);
                builder.addField("Flag Carrier Kills", String.valueOf(((JSONObject) lifetimeStats.get("flagCarrierKills")).get("displayValue")), true);
                builder.addField("Oddballs Picked Up", String.valueOf(((JSONObject) lifetimeStats.get("oddballsPickedUp")).get("displayValue")), true);
                builder.addField("Oddball Kills", String.valueOf(((JSONObject) lifetimeStats.get("oddballKills")).get("displayValue")), true);
                builder.addField("Oddball Carrier Kills", String.valueOf(((JSONObject) lifetimeStats.get("oddballCarrierKills")).get("displayValue")), true);
                builder.addField("Teabags Denied", String.valueOf(((JSONObject) lifetimeStats.get("teabagsDenied")).get("displayValue")), true);
                
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
        return "gamemodestats";
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
