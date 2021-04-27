package lc.dave.systemdminecraftd;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.IOException;

public final class Main extends JavaPlugin {

    private final Logger logger = getSLF4JLogger();
    private MobServiceBind mobServiceBind;

    @Override
    public void onEnable() {
        if(!"root".equals(System.getProperty("user.name"))) {
            logger.error("systemd-minecraftd must run as root.");
            Bukkit.shutdown();
        }

        for (final Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof Player) {
                continue;
            }
            entity.remove();
        }

        mobServiceBind = new MobServiceBind(this);

        try {
            String output = Processes.run("systemctl");
            World world = Bukkit.getWorld("world");
            for (String line : output.split("\n")) {
                if (line.contains("running")) {
                    Entity entity = world.spawnEntity(world.getSpawnLocation(), EntityType.VILLAGER);
                    entity.setCustomNameVisible(true);
                    entity.setCustomName(line.split("running")[1].trim());
                    mobServiceBind.set(entity, line.trim().split(" ")[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.shutdown();
        }
        getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);
        new SpawnSystemdServiceMobs(mobServiceBind).runTaskTimer(this, 20, 10);
    }

}
