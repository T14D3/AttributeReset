package de.t14d3.attributereset;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public final class AttributeReset extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void clearAttributes(Player player) {
        for (Attribute attribute : Attribute.values()) {
            AttributeInstance attributeInstance = player.getAttribute(attribute);
            if (attributeInstance != null) {
                Collection<AttributeModifier> modifiers = attributeInstance.getModifiers();
                for (AttributeModifier modifier : modifiers) {
                    attributeInstance.removeModifier(modifier);
                }
                switch (attribute) {
                    case GENERIC_MOVEMENT_SPEED:
                        attributeInstance.setBaseValue(0.1);
                        break;
                    case GENERIC_ATTACK_DAMAGE:
                        attributeInstance.setBaseValue(1);
                        break;
                    default:
                        double defaultValue = attributeInstance.getDefaultValue();
                        attributeInstance.setBaseValue(defaultValue);
                        break;
                }
            }
        }
    }
    public class LoginListener implements Listener {
        private final AttributeReset plugin;

        public LoginListener(AttributeReset plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onPlayerLogin(PlayerLoginEvent e) {
            Player p = e.getPlayer();
            clearAttributes(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    clearAttributes(p);
                }
            }.runTaskLater(plugin, 10L);
        }
    }
}
