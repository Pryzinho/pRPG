package br.pryzat.rpg.builds.events;

import br.pryzat.rpg.api.events.Event;
import br.pryzat.rpg.utils.ItemBuilder;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.main.RpgMain;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;

public class ColheitaMaldita extends Event {

    private HashMap<Integer, Location> flowers;
    private List<Entity> guardians;
    private int reamingFlowers = 0;

    public ColheitaMaldita(RpgMain plugin, String euid) {
        super(plugin, euid);
        setReady(false);
        setStarted(false);
        setFinished(false);
        flowers = new HashMap<>();
        if (plugin.getLocationManager().getYml().getSection(LOCATIONS_PATH + "flowers") != null) {
            for (String key : plugin.getLocationManager().getYml().getSection(LOCATIONS_PATH + "flowers")) {
                if (key != null) {
                    flowers.put(Integer.parseInt(key), plugin.getLocationManager().getYml().getLocation(LOCATIONS_PATH + "flowers." + key));
                }
            }
        }
        this.guardians = new ArrayList<>();
    }

    @Override
    public void finish(Player p) {
        if (isFinished()) return;
        if (reamingFlowers <= 0) {
            for (Entity e : guardians) {
                if (e != null) {
                    e.remove();
                }
            }
            Bukkit.getServer().broadcast(Component.text(PryColor.color("&cTodas as &dFlores Obsidianas&c foram colhidas, em consequência às raizes destruirão todas as entidades proximas.")));
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
                                cancel();
                            }
                        }.runTaskLater(getPlugin(), 30);
                    }
                });
            }
        }
    }

    @Override
    public void start(Player p) {
        if (p != null) {
            if (isStarted()) {
                p.sendMessage(PryColor.color("&eSistema &f> &cO evento já está ocorrendo&f!"));
                return;
            }
        }
        if (isStarted()) return;
        for (Location loc : flowers.values()) {
            this.reamingFlowers += 1;
            Block block = loc.getBlock();
            block.setType(Material.OBSIDIAN);
            ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            as.customName(Component.text(PryColor.color("&dFlor obsidiana petrificada")));
            as.setCustomNameVisible(true);
            as.setInvisible(true);
            as.setInvulnerable(true);
            as.setCollidable(false);
            as.setCanPickupItems(false);
            as.setGravity(false);
            as.getNearbyEntities(3, 3, 3).forEach(e -> {
                if (e instanceof Player t) {
                    t.damage(getPlugin().getCharacterManager().getCharacter(t.getUniqueId()).getMaxHealth() * 0.15);
                }
            });
        }
        setStarted(true);
    }

    @Override
    public void ready(Player p) {
        if (p != null) {
            if (!isEnabled()) {
                p.sendMessage(PryColor.color("&eSistema &f> &cO evento está desativado&f."));
                return;
            }
            if (isReady()) {
                p.sendMessage(PryColor.color("&eSistema &f> &cO evento já está ocorrendo&f!"));
                return;
            }
        }
        if (!isEnabled()) return;
        if (isReady()) return;
        Bukkit.getServer().broadcast(Component.text(" "));
        Bukkit.getServer().broadcast(Component.text(PryColor.color("&aColheita &4Maldita")));
        Bukkit.getServer().broadcast(Component.text(PryColor.color("&bAs flores obsidianas irão crescer em &b1 Minuto")));
        Bukkit.getServer().broadcast(Component.text(" "));

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
        if (n <= 49) {
            ItemStack is = ItemBuilder.create("&dFlor Obsidiana", Material.WITHER_ROSE, Arrays.asList("&5Uma flor misteriosa que nasce em raizes obsidianas", "&5Diz a lenda que começaram a crescer na grama ", "&5que a caixa de pandora estava em cima.", "&bQuando aberta ocorrera um evento aleatório com o personagem."));
            is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            p.getInventory().addItem(is);
        } else {
            Warden wd = (Warden) p.getWorld().spawnEntity(p.getLocation(), EntityType.WARDEN);
            wd.customName(Component.text(PryColor.color("&cGuardião Obsidiano")));
            wd.setCustomNameVisible(true);
            wd.setCanPickupItems(false);
            wd.setAnger(p, 150);
            wd.setRemoveWhenFarAway(false);
            guardians.add(wd);
        }
        finish(p);
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (!(s instanceof Player p)) {
            s.sendMessage(PryColor.color("&eSistema &f> &cSomente jogadores podem jogadores podem utilizar esse comando&f."));
            return true;
        }
        if (!p.hasPermission("pryzat.rpg.admin")) {
            p.sendMessage(PryColor.color("&eSistema > &cEste comando não existe!"));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(" ");
            p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid()));
            p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
            p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
            p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
            p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &ahelp &f: &aAjuda do evento&f."));
            p.sendMessage(" ");
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            if (args.length != 1) {
                p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual&f,&c o comando será executado mesmo com esse erro&f."));
            }
            p.sendMessage(" ");
            p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid()));
            p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
            p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
            p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
            p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
            p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &bstatus &f: &aVerifica se está ativado&f."));
            p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &aenable &f: &aAtiva o evento&f."));
            p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &cdisable &f: &aDesativa o evento&f."));
            if (isEnabled()) {
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &blocations &f: &aGerencia as localizações salvas do evento&f."));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &astart &f: &aInicia forçadamente o evento&f."));

            }
            p.sendMessage(" ");
            return true;
        }
        if (args[0].equalsIgnoreCase("status")) {
            if (args.length > 1) {
                p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
            }
            if (isEnabled()) {
                p.sendMessage(PryColor.color("&eSistema &f> &bO evento está &aativado&f."));
            } else {
                p.sendMessage(PryColor.color("&eSistema &f> &bO evento está &cdesativado&f."));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length > 1) {
                p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
            }
            if (isEnabled()) {
                p.sendMessage(PryColor.color("&eSistema &f> &cO evento já está ativo&f."));
            } else {
                setEnabled(true);
                p.sendMessage(PryColor.color("&eSistema &f> &aEvento &8" + getEuid() + " &a&lativado&a com sucesso&f."));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("disable")) {
            if (args.length > 1) {
                p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
            }
            if (isEnabled()) {
                setEnabled(false);
                p.sendMessage(PryColor.color("&eSistema &f> &aEvento &8" + getEuid() + " &c&ldesativado&a com sucesso&f."));
            } else {
                p.sendMessage(PryColor.color("&eSistema &f> &cO evento já está desativado&f."));
            }
            return true;
        }

        if (isEnabled()) {
            p.sendMessage(PryColor.color("&eSistema &f> &cEsse evento está desabilitado&f, &cas únicas funções disponiveis são&f: &bhelp&f, &astatus&f, &aenable&f, &cdisable&f."));
            return true;
        }
        if (args[0].equalsIgnoreCase("start")) {
            if (args.length > 1) {
                p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
            }
            ready(p);
            return true;
        }
        if (args[0].equalsIgnoreCase("locations")) {
            if (args.length < 3) {
                p.sendMessage(" ");
                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid() + " locations"));
                p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
                p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
                p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &eset &dflower &f<&bNúmero da Flor&f> &f: &aDefine a localização que os jogadores serão teleportados ao entrar&f."));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &crem &f<&bNúmero da Flor&f> &f: &aRemove o ponto de nascimento de uma flor&f.."));
                p.sendMessage(" ");
                return true;
            }
            if (args[1].equalsIgnoreCase("help")) {
                p.sendMessage(" ");
                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid() + " locations"));
                p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
                p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
                p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &eset &dflower &f<&bNúmero da Flor&f> &f: &aDefine a localização que os jogadores serão teleportados ao entrar&f."));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &crem &f<&bNúmero da Flor&f> &f: &aRemove o ponto de nascimento de uma flor&f.."));
                p.sendMessage(" ");
                return true;
            }
            if (args[1].equalsIgnoreCase("set")) {
                if (args.length != 4) {
                    p.sendMessage(" ");
                    p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid() + " locations set"));
                    p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid() + " locations"));
                    p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
                    p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
                    p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
                    p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &eset &dflower &f<&bNúmero da Flor&f> &f: &aDefine a localização de uma flor de acordo com seu número&f."));
                    p.sendMessage(" ");
                    return true;
                }
                if (args[2].equalsIgnoreCase("flower")) {
                    try {
                        int index = Integer.parseInt(args[3]);
                        if (flowers.containsKey(index)) {
                            flowers.replace(index, p.getLocation());
                            p.sendMessage(PryColor.color("&eSistema &f> &aLocalização redefinida com sucesso&f! (&dFlor &b" + index + "&f)"));
                        } else {
                            flowers.put(index, p.getLocation());
                            p.sendMessage(PryColor.color("&eSistema &f> &aLocalização definida com sucesso&f! (&dFlor &b" + index + "&f)"));
                        }
                    } catch (NumberFormatException ex) {
                        p.sendMessage(PryColor.color("&eSistema &f> &cNão é um número&f, &cimpossível definir a localização&f."));
                        return true;
                    }
                    return true;
                }
                p.sendMessage(" ");
                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid() + " locations set"));
                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid() + " locations"));
                p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
                p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
                p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
                p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " locations &eset &dflower &f<&bNúmero da Flor&f> &f: &aDefine a localização de uma flor de acordo com seu número&f."));
                p.sendMessage(" ");
                return true;
            }
            if (args[1].equalsIgnoreCase("rem")) {
                // Se o meu eu que fez aquela gambiarra na RpgCommand não programou isso, eu que não vou!! :D
                // Usage: /colheitamaldita locations(args[0]) rem(args[1]) número da flor (basciamente o id  que fica registrado na hashmap <Integer, Location>)
                // é necessario checar se as args length sao igual a 4
                p.sendMessage("a"); // me nego a nao poder reduzir o tamanho do if a 1 linha no intelij idea
                return true;
            }
        }
        p.sendMessage(" ");
        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + getEuid()));
        p.sendMessage(PryColor.color("&4Atenção&f, &4saiba que gerenciar eventos utilizando esse comando é ineficiente&f!"));
        p.sendMessage(PryColor.color("&4Se deseja gerenciar algum evento é &aextremamente recomendado&4 utilizar&f: &8/&f<&bId do Evento&f>"));
        p.sendMessage(PryColor.color("&eExexemplo&f: &8/colheitamaldita"));
        p.sendMessage(PryColor.color("&8/rpg event " + getEuid() + " &ahelp &f: &aAjuda do evento&f."));
        p.sendMessage(" ");
        return true;
    }

    @Override
    public void saveConfiguration() {
        if (!isEnabled()) return;
        for (int i = 0; i < flowers.size(); i++) {
            @Nullable Location loc = flowers.get(i);
            if (loc != null) {
                getPlugin().getLocationManager().getYml().setLocation(LOCATIONS_PATH + "flowers." + i, loc);
            }
        }
        getPlugin().getConfigManager().getYml().set(CONFIG_PATH + ".enabled", isEnabled());
        getPlugin().getConfigManager().getYml().saveConfig();
        getPlugin().getLocationManager().getYml().saveConfig();
    }

    public HashMap<Integer, Location> getFlowers() {
        return flowers;
    }

}
