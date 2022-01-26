package br.pryzat.rpg.bukkit.builds.events;

import br.pryzat.rpg.bukkit.api.events.Event;
import br.pryzat.rpg.bukkit.utils.ItemBuilder;
import br.pryzat.rpg.bukkit.utils.PryColor;
import br.pryzat.rpg.bukkit.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ColheitaMaldita extends Event {

    private HashMap<Integer, Location> flowers;
    private List<Entity> guardians;
    private int reamingFlowers = 0;

    public ColheitaMaldita(RpgMain plugin, String euid, HashMap<Integer, Location> flowers) {
        super(plugin, euid);
        setReady(false);
        setStarted(false);
        setFinished(false);
        if (flowers == null) {
            this.flowers = new HashMap<>();
        }
        this.guardians = new ArrayList<Entity>();
    }

    @Override
    public void finish(Player p) {
        if (isFinished()) return;
        if (reamingFlowers <= 0) {
            for (Entity e : guardians){
                if (e != null){
                    e.remove();
                }
            }
            Bukkit.broadcastMessage(PryColor.color("&cTodas as &dFlores Obsidianas&c foram colhidas, em consequência às raizes destruirão todas as entidades proximas."));
            for (Location loc : flowers.values()) {
                loc.getWorld().getNearbyEntities(loc, 3, 3, 3).forEach(e -> {
                    if (e instanceof ArmorStand) {
                        e.remove();
                    } else if (e instanceof Player) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Player t = (Player) e;
                                t.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 30, 1, false));
                                t.sendMessage(PryColor.color("&5Você foi envenenado pelas raizes da flor obsidiana."));
                                getPlugin().getCharacterManager().getCharacter(t.getUniqueId()).updateGraphics();
                                cancel();
                            }
                        }.runTaskLater(getPlugin(), 3 * 20);
                    }
                });
            }
        }
    }

    @Override
    public void start(Player p) {
        if (p != null) {
            if (isStarted()) {
                p.sendMessage(PryColor.color("&eSitema &f> &cO evento ja foi iniciado&f!"));
                return;
            }
        }
        if (isStarted())return;
        for (Location loc : flowers.values()) {
            this.reamingFlowers += 1;
            Block block = loc.getBlock();
            block.setType(Material.OBSIDIAN);
            ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            as.setCustomName(PryColor.color("&dFlor obsidiana petrificada"));
            as.setCustomNameVisible(true);
            as.setInvisible(true);
            as.setInvulnerable(true);
            as.setCollidable(false);
            as.setCanPickupItems(false);
            as.setGravity(false);
            if (as.getNearbyEntities(3, 3, 3) != null) {
                as.getNearbyEntities(3, 3, 3).forEach(e -> {
                    if (e instanceof Player) {
                        Player t = (Player) e;
                        t.damage(getPlugin().getCharacterManager().getCharacter(t.getUniqueId()).getMaxHealth() * 0.15);
                    }
                });
            }
        }
        setStarted(true);
    }

    public void initFlowers() {
        //recreate flowers hashmap
        this.flowers = new HashMap<>();

    }

    @Override
    public void ready(Player p) {
        if (p != null) {
            if (isReady()) {
                p.sendMessage(PryColor.color("&eSitema &f> &cO evento ja foi iniciado&f!"));
                return;
            }
        }
        if (isReady())return;
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PryColor.color("&aColheita &4Maldita"));
        Bukkit.broadcastMessage(PryColor.color("&bAs flores obsidianas irão crescer em &b1 Minuto"));
        Bukkit.broadcastMessage(" ");
        new BukkitRunnable() {
            @Override
            public void run() {
                setReady(true);
                start(p);
                cancel();
            }
        }.runTaskLater(getPlugin(), 20 * 60);
    }

    public void remReamingFlower(Player p) {
        this.reamingFlowers -= 1;
        //   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpg character " + p.getName() + " items add CustomItemUID");
        // Material.WHITER_ROSE troquei pq o viaversion so roda items da 1.8, mas a wither fica perfeita nos client 1.17.x
        Random r = new Random();
        int n = r.nextInt(99);
        if (n <= 50){
            ItemStack is = ItemBuilder.create("&dFlor Obsidiana", Material.ALLIUM, Arrays.asList("&5Uma flor misteriosa que nasce em raizes obsidianas", "&5Diz a lenda que começaram a crescer na grama ", "&5que a caixa de pandora estava em cima.", "&bQuando aberta ocorrera um evento aleatório com o personagem."));
            is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            p.getInventory().addItem(is);
        } else {
            IronGolem ig = (IronGolem)p.getWorld().spawnEntity(p.getLocation(), EntityType.IRON_GOLEM);
            ig.setCustomName(PryColor.color("&cGuardião Obsidiano"));
            ig.setCustomNameVisible(true);
            ig.setCanPickupItems(false);
            ig.setHealth(100);
            ig.setPlayerCreated(false);
            guardians.add(ig);
        }
        finish(p);
    }

    public HashMap<Integer, Location> getFlowers() {
        return flowers;
    }
}
