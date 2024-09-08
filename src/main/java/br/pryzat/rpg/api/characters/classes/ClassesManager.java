package br.pryzat.rpg.api.characters.classes;

import br.pryzat.rpg.api.RPG;
import br.pryzat.rpg.api.characters.stats.AttributeHandler;
import br.pryzat.rpg.api.items.ItemHandler;
import br.pryzat.rpg.main.RpgMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ClassesManager {
    private final RpgMain instance;
    private final File classesFolder;
    private final HashMap<String, BaseClass> classes;


    // Futuramente possibilitar tanto YML e JSON ou algum formato de estrutura que seja bonito


    /**
     * @param instance Singleton instance
     */
    public ClassesManager(RpgMain instance) {
        this.instance = instance;
        this.classes = new HashMap<>();
        this.classesFolder = new File(instance.getDataFolder().toPath().resolve("classes").toUri());
        loadClasses();
    }

    /**
     * Exporta todos os arquivos de classes internos (armazenados na JAR) para a pasta do plugin.
     */
    private void setupDefaultClasses() {
        RPG.getDICRF().forEach(c -> {
            try {
                Files.copy(Objects.requireNonNull(instance.getResource(c + ".yml")), Path.of(classesFolder.getPath()).resolve(c + ".yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Carrega todas as classes identificadas dentro da pasta de classes.
     */
    public void loadClasses() {
        // Se a pasta de classes ainda não existir...
        if (!classesFolder.exists()) {
            setupDefaultClasses();
        }
        // Array com todos os arquivos na pasta de classe.
        File[] classesArray = classesFolder.listFiles();
        if (classesArray == null) {
            instance.getLogger().warning("Nenhuma classe foi encontrada.");
            return;
        }
        // Converte a Array em List.
        List<File> fileClasses = new ArrayList<>(List.of(classesArray));
        // Filtra todos os arquivos que terminam com .yml (Tipo de arquivo de uma classe).
        fileClasses.stream().filter(f -> f.getName().endsWith(".yml")).forEach(clazz -> {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(clazz);
            ConfigurationSection attributes = yml.getConfigurationSection("attributes");
            if (attributes == null) {
                instance.getLogger().warning("Não foi possível encontrar os atributos da classe " + clazz.getName());
                return;
            }
            String classUid = yml.getString("id");
            classes.put(classUid, new BaseClass(classUid, ItemHandler.getInitialItems(yml.getStringList("initialitems")), AttributeHandler.byConfig(attributes)));
        });
    }

    public Collection<BaseClass> getAllClasses(){
        return classes.values();
    }

}
