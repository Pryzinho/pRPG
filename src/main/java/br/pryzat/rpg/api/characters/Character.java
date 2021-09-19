package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.characters.classes.Clazz;
import br.pryzat.rpg.api.characters.skills.Skills;
import br.pryzat.rpg.api.characters.stats.Stats;
import br.pryzat.rpg.utils.ActionBar;
import br.pryzat.rpg.utils.ItemBuilder;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.*;

public class Character {
    private JavaPlugin plugin;
    private UUID uuid;
    private Player player;
    private String dateOfBirth; // (dd/MM/yyyy) > 01/01/2000
    private int max_hp, max_mana;
    private int hp, mana;
    private Clazz clazz;
    private Stats stats;
    private Level level;
    private Skills skills;

    public Character(UUID uuid, JavaPlugin plugin) {
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
    }

    public void selectClazz() {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&bSelecione sua classe..."));
        List<String> slore = new ArrayList<>();
        slore.add("&fClasse focada em ataque, defesa, hp e absorção de dano.");
        slore.add("Atributos iniciais: Força: 00, Resistência: 00");
        ItemStack swordsman = ItemBuilder.create("&f&lCavaleiro", Material.IRON_SWORD, slore);
        List<String> mlore = new ArrayList<>();
        slore.add("&bMAGAO");
        ItemStack mage = ItemBuilder.create("&b&lMago", Material.STICK, mlore);
        List<String> suplore = new ArrayList<>();
        suplore.add("&aEU CURO E SOU CHATO PRA K7");
        ItemStack support = ItemBuilder.create("&a&lSacerdote", Material.SLIME_BALL, suplore);
        // 11, 14, 17
        inv.setItem(10, swordsman);
        inv.setItem(13, mage);
        inv.setItem(16, support);

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
