package br.pryzat.rpg.listeners;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.characters.classes.Beast;
import br.pryzat.rpg.api.characters.classes.BaseClass;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.events.EventManager;
import br.pryzat.rpg.api.events.bukkit.character.CharacterTargettedBySkillEvent;
import br.pryzat.rpg.builds.events.ColheitaMaldita;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.Announcer;
import br.pryzat.rpg.utils.ConfigManager;
import br.pryzat.rpg.utils.PryColor;
import com.nickuc.login.api.nLoginAPI;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerEvent implements Listener {
    private RpgMain main;
    private CharacterManager cm;
    private ConfigManager confm;
    private EventManager em;

    public PlayerEvent(RpgMain main) {
        this.main = main;
        this.cm = main.getCharacterManager();
        this.em = main.getEventManager();
        this.confm = main.getConfigManager();
    }

    //Listener responsavel pela soma de xp por mob morto pelo jogador ou outros...
    private NamespacedKey ENTITY_UID_KEY;
    private final String CONFIG_ENTITY_SETTINGS_PATH = "entity_settings";

    public void a(String txt) {
        Bukkit.getConsoleSender().sendMessage(txt);
    }
    @EventHandler
    public void onTryMoveInNullClazz(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (ch == null)return;
        if (ch.getClazz() == null && nLoginAPI.getApi().isAuthenticated(p.getName())) {
            p.sendMessage(Announcer.text("&eSistema &f> &cVocê ainda não selecionou sua classe&f,&c digite&f: &8/&bclasse"));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        a("EntityDeathEvent invoked");
        if (e.getEntity().getKiller() == null) return;
        a("Entity killer is a valid player");
        ENTITY_UID_KEY = new NamespacedKey(main, "pry.rpg.entity");
        PersistentDataContainer pdc = e.getEntity().getPersistentDataContainer();
        Player p = e.getEntity().getKiller();
        p.sendMessage("you're the killer");
        confm.getYml().saveDefaultConfig();
        confm.getYml().getList(CONFIG_ENTITY_SETTINGS_PATH)
                .stream()
                .forEach(t -> {
                    if (pdc.has(ENTITY_UID_KEY)) {
                        a("Custom Entity Dead");
                        if (confm.getYml().getString(CONFIG_ENTITY_SETTINGS_PATH + "." + t).equals(pdc.get(ENTITY_UID_KEY, PersistentDataType.STRING))) {
                            a("Custom Entity Recognized");
                            cm.getCharacter(p.getUniqueId()).getLevelManager().addExp(confm.getYml().getLong(CONFIG_ENTITY_SETTINGS_PATH + "." + t + ".experience"));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 40f, 40f); //Debugg test
                        }
                    } else {
                        a("Normal Entity Dead");
                        cm.getCharacter(p.getUniqueId()).getLevelManager().addExp((confm.getYml().getLong(CONFIG_ENTITY_SETTINGS_PATH + "." + e.getEntityType().toString().toLowerCase() + ".experience")));
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 40f, 40f); //Debugg test
                    }
                });
    }

    @EventHandler
    private void tempSkill(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (e.isSneaking()) {
            // if (ch.getSkills().get("stomper") == null) return;
            //  ch.getSkills().get(SkillType.STOMPER).preExecute();
            if (ch.getSkills().get("puxao") != null) {
                ch.getSkills().get("puxao").preExecute();
            }
        } else {
            if (ch.getSkills().get("perseguir") != null) {
                //  ch.getSkills().get("perseguir").preExecute();
                return;
            }
        }
    }

    /*
                        ch.setClazz(new Clazz(main, ClazzType.SWORDSMAN, new Stats(50, 0, 10, 60)));
                        ch.setClazz(new Clazz(main, ClazzType.MAGE, new Stats(10, 60, 0, 55)));
                        ch.setClazz(new Clazz(main, ClazzType.PRIEST, new Stats(10, 30, 0, 100)));
    *//*
        if (e.getView().getTitle().equals(PryColor.color("&eRamificações de Habilidades"))) {
            if (e.getCurrentItem() == null) {
                e.setCancelled(true);
                return;
            }
            for (Branch branch : ch.getClazz().getBranches()) {
                if (e.getCurrentItem().getType().equals(branch.getMaterial())) branch.openBranch(p);
            }
            e.setCancelled(true);
            return;
        }

    }
    */
