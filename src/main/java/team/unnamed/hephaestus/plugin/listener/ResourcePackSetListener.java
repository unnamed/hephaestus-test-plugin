package team.unnamed.hephaestus.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import team.unnamed.creative.ResourcePack;

public class ResourcePackSetListener implements Listener {

    private final ResourcePack resourcePack;
    private final String resourcePackUrl;

    public ResourcePackSetListener(
            ResourcePack resourcePack,
            String resourcePackUrl
    ) {
        this.resourcePack = resourcePack;
        this.resourcePackUrl = resourcePackUrl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setResourcePack(resourcePackUrl, resourcePack.hash());
    }

}
