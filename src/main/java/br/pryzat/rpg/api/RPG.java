package br.pryzat.rpg.api;

import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.events.Event;
import br.pryzat.rpg.builds.skills.swordsman.Stomper;

import java.util.*;

public class RPG {
    // Skill Area
    private static HashMap<String, Skill> learnableskills = new HashMap<>();
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
     * Registra todas as habilidades criadas, deve ser feito manualmente de acordo com as criações de habilidades.
     */
    public static void registerAllSkills() {
        /*
        registerSUID(new SUID("stomper", "&aSalto Esmagador", Material.SLIME_BALL, Arrays.asList("&bPula em determinada altura e cai causando dano aos inimigos proximos")), Stomper.class);
        registerSUID(new SUID("perseguir", "&aPerseguição Imparavel", Material.BLAZE_ROD, Arrays.asList("&cELE PULA E PEI PA PEGOU")), Perseguir.class);
        registerSUID(new SUID("fireball", "&cSou eu bola de fogo", Material.FIRE_CHARGE, Arrays.asList("&cMeu nao enxe")), Fireball.class);
        registerSUID(new SUID("puxao", "&bVa pa onde?", Material.IRON_SHOVEL, Arrays.asList("Se correr o bixo pega, se ficar o bixo come")), Puxao.class);
  */
        learnableskills.put("stomper", new Stomper(null, 1));
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

    public static HashMap<String, Skill> getLearnablesSkills() {
        return learnableskills;
    }

    public static void loadStaticAcces() {
        registerAllSkills();
        registerAllItems();
    }
}
