package me.blueslime.supervotes.modules.actions.container;

import me.blueslime.utilitiesapi.item.ItemWrapper;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ActionContainer {
    private final List<String> actionList = new ArrayList<>();
    private final ItemWrapper wrapper;
    private final String type;

    public ActionContainer(ItemWrapper wrapper) {
        this.type = "item";
        this.wrapper = wrapper;
    }

    public ActionContainer(List<String> actions) {
        this.wrapper = null;
        this.actionList.addAll(actions);
        this.type = "actions";
    }

    public static ActionContainer build(ConfigurationSection configuration, String path) {
        String type = configuration.getString(path + ".type", "item");

        if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("itemStack")) {
            return new ActionContainer(
                ItemWrapper.fromData(
                    configuration,
                    path
                )
            );
        }
        return new ActionContainer(
            configuration.getStringList(type + ".actions")
        );
    }

    public List<String> getActionList() {
        return actionList;
    }

    public ItemWrapper getWrapper() {
        return wrapper;
    }

    public String getType() {
        return type;
    }

    public boolean isItem() {
        return ("item".equals(type) || "itemstack".equals(type)) && wrapper != null;
    }
}
