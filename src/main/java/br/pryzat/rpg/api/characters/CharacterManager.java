package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.events.bukkit.character.CharacterChooseClassEvent;
import br.pryzat.rpg.api.events.bukkit.character.CharacterLevelChangeEvent;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.utils.PryConfig;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class CharacterManager implements Listener {
    private RpgMain plugin;
    private PryConfig charactersyml;
    private HashMap<UUID, Character> characters = new HashMap<>();

    public CharacterManager(RpgMain plugin) {
        this.plugin = plugin;
        charactersyml = new PryConfig(plugin, "characters.yml");
        charactersyml.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void createCharacter(UUID uuid) {
        characters.put(uuid, new Character(uuid, plugin));
    }

    public Character getCharacter(UUID uuid) {
        return characters.get(uuid);
    }

    public void saveCharacters() {
        if (characters.keySet() != null) {
            for (UUID uuid : characters.keySet()) {
                Character ch = characters.get(uuid);
                charactersyml.set(uuid.toString() + ".dateOfBirth", ch.getDateOfBirth());
                charactersyml.set(uuid + ".skillsPoints", ch.getSkillPoints());
                if (ch.getSkills() != null) {
                    for (Skill skill : ch.getSkills().values()) {
                        if (skill != null) {
                            charactersyml.set(uuid + ".skills." + skill.getUniqueId(), skill.getLevel());
                        }
                    }
                }
                if (ch.getClazz() == null){
                    ch.setClazz(ClazzType.SWORDSMAN);
                }
                charactersyml.set(uuid + ".class", ch.getClazz().toString());
                charactersyml.set(uuid + ".immunities.skills", ch.getImmunities().checkSkills());
                charactersyml.set(uuid + ".immunities.damage.physical", ch.getImmunities().checkPhysicalDamage());
                charactersyml.set(uuid + ".immunities.damage.magic", ch.getImmunities().checkMagicDamage());
                charactersyml.set(uuid + ".stats.strength", ch.getAttributes().getStrength());
                charactersyml.set(uuid + ".stats.inteligency", ch.getAttributes().getInteligency());
                charactersyml.set(uuid + ".stats.velocity", ch.getAttributes().getVelocity());
                charactersyml.set(uuid + ".stats.resistance", ch.getAttributes().getResistance());
                charactersyml.set(uuid + ".level", ch.getLevel());
                charactersyml.set(uuid + ".experience", ch.getLevelManager().getExp());
            }
            charactersyml.saveConfig();
        }
    }

    public void loadCharacters() {
        for (String key : charactersyml.getSection("")) {
            if (key != null) {
                UUID uuid = UUID.fromString(key);
                Character ch = new Character(uuid, plugin);
                ch.setPlayer(Bukkit.getPlayer(uuid));
                ch.setDateOfBirth(charactersyml.getString(key + ".dateOfBirth"));
                ch.setSkillPoints(charactersyml.getInt(uuid + ".skillsPoints"));
                ClazzType classtype = ClazzType.valueOf(charactersyml.getString(uuid + ".class"));
                Attributes attributes = new Attributes(charactersyml.getInt(uuid + "stats.strength"), charactersyml.getInt(uuid.toString() + "stats.inteligency"), charactersyml.getInt(uuid.toString() + "stats.velocity"), charactersyml.getInt(uuid.toString() + "stats.resistance"));
                ch.setClazz(classtype);
                ch.setAttributes(attributes);
                ch.getLevelManager().set(charactersyml.getInt(uuid + ".level"));
                ch.getLevelManager().setExp(charactersyml.getLong(uuid + ".experience"));
                for (String skill : charactersyml.getSection(key + ".skills")) {
                    ch.addSkill(null, skill, charactersyml.getInt(key + ".skills." + skill), true);
                }
                ch.getImmunities().setSkills(charactersyml.getBoolean(uuid + ".immunities.skills"));
                ch.getImmunities().setPhysicalDamage(charactersyml.getBoolean(uuid + ".immunities.damage.physical"));
                ch.getImmunities().setMagicDamage(charactersyml.getBoolean(uuid + "immunities.damage.magic"));
                characters.put(uuid, ch);
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Character ch = getCharacter(uuid);
        if (ch == null) {
            createCharacter(uuid);
            ch = getCharacter(uuid);
        }
    }


    @EventHandler
    private void onSelectClazz(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        ItemStack is = e.getCurrentItem();
        Character ch = getCharacter(p.getUniqueId());
        if (e.getView().getTitle().equals(PryColor.color("&bSelecione sua classe..."))) {

            for (String key : plugin.getConfigManager().getYml().getSection("classes")) {
                if (is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin,"rpg.representative.item"), PersistentDataType.STRING)) {
                    if (key.toLowerCase().equals(is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin,"rpg.representative.item"), PersistentDataType.STRING))) {
                        ClazzType newClass = ClazzType.valueOf(key);
                        CharacterChooseClassEvent ccce = new CharacterChooseClassEvent(ch, newClass);
                        Bukkit.getPluginManager().callEvent(ccce);
                        if (!ccce.isCancelled()) {
                            ch.setClazz(newClass);
                        }
                        p.closeInventory();
                    }
                }
            }
            p.closeInventory();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepInventory(true);
        e.setShouldDropExperience(false);
        e.getDrops().clear();
        Character c = getCharacter(e.getEntity().getUniqueId());
        if (c.isWithBeast()){
            c.getBeast().despawn();
        }
        if (!(e.getEntity().getKiller() instanceof Player)) return;
        Player p = e.getEntity();
        Player k = e.getEntity().getKiller();
        Character killer = getCharacter(k.getUniqueId());
        killer.getLevelManager().addExp(10);


    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Character ch = getCharacter(p.getUniqueId());
        ch.setHealth(ch.getMaxHealth() * 0.10);
        ch.setMana(ch.getMaxMana() * 0.30);
    }

    @EventHandler
    public void onCharLevelled(CharacterLevelChangeEvent e) {
        Character t = getCharacter(e.getTrigger());
        if (t == null){
            return;
        }
        if (e.getNewLevel() > 100) {
            e.setCancelled(true);
            return;
        }
        if (e.getNewLevel() <= 0) {
            e.setCancelled(true);
            t.getLevelManager().set(1);
            return;
        }
        if (e.getCause() == CharacterLevelChangeEvent.Cause.ADD){
            Player p = Bukkit.getPlayer(e.getTrigger());
            if (p != null && p.isOnline()){
                p.sendMessage(PryColor.color("&eSistema &f> &aVocê subiu de nível."));
                t.getAttributes().addResistance((e.getNewLevel() - t.getLevel()) * 2);
            }
        }
        if (t.getLevel() % 2 == 0 && e.getCause() == CharacterLevelChangeEvent.Cause.REMOVE) {
            t.remSkillPoints(1);
            return;
        }
        if (e.getCause() == CharacterLevelChangeEvent.Cause.ADD && e.getNewLevel() % 2 == 0) {
            t.addSkillPoints(1);
            return;
        }
        if (e.getCause() == CharacterLevelChangeEvent.Cause.SET) {
            if (e.getNewLevel() > t.getLevel()) {
                // O nivel setado é maior que o antigo.
                int tempL = e.getNewLevel() - t.getLevel();
                if (tempL % 2 == 0) {
                    // Foram adicionados um numero par de leveis ao personagem.
                    t.addSkillPoints(tempL / 2);
                } else {
                    // Foram adicionadas um numero impar ao level do persoangem
                    t.addSkillPoints((int) ((tempL / 2) - 0.5));
                }
            } else if (e.getNewLevel() < t.getLevel()) {
                // O nivel setado é menor que o antigo
                int tempL = t.getLevel() - e.getNewLevel();
                if (tempL % 2 == 0) {
                    // Foi removidos numero par de level
                    t.remSkillPoints(tempL / 2);
                } else {
                    // Foi removido numero impar de level
                    t.remSkillPoints((int) ((tempL / 2) - 0.5));
                }
            }
            return;
        }
    }

    /**
     * Somente um teste, sera removido depois...
     * @param e
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void testBlockClass(CharacterChooseClassEvent e) {
        if (e.getClazz().equals(ClazzType.PRIEST)) {
            Player t = Bukkit.getPlayer(e.getTrigger().getUUID());
            if (t != null && t.isOnline()) {
                t.sendMessage(PryColor.color("&eSistema &f> &cNão foi possivel selecionar essa classe&f, &cvocê é um pecador safado&f!!! &b:D"));
            }
            e.setCancelled(true);
        }
    }

    public HashMap<UUID, Character> getCharacters() {
        return characters;
    }

    public PryConfig getCharactersYml() {
        return charactersyml;
    }

}
