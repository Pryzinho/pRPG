package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.classes.Beast;
import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.characters.stats.Immunities;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.items.CustomItem;
import br.pryzat.rpg.api.items.ItemManager;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.utils.PryConfig;
import br.pryzat.rpg.utils.ActionBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Character {
    private final RpgMain plugin;
    private final UUID uuid;
    private Player player;
    private String dateOfBirth; // (dd/MM/yyyy) > 01/01/2000
    private ClazzType clazz;
    private Beast beast;
    private Attributes attributes;
    private final Level level;
    private HashMap<String, Skill> skills;
    private int skillPoints;
    private final Immunities immunities;

    public Character(UUID uniqueuid, RpgMain plugin) {
        this.plugin = plugin;
        this.uuid = uniqueuid;
        this.skillPoints = 0;
        this.player = Bukkit.getPlayer(uniqueuid);
        this.attributes = new Attributes();
        this.immunities = new Immunities();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setCalendar(new GregorianCalendar());
        this.dateOfBirth = sdf.format(new Date(System.currentTimeMillis()));
        level = new Level(plugin, uniqueuid);
        String NEWBIE_GROUP = (plugin.getConfigManager().getYml().getString("NEWBIE_GROUP") == null) ? plugin.getConfigManager().getYml().getString("NEWBIE_GROUP") : "iniciante";
        if (level.get() <= 20) {
            if (player == null) {
                if (!plugin.getPermissionsManager().playerInGroup(null, Bukkit.getOfflinePlayer(uniqueuid), NEWBIE_GROUP)) {
                    plugin.getPermissionsManager().playerAddGroup(null, Bukkit.getOfflinePlayer(uniqueuid), NEWBIE_GROUP);
                }
            } else {
                if (!plugin.getPermissionsManager().playerInGroup(player, NEWBIE_GROUP)) {
                    plugin.getPermissionsManager().playerAddGroup(player, NEWBIE_GROUP);
                }
            }
        } else {
            if (player == null || !player.isOnline()) {
                if (plugin.getPermissionsManager().playerInGroup(null, Bukkit.getOfflinePlayer(uniqueuid), NEWBIE_GROUP)) {
                    plugin.getPermissionsManager().playerRemoveGroup(null, Bukkit.getOfflinePlayer(uniqueuid), NEWBIE_GROUP);
                }
            } else {
                if (plugin.getPermissionsManager().playerInGroup(player, NEWBIE_GROUP)) {
                    plugin.getPermissionsManager().playerRemoveGroup(player, NEWBIE_GROUP);
                }
            }
        }
        level.syncSet(21);
        this.skills = new HashMap<>();
        this.beast = new Beast(plugin, this, Beast.Type.NONE);

    }

    public ClazzType getClazz() {
        return clazz;
    }

    public void setClazz(ClazzType clazz) {
        this.clazz = clazz;
        attributes = new Attributes(clazz.getAttributes());
        setMaxHealth(50 + clazz.getAttributes().getResistance());
        setHealth(getMaxHealth());
        setMaxMana(20);
        setMana(20);
        //skills = clazz.getSkills();
        this.clazz.giveInitialItens(getPlayer());

    }

    /*
    YML Class Info
    SWORDSMAN: <- ClassType
      displayname: "&f&lCavaleiro"
      material: IRON_SWORD
      description:
        - Lore 1
        - Lore 2
        - Lore 3
    */
    public void selectClazz() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, Component.text(PryColor.color("&bSelecione sua classe...")));
        PryConfig config = plugin.getConfigManager().getYml();
        Set<String> clazzes = config.getSection("classes");
        for (int i = 0; i < clazzes.size(); i++) {
            String key = (String) clazzes.toArray()[i];
            CustomItem ci = ItemManager.getItemFromPath(config, "classes." + key + ".material");
            ci.setName(config.getString("classes." + key + ".displayName"));
            ci.setLore((List<String>) config.getList("classes." + key + ".description"));
            ci.hideEnchants(true);
            ci.hideAttributes(true);
            String codeclass = key.toLowerCase();
            ci.getDataManager().set(new NamespacedKey(plugin, "rpg.representative.item"), PersistentDataType.STRING, codeclass);
            inv.setItem(10 + i, ci.toItemStack());
        }
        player.openInventory(inv);
    }

    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        Player temp = Bukkit.getPlayer(getUUID());
        if (temp == null || !temp.isOnline()) {
            return null;
        }
        return temp;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /* Atributos Naturais */
    public double getMaxHealth() {
        return attributes.getMaxHealth();
    }

    public double getHealth() {
        return attributes.getHealth();
    }

    public void setMaxHealth(double maxhealth) {
        attributes.setMaxHealth(maxhealth);
        updateGraphics();
    }

    public void setHealth(double health) {
        attributes.setHealth(health);
        checkHealth();
        updateGraphics();

    }

    public void addHealth(double health) {
        attributes.addHealth(health);
        checkHealth();
        updateGraphics();
    }

    public double getMaxMana() {
        return attributes.getMaxMana();
    }

    public double getMana() {
        return attributes.getMana();
    }

    public void setMaxMana(double maxmana) {
        attributes.setMaxMana(maxmana);
        updateGraphics();
    }

    public void setMana(double MANA) {
        attributes.setMana(MANA);
        updateGraphics();
    }

    public void addMana(double mana) {
        attributes.addMana(mana);
        updateGraphics();
    }

    public void checkHealth() {
        if (attributes.getHealth() <= 0) {
            attributes.setHealth(0);
            if (getPlayer() != null) {
                getPlayer().damage(getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }
        } else if (attributes.getHealth() > attributes.getMaxHealth()) {
            attributes.setHealth(attributes.getMaxHealth());
        }
    }

    // Skill Development
    public HashMap<String, Skill> getSkills() {
        return skills;
    }

    /**
     * @param suid Skill Unique Identifier
     * @return Retorna a skill desejada, ou, retorna null se o jogador não tiver a skill.
     */
    @Nullable
    public Skill getSkill(String suid) {
        return skills.get(suid);
    }

    public boolean hasSkill(String suid) {
        for (String id : skills.keySet()) {
            if (suid.equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param trigger Pessoal que executou a ação, null em caso de nao ter sido alguem.
     * @param suid    Skill Unique Identifier
     * @param silent  Se true não avisa ao jogador que a habilidade dele foi removida, se false, avisa ao jogador sobre sua perda.
     */
    public void removeSkill(@Nullable Player trigger, String suid, boolean silent) {
        if (hasSkill(suid)) {
            Player t = Bukkit.getPlayer(getUUID());
            if (t != null && t.isOnline()) {
                if (!silent) {
                    t.sendMessage(PryColor.color("&eSistema &f> &cSua habilidade " + getSkill(suid).getDisplayName() + "&c foi removida de sua conta&f."));
                }

            }
            skills.remove(suid);
            if (trigger != null && trigger.isOnline()) {
                trigger.sendMessage(PryColor.color("&eSistema &f> &aVocê removeu a habildiade " + getSkill(suid).getDisplayName() + " &ado jogador " + Bukkit.getOfflinePlayer(getUUID()).getName() + "&f."));
            }
        }
    }

    public void addSkill(Player trigger, String skilluid, int level, boolean silent) {
        if (hasSkill(skilluid)) {
            if (trigger != null && trigger.isOnline()) {
                trigger.sendMessage(PryColor.color("&eSistema &f> &aO Jogador já possui está habilidade&f, &amas o novo nivel dela foi atualizado&f."));
            }
            Player t = Bukkit.getPlayer(getUUID());
            if (t != null && t.isOnline()) {
                if (!silent) {
                    t.sendMessage(PryColor.color("&eSistema &f> &cSua habilidade " + getSkill(skilluid).getDisplayName() + "&c foi atualizada para nivel " + level + "&f."));
                }
            }
            Skill temp = getSkill(skilluid);
            temp.setLevel(level);
            skills.replace(skilluid, temp);
        } else {
            if (trigger != null && trigger.isOnline()) {
                trigger.sendMessage(PryColor.color("&eSistema &f> &aA habilidade " + getLearnableSkill(skilluid).getDisplayName() + " &ade nivel " + level + "&f, &afoi adicionado ao jogador " + Bukkit.getOfflinePlayer(getUUID()).getName() + "&f."));
            }
            Player t = Bukkit.getPlayer(getUUID());
            if (t != null && t.isOnline()) {
                if (!silent) {
                    t.sendMessage(PryColor.color("&eSistema &f> &cVocê adquiriu " + getSkill(skilluid).getDisplayName() + "&c de nivel " + level + "&f."));
                }
            }
            Skill temp = getLearnableSkill(skilluid);
            temp.setOwner(this);
            temp.setLevel(level);
            skills.put(skilluid, temp);
        }
    }

    public Skill getLearnableSkill(String suid) {
        return RPG.getLearnablesSkills().get(suid);
    }


    public int getSkillPoints() {
        return this.skillPoints;
    }

    public void addSkillPoints(int points) {
        this.skillPoints += points;
    }

    public void remSkillPoints(int points) {
        this.skillPoints -= points;
    }

    public void setSkillPoints(int points) {
        this.skillPoints = points;
    }
// Skill Development


    public Attributes getAttributes() {
        return attributes;
    }

    public int getLevel() {
        return level.get();
    }

    public Level getLevelManager() {
        return level;
    }


    /**
     * @return Retorna uma classe de gerenciamento de imunidades do jogador, leia a classe para mais informações.
     */
    public Immunities getImmunities() {
        return immunities;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Beast getBeast() {
        return this.beast;
    }

    public boolean hasBeast() {
        return beast.getType() != Beast.Type.NONE;
    }

    /**
     * Verifica se o jogador tem uma besta e ela está invocada
     *
     * @return Retorna verdadeiro se o jogador estiver com sua besta invocada
     */
    public boolean isWithBeast() {
        return hasBeast() && getBeast().isInvoked();
    }


    // Graphics area, frontend
    public void updateGraphics() {
        if (getPlayer() == null) return;
        ActionBar.sendMessage(getPlayer(), "&cHP&f: " + (int) getHealth() + "&f/&c" + (int) getMaxHealth() + " &bMana&f: " + (int) getMana() + "&f/&b" + (int) getMaxMana(), Integer.MAX_VALUE, plugin);
    }


}
