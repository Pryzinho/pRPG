package br.pryzat.rpg.api.storage;

import br.pryzat.rpg.api.items.types.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomPersistentData {
    private JavaPlugin pluginInstance;
    private Item itemInstance;
    private PersistentDataContainer pdc;

    public CustomPersistentData(Item instance) {
        this.pluginInstance = instance.getPluginInstance();
        this.itemInstance = instance;
        this.pdc = instance.getItemMeta().getPersistentDataContainer();
    }

    public PersistentDataContainer getDataManager() {
        return pdc;
    }

    /**
     * Adiciona um novo dado dentro do PersistentDataContainer do item
     *
     * @param key                Nome do local que vai está armazenado. Ex:
     *                           item_customizados; meu.plugin.dados.items.vip;
     * @param persistentdatatype Tipo de objeto que está sendo armazenado (Enum)
     * @param value              Objeto a ser armazenado
     */
    public CustomPersistentData setData(String key, PersistentDataType persistentdatatype, Object value) {
        pdc.set(new NamespacedKey(pluginInstance, key), persistentdatatype, value);
        pushToItemPDC();
        return this;
    }

    /**
     * Adiciona um novo dado dentro do PersistentDataContainer do item
     *
     * @param key                Namespaced do local que vai está armazenado. Ex:
     *                           item_customizados; meu.plugin.dados.items.vip;
     * @param persistentdatatype Tipo de objeto que está sendo armazenado (Enum)
     * @param value              Objeto a ser armazenado
     */
    public CustomPersistentData setData(NamespacedKey key, PersistentDataType persistentdatatype, Object value) {
        pdc.set(key, persistentdatatype, value);
        pushToItemPDC();
        return this;
    }

    /**
     * @param key   Nome do local que está armazenado
     * @param type  Tipo de objeto que está armazenado
     * @param value Objeto a ser verificado
     * @return Verficia se o item contem o objeto armazenado no mesmo local e
     * retorna verdadeiro ou falso
     */
    public boolean contains(String key, PersistentDataType type, Object value) {
        if (!hasKey(key, type))
            return false;
        return pdc.get(new NamespacedKey(pluginInstance, key), type).equals(value);

    }

    /**
     * @param key  Nome do local que está armazenado
     * @param type Tipo de objeto que está armazenado
     * @return Verifica se o item tem o local de armazenamento especifico e retorna
     * verdadeiro ou falso.
     */
    public boolean hasKey(String key, PersistentDataType type) {
        return pdc.has(new NamespacedKey(pluginInstance, key), type);
    }

    /**
     * @param key  Namespacedkey do local armazenado.
     * @param type Tipo de objeto que está armazenado
     * @return Verifica se o item tem o local de armazenamento especifico e retorna
     * verdadeiro ou falso.
     */
    public boolean hasKey(NamespacedKey key, PersistentDataType type) {
        return pdc.has(key, type);
    }

    /**
     * Adiciona uma chave vazia no PDC.
     * Para casos onde o que importa é o item ter uma respectiva chave independente do valor.
     * P.S: Utilize o outro método se quiser especificar o plugin em que a chave é criada.
     *
     * @param key Chave a ser adicionada em formato string.
     */
    public CustomPersistentData addKey(String key) {
        if (!hasKey(key, PersistentDataType.BOOLEAN))
            setData(key, PersistentDataType.BOOLEAN, true);
        pushToItemPDC();
        return this;
    }

    /**
     * Adiciona uma chave vazia no PDC.
     * Para casos onde o que importa é o item ter uma respectiva chave independente do valor.
     * P.S: Utilize o outro método se quiser especificar o plugin em que a chave é criada.
     *
     * @param key Chave a ser adicionada em formato string.
     */
    public CustomPersistentData addKey(NamespacedKey key) {
        if (!hasKey(key, PersistentDataType.BOOLEAN))
            setData(key, PersistentDataType.BOOLEAN, true);
        pushToItemPDC();
        return this;
    }

    /**
     * Apaga o local de armazenamento juntamente com o objeto armazenado
     *
     * @param key  Nome do local que está armazenado
     * @param type Tipo de objeto que está armazenado
     */
    public void removeKey(String key, PersistentDataType type) {
        if (hasKey(key, type))
            pdc.remove(new NamespacedKey(pluginInstance, key));
        pushToItemPDC();
    }

    private void pushToItemPDC() {
        pdc.copyTo(itemInstance.getItemMeta().getPersistentDataContainer(), true);
    }

}
