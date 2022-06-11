package br.pryzat.rpg.commands;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.CharacterManager;
import br.pryzat.rpg.api.characters.skills.Skill;
import br.pryzat.rpg.api.characters.stats.Attributes;
import br.pryzat.rpg.api.events.Event;
import br.pryzat.rpg.api.events.EventManager;
import br.pryzat.rpg.utils.ItemBuilder;
import br.pryzat.rpg.utils.PryColor;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RpgCommand implements CommandExecutor {
    private RpgMain main;

    public RpgCommand(RpgMain main) {
        this.main = main;
    }

    //o
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pryzat.rpg.admin")) {
            sender.sendMessage(PryColor.color("&eSistema > &cEste comando não existe!"));
            return true;
        }
        CharacterManager cm = main.getCharacterManager();
        EventManager em = main.getEventManager();
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(PryColor.color("&e Ajuda comando &erpg"));
            sender.sendMessage(PryColor.color("&8/rpg &bcharacter &f: &aGerenciador de jogador. "));
            sender.sendMessage(PryColor.color("&8/rpg &bevent &f: &aGerenciador de evento. "));
            sender.sendMessage(" ");
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(" ");
            sender.sendMessage(PryColor.color("&e Ajuda comando &erpg"));
            sender.sendMessage(PryColor.color("&8/rpg &bcharacter &f: &aGerenciador de jogador. "));
            sender.sendMessage(PryColor.color("&8/rpg &bevent &f: &aGerenciador de evento. "));
            sender.sendMessage(" ");
            return true;
        }
        if (args[0].equalsIgnoreCase("character")) {
            if (args.length > 6) {
                sender.sendMessage(PryColor.color("&cUse: /rpg help"));
                return true;
            }
            Player target;
            try {
                target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(PryColor.color("&cPlayer invalido."));
                    return true;
                }
            } catch (Exception ex) {
                sender.sendMessage(PryColor.color("&cPlayer invalido."));
                return true;
            }
            Character ch = cm.getCharacter(target.getUniqueId());
            if (args.length < 3) {
                sender.sendMessage(" ");
                sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character"));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b info &f: &aObtem informações sobre o personagem do jogador.. "));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b skills &f: &aGerenciador de habilidades do jogador. "));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b stats &f: &aGerenciador de atributos do jogador. "));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b class &f: &aGerenciador de classe do jogador. "));
                sender.sendMessage(" ");
                return true;
            }
            if (args[2].equalsIgnoreCase("help")) {
                sender.sendMessage(" ");
                sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character"));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b info &f: &aObtem informações sobre o personagem do jogador.. "));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b skills &f: &aGerenciador de habilidades do jogador. "));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b stats &f: &aGerenciador de atributos do jogador. "));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b class &f: &aGerenciador de classe do jogador. "));
                sender.sendMessage(" ");
                return true;
            }
            if (args[2].equalsIgnoreCase("class")) {
                if (args.length < 4) {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character class"));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " class &ereset &f: &aReseta a classe do jogador. "));
                    sender.sendMessage(" ");
                    return true;
                }
                if (args[3].equalsIgnoreCase("help")) {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character class"));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " class &ereset &f: &aReseta a classe do jogador. "));
                    sender.sendMessage(" ");
                    return true;
                }
                if (args[3].equalsIgnoreCase("reset")) {
                    if (args.length != 4) {
                        sender.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta&f,&c mas o comando foi &abem &cexecutado."));
                    }
                    ch.selectClazz();
                    return true;
                }
                sender.sendMessage(" ");
                sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character class"));
                sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " class &ereset &f: &aReseta a classe do jogador. "));
                sender.sendMessage(" ");
                return true;
            }
            if (args[2].equalsIgnoreCase("skills")) {
                if (args.length < 4) {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character skills"));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " skills &eset &f: &aDefine uma habilidade e seu nivel nas habilidades do jogador. "));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " skills &aadd &f: &aAdiciona uma habilidade e seu nivel nas habilidades do jogador. "));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " skills &crem &f: &aDiminui uma habilidade e seu nivel nas habilidades do jogador, caso o level for 10, a habilidade será removida. "));
                    sender.sendMessage(" ");
                    return true;
                }
                if (args[3].equalsIgnoreCase("help")) {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character skills"));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " skills &eset &f: &aDefine uma habilidade e seu nivel nas habilidades do jogador. "));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " skills &aadd &f: &aAdiciona uma habilidade e seu nivel nas habilidades do jogador. "));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " skills &crem &f: &aDiminui uma habilidade e seu nivel nas habilidades do jogador, caso o level for 10, a habilidade será removida. "));
                    sender.sendMessage(" ");
                    return true;
                }
                if (args[3].equalsIgnoreCase("add")) {
                    String suid = args[4].toLowerCase();
                    if (RPG.getRegistredSkill(suid) == null) {
                        sender.sendMessage(PryColor.color("&cHabilidade invalida."));
                        return true;
                    }
                    int level;
                    try {
                        level = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        sender.sendMessage(PryColor.color("&cO level escolhido não é um número."));
                        return true;
                    }
                    Skill skill = ch.getSkills().get(suid);
                    if (skill != null) {
                        skill.addLevel(level);
                        sender.sendMessage(PryColor.color("&aVocê adicionou &e" + level + "&a Level's na habilidade &b" + skill.getUniqueId() + "&a de &e" + target.getName() + "&a !"));
                        target.sendMessage(PryColor.color("&aSua afinidade com &b" + skill.getUniqueId() + "&a aumentou, sua habilidade subiu em &e" + level + " &aLevel's !"));
                    } else {
                        ch.getSkills().add(suid, target.getUniqueId(), level);
                        ch.getSkills().get(suid).setUniqueId(suid);
                        sender.sendMessage(PryColor.color("&aVocê adicionou &e" + level + "&a Level's na habilidade &b" + skill.getUniqueId() + "&a de &e" + target.getName() + "&a !"));
                        target.sendMessage(PryColor.color("&dUma força misteriosa interviu em seu personagem, agora você possui à habilidade &b" + skill.getUniqueId() + "&d !"));
                        target.sendMessage(PryColor.color("&aSua afinidade com &b" + skill.getUniqueId() + "&a aumentou, sua habilidade subiu em &e" + level + " &aLevel's !"));
                    }
                    return true;
                }
                if (args[3].equalsIgnoreCase("dec") || args[3].equalsIgnoreCase("rem")) {
                    String suid = args[4].toLowerCase();
                    if (RPG.getRegistredSkill(suid) == null) {
                        sender.sendMessage(PryColor.color("&cHabilidade invalida."));
                        return true;
                    }
                    int level;
                    try {
                        level = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        sender.sendMessage("&cO level escolhido não é um número.");
                        return true;
                    }
                    Skill skill = ch.getSkills().get(suid);
                    if (skill != null) {
                        skill.remLevel(level);
                        sender.sendMessage(PryColor.color("&aVocê &cremoveu &e" + level + "&a Level's na habilidade &b" + suid + "&a de &e" + target.getName() + "&a !"));
                        target.sendMessage(PryColor.color("&aSua afinidade com &b" + suid + "&c diminuiu&a, sua habilidade &cregrediu&a em &e" + level + " &aLevel's !"));
                    } else {
                        sender.sendMessage(PryColor.color("&cEsse jogador não tem essa habilidade!"));
                    }
                    return true;
                }
                if (args[3].equalsIgnoreCase("set")) {
                    String suid = args[4].toLowerCase();
                    if (RPG.getRegistredSkill(suid) == null) {
                        sender.sendMessage(PryColor.color("&cHabilidade invalida."));
                        return true;
                    }
                    int level;
                    try {
                        level = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        sender.sendMessage("&cO level escolhido não é um número.");
                        return true;
                    }
                    Skill skill = ch.getSkills().get(suid);
                    if (skill != null) {
                        skill.setLevel(level);
                        sender.sendMessage(PryColor.color("&aVocê definiu &e" + level + "&a Level's na habilidade &b" + skill.getUniqueId() + "&a de &e" + target.getName() + "&a !"));
                        target.sendMessage(PryColor.color("&aSua afinidade com &b" + skill.getUniqueId() + "&a aumentou, sua habilidade está no Level &e" + level + "&a !"));

                    } else {
                        ch.getSkills().add(suid, target.getUniqueId(), level);
                        ch.getSkills().get(suid).setUniqueId(suid);
                        skill = ch.getSkills().get(suid);
                        sender.sendMessage(PryColor.color("&aVocê definiu &e" + level + "&a Level's na habilidade &b" + skill.getUniqueId() + "&a de &e" + target.getName() + "&a !"));
                        target.sendMessage(PryColor.color("&dUma força misteriosa interviu em seu personagem, agora você possui à habilidade &b" + skill.getUniqueId() + "&d !"));
                        target.sendMessage(PryColor.color("&aSua afinidade com &b" + skill.getUniqueId() + "&a aumentou, sua habilidade está no Level &e" + level + "&a !"));
                    }
                    return true;
                }
                sender.sendMessage(PryColor.color("&cUse: /rpg help"));
                return true;
            }
            if (args[2].equalsIgnoreCase("stats")) {
                if (args.length < 4) {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character stats"));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " stats &eset &f: &aDefine um atributo e seu(s) ponto(s) nos atributos do jogador."));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " stats &aadd &f: &aAdiciona um atributo e seu(s) ponto(s) nos atributos do jogador."));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " stats &crem &f: &aDiminui um atributo e seu(s) ponto(s) nos atributos do jogador,."));
                    sender.sendMessage(" ");
                    return true;
                }
                if (args[3].equalsIgnoreCase("help")) {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character stats"));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " stats &eset &f: &aDefine um atributo e seu(s) ponto(s) nos atributos do jogador."));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " stats &aadd &f: &aAdiciona um atributo e seu(s) ponto(s) nos atributos do jogador."));
                    sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + " stats &crem &f: &aDiminui um atributo e seu(s) ponto(s) nos atributos do jogador,."));
                    sender.sendMessage(" ");
                    return true;
                }
                if (args[3].equalsIgnoreCase("add")) {
                    int points;
                    try {
                        points = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        sender.sendMessage(PryColor.color("&cA quantidade de pontos escolhidos não é um número."));
                        return true;
                    }
                    switch (args[4]) {
                        case "strength":
                            ch.getClazz().getAttributes().addStrength(points);
                            sender.sendMessage(PryColor.color("&aVocê adicionou &e" + points + "&a Ponto(s) no atributo &4Força &ade &e" + target.getName() + "&a !"));
                            target.sendMessage(PryColor.color("&aSeus pontos de &4Força &a aumentou em &e" + points + " &a Ponto(s)"));
                            return true;
                        case "resistance":
                            ch.getClazz().getAttributes().addResistance(points);
                            sender.sendMessage(PryColor.color("&aVocê adicionou &e" + points + "&a Ponto(s) no atributo &8Resistência &ade &e" + target.getName() + "&a !"));
                            target.sendMessage(PryColor.color("&aSeus pontos de &8Resistência &a aumentou em &e" + points + " &a Ponto(s)"));
                            return true;
                        default:
                            sender.sendMessage(PryColor.color("&cEsse atributo não existe."));
                            return true;
                    }

                }
                if (args[3].equalsIgnoreCase("dec") || args[3].equalsIgnoreCase("rem")) {
                    int points;
                    try {
                        points = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        sender.sendMessage(PryColor.color("&cA quantidade de pontos escolhidos não é um número."));
                        return true;
                    }
                    switch (args[4]) {
                        case "strength":
                            ch.getClazz().getAttributes().remStrength(points);
                            sender.sendMessage(PryColor.color("&aVocê &cremoveu &e" + points + "&a Ponto(s) no atributo &4Força &ade &e" + target.getName() + "&a !"));
                            target.sendMessage(PryColor.color("&aSeus pontos de &4Força &c diminuiu &aem &e" + points + " &a Ponto(s)"));
                            return true;
                        case "resistance":
                            ch.getClazz().getAttributes().remResistance(points);
                            sender.sendMessage(PryColor.color("&aVocê &cremoveu &e" + points + "&a Ponto(s) no atributo &8Resistência &ade &e" + target.getName() + "&a !"));
                            target.sendMessage(PryColor.color("&aSeus pontos de &8Resistência &c diminuiu &aem &e" + points + " &a Ponto(s)"));
                            return true;
                        default:
                            sender.sendMessage(PryColor.color("&cEsse atributo não existe."));
                            return true;
                    }

                }
                if (args[3].equalsIgnoreCase("set")) {
                    int points;
                    try {
                        points = Integer.parseInt(args[5]);
                    } catch (Exception ex) {
                        sender.sendMessage(PryColor.color("&cA quantidade de pontos escolhidos não é um número."));
                        return true;
                    }
                    switch (args[4]) {
                        case "strength":
                            ch.getClazz().getAttributes().setStrength(points);
                            sender.sendMessage(PryColor.color("&aVocê definiu &e" + points + "&a Ponto(s) no atributo &4Força &ade &e" + target.getName() + "&a !"));
                            target.sendMessage(PryColor.color("&aSeus pontos de &4Força&a foram redefinidos para &e" + points + " &a Ponto(s)"));
                            return true;
                        case "resistance":
                            ch.getClazz().getAttributes().setResistance(points);
                            sender.sendMessage(PryColor.color("&aVocê definiu &e" + points + "&a Ponto(s) no atributo &8Resistência &ade &e" + target.getName() + "&a !"));
                            target.sendMessage(PryColor.color("&aSeus pontos de &8Resistência&a foram redefenidos para &e" + points + " &a Ponto(s)"));
                            return true;
                        default:
                            sender.sendMessage(PryColor.color("&cEsse atributo não existe."));
                            return true;
                    }

                }
            }
            if (args[2].equalsIgnoreCase("info")) {
                if (args.length > 4) {
                    sender.sendMessage(PryColor.color("&cUse: &8/rpg character" + target.getName() + "&b info"));
                    return true;
                }
                Attributes attributes = ch.getAttributes();
                if (sender instanceof Player) {
                    Inventory inv = Bukkit.createInventory(null, 9 * 3, PryColor.color("&eInformações sobre " + target.getName()));
                    // 11, 13, 14
                    List<String> basiclore = new ArrayList<>();
                    basiclore.add("&eLevel&f: " + ch.getLevel());
                    basiclore.add("&aData de criação&f: " + ch.getDateOfBirth());
                    ItemStack basic = ItemBuilder.create(target.getDisplayName(), Material.PLAYER_HEAD, basiclore); // Name: target display name, Lore : Level, date of birth
                    List<String> statslore = new ArrayList<>();
                    statslore.add("&cForça &f: " + attributes.getStrength() + " Ponto(s)");
                    statslore.add("&bInteligência&f: " + attributes.getInteligency() + " Ponto(s)");
                    statslore.add("&dVelocidade&f: " + attributes.getVelocity() + " Ponto(s)");
                    statslore.add("&8Resistência &f: " + attributes.getResistance() + " Ponto(s)");
                    ItemStack istats = ItemBuilder.create("&1Atributos", Material.NETHER_STAR, statslore); // Name: &1Atributos, Lore: stats:points
                    List<String> skillslore = new ArrayList<>();
                    if (ch.getSkills() != null) {
                        for (Skill skill : ch.getSkills().toList()) {
                            if (skill != null) {
                                skillslore.add(skill.getDisplayName() + "&f, &eLevel&f: " + skill.getLevel());
                            }
                        }
                    }
                    ItemStack iskills = ItemBuilder.create("&2Habilidades", Material.IRON_SWORD, skillslore); // Name: &2Skills, Lore: for skill in skills : add skill:level

                    inv.setItem(11, basic);
                    inv.setItem(13, istats);
                    inv.setItem(14, iskills);

                    ((Player) sender).openInventory(inv);
                    return true;
                } else {
                    sender.sendMessage(" ");
                    sender.sendMessage(PryColor.color("&eInformações sobre " + target.getName()));
                    sender.sendMessage(PryColor.color("&eLevel&f: " + ch.getLevel()));
                    sender.sendMessage(PryColor.color("&1Atributos&f:"));
                    sender.sendMessage(PryColor.color("&f- &cForça &f: " + attributes.getStrength() + " Ponto(s)"));
                    sender.sendMessage(PryColor.color("&f- &bInteligência &f: " + attributes.getInteligency() + " Ponto(s)"));
                    sender.sendMessage(PryColor.color("&f- &dVelocidade &f: " + attributes.getVelocity() + " Ponto(s)"));
                    sender.sendMessage(PryColor.color("&f- &8Resistência &f: " + attributes.getResistance() + " Ponto(s)"));
                    sender.sendMessage(PryColor.color("&2Habilidades&f:"));
                    if (ch.getSkills() != null) {
                        for (Skill skill : ch.getSkills().toList()) {
                            sender.sendMessage(PryColor.color("&f- " + skill.getDisplayName() + "&f, Level " + skill.getLevel()));
                        }
                    }
                    sender.sendMessage(PryColor.color("&aData de criação&f: " + ch.getDateOfBirth()));
                    sender.sendMessage(" ");
                    return true;
                }
            }
            sender.sendMessage(" ");
            sender.sendMessage(PryColor.color("&e Ajuda subcomando &erpg character"));
            sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b info &f: &aObtem informações sobre o personagem do jogador.. "));
            sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b skills &f: &aGerenciador de habilidades do jogador. "));
            sender.sendMessage(PryColor.color("&8/rpg character " + target.getName() + "&b stats &f: &aGerenciador de atributos do jogador. "));
            sender.sendMessage(" ");
            return true;
        }
        if (args[0].equalsIgnoreCase("selectclass")) {
            if (!(sender instanceof Player)) {
                if (args.length != 2) {
                    sender.sendMessage(PryColor.color("&cUse: &8/rpg selectclass &ePlayer"));
                    return true;
                }
                Player t = null;
                try {
                    t = Bukkit.getPlayerExact(args[1]);
                } catch (Exception e) {
                    sender.sendMessage(PryColor.color("&cJogador não encontrado."));
                    return true;
                }
                if (t != null) {
                    if (!t.isOnline()) {
                        sender.sendMessage(PryColor.color("&cO Jogador não está online."));
                        return true;
                    }
                    Character ch = cm.getCharacter(t.getUniqueId());
                    ch.selectClazz();
                }
                return true;
            } else {
                Player p = (Player) sender;
                if (args.length != 1) {
                    p.sendMessage("args erradinha");
                }
                Character ch = cm.getCharacter(p.getUniqueId());
                ch.selectClazz();

            }
            return true;
        }

        if (args[0].equalsIgnoreCase("event")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(PryColor.color("&cApenas jogadores podem utilizar este comando."));
                return true;
            }
            Player p = (Player) sender;
            // /rpg event colheitamaldita locations set flower 1
            // /rpg event <EUID> locations set <spawn/flower> <Number>
            if (args.length > 7 || args.length < 2) {
                Character ch = cm.getCharacter(p.getUniqueId());
                p.sendMessage(" ");
                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event"));
                p.sendMessage(PryColor.color("&8/rpg event &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                p.sendMessage(PryColor.color("&8/rpg event &f<&eEUID&f> : &aGerencia determinado evento, especificado pelo Event Unique Identifier."));
                p.sendMessage(" ");
                return true;
            }
            if (args[1].equalsIgnoreCase("help")) {
                p.sendMessage(" ");
                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event"));
                p.sendMessage(PryColor.color("&8/rpg event &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                p.sendMessage(PryColor.color("&8/rpg event &f<&eEUID&f> : &aGerencia determinado evento, especificado pelo Event Unique Identifier."));
                p.sendMessage(" ");
                return true;
            }
            if (RPG.verifyEUID(args[1])) {
                Event event = RPG.getEvent(args[1]);
                if (!event.isEnabled()) {
                    if (args.length == 2) {
                        p.sendMessage(PryColor.color("&eSistema &f> &cEsse evento está desativado&f"));
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("help")) {
                        if (args.length != 3) {
                            p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
                        }
                        p.sendMessage(" ");
                        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1]));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &aenable &f: &aAtiva o evento&f."));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &cdisable &f: &aDesativa o evento&f."));
                        p.sendMessage(" ");
                        return true;
                    }
                    if (args.length != 3) {
                        p.sendMessage(" ");
                        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1]));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &aenable &f: &aAtiva o evento&f."));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &cdisable &f: &aDesativa o evento&f."));
                        p.sendMessage(" ");
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("enable")) {
                        event.setEnabled(true);
                        p.sendMessage(PryColor.color("&eSistema &f> &aEvento &8" + event.getEuid() + " &a&lativado&a com sucesso&f."));
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("disable")) {
                        event.setEnabled(false);
                        p.sendMessage(PryColor.color("&eSistema &f> &aEvento &8" + event.getEuid() + " &c&ldesativado&a com sucesso&f."));

                        return true;
                    }
                    p.sendMessage(" ");
                    p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " " + args[2]));
                    p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &aenable &f: &aAtiva o evento&f."));
                    p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &cdisable &f: &aDesativa o evento&f."));
                    p.sendMessage(" ");
                    return true;
                } else {
                    if (args.length < 3) {
                        p.sendMessage(" ");
                        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1]));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &blocations &f: &aGerencia as localizações salvas do evento."));
                        p.sendMessage(" ");
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("start")) {
                        if (args.length > 3) {
                            p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
                        }
                        //((ColheitaMaldita)event).ready(p);
                        em.getColheitaMaldita().ready(p);
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("help")) {
                        if (args.length > 3) {
                            p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
                        }
                        p.sendMessage(" ");
                        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1]));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &blocations &f: &aGerencia as localizações salvas do evento."));
                        p.sendMessage(" ");
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("locations")) {
                        if (args.length < 4) {
                            p.sendMessage(" ");
                            p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " locations"));
                            p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                            p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &f<&aSpawn&f/&dAnotherLocation&f> &f: &aDefine certa localização utilizada no evento."));
                            p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &crem &f<&aSpawn&f/&dAnotherLocation&f> &f: &aRemove certa localização utilizada no evento."));
                            p.sendMessage(" ");
                            return true;
                        }
                        if (args[3].equalsIgnoreCase("help")) {
                            if (args.length > 4) {
                                p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
                            }
                            p.sendMessage(" ");
                            p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " locations"));
                            p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                            p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &f<&aSpawn&f/&dAnotherLocation&f> &f: &aDefine certa localização utilizada no evento."));
                            p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &crem &f<&aSpawn&f/&dAnotherLocation&f> &f: &aRemove certa localização utilizada no evento."));
                            p.sendMessage(" ");
                            return true;
                        }
                        if (args[3].equalsIgnoreCase("set")) {
                            if (args.length < 5 || args.length > 6) {
                                p.sendMessage(" ");
                                p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " locations set"));
                                p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &aspawn &f: &aDefine o local de aparição no evento."));
                                p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &aanotherlocation &f<ID> &f: &aDefine certa localização utilizada no evento de acordo com o id."));
                                p.sendMessage(" ");
                                return true;
                            }
                            switch (args[4].toLowerCase()) {
                                case "spawn":
                                    if (args.length != 5) {
                                        p.sendMessage(PryColor.color("&eSistema &f> &cSintaxe incorreta verifique o manual, o comando será executado mesmo com esse erro."));
                                    }
                                    return true;
                                case "anotherlocation":
                                    if (args.length != 6) {
                                        p.sendMessage(" ");
                                        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " locations set"));
                                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &aspawn &f: &aDefine o local de aparição no evento."));
                                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &aanotherlocation &f<ID> &f: &aDefine certa localização utilizada no evento de acordo com o id."));
                                        p.sendMessage(" ");
                                        return true;
                                    }
                                    try {
                                        int index = Integer.parseInt(args[5]);
                                        if (em.getColheitaMaldita().getFlowers() == null) {
                                            em.getColheitaMaldita().initFlowers();
                                        }
                                        em.getColheitaMaldita().getFlowers().put(index, p.getLocation());
                                        p.sendMessage(PryColor.color("&aLocalização definida com sucesso!"));
                                    } catch (Exception ex) {
                                        p.sendMessage(PryColor.color("&cO Id não é um número."));
                                        return true;
                                    }
                                    return true;
                                default:
                                    p.sendMessage(" ");
                                    p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " locations set"));
                                    p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &aspawn &f: &aDefine o local de aparição no evento."));
                                    p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &aanotherlocation &f<ID> &f: &aDefine certa localização utilizada no evento de acordo com o id."));
                                    p.sendMessage(" ");
                                    return true;
                            }
                        }
                        if (args[3].equalsIgnoreCase("rem")) {
                            return true;
                        }
                        p.sendMessage(" ");
                        p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1] + " locations"));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &eset &f<&aSpawn&f/&dAnotherLocation&f> &f: &aDefine certa localização utilizada no evento."));
                        p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " locations &crem &f<&aSpawn&f/&dAnotherLocation&f> &f: &aRemove certa localização utilizada no evento."));
                        p.sendMessage(" ");
                        return true;
                    }
                    p.sendMessage(" ");
                    p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event " + args[1]));
                    p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
                    p.sendMessage(PryColor.color("&8/rpg event " + args[1] + " &blocations &f: &aGerencia as localizações salvas do evento."));
                    p.sendMessage(" ");
                    return true;
                }
            } else {
                p.sendMessage(PryColor.color("&cEsse evento não existe."));
                return true;
            }
			/*
            p.sendMessage(" ");
            p.sendMessage(PryColor.color("&eAjuda sobre o comando &f/rpg event"));
            p.sendMessage(PryColor.color("&8/rpg event &bhelp &f: &aAjuda na utilização do(s) comando(s)."));
            p.sendMessage(PryColor.color("&8/rpg event &f<&eEUID&f> : &aGerencia determinado evento, especificado pelo Event Unique Identifier."));
            p.sendMessage(" ");
            return true;
			*/
        }
        sender.sendMessage(PryColor.color("&cUse: /rpg help"));
        return true;
    }
}



