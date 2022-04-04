package rabbitown.bukkit.fourneau.common;

import com.google.common.io.Files;
import lombok.SneakyThrows;
import rabbitown.bukkit.fourneau.RabbiFourneau;
import rabbitown.bukkit.fourneau.util.IO;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author Milkory
 */
public class Logger {

    private static final RabbiFourneau plugin = RabbiFourneau.getInstance();

    public static void info(String msg) {
        plugin.getLogger().info(msg);
    }

    public static void warning(String msg) {
        plugin.getLogger().warning(msg);
    }

    public static void severe(String msg) {
        plugin.getLogger().severe(msg);
    }

    public static void logPopTeamEpic() {
        info(getPopTeamEpic());
    }

    private static String getPopTeamEpic() {
        switch (getPopTeamEpicEpisode()) {
            case 1:
                return "\u7b2c\u4e00\u661f  \u541b\u3060\u3051\u306b\u6559\u3048\u308b\u3088\uff01";
            case 2:
                return "\u7b2c\u4e8c\u661f  \u30d8\u30eb\u30d7\u3001\u305d\u305d\u3050\u306f\u30a2\u30a4\u30c9\u30eb\uff01";
            case 3:
                return "\u7b2c\u4e09\u661f  \u5927\u5730\u304f\u3093\u304c\u65b0\u3057\u3044\u30de\u30cd\u30fc\u30b8\u30e3\u30fc\uff1f";
            case 4:
                return "\u7b2c\u56db\u661f  \u30c7\u30d3\u30eb\u30dc\u30eb\u30b1\u30fc\u767b\u5834\uff01\u4e8c\u4eba\u3060\u3051\u306e\u30e9\u30a4\u30d6\uff01";
            case 5:
                return "\u7b2c\u4e94\u661f  \u3057\u305a\u304f\u306e\u30e9\u30a4\u30d0\u30eb\u30cf\u30fc\u30c8\u708e\u4e0a\u4e2d\uff01";
            case 6:
                return "\u7b2c\u516d\u661f  \u4e09\u89d2\u95a2\u4fc2\uff01\uff1f\u5f37\u6575\u306f\u3053\u308d\u306a\u30d1\u30a4\u30bb\u30f3";
            case 7:
                return "\u7b2c\u4e03\u661f  \u90e8\u5c4b\u304c\u4e00\u7dd2\u306a\u3089\u3001\u6238\u7c4d\u3082\u4e00\u7dd2\u306b\u266a";
            case 8:
                return "\u7b2c\u516b\u661f  \u30c9\u30ed\u30c3\u30d7\u30b9\u30bf\u30fc\u30ba\u3001\u89e3\u6563\u306e\u5371\u6a5f\uff01\uff1f";
            case 9:
                return "\u7b2c\u4e5d\u661f  \u3042\u306a\u305f\u306b\u5c4a\u3051\u3001\u79c1\u305f\u3061\u306e\u65b0\u66f2\uff01";
            case 10:
                return "\u7b2c\u5341\u661f  \u6e80\u5929\u306e\u30ad\u30b9";
            case 11:
                return "\u7b2c\u5341\u4e00\u661f  \u7a81\u7136\u306e\u5225\u308c";
            case 12:
                return "\u7b2c\u5341\u4e8c\u661f  \u661f\u964d\u308b\u5927\u5730\u3001\u5927\u5207\u306a\u7d04\u675f";
            case 13:
                return "\u7b2c\u5341\u4e09\u661f  \u30a2\u30a4\u30c9\u30eb\u304b\u82b1\u5ac1\uff01\u9078\u3076\u306e\u306f\u3069\u3063\u3061\uff1f";
            default:
                return "\u7b2c\u96f6\u661f  \u5f31\u3059\u304e\u308b\u3001\u529b\u304c\u306a\u3044\uff01";
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @SneakyThrows private static int getPopTeamEpicEpisode() {
        var file = IO.initFile(new File(plugin.getDataFolder(), "pop.milkory"));
        var line = Files.readFirstLine(file, StandardCharsets.UTF_8);
        int pop = 0;
        if (line != null) {
            try {
                pop = Integer.parseInt(line);
            } catch (NumberFormatException ignore) {
            }
        }
        var next = pop % 13 + 1;
        IO.writeFile(file, String.valueOf(next));
        return next;
    }

}
