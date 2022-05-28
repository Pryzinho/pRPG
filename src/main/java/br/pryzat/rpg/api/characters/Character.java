package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.characters.classes.Clazz;
import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.api.characters.skills.Skills;
import br.pryzat.rpg.api.characters.stats.Immunities;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.items.CustomItem;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.utils.PryConfig;
import br.pryzat.rpg.utils.ActionBar;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Character {
    private final RpgMain plugin;
    private final LuckPerms lp;
    private final UUID uuid;
    private Player player;
    private String dateOfBirth; // (dd/MM/yyyy) > 01/01/2000
    private int MAX_HP, MAX_MANA;
    private int HEALTH, MANA;
    private ClazzType clazz;
    private Attributes attributes;
    private final Level level;
    private Skills skills;
    private final Immunities immunities;

    public Character(UUID uuid, RpgMain plugin) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.lp = plugin.getLuckPerms();
        this.immunities = new Immunities();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setCalendar(new GregorianCalendar());
        this.dateOfBirth = sdf.format(new Date(System.currentTimeMillis()));
        level = new Level(plugin, uuid);
        if (level.get() <= 20) {
            User user;
            if (player == null) {
                CompletableFuture<User> userFuture = lp.getUserManager().loadUser(uuid);
                try {
                    user = userFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    user = null;
                }
                if (user != null) {
                    assert user != null;
                    InheritanceNode node = InheritanceNode.builder("iniciante").value(true).build();
                    user.data().add(node);
                    lp.getUserManager().saveUser(user);
                }
            } else {
                if (player.isOnline()) {
                    user = lp.getUserManager().getUser(uuid);
                } else {
                    CompletableFuture<User> userFuture = lp.getUserManager().loadUser(uuid);
                    try {
                        user = userFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        user = null;
                    }
                }
                if (!player.hasPermission("group.iniciante") && user != null) {
                    assert user != null;
                    InheritanceNode node = InheritanceNode.builder("iniciante").value(true).build();
                    user.data().add(node);
                    lp.getUserManager().saveUser(user);
                }
            }
        } else {
            User user;
            if (player == null) {
                OfflinePlayer offp = Bukkit.getOfflinePlayer(uuid);
                CompletableFuture<User> userFuture = lp.getUserManager().loadUser(uuid);
                try {
                    user = userFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    user = null;
                }
                if (user != null) {
                    assert user != null;
                    InheritanceNode node = InheritanceNode.builder("iniciante").value(true).build();
                    user.data().remove(node);
                    lp.getUserManager().saveUser(user);
                }
            } else {
                if (player.isOnline()) {
                    user = lp.getUserManager().getUser(uuid);
                } else {
                    CompletableFuture<User> userFuture = lp.getUserManager().loadUser(uuid);
                    try {
                        user = userFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        user = null;
                    }
                }
                if (player.hasPermission("group.iniciante") && user != null) {
                    assert user != null;
                    InheritanceNode node = InheritanceNode.builder("iniciante").value(true).build();
                    user.data().remove(node);
                    lp.getUserManager().saveUser(user);
                }
            }
        }
        level.set(21);
    }

    public ClazzType getClazz() {
        return clazz;
    }

    public void setClazz(ClazzType clazz) {
        this.clazz = clazz;
        setMaxHealth(50 + clazz.getAttributes().getResistance());
        setHealth(getMaxHealth());
        setMaxMana(20);
        setMana(20);
        //skills = clazz.getSkills();
        attributes = new Attributes(clazz.getAttributes());
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
        Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&bSelecione sua classe..."));
        PryConfig config = plugin.getConfigManager().getYml();
        Set<String> clazzes = config.getSection("classes");
        for (int i = 0; i < clazzes.size(); i++) {
            String key = (String) clazzes.toArray()[i];
            CustomItem ci = new CustomItem(Material.valueOf(config.getString("classes." + key + ".material")));
            ci.setName(config.getString("classes." + key + ".displayName"));
            ci.setLore((List<String>) config.getList("classes." + key + ".description"));
            ci.hideEnchants(true);
            ci.hideAttributes(true);
            String codeclass = key.toLowerCase();
            ci.getDataManager().set(NamespacedKey.fromString("rpg.representative.item"), PersistentDataType.STRING, codeclass);
            inv.setItem(10 + i, ci.toItemStack());
        }
        player.openInventory(inv);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
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

    public int getMaxHealth() {
        return MAX_HP;
    }

    public void setMaxHealth(int maxhealth) {
        this.MAX_HP = maxhealth;
        updateGraphics();
    }

    public int getHealth() {
        return HEALTH;
    }

    public void setHealth(int health) {
        this.HEALTH = health;
        updateGraphics();
    }

    public void addHealth(int health) {
        this.HEALTH += health;
        updateGraphics();
    }

    public void remHealth(int health) {
        this.HEALTH -= health;
        updateGraphics();
    }

    public int getMaxMana() {
        return MAX_MANA;
    }

    public void setMaxMana(int maxmana) {
        this.MAX_MANA = maxmana;
        updateGraphics();
    }

    public int getMana() {
        return MANA;
    }

    public void setMana(int MANA) {
        this.MANA = MANA;
        updateGraphics();
    }

    public void addMana(int mana) {
        this.MANA += mana;
        updateGraphics();
    }

    public void remMana(int mana) {
        this.MANA -= mana;
        updateGraphics();
    }

    public Skills getSkills() {
        return skills;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public int getLevel() {
        return level.get();
    }

    public Level getLevelManager() {
        return level;
    }

    public void addSkill(String skilluid, int level) {
        getSkills().add(skilluid, uuid, level);
    }

    /**
     * @return Retorna uma classe de gerenciamento de imunidades do jogador, leia a classe para mais informações.
     */
    public Immunities getImmunities() {
        return immunities;
    }


    // Graphics area, frontend
    public void updateGraphics() {
        if (getPlayer() == null) return;
        if (!getPlayer().isOnline()) return;
        ActionBar.sendMessage(getPlayer(), "&cHP&f: " + getHealth() + "&f/&c" + getMaxHealth() + " &bMana&f: " + getMana() + "&f/&b" + getMaxMana(), Integer.MAX_VALUE, plugin);
    }

    public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    }
}
