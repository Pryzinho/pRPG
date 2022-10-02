package br.pryzat.rpg.listeners;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.classes.Beast;
import br.pryzat.rpg.api.events.bukkit.beast.BeastModeChangeEvent;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BeastEvent implements Listener {
    private RpgMain main;

    public BeastEvent(RpgMain main) {
        this.main = main;
    }

    @EventHandler
    public void onBeastModeChange(BeastModeChangeEvent e) {
        if (e.getNewMode() == Beast.Mode.ATTACK && e.getBeast().isInvoked()) {
            Player p = e.getOwner().getPlayer();
            Wolf w = e.getBeast().getEntity();
            if (w.getTarget() == null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.getNearbyEntities(5, 5, 5).stream().filter(et -> et instanceof LivingEntity).forEach(t -> {
                            if (t != null && t != w) {
                                w.setTarget((LivingEntity) t);
                                if (!isCancelled()) {
                                    cancel();
                                }

                            }
                        });
                    }
                }.runTaskTimer(main, 0, 20);
            }
        }
    }

    @EventHandler
    public void onBeastTarget(EntityTargetEvent e) {
        if (!(e.getEntity() instanceof Wolf b)) return;
        Character owner = main.getCharacterManager().getCharacter(b.getOwnerUniqueId());
        if (e.getReason() == EntityTargetEvent.TargetReason.FORGOT_TARGET) {
            if (owner.getBeast().getMode() == Beast.Mode.ATTACK) {
                e.setCancelled(true);
            }
        }
        if (e.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER) {
            if (owner.getBeast().getMode() == Beast.Mode.FOLLOW) {
                e.setCancelled(true); // Cancela o evento de ataque se a besta estiver no modo de seguir.
            }
        }
        // fazer sistema do modo ataque


    }
}
