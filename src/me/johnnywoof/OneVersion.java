package me.johnnywoof;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OneVersion extends Plugin implements Listener {

	private String connect_message = null, protocol_name = null;
	private int protocol_id = -1;

    public void onEnable() {

        this.getProxy().getPluginManager().registerListener(this, this);

        this.saveDefaultConfig();

        try {

            Configuration yml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getConfig());

            this.connect_message = ChatColor.translateAlternateColorCodes('&', yml.getString("kick-message",
					"This server has been updated!.newline.Please use the latest minecraft version")
					.replaceAll(".newline.", "\n"));

			this.protocol_name = ChatColor.stripColor(yml.getString("protocol-name", "null"));

			if ("null".equals(this.protocol_name))
				this.protocol_name = null;

			this.protocol_id = yml.getInt("protocol-version", -1);

			if (this.protocol_id < 0) {//-1 value for example
				//noinspection deprecation
				this.protocol_id = this.getProxy().getProtocolVersion();
			}

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPing(ProxyPingEvent event) {
		ServerPing ping = event.getResponse();

		ServerPing.Protocol protocol = ping.getVersion();

		if (this.protocol_name != null)
			protocol.setName(this.protocol_name);

        if (!(protocol.getProtocol() > this.protocol_id))
		    protocol.setProtocol(this.protocol_id);

		ping.setVersion(protocol);
		event.setResponse(ping);
	}

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginEvent(PreLoginEvent event) {

        if (event.getConnection().getVersion() < this.protocol_id) {

            event.setCancelReason(this.connect_message);
            event.setCancelled(true);

        }

    }

    /**
     * Generates a file object for the config file
     *
     * @return The config file object
     */
    private File getConfig() {

		return new File(this.getDataFolder(), "config.yml");

    }

    /**
     * Saves the default plugin configuration file from the jar
     */
    private void saveDefaultConfig() {

		File configFile = this.getConfig();

        if (!configFile.exists()) {

			if (!this.getDataFolder().exists()) {
				if (!this.getDataFolder().mkdir()) {

					this.getLogger().warning("Failed to create directory " + this.getDataFolder().getAbsolutePath());

				}
			}

            try {
                if (configFile.createNewFile()) {

                    try (InputStream is = this.getClass().getResourceAsStream("/config.yml");
                         OutputStream os = new FileOutputStream(configFile)) {
                        ByteStreams.copy(is, os);
                    }

                } else {

                    this.getLogger().severe("Failed to create the configuration file!");

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
