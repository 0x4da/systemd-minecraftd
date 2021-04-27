package lc.dave.systemdminecraftd;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SpawnSystemdServiceMobs extends BukkitRunnable {

    private final MobServiceBind mobServiceBind;

    public SpawnSystemdServiceMobs(final MobServiceBind mobServiceBind) {
        this.mobServiceBind = mobServiceBind;
    }

    @Override
    public void run() {
        Set<String> runningServices = new HashSet<>();
        try {
            String output = Processes.run("systemctl");
            for (String line : output.split("\n")) {
                if (line.contains("running")) {
                    runningServices.add(line.trim().split(" ")[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            runningServices.remove(mobServiceBind.get(entity));
        }

        for (String service : runningServices) {
            try {
                Processes.run("systemctl", "stop", service);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