/*
    @EventHandler
    public void onClickSkill(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (ch.getClazz().getBranches() != null) {
            for (Branch branch : ch.getClazz().getBranches()) {
                if (p.getOpenInventory().getTitle().equals(branch.getDisplayName())) {
                    if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                        return;
                    }
                    for (SUID suid : RPG.getAllSUIDS()) {
                        if (e.getClickedBlock().getType().equals(suid.getMaterial())) {
                            Skill skill = ch.getSkills().get(suid.toString());
                            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                                skill.addLevel(1);
                                p.closeInventory();
                                branch.openBranch(p);
                                return;
                            }
                            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                                skill.decLevel(1);
                                p.closeInventory();
                                branch.openBranch(p);
                                return;
                            }
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
*/
    @EventHandler
    public void onTryCloseUnSelected(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player p)) return;
        if (e.getView().title().toString().equals(PryColor.color("&bSelecione sua classe..."))) {
            Character ch = cm.getCharacter(p.getUniqueId());
            BaseClass clazz = ch.getClazz();
            if (clazz == null) {
                ch.selectClazz();
            }
        }
    }


    @EventHandler
    public void onTryChatInNullClazz(AsyncChatEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (ch.getClazz() == null) {
            p.sendMessage(PryColor.color("&eSistema &f> &cVocê ainda não selecionou sua classe, digite: &8/&bclasse"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (e.getCause() == EntityDamageEvent.DamageCause.POISON) {
            ch.addHealth((int) -(ch.getMaxHealth() * 0.01));
            if (ch.getHealth() <= 0) {
                ch.setHealth(0);
                e.setDamage(20);
                return;
            }
            e.setDamage(0);
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (ch.getSkill("stomper") != null) {
                Skill skill = ch.getSkills().get("stomper");
                if (skill.isInUse()) {
                    skill.setInUse(false);
                    int tempLife = (int) (ch.getHealth() - skill.getLifeCoust());
                    if (tempLife <= 0) {
                        ch.setHealth(0);
                        e.setDamage(20);
                        return;
                    } else {
                        ch.setHealth(tempLife);
                    }
                } else {
                    int tempLife = (int) (ch.getHealth() - e.getDamage());
                    if (tempLife <= 0) {
                        e.setDamage(20);
                        return;
                    } else {
                        ch.setHealth(tempLife);
                    }

                }
            }
            e.setDamage(0);
            return;
        }

    }


    @EventHandler
    private void onDamagedByCharacter(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
            Player d = (Player) e.getDamager(); // Deu dano
            Character damager = cm.getCharacter(d.getUniqueId());
            if (damager.hasBeast() && damager.getBeast().isInvoked()) {
                if (damager.getBeast().getMode() == Beast.Mode.ATTACK) {
                    damager.getBeast().getEntity().setTarget((LivingEntity) e.getEntity());
                }
            }
        }
        if (!(e.getEntity() instanceof Player p)) {
            return;
        }
        Character damaged = cm.getCharacter(p.getUniqueId());

        int armordefense = 0;
        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor != null) {
                if (armor.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "rpg.item.resistance"), PersistentDataType.STRING)) {
                    armordefense += Integer.parseInt(armor.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "rpg.item.resistance"), PersistentDataType.STRING));
                }
            }
        }
        // defesa e redução de dano (%) deveria ser parte do propio characters
        int totalDefense = (damaged.getClazz().getAttributes().getResistance() / 2) + armordefense;
        double dr = 1; // Redução de dano
        if (damaged.getClazz() == BaseClass.SWORDSMAN){
            dr -= 10/100; // 10% de redução de dano por ser cavaleiro.
        }
        if (damaged.hasBeast() && damaged.getBeast().isInvoked()) {
            if (damaged.getBeast().getMode() == Beast.Mode.DEFENSE) {
                // evento que quando muda o modo da besta aumenta a redução de dano? Em realidade isso tem que ser uma redução separada, ja que pode anular ottalemtne o dano se nao for feito adequadamente
                dr -= 25/100; // 25% de redução de dano. Futuramente a redução pode depender da besta.

            }
        }

        if (e.getDamager() instanceof Warden) { // inseguro definir isso assim, melhor usar pdc
            damaged.addHealth(-damaged.getMaxHealth() * 0.25);
            e.setDamage(0);
            return;
        }
        if (e.getDamager() instanceof Player) {
            Player d = (Player) e.getDamager(); // Deu dano
            Character damager = cm.getCharacter(d.getUniqueId());
       /* if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (skill.isInUse()) {
                skill.setInUse(false);
                int tempLife = ch.getHealth() - skill.getLifeCoust();
                if (tempLife <= 0) {
                    e.setDamage(20);
                } else {
                    ch.setHealth(tempLife);

                } pq iso ta aqui eu nao sei, a cause sempre vai ser uma entidade, que viajem!
            }
            return;
        }*/
            if (damaged.getLevel() < 20) { // Cancela o dano se o damaged estiver abaixo do nivel 20.
                e.setCancelled(true);
                return;
            }

            int totalDamage = ((int) e.getDamage()) + (damager.getClazz().getAttributes().getStrength() / 2);
            int finalDamage = (int) ((totalDamage - totalDefense) * dr);
            if (damaged.isWithBeast() && damaged.getBeast().getMode() == Beast.Mode.DEFENSE){
                    damaged.getBeast().getEntity().damage(finalDamage * 0.25, e.getDamager());
            }
            int tempLife = (int) (damaged.getHealth() - finalDamage);
            damaged.setHealth(tempLife);
            e.setDamage(0);
            return;
        }
        int finalDamage = (int) (e.getDamage() * 20) - totalDefense; //Adição temporaria no dano so para testes...
        if (finalDamage > 0) {
            damaged.addHealth(-finalDamage);
        }
        e.setDamage(0);
    }


    @EventHandler
    public void onBreakFlower(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (b.getType() != Material.OBSIDIAN) return;
        ColheitaMaldita cm = em.getColheitaMaldita();
        if (!cm.isStarted()) return;
        if (cm.isFinished()) return;
        cm.remReamingFlower(p);
        e.setDropItems(false);
        e.setExpToDrop(0);
    }

    @EventHandler
    public void onTryBreakHologram(PlayerArmorStandManipulateEvent e) {
        if (!e.getRightClicked().isVisible()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTargettedBySkill(CharacterTargettedBySkillEvent e) {
        Character c = cm.getCharacter(e.getTarget());
        if (c.getImmunities().checkSkills()) {
            e.setCancelled(true);
        }

    }
}

