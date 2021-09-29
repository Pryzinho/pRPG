package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.characters.classes.Clazz;
import br.pryzat.rpg.api.characters.skills.Skills;
import br.pryzat.rpg.api.characters.stats.Stats;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.ActionBar;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.utils.PryConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class Character {
    private RpgMain plugin;
    private UUID uuid;
    private Player player;
    private String dateOfBirth; // (dd/MM/yyyy) > 01/01/2000
    private int max_hp, max_mana;
    private int hp, mana;
    private Clazz clazz;
    private Stats stats;
    private Level level;
    private Skills skills;

    public Character(UUID uuid, RpgMain plugin) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setCalendar(new GregorianCalendar());
        this.dateOfBirth = sdf.format(new Date(System.currentTimeMillis()));
        level = new Level(uuid);
        level.set(21);
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
        setMaxHealth(50 + clazz.getStats().getResistance());
        setHealth(getMaxHealth());
        setMaxMana(20);
        setMana(20);
        skills = clazz.getSkills();
        stats = clazz.getStats();
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
            ItemStack is = new ItemStack(Material.valueOf(config.getString("classes." + key + ".material")));
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(PryColor.color(config.getString("classes." + key + ".displayName")));
            im.setLore((List<String>) config.getList("classes." + key + ".description"));
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            is.setItemMeta(im);
            net.minecraft.world.item.ItemStack nis = CraftItemStack.asNMSCopy(is);
            NBTTagCompound nbt = nis.getOrCreateTag();
            nbt.setString("rpg.representative.item", "clazz." + key.toLowerCase());
            inv.setItem(10 + i, CraftItemStack.asBukkitCopy(nis));
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
        return max_hp;
    }

    public void setMaxHealth(int maxhealth) {
        this.max_hp = maxhealth;
        updateGraphics();
    }

    public int getHealth() {
        return hp;
    }

    public void setHealth(int health) {
        this.hp = health;
        updateGraphics();
    }

    public void addHealth(int health) {
        this.hp += health;
        updateGraphics();
    }

    public void remHealth(int health) {
        this.hp -= health;
        updateGraphics();
    }

    public int getMaxMana() {
        return max_mana;
    }

    public void setMaxMana(int maxmana) {
        this.max_mana = maxmana;
        updateGraphics();
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
        updateGraphics();
    }

    public void addMana(int mana) {
        this.mana += mana;
        updateGraphics();
    }

    public void remMana(int mana) {
        this.mana -= mana;
        updateGraphics();
    }

    public Skills getSkills() {
        return skills;
    }

    public Stats getStats() {
        return stats;
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


    // Graphics area, frontend
    public void updateGraphics() {
        if (getPlayer() == null) return;
        if (!getPlayer().isOnline()) return;
        ActionBar.sendMessage(getPlayer(), "&cHP&f: " + getHealth() + "&f/&c" + getMaxHealth() + " &bMana&f: " + getMana() + "&f/&b" + getMaxMana(), Integer.MAX_VALUE, plugin);
    }
}
