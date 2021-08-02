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

public class MiscStats extends BaseCommand {
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

                builder.addField(username + "'s Misc Stats", "", false);
                
                builder.addField("Revenge Kills", String.valueOf(((JSONObject) lifetimeStats.get("revengeKills")).get("displayValue")), true);
                builder.addField("Portals Spawned", String.valueOf(((JSONObject) lifetimeStats.get("portalsSpawned")).get("displayValue")), true);
                builder.addField("Own Portals Entered", String.valueOf(((JSONObject) lifetimeStats.get("ownPortalsEntered")).get("displayValue")), true);
                builder.addField("Enemy Portals Entered", String.valueOf(((JSONObject) lifetimeStats.get("enemyPortalsEntered")).get("displayValue")), true);
                builder.addField("Enemy Portals Destroyed", String.valueOf(((JSONObject) lifetimeStats.get("enemyPortalsDestroyed")).get("displayValue")), true);
                builder.addField("Ally Portals Destroyed", String.valueOf(((JSONObject) lifetimeStats.get("allyPortalsEntered")).get("displayValue")), true);
                builder.addField("King Slayers", String.valueOf(((JSONObject) lifetimeStats.get("kingSlayers")).get("displayValue")), true);
                builder.addField("First Bloods", String.valueOf(((JSONObject) lifetimeStats.get("firstBloods")).get("displayValue")), true);
                builder.addField("Distance Portaled", String.valueOf(((JSONObject) lifetimeStats.get("distancePortaled")).get("displayValue")), true);

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
        return "miscstats";
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
