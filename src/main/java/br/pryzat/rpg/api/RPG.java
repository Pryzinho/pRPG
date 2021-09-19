package br.pryzat.rpg.api;

import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.api.characters.skills.Branch;
import br.pryzat.rpg.api.characters.skills.Branches;
import br.pryzat.rpg.api.characters.skills.SUID;
import br.pryzat.rpg.api.events.Event;
import br.pryzat.rpg.builds.skills.*;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.util.*;

public class RPG {
    // Skill Area
    private static HashMap<SUID, Class> registredskills = new HashMap<>();
    // Clazz Skills Branches
    private static HashMap<ClazzType, Branches> classbranches = new HashMap<>();
    // Events Area
    private static List<String> euids = new ArrayList<>();
    private static HashMap<String, Event> events = new HashMap<>();



    /**
     * @param clazzType Classe escolhida
     * @return Retorna as ramificações de determinada classe
     */
    public static Branches getBranches(ClazzType clazzType) {
        return classbranches.get(clazzType);
    }

    /**
     * @param clazztype A classe escolhida
     * @param index     Ramificação que deve ser escolhida, valores disponiveis: 1 e 2
     * @return Uma ramificação de acordo com a classe escolhida e a index
     */
    public static Branch getBranch(ClazzType clazztype, int index) {
        return classbranches.get(clazztype).getBranch(index);
    }

    /**
     * Registra todas a ramificações de habilidades de todas as classes
     */
    public static void registerAllClassesSkillsBranches() {
        registerBranches(ClazzType.SWORDSMAN,
                new Branches(Arrays.asList(
                        new Branch("&cAtaque", Material.IRON_SWORD, 11, Arrays.asList(getSUID("stomper"))),
                        new Branch("&bDefesa", Material.SHIELD, 15, Arrays.asList(getSUID("stomper")))
                ))
        );
    }

    /**
     * Registra as 2 duas ramificações de habilidades existente de uma classe
     *
     * @param clazztype Classe escolhida
     * @param branches  Branches utilizada, cria uma utilizando o construtor
     */
    public static void registerBranches(ClazzType clazztype, Branches branches) {
        classbranches.put(clazztype, branches);
    }

    /**
     * Registra todas as habilidades criadas, deve ser implementado no metodo onEnable da main
     */
    public static void registerAllSkills() {
        registerSUID(new SUID("stomper", "&aSalto Esmagador", Material.SLIME_BALL, Arrays.asList("&bPula em determinada altura e cai causando dano aos inimigos proximos")), Stomper.class);
 registerSUID(new SUID("perseguir", "&aSalto Esmagador", Material.BLAZE_ROD, Arrays.asList("&cELE PULA E PEI PA PEGOU")), Perseguir.class);
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

    public static SUID getSUID(String suid){
        for (SUID s: registredskills.keySet()){
            if (s.toString().equalsIgnoreCase(suid))return s;
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
    public static Set<SUID> getAllSUIDS(){
        return registredskills.keySet();
    }

    /**
     * Registra um Event Unique Identifier na lista.
     * @param euid
     */
    public static void registerEUID(String euid){
        if (!verifyEUID(euid)){
            euids.add(euid);
        }
    }

    /**
     * Verifica se um EUID está na lista.
     * @param euid
     * @return Retorna se um euid ta ou não na lista
     */
    public static boolean verifyEUID(String euid) {
        return euids.contains(euid);
    }

    /**
     *
     * @param euid
     * @return Retorna a classe abastrata Event de determinado EUID
     */
    public static Event getEvent(String euid){
        return events.get(euid);
    }
}
