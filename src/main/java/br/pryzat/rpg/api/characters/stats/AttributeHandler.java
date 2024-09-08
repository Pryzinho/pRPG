package br.pryzat.rpg.api.characters.stats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeHandler {

    public static Attributes byConfig(ConfigurationSection attributesSection) {
        Attributes attr = new Attributes();
        attributesSection.getKeys(false).forEach(attribute -> {
            if (attr.contains(attribute)) {
                attr.put(attribute, attributesSection.getDouble(attribute));
            }
        });
        return attr;
    }

    public static Attributes byStringConfig(String rpgitem) {
        Attributes attr = new Attributes();
        Matcher matcher = Pattern.compile("\\[(.*?)\\]").matcher(rpgitem);
        if (matcher.find()) {
            String[] serializedAttrs = matcher.group(1).split(",");

            for (String srlAttr : serializedAttrs) {
                String[] desrializedAttr = srlAttr.split(":");
                if (desrializedAttr.length == 2) {
                    String attrKey = desrializedAttr[0].trim();
                    double attrValue = Double.parseDouble(desrializedAttr[1].trim());
                    if (attr.contains(attrKey)) {
                        attr.put(attrKey, attrValue);
                    }
                }
            }
        }
        return attr;
    }
}
