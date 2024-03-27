package me.blueslime.supervotes.modules.placeholders;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.event.Events;
import me.blueslime.supervotes.modules.event.list.PlayingTask;
import me.blueslime.supervotes.modules.event.list.StartingTask;
import me.blueslime.supervotes.modules.utils.list.ReturnableArrayList;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class SuperPlaceholders extends PluginPlaceholders {
    public SuperPlaceholders(SuperVotes plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return new ReturnableArrayList<String>().addValues(
            "%supervotes_votes_current%",
            "%supervotes_votes_left%",
            "%supervotes_votes_max%",
            "%supervotes_is_refreshing%",
            "%supervotes_is_starting%",
            "%supervotes_is_playing%"
        );
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return onParameter(params.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        return onParameter(params.toLowerCase(Locale.ENGLISH));
    }

    public String onParameter(String parameter) {
        int votes = getVotes().getInt("votes.current", 0);
        int max = getVotes().getInt("votes.max", 50);

        switch (parameter) {
            default:
            case "votes_max":
            case "vote_max":
            case "max":
                return String.valueOf(max);
            case "vote_left":
            case "left":
            case "votes_left":
                return String.valueOf(max - votes);
            case "vote_current":
            case "current":
            case "votes_current":
                return String.valueOf(votes);
            case "is_refreshing":
                return String.valueOf(
                        getMainPlugin().getModule(Events.class).getCurrentTime() != null
                        && !getMainPlugin().getModule(Events.class).getCurrentTime().reached()
                );
            case "is_starting":
                return String.valueOf(
                    getMainPlugin().getModule(Events.class).getCurrentEvent() != null
                    && getMainPlugin().getModule(Events.class).getCurrentEvent() instanceof StartingTask
                );
            case "is_playing":
                return String.valueOf(
                    getMainPlugin().getModule(Events.class).getCurrentEvent() != null
                    && getMainPlugin().getModule(Events.class).getCurrentEvent() instanceof PlayingTask
                );
        }
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "supervotes";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JustJustin";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
