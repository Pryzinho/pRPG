package br.pryzat.rpg.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;
import java.util.Map;

public class Announcer {
    private static final Map<Character, NamedTextColor> colorMap = new HashMap<>();

    static {
        colorMap.put('0', NamedTextColor.BLACK);
        colorMap.put('1', NamedTextColor.DARK_BLUE);
        colorMap.put('2', NamedTextColor.DARK_GREEN);
        colorMap.put('3', NamedTextColor.DARK_AQUA);
        colorMap.put('4', NamedTextColor.DARK_RED);
        colorMap.put('5', NamedTextColor.DARK_PURPLE);
        colorMap.put('6', NamedTextColor.GOLD);
        colorMap.put('7', NamedTextColor.GRAY);
        colorMap.put('8', NamedTextColor.DARK_GRAY);
        colorMap.put('9', NamedTextColor.BLUE);
        colorMap.put('a', NamedTextColor.GREEN);
        colorMap.put('b', NamedTextColor.AQUA);
        colorMap.put('c', NamedTextColor.RED);
        colorMap.put('d', NamedTextColor.LIGHT_PURPLE);
        colorMap.put('e', NamedTextColor.YELLOW);
        colorMap.put('f', NamedTextColor.WHITE);
    }


    public static Component text(String text){
        return MiniMessage.miniMessage().deserialize(convertColors(text));
    }
    /*
      return Tag.styling(
                NamedTextColor.BLUE,
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(link),
                HoverEvent.showText(Component.text("Open " + link))
        );
     */



    private static String convertColors(String input) {
        for (Map.Entry<Character, NamedTextColor> entry : colorMap.entrySet()) {
            input = input.replace("&" + entry.getKey(), "<" + entry.getValue().asHexString() + ">");
        }
        return input;
    }
}
