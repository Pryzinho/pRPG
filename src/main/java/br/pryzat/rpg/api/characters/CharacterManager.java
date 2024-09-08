package br.pryzat.rpg.api.characters;

import br.pryzat.rpg.api.characters.classes.BaseClass;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.events.bukkit.character.CharacterChooseClassEvent;
import br.pryzat.rpg.api.events.bukkit.character.CharacterLevelChangeEvent;
import br.pryzat.rpg.api.items.ItemHandler;
import br.pryzat.rpg.api.items.types.Item;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.utils.PryConfig;
import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CharacterManager implements Listener {
    private RpgMain plugin;
    private HashMap<UUID, Character> characters = new HashMap<>();


    private PryConfig charactersyml; // Current data storage method

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


    public void selectClass(Player target) {
        // depois talvez transformar o inventory em algo que ja ta criado e usar o metodo so pra abrir ele.
        // como ta aqu isemrpe que rodar um novo inventory é criado.
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, Component.text(PryColor.color("&bSelecione sua classe...")));
        PryConfig config = plugin.getConfigManager().getYml();
        Set<String> clazzes = config.getSection("classes");
        for (int i = 0; i < clazzes.size(); i++) {
            String key = (String) clazzes.toArray()[i];
           Item CLASS_ITEM = ItemHandler.getItemFromPath(config, "classes." + key + ".material")
                    .displayName(config.getString("classes." + key + ".displayName"))
                    .stringLore((List<String>) config.getList("classes." + key + ".description"))
                    .setCustomModelData(config.getInt("classes." + key + ".custom_model_data"))
                    .hideEnchants(true)
                    .hideAttributes(true);
            CLASS_ITEM.getCustomPersistentData().setData(plugin.getItemHandler().REPRESENTATIVE_ITEM_NAMESPACE(), PersistentDataType.STRING, key.toLowerCase());
            inv.setItem(10 + i, CLASS_ITEM);

        }
        target.openInventory(inv);
    }

    public void saveCharacters() {
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
            if (ch.getClazz() == null) {
                ch.setClazz(BaseClass.SWORDSMAN);
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

    public void loadCharacters() {
        for (String key : charactersyml.getSection("")) {
            if (key != null) {
                UUID uuid = UUID.fromString(key);
                Character ch = new Character(uuid, plugin);
                ch.setPlayer(Bukkit.getPlayer(uuid));
                ch.setDateOfBirth(charactersyml.getString(key + ".dateOfBirth"));
                ch.setSkillPoints(charactersyml.getInt(uuid + ".skillsPoints"));
                BaseClass classtype = BaseClass.valueOf(charactersyml.getString(uuid + ".class"));
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
    private void onJoin(AuthenticateEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Character ch = getCharacter(uuid);
        if (ch == null) {
            createCharacter(uuid);
            ch = getCharacter(uuid);
        }
    }


    @EventHandler
    private void onSelectedClass(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getCurrentItem() == null) return;
        ItemStack is = e.getCurrentItem();
        Character ch = getCharacter(p.getUniqueId());
        plugin.getClassesManager().getAllClasses().forEach(c -> {
            if (plugin.getItemHandler().matchInNamespace(plugin.getItemHandler().REPRESENTATIVE_ITEM_NAMESPACE(), is, c.getUniqueId())) {
                BaseClass newClass = new BaseClass(c);
                CharacterChooseClassEvent ccce = new CharacterChooseClassEvent(ch, newClass);
                Bukkit.getPluginManager().callEvent(ccce);
                if (!ccce.isCancelled()) {
                    ch.setClazz(newClass);
                }
                e.setCancelled(true);
                p.closeInventory();
            }
        });
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepInventory(true);
        e.setShouldDropExperience(false);
        e.getDrops().clear();
        Character c = getCharacter(e.getEntity().getUniqueId());
        /*
        Integrar no futuro
        if (c.isWithBeast()) {
            c.getBeast().despawn();
        }
         */
        if (e.getEntity().getKiller() == null) return;
        Player p = e.getEntity();
        Player k = e.getEntity().getKiller();
        assert k != null;
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
        if (t == null) {
            return;
        }
        if (e.getCause() == CharacterLevelChangeEvent.Cause.SET && t.getPlayer() != null) {
            Player player = t.getPlayer();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 30f, 30f);
            player.sendMessage(PryColor.color("&aSeu nivel foi definido para " + e.getNewLevel()));
        }
        if (e.getCause() == CharacterLevelChangeEvent.Cause.ADD) {
            Player p = Bukkit.getPlayer(e.getTrigger());
            if (p != null && p.isOnline()) {
                p.sendMessage(PryColor.color("&eSistema &f> &aVocê subiu de nível."));
                t.getAttributes().addResistance((e.getNewLevel() - t.getLevel()) * 2);
            }
        }
        if (t.getLevel() % 2 == 0 && e.getCause() == CharacterLevelChangeEvent.Cause.REMOVE) {
            t.remSkillPoints(1);
            return;
        }
        if (e.getCause() == CharacterLevelChangeEvent.Cause.ADD) {
            if (e.getNewLevel() % 2 == 0) {
                t.addSkillPoints((e.getNewLevel() - e.getOldLevel()) / 2);
            } else {
                t.addSkillPoints((int) (((e.getNewLevel() - e.getOldLevel()) / 2) + 0.5));
            }
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

    public HashMap<UUID, Character> getCharacters() {
        return characters;
    }

    public PryConfig getCharactersYml() {
        return charactersyml;
    }

}
