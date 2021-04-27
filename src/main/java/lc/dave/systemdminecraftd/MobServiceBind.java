package lc.dave.systemdminecraftd;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


public class MobServiceBind {

    private final NamespacedKey key;

    public MobServiceBind(final Plugin plugin) {
        this.key = new NamespacedKey(plugin, "systemd-service");
    }

    public String get(Entity entity) {
        String service = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return service == null ? "" : service;
    }

    public void set(Entity entity, String service) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, service);
    }

}
