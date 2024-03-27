package me.blueslime.supervotes;

import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.actions.Actions;
import me.blueslime.supervotes.modules.commands.Commands;
import me.blueslime.supervotes.modules.event.Events;
import me.blueslime.supervotes.modules.libs.metrics.Metrics;
import me.blueslime.supervotes.modules.placeholders.SuperPlaceholders;
import me.blueslime.supervotes.modules.plugin.Plugin;
import me.blueslime.supervotes.modules.reload.ReloadType;

public final class SuperVotes extends Plugin {

    @Override
    public void onEnable() {
        initialize(this);

        new Metrics(this, 20918);

        if (isPluginEnabled("PlaceholderAPI")) {
            new SuperPlaceholders(this).register();
        }
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    @Override
    public void registerModules() {
        registerModule(
            new Commands(this),
            new Events(this),
            new Actions(this)
        ).finish();
    }

    @Override
    public void reload() {
        build(ReloadType.ALL);

        for (SuperModule module : getModules().values()) {
            module.update();
        }
    }
}
