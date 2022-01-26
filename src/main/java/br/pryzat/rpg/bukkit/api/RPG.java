package br.pryzat.rpg.bukkit.api;

import br.pryzat.rpg.bukkit.api.characters.skills.SUID;
import br.pryzat.rpg.bukkit.api.events.Event;
import br.pryzat.rpg.bukkit.builds.skills.mage.Fireball;
import br.pryzat.rpg.bukkit.builds.skills.swordsman.Perseguir;
import br.pryzat.rpg.bukkit.builds.skills.swordsman.Puxao;
import br.pryzat.rpg.bukkit.builds.skills.swordsman.Stomper;
import br.pryzat.rpg.bukkit.main.RpgMain;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.util.*;

public class RPG {
    // Skill Area
    private static HashMap<SUID, Class> registredskills = new HashMap<>();
    // Clazz Skills Branches
    // Events Area
    private static List<String> euids = new ArrayList<>();
    private static HashMap<String, Event> events = new HashMap<>();
    // Items Area
    private static List<String> iuids = new ArrayList<>();


    public static void registerAllItems() {
        // Swordsman
        iuids.add("initial.swordsman.helmet");
        iuids.add("initial.swordsman.chestplate");
        iuids.add("initial.swordsman.leggings");
        iuids.add("initial.swordsman.boots");
        iuids.add("initial.swordsman.sword");
        iuids.add("initial.swordsman.shield");
    }


    /**
     * Registra todas as habilidades criadas, deve ser implementado no metodo onEnable da main
     */
    public static void registerAllSkills() {
        registerSUID(new SUID("stomper", "&aSalto Esmagador", Material.SLIME_BALL, Arrays.asList("&bPula em determinada altura e cai causando dano aos inimigos proximos")), Stomper.class);
        registerSUID(new SUID("perseguir", "&aPerseguição Imparavel", Material.BLAZE_ROD, Arrays.asList("&cELE PULA E PEI PA PEGOU")), Perseguir.class);
        registerSUID(new SUID("fireball", "&cSou eu bola de fogo", Material.FIRE_CHARGE, Arrays.asList("&cMeu nao enxe")), Fireball.class);
        registerSUID(new SUID("puxao", "&bVa pa onde?", Material.IRON_SHOVEL, Arrays.asList("Se correr o bixo pega, se ficar o bixo come")), Puxao.class);
    }

    /**
     * Permite você registrar um Skill Unique Identifier.
     *
     * @param suid
     * @param clazz
     */
    private static void registerSUID(SUID suid, Class clazz) {
        registredskills.put(suid, clazz);
    }

    public static SUID getSUID(String suid) {
        for (SUID s : registredskills.keySet()) {
            if (s.toString().equalsIgnoreCase(suid)) return s;
        }
        return null;
    }

    /**
     * @param suid
     * @return Um Constructor da skill, retorna null caso o suid nao esteja registrado.
     */
    public static Constructor getRegistredSkill(String suid) {
        for (SUID s : registredskills.keySet()) {
            if (s.toString().equals(suid)) {
                try {
                    return registredskills.get(s).getConstructor(RpgMain.class, UUID.class, int.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    public static Set<SUID> getAllSUIDS() {
        return registredskills.keySet();
    }

    /**
     * Registra um Event Unique Identifier na lista.
     *
     * @param euid
     */
    public static void registerEUID(String euid) {
        if (!verifyEUID(euid)) {
            euids.add(euid);
        }
    }

    /**
     * Verifica se um EUID está na lista.
     *
     * @param euid
     * @return Retorna se um euid ta ou não na lista
     */
    public static boolean verifyEUID(String euid) {
        return euids.contains(euid);
    }

    /**
     * @param euid
     * @return Retorna a classe abastrata Event de determinado EUID
     */
    public static Event getEvent(String euid) {
        return events.get(euid);
    }
}
