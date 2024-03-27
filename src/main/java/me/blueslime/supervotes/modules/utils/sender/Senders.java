package me.blueslime.supervotes.modules.utils.sender;

import me.blueslime.utilitiesapi.UtilitiesAPI;
import me.blueslime.utilitiesapi.color.ColorHandler;
import me.blueslime.utilitiesapi.text.TextReplacer;
import me.blueslime.utilitiesapi.text.TextUtilities;
import me.blueslime.utilitiesapi.tools.PlaceholderParser;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Senders {
    private final List<CommandSender> sender = new ArrayList<>();

    private Senders(CommandSender... sender) {
        this.sender.addAll(Arrays.asList(sender));
    }

    private Senders(List<CommandSender> sender) {
        this.sender.addAll(sender);
    }

    private Senders(Collection<? extends Player> sender) {
        this.sender.addAll(sender);
    }

    /**
     * Create a Sender instance
     * @param sender to be converted
     * @return Sender instance
     */
    public static Senders build(CommandSender... sender) {
        return new Senders(sender);
    }

    /**
     * Create a Sender instance
     * @param sender to be converted
     * @return Sender instance
     */
    public static Senders build(Collection<? extends Player> sender) {
        return new Senders(sender);
    }

    /**
     * Create a Sender instance
     * @param sender to be converted
     * @return Sender instance
     */
    public static Senders build(List<CommandSender> sender) {
        return new Senders(sender);
    }

    public Senders addSender(CommandSender... sender) {
        this.sender.addAll(Arrays.asList(sender));
        return this;
    }

    /**
     * Convert the sender to an Entity
     * @return Entity
     */
    public Entity toEntity() {
        return (Entity)sender;
    }

    /**
     * Send a message to the sender
     * @param target player for PlaceholdersAPI if the server has it.
     * @param replacer replacements for the messages
     * @param messages to be sent
     */
    public void send(Player target, TextReplacer replacer, String... messages) {
        if (messages == null || messages.length == 0) {
            for (CommandSender sender : this.sender) {
                sender.sendMessage(" ");
            }
            return;
        }

        List<String> messageList = new ArrayList<>(Arrays.asList(messages));

        boolean placeholders = UtilitiesAPI.hasPlaceholders();

        if (placeholders && target != null) {

            StringBuilder builder = new StringBuilder();

            int current = 1;
            int total = messageList.size();

            for (String message : messageList) {
                message = colorize(
                        PlaceholderParser.parse(
                                target,
                                replacer == null ?
                                        message :
                                        replacer.apply(message)
                        )
                );

                if (current != total) {
                    builder.append(
                            message
                    ).append(
                            "\n"
                    );
                } else {
                    builder.append(
                            message
                    );
                }

                current++;
            }


            String message = builder.toString();
            for (CommandSender sender : this.sender) {
                sender.sendMessage(message);
            }
            return;
        }
        StringBuilder builder = new StringBuilder();

        int current = 1;
        int total = messageList.size();

        for (String message : messageList) {
            message = colorize(
                    replacer == null ?
                            message :
                            replacer.apply(message)
            );

            if (current != total) {
                builder.append(
                        message
                ).append(
                        "\n"
                );
            } else {
                builder.append(
                        message
                );
            }

            current++;
        }

        String message = builder.toString();

        if (!placeholders) {
            for (CommandSender sender : this.sender) {
                sender.sendMessage(message);
            }
            return;
        }

        for (CommandSender sender : this.sender) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                player.sendMessage(
                    colorize(
                        PlaceholderParser.parse(
                            player,
                            message
                        )
                    )
                );
                continue;
            }
            sender.sendMessage(message);
        }
    }

    /**
     * Send messages for the sender
     * @param replacer replacements for messages
     * @param messages to be sent
     */
    public void send(TextReplacer replacer, String... messages) {
        send(null, replacer, messages);
    }

    /**
     * Send messages to the sender
     * @param target player to cast PlaceholdersAPI if the server has it.
     * @param messages to be sent
     */
    public void send(Player target, String... messages) {
        send(target, null, messages);
    }

    /**
     * Send messages to the sender
     * @param messages to be sent
     */
    public void send(String... messages) {
        send((Player)null, null, messages);
    }

    /**
     * Send messages from a configuration path to the sender
     * @param target player to cast in PlaceholdersAPI if the server has it.
     * @param configuration for search the current path
     * @param path of the message
     * @param def value if the path is not set
     * @param replacer replacements for messages
     */
    public void send(Player target, ConfigurationSection configuration, String path, Object def, TextReplacer replacer) {
        Object ob = configuration.get(path, def);

        if (ob == null) {
            return;
        }

        if (ob instanceof List) {
            List<?> list = (List<?>)ob;
            for (Object object : list) {
                send(
                    target,
                    replacer,
                    object.toString()
                );
            }
        } else {
            send(
                target,
                colorize(
                    replacer == null ?
                        ob.toString() :
                        replacer.apply(ob.toString())
                )
            );
        }
    }

    /**
     * Send messages from a configuration path to the sender
     * @param configuration for search the current path
     * @param path of the message
     * @param def value if the path is not set
     * @param replacer replacements for messages
     */
    public void send(ConfigurationSection configuration, String path, Object def, TextReplacer replacer) {
        send(null, configuration, path, def, replacer);
    }

    /**
     * Send messages from a configuration path to the sender
     * @param configuration for search the current path
     * @param path of the message
     * @param def value if the path is not set
     */
    public void send(ConfigurationSection configuration, String path, Object def) {
        send(null, configuration, path, def, null);
    }

    /**
     * Send messages from a configuration path to the sender
     * @param target player to cast in PlaceholdersAPI if the server has it.
     * @param configuration for search the current path
     * @param path of the message
     * @param def value if the path is not set
     */
    public void send(Player target, ConfigurationSection configuration, String path, Object def) {
        send(target, configuration, path, def, null);
    }

    /**
     * Send base components to the sender
     * @param components to be sent
     */
    public void send(BaseComponent... components) {
        for (CommandSender sender : this.sender) {
            sender.spigot().sendMessage(components);
        }
    }

    /**
     * Send messages from a configuration path to the sender
     * @param target player to cast in PlaceholdersAPI if the server has it.
     * @param configuration for search the current path
     * @param path of the message
     * @param replacer replacements for messages
     */
    public void send(Player target, ConfigurationSection configuration, String path, TextReplacer replacer) {
        Object ob = configuration.get(path);

        if (ob == null) {
            return;
        }

        if (ob instanceof List) {
            List<?> list = (List<?>)ob;
            for (Object object : list) {
                send(
                    target,
                    colorize(
                        replacer == null ?
                                object.toString() :
                                replacer.apply(object.toString())
                    )
                );
            }
        } else {
            send(
                target,
                colorize(
                    replacer == null ?
                        ob.toString() :
                        replacer.apply(ob.toString())
                )
            );
        }
    }

    /**
     * Send messages from a configuration path to the sender
     * @param configuration for search the current path
     * @param path of the message
     * @param replacer replacements for messages
     */
    public void send(ConfigurationSection configuration, String path, TextReplacer replacer) {
        send(null, configuration, path, replacer);
    }

    /**
     * Send messages from a configuration path to the sender
     * @param target player to cast in PlaceholdersAPI if the server has it.
     * @param configuration for search the current path
     * @param path of the message
     */
    public void send(Player target, ConfigurationSection configuration, String path) {
        send(target, configuration, path, null);
    }

    /**
     * Send messages from a configuration path to the sender
     * @param configuration for search the current path
     * @param path of the message
     */
    public void send(ConfigurationSection configuration, String path) {
        send(configuration, path, null);
    }

    /**
     * Send a list of messages to the sender
     * @param messages to be sent
     * @param replacer replacements for messages
     */
    public void send(List<String> messages, TextReplacer replacer) {
        send(null, messages, replacer);
    }

    /**
     * Send a list of messages to the sender
     * @param target player to cast in PlaceholdersAPI if the server has it
     * @param messages to be sent
     * @param replacer replacements for messages
     */
    public void send(Player target, List<String> messages, TextReplacer replacer) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        if (!UtilitiesAPI.hasPlaceholders() && target != null) {
            StringBuilder builder = new StringBuilder();

            int current = 1;
            int total = messages.size();

            for (String message : messages) {
                message = colorize(
                        PlaceholderParser.parse(
                                target,
                                replacer == null ?
                                        message :
                                        replacer.apply(message)
                        )
                );

                if (current != total) {
                    builder.append(
                            message
                    ).append(
                            "\n"
                    );
                } else {
                    builder.append(
                            message
                    );
                }

                current++;
            }
            String message = builder.toString();
            for (CommandSender sender : this.sender) {
                sender.sendMessage(
                        message
                );
            }
            return;
        }
        StringBuilder builder = new StringBuilder();

        int current = 1;
        int total = messages.size();

        for (String message : messages) {
            message = colorize(
                    replacer == null ?
                            message :
                            replacer.apply(message)
            );

            if (current != total) {
                builder.append(
                        message
                ).append(
                        "\n"
                );
            } else {
                builder.append(
                        message
                );
            }

            current++;
        }
        String message = builder.toString();
        for (CommandSender sender : this.sender) {
            sender.sendMessage(
                    message
            );
        }
    }

    /**
     * Send a message list to the sender
     * @param messages to be sent
     */
    public void send(List<String> messages) {
        send(messages, null);
    }

    /**
     * Colorize a text value
     * @param text to be colorized
     * @return colored text
     */
    public static String colorize(String text) {
        return ColorHandler.convert(text);
    }

    /**
     * Colorize a String List text value
     * @param list to be colorized
     * @return colored text
     */
    public static List<String> colorizeList(List<String> list) {
        return TextUtilities.colorizeList(list);
    }
}

