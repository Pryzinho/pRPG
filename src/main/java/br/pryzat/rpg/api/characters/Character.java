package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.api.characters.skills.Skill;
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
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
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
    private double MAX_HP, MAX_MANA;
    private double HEALTH, MANA;
    private ClazzType clazz;
    private Attributes attributes;
    private final Level level;
    private HashMap<String, Skill> skills;
    private int skillPoints;
    private final Immunities immunities;

    public Character(UUID uuid, RpgMain plugin) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.skillPoints = 0;
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
        this.skills = new HashMap<>();
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

    public UUID getUUID() {
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

    public double getMaxHealth() {
        return MAX_HP;
    }

    public void setMaxHealth(double maxhealth) {
        this.MAX_HP = maxhealth;
        updateGraphics();
    }

    public double getHealth() {
        return HEALTH;
    }

    public void setHealth(double health) {
        this.HEALTH = health;
        updateGraphics();
    }

    public void addHealth(double health) {
        this.HEALTH += health;
        updateGraphics();
    }

    public void remHealth(double health) {
        this.HEALTH -= health;
        updateGraphics();
    }

    public double getMaxMana() {
        return MAX_MANA;
    }

    public void setMaxMana(double maxmana) {
        this.MAX_MANA = maxmana;
        updateGraphics();
    }

    public double getMana() {
        return MANA;
    }

    public void setMana(double MANA) {
        this.MANA = MANA;
        updateGraphics();
    }

    public void addMana(double mana) {
        this.MANA += mana;
        updateGraphics();
    }

    public void remMana(double mana) {
        this.MANA -= mana;
        updateGraphics();
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


    // Graphics area, frontend
    public void updateGraphics() {
        if (getPlayer() == null) return;
        if (!getPlayer().isOnline()) return;
        ActionBar.sendMessage(getPlayer(), "&cHP&f: " + (int)getHealth() + "&f/&c" + (int)getMaxHealth() + " &bMana&f: " + (int)getMana() + "&f/&b" + (int)getMaxMana(), Integer.MAX_VALUE, plugin);
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
