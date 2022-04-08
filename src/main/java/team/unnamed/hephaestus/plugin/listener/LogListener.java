package team.unnamed.hephaestus.plugin.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import team.unnamed.hephaestus.bukkit.ModelEntity;

// listens to some bukkit events that imply a model
// entity and logs them
public class LogListener implements Listener {

    private final Plugin plugin;

    public LogListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ModelEntity) {
            plugin.getLogger().info("Spawned entity " + entity);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (entity instanceof ModelEntity) {
            plugin.getLogger().info("Entity " + entity + " received damage from " + damager);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof ModelEntity) {
            plugin.getLogger().info("Player " + player + " right-clicked " + entity);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ModelEntity) {
            plugin.getLogger().info("Entity " + entity + " died");
        }
    }

}
