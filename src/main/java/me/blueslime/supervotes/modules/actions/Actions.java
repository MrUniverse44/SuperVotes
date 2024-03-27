package me.blueslime.supervotes.modules.actions;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.actions.action.Action;
import me.blueslime.supervotes.modules.actions.type.ConsoleAction;
import me.blueslime.supervotes.modules.actions.type.MessageAction;
import me.blueslime.supervotes.modules.actions.type.PlayersAction;
import me.blueslime.supervotes.modules.actions.type.RandomPlayerAction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Actions extends SuperModule {
    private final List<Action> externalActions = new ArrayList<>();
    private final List<Action> action = new ArrayList<>();

    public Actions(SuperVotes plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        action.clear();

        registerInternalAction(
            new MessageAction(),
            new PlayersAction(),
            new RandomPlayerAction(),
            new ConsoleAction()
        );
    }

    /**
     * Register actions to the plugin
     * @param actions to register
     */
    private void registerInternalAction(Action... actions) {
        action.addAll(Arrays.asList(actions));
    }

    /**
     * Register actions to the plugin
     * @param actions to register
     */
    public void registerAction(Action... actions) {
        externalActions.addAll(Arrays.asList(actions));
    }

    public List<Action> getActions() {
        return action;
    }

    public List<Action> getExternalActions() {
        return externalActions;
    }

    public void execute(List<String> actions) {
        execute(actions, null);
    }

    public void execute(List<String> actions, Player randomPlayer) {
        List<Action> entireList = new ArrayList<>();

        entireList.addAll(externalActions);
        entireList.addAll(action);

        for (String param : actions) {
            fetch(entireList, randomPlayer, param);
        }
    }

    private void fetch(List<Action> list, Player randomPlayer, String param) {
        for (Action action : list) {
            if (action.isAction(param) && ((action.isOnlyRandomPlayer() && randomPlayer != null) || !action.isOnlyRandomPlayer())) {
                action.execute(getPlugin(), randomPlayer, param);
                return;
            }
        }
        getLogger().info("'" + param + "' don't have an action, please see actions with /<command> actions");
    }
}
