package SplitStats;

import SplitStats.Commands.Abstract.BaseCommand;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import
        java.util.Random;

public class Utils {
    public static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public static String parseId(String str) {
        str = str.replace("<", "").replace("@", "").replace(">", "").replace("!", "").replace("#", "");
        return str;
    }


    public static int getNumMembers() {
        int users = 0;
        for (Guild guild : BotContainer.jda.getGuildCache()) {
            users += guild.getMemberCount();
        }

        return users;
    }

    public static String getDateFormatted() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String formatDate(OffsetDateTime date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        return dtf.format(date);
    }

    public static String getClosestCommand(String str) {
        String closest = str;
        int closestDist = Integer.MAX_VALUE;

        for (BaseCommand c : BotContainer.commandManager.getCommands()) {
            if (c.getAliases() != null) {
                for (String s : c.getAliases()) {
                    int curDist = levenshteinDistance(s, str.toLowerCase());
                    if (curDist < closestDist) {
                        closest = s;
                        closestDist = curDist;
                    }
                }
            }

            int curDist = levenshteinDistance(c.getName(), str.toLowerCase());
            if (curDist < closestDist) {
                closest = c.getName();
                closestDist = curDist;
            }
        }
        return closest;
    }

    // https://www.geeksforgeeks.org/java-program-to-implement-levenshtein-distance-computing-algorithm/
    public static int levenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = minEdits(dp[i - 1][j - 1] + numOfReplacement(str1.charAt(i - 1), str2.charAt(j - 1)), dp[i - 1][j] + 1, dp[i][j - 1] + 1);
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    public static int numOfReplacement(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    static int minEdits(int... nums) {

        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }
}
