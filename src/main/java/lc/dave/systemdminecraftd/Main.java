package lc.dave.systemdminecraftd;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class Main extends JavaPlugin {

    private final Logger logger = getSLF4JLogger();

    @Override
    public void onEnable() {
        if(!"root".equals(System.getProperty("user.name"))) {
            logger.error("systemd-minecraftd must run as root.");
            Bukkit.shutdown();
        }
    }

}
