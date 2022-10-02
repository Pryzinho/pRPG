package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.events.bukkit.beast.BeastModeChangeEvent;
import br.pryzat.rpg.main.RpgMain;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class Beast {
    private final RpgMain plugin;
    private Character master;
    private boolean invoked;
    private Type type;
    private Mode mode;
    private Wolf entity;

    public Beast(RpgMain plugin, Character master, Type type) {
        this.plugin = plugin;
        this.master = master;
        this.type = type;
        this.invoked = false;
    }

    public void spawn() {
        if (type == Type.NONE) {
            return;
        }
        Player p = Bukkit.getPlayer(master.getUUID());
        if (p == null || !p.isOnline()) return;
        entity = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.setCustomNameVisible(true);
        entity.customName(Component.text(type.getDisplayName() + "\n" + p.displayName()));
        entity.getPersistentDataContainer().set(new NamespacedKey(plugin, "pry.rpg.entity"), PersistentDataType.STRING, type.getUniqueId());
        entity.setOwner(p);
        entity.setAdult();
        entity.setAgeLock(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) (master.getMaxHealth() / 3));
        this.invoked = true;
    }

    public void despawn() {
        if (type == Type.NONE) {
            return;
        }
        entity.remove();
        this.invoked = false;
        this.type = Type.NONE;
        this.mode = Mode.ATTACK;
    }

    public void setType(Type beastype) {
        this.type = beastype;
    }

    public void setMode(Mode mode) {
        BeastModeChangeEvent bmce = new BeastModeChangeEvent(master, this, mode);
        Bukkit.getServer().getPluginManager().callEvent(bmce);
        if (bmce.isCancelled()){
            return;
        }
        this.mode = mode;
    }

    public Type getType() {
        return this.type;
    }

    public Mode getMode() {
        return this.mode;
    }

    public boolean isInvoked() {
        return invoked;
    }

    /**
     * @return Retorna verdadeiro se o jogador tiver um animal.
     */
    public boolean exists() {
        if (master.getClazz() == ClazzType.TAMER && master.getLevel() >= 30) {
            return true;
        }
        return false;
    }

    public Wolf getEntity() {
        return entity;
    }

    public enum Type {
        NONE("", ""), // None
        METAL_PIG("Porco de Pele de Aço", "metalpig"), // Porco de Pele de Aço (Porco)
        WAR_WOLF("Lobo da guerra", "warwolf"), // Lobo da Guerra (Wolf)
        SHADOW_BEAMON("Beamon das Sombras", "shadowbeamon"), // Beamon das Sombras (Warden)
        SPIRIT_FOX("Raposa Espiritual", "spiritfox"), // Raposa Espiritual
        DAVID_DEER("Cervo de David", "daviddeer"); // Cervo de David

        private String displayName;
        private String buid; // Beats Unique Identifier

        Type(String displayName, String buid) {
            this.displayName = displayName;
            this.buid = buid;
        }

        String getDisplayName() {
            return displayName;
        }

        String getUniqueId() {
            return buid;
        }
    }

    public enum Mode {
        ATTACK,
        DEFENSE,
        FOLLOW;
    }
}
