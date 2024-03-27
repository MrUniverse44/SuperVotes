package me.blueslime.supervotes.modules.api;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.actions.Actions;
import me.blueslime.supervotes.modules.actions.action.Action;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperVotesAPI extends SuperModule {
    private static SuperVotesAPI API = null;

    private static SuperVotesAPI get() {
        if (API == null) {
            API = new SuperVotesAPI(JavaPlugin.getPlugin(SuperVotes.class));
        }
        return API;
    }

    private SuperVotesAPI(SuperVotes plugin) {
        super(plugin);
    }

    public static void registerActions(Action... actions) {
        get().getModule(Actions.class).registerAction(actions);
    }

    @Override
    public void load() {

    }
}
