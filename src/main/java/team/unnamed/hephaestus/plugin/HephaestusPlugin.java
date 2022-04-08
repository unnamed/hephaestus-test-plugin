/*
 * This file is part of hephaestus-engine, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.hephaestus.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.server.ResourcePackServer;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.v1_18_R2.ModelEngine_v1_18_R2;
import team.unnamed.hephaestus.plugin.listener.LogListener;
import team.unnamed.hephaestus.plugin.listener.ResourcePackSetListener;
import team.unnamed.hephaestus.reader.blockbench.BBModelReader;
import team.unnamed.hephaestus.reader.ModelReader;
import team.unnamed.hephaestus.bukkit.ModelEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.logging.Level;

public class HephaestusPlugin extends JavaPlugin {

    private static final ModelReader READER = BBModelReader.blockbench();

    private ResourcePackServer resourcePackServer;

    @Override
    public void onEnable() {
        ModelEngine engine = ModelEngine_v1_18_R2.create(this, CustomModelEntity::new);

        // load models from resources
        ModelRegistry registry = new ModelRegistry();
        registry.registerModel(loadModel("butterfly.bbmodel"));
        registry.registerModel(loadModel("redstone_monstrosity.bbmodel"));

        // generate resource pack
        ResourcePack pack = ResourcePack.build(new ResourcePackWriter(registry));

        // start resource pack server
        getLogger().info("Starting resource pack server");
        String resourcePackUrl;
        try {
            String hostname = System.getProperty("hephaestus.resourcePackServer.hostname", "127.0.0.1");
            int port = Integer.getInteger("hephaestus.resourcePackServer.port", 7270);
            resourcePackServer = ResourcePackServer.builder()
                    .address(hostname, port)
                    .pack(pack)
                    .build();
            resourcePackServer.start();
            getLogger().info("Resource pack server listening on port " + port);
            // important: we use HTTP because we did not configure HTTPS, using HTTPS will fail
            resourcePackUrl = "http://" + hostname + ':' + port + '/' + pack.hash() + ".zip";
            getLogger().info("Resource pack available at " + resourcePackUrl);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Cannot start resource pack server", e);
            setEnabled(false);
            return;
        }

        // register commands, listeners and tasks
        getLogger().info("Registering commands, listeners and tasks...");
        registerCommand("model", new ModelCommand(registry, engine));
        registerListener(new ResourcePackSetListener(pack, resourcePackUrl));
        registerListener(new LogListener(this));
    }

    @Override
    public void onDisable() {
        if (resourcePackServer != null) {
            getLogger().info("Stopping resource pack server");
            resourcePackServer.stop(0);
        }
    }

    private Model loadModel(String resourceName) {
        try (InputStream input = super.getResource("models/" + resourceName)) {
            Model model = READER.read(input);
            getLogger().info("Loaded model " + model.name());
            return model;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read model " + resourceName, e);
        }
    }

    // utility methods for JavaPlugin
    private <T extends CommandExecutor & TabCompleter> void registerCommand(String name, T handler) {
        PluginCommand command = super.getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Command not found: " + name);
        }
        command.setExecutor(handler);
        command.setTabCompleter(handler);
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

}
