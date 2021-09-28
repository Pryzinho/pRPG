package br.pryzat.rpg.events;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.characters.classes.Clazz;
import br.pryzat.rpg.api.characters.classes.ClazzType;
import br.pryzat.rpg.api.characters.skills.Branch;
import br.pryzat.rpg.api.characters.skills.SUID;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.characters.stats.Stats;
import br.pryzat.rpg.api.events.EventManager;
import br.pryzat.rpg.builds.events.ColheitaMaldita;
import br.pryzat.rpg.main.RpgMain;
import br.pryzat.rpg.utils.PryColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerEvent implements Listener {
    private RpgMain main;
    private CharacterManager cm;
    private EventManager em;

    public PlayerEvent(RpgMain main) {
        this.main = main;
        this.cm = main.getCharacterManager();
        this.em = main.getEventManager();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        CharacterManager cm = main.getCharacterManager();
        UUID uuid = e.getPlayer().getUniqueId();
        Character ch = cm.getCharacter(uuid);
        if (ch == null) {
            cm.createCharacter(uuid);
            ch = cm.getCharacter(uuid);
        }
    }

    @EventHandler
    private void tempSkill(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (e.isSneaking()) {
            if (ch.getSkills().get("stomper") == null) return;
            //  ch.getSkills().get(SkillType.STOMPER).preExecute();
        } else {
            if (ch.getSkills().get("perseguir") != null) {
                ch.getSkills().get("perseguir").preExecute();
                return;
            }
        }
    }

    @EventHandler
    private void onSelectClazz(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        ItemStack is = e.getCurrentItem();
        Character ch = cm.getCharacter(p.getUniqueId());
        net.minecraft.world.item.ItemStack nis = CraftItemStack.asNMSCopy(is);
        if (e.getView().getTitle().equals(PryColor.color("&bSelecione sua classe..."))) {
            if (!nis.hasTag()) return;
            assert nis.getTag() != null;
            if (!nis.getTag().hasKey("rpg.representative.item")) return;
            for (String key : main.getConfigManager().getYml().getSection("classes")) {
                if (nis.getTag().getString("rpg.representative.item") == "clazz." + key.toLowerCase()) {
                    ch.setClazz(new Clazz(main, ClazzType.valueOf(key)));
                    p.closeInventory();
                }
            }
            e.setCancelled(true);
        }
/*
                    ch.setClazz(new Clazz(main, ClazzType.SWORDSMAN, new Stats(50, 0, 10, 60)));
                    ch.setClazz(new Clazz(main, ClazzType.MAGE, new Stats(10, 60, 0, 55)));
                    ch.setClazz(new Clazz(main, ClazzType.PRIEST, new Stats(10, 30, 0, 100)));
*/
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

    @EventHandler
    public void onClickSkill(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
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

    @EventHandler
    public void onTryCloseUnSelected(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if (e.getView().getTitle().equals(PryColor.color("&bSelecione sua classe..."))) {
            Character ch = cm.getCharacter(p.getUniqueId());
            Clazz clazz = ch.getClazz();
            if (clazz == null) {
                ch.selectClazz();
            }
        }
    }

    @EventHandler
    public void onTryMoveInNullClazz(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (ch.getClazz() == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTryChatInNullClazz(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        if (ch.getClazz() == null) {
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
            ch.remHealth((int) (ch.getMaxHealth() * 0.01));
            if (ch.getHealth() <= 0) {
                ch.setHealth(0);
                e.setDamage(20);
                return;
            }
            e.setDamage(0);
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (ch.getSkills().has("stomper")) {
                Skill skill = ch.getSkills().get("stomper");
                if (skill.isInUse()) {
                    skill.setInUse(false);
                    int tempLife = ch.getHealth() - skill.getLifeCoust();
                    if (tempLife <= 0) {
                        ch.setHealth(0);
                        e.setDamage(20);
                        return;
                    } else {
                        ch.setHealth(tempLife);
                    }
                } else {
                    int tempLife = ch.getHealth() - (int) e.getDamage();
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
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity(); // Tomou dano
        Player d = (Player) e.getDamager(); // Deu dano
        Character damaged = cm.getCharacter(p.getUniqueId());
        Character damager = cm.getCharacter(d.getUniqueId());
       /* if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (skill.isInUse()) {
                skill.setInUse(false);
                int tempLife = ch.getHealth() - skill.getLifeCoust();
                if (tempLife <= 0) {
                    e.setDamage(20);
                } else {
                    ch.setHealth(tempLife);

                }
            }
            return;
        }*/
        if (damaged.getLevel() < 20) {
            e.setCancelled(true);
            return;
        }
        int armordefense = 0;
        if (p.getInventory().getArmorContents() != null) {
            for (ItemStack armor : p.getInventory().getArmorContents()) {
                armordefense += 1;
            }
        }
        int totalDefense = (damaged.getClazz().getStats().getResistance() / 2) + armordefense;
        int totalDamage = ((int) e.getFinalDamage()) + (damager.getClazz().getStats().getStrength() / 2);

        int finalDamage = totalDamage - totalDefense;
        if (finalDamage <= 0) {
            e.setDamage(0);
            return;
        } else {
            int tempLife = damaged.getHealth() - finalDamage;
            if (tempLife <= 0) {
                e.setDamage(20);
                return;
            }
            damaged.setHealth(tempLife);
        }
        e.setDamage(0);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player)) return;
        Player p = e.getEntity();
        Player k = e.getEntity().getKiller();
        Character killer = cm.getCharacter(k.getUniqueId());
        killer.getLevelManager().addExp(10);

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Character ch = cm.getCharacter(p.getUniqueId());
        ch.setHealth((int) (ch.getMaxHealth() * 0.10));
        ch.setMana((int) (ch.getMaxMana() * 0.30));
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
}

