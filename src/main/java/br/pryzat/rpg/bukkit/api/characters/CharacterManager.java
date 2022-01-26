package br.pryzat.rpg.bukkit.api.characters;

import br.pryzat.rpg.bukkit.api.RPG;
import br.pryzat.rpg.bukkit.api.characters.classes.Clazz;
import br.pryzat.rpg.bukkit.api.characters.classes.ClazzType;
import br.pryzat.rpg.bukkit.api.characters.skills.Skill;
import br.pryzat.rpg.bukkit.api.characters.stats.Stats;
import br.pryzat.rpg.bukkit.main.RpgMain;
import br.pryzat.rpg.bukkit.utils.PryConfig;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class CharacterManager {
    private RpgMain plugin;
    private PryConfig charactersyml;
    private HashMap<UUID, Character> characters = new HashMap<>();

    public CharacterManager(RpgMain plugin) {
        this.plugin = plugin;
        charactersyml = new PryConfig(plugin, "characters.yml");
        charactersyml.saveDefaultConfig();
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
                if (ch.getSkills().toList() != null) {
                    for (Skill skill : ch.getSkills().toList()) {
                        if (skill != null) {
                            charactersyml.set(uuid.toString() + ".skills." + skill.getUniqueId(), skill.getLevel());
                        }
                    }
                }
                charactersyml.set(uuid.toString() + ".class", ch.getClazz().getClassType().toString());
                charactersyml.set(uuid.toString() + ".stats.strength", ch.getStats().getStrength());
                charactersyml.set(uuid.toString() + ".stats.inteligency", ch.getStats().getInteligency());
                charactersyml.set(uuid.toString() + ".stats.velocity", ch.getStats().getVelocity());
                charactersyml.set(uuid.toString() + ".stats.resistance", ch.getStats().getResistance());
                charactersyml.set(uuid.toString() + ".level", ch.getLevel());
                charactersyml.set(uuid.toString() + ".experience", ch.getLevelManager().getExp());
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
                ClazzType classtype = ClazzType.valueOf(charactersyml.getString(uuid.toString() + ".class"));
                Stats stats = new Stats(charactersyml.getInt(uuid.toString() + "stats.strength"), charactersyml.getInt(uuid.toString() + "stats.inteligency"), charactersyml.getInt(uuid.toString() + "stats.velocity"), charactersyml.getInt(uuid.toString() + "stats.resistance"));
                ch.setClazz(new Clazz(plugin, classtype));
                ch.getClazz().setStats(stats);
                ch.getLevelManager().set(charactersyml.getInt(uuid.toString() + ".level"));
                ch.getLevelManager().setExp(charactersyml.getLong(uuid.toString() + ".experience"));
                for (String skill : charactersyml.getSection(key + ".skills")) {
                    if (RPG.getRegistredSkill(skill) != null) {
                        ch.addSkill(skill, charactersyml.getInt(key + ".skills." + skill));
                    }
                }
                characters.put(uuid, ch);
            }
        }
    }

    public HashMap<UUID, Character> getCharacters() {
        return characters;
    }

    public PryConfig getCharactersYml() {
        return charactersyml;
    }

}
