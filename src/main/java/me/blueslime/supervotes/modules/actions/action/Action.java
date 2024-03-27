package me.blueslime.supervotes.modules.actions.action;

import me.blueslime.supervotes.SuperVotes;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class Action {
    private final List<String> prefixes = new ArrayList<>();



    public Action(String prefix, String... extraPrefixes) {
        this.prefixes.addAll(Arrays.asList(extraPrefixes));
        this.prefixes.add(prefix);
    }


    /**
     * Execute action
     * @param plugin of the event
     * @param randomPlayer of the event (WARN! This can be null)
     * @param parameter text
     */
    public abstract void execute(SuperVotes plugin, Player randomPlayer, String parameter);

    /**
     * Check if this action is only for a random player action list
     * @return true if only works with a player specified, false if works with all actions modules
     */
    public abstract boolean isOnlyRandomPlayer();

    public abstract String getExampleUsage();

    public String replace(String parameter) {
        for (String prefix : prefixes) {
            parameter = parameter.replace(prefix + " ", "").replace(prefix, "");
        }
        return parameter;
    }

    public boolean isAction(String parameter) {
        if (parameter == null) {
            return false;
        }
        for (String prefix : prefixes) {
            if (parameter.toLowerCase(Locale.ENGLISH).contains(prefix.toLowerCase(Locale.ENGLISH))) {
                return true;
            }
        }
        return false;
    }

    public List<String> getPrefixes() {
        return prefixes;
    }
}

