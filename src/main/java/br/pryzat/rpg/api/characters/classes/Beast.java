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

import java.util.Objects;

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
        if (type.equals("none")) {
            return;
        }
        Player p = master.getPlayer();
        if (p == null){
            return;
        }
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
        if (type.equals("none")) {
            return;
        }
        entity.remove();
        this.invoked = false;
        this.type = new Type("none", "");
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


    public Wolf getEntity() {
        return entity;
    }

    public class Type {
  /*      NONE("", ""), // None
        METAL_PIG("Porco de Pele de Aço", "metalpig"), // Porco de Pele de Aço (Porco)
        WAR_WOLF("Lobo da guerra", "warwolf"), // Lobo da Guerra (Wolf)
        SHADOW_BEAMON("Beamon das Sombras", "shadowbeamon"), // Beamon das Sombras (Warden)
        SPIRIT_FOX("Raposa Espiritual", "spiritfox"), // Raposa Espiritual
        DAVID_DEER("Cervo de David", "daviddeer"); // Cervo de David
*/
        private final String displayName;
        private final String BEAST_UNIQUE_IDENTIFIER; // BUID

        Type(String BUID, String displayName) {
            this.BEAST_UNIQUE_IDENTIFIER = BUID;
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getUniqueId() {
            return BEAST_UNIQUE_IDENTIFIER;
        }

        public boolean equals(String id) {
            if (id == null) return false;
            return BEAST_UNIQUE_IDENTIFIER.equals(id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(BEAST_UNIQUE_IDENTIFIER);
        }
    }

    public enum Mode {
        ATTACK,
        DEFENSE,
        FOLLOW;
    }
}
