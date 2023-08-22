package br.pryzat.rpg.api.storage;

import br.pryzat.rpg.utils.PryConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.sqlite.SQLiteDataSource;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class StorageManager {
    private JavaPlugin instance;
    private StorageType storageType = StorageType.SQLITE;
    private PryConfig userData;

    private String USERDATA_TABLE;
    private String USERDATA_ID = "account_id";

    public StorageManager(JavaPlugin instance, StorageType storageType) {
        this.instance = instance;
        this.storageType = storageType;
        this.userData = new PryConfig(instance, "userData.yml");
        this.USERDATA_TABLE = (instance.getName() + "_userdata").toUpperCase();
        switch (storageType) {
            case YML -> {
                instance.getLogger().log(Level.INFO, "Armazenamento - Serviço YML selecionado.");
                this.userData.saveDefaultConfig();
            }
            case SQLITE -> {
                instance.getLogger().log(Level.INFO, "Armazenamento - Serviço SQLite selecionado.");
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:userdata.db")) {
                    connection.createStatement().execute("CREATE TABLE IF NOT EXISTS" + USERDATA_TABLE + "( ACCOUNT_ID VARCHAR, POINTS INTEGER )");
                    instance.getLogger().log(Level.INFO, "SQLite - Iniciado com sucesso.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return Retorna a lista de UUID dos jogadores armazenados. Retorna null se nenhum tipo de StorageType for definido.
     */
    @Nullable
    public Set<String> getCachedPlayers() {
        switch (storageType) {
            case YML -> {
                return userData.getConfig().getKeys(false);
            }
            case SQLITE -> {
                Set<String> cachedPlayers = new HashSet<>();
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:userdata.db")) {
                    String sqlQuery = "SELECT * FROM " + USERDATA_TABLE;
                    PreparedStatement ps = connection.prepareStatement(sqlQuery);
                    ResultSet resultSet = ps.getResultSet();
                    while (resultSet.next()) {
                        cachedPlayers.add(resultSet.getString(USERDATA_ID));
                    }
                    resultSet.close();
                    ps.close();
                    return cachedPlayers;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public void save(HashMap<UUID, Integer> hashMap) {
        switch (storageType) {
            case YML:
                hashMap.keySet().forEach(p -> {
                    userData.set(p.toString(), hashMap.get(p));
                });
                userData.saveDefaultConfig();
                break;
            case SQLITE:
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:userdata.db")) {
                    connection.setAutoCommit(false);
                    // Inserindo os dados dos jogadores.
                    PreparedStatement pS2 = connection.prepareStatement("INSERT INTO" + USERDATA_TABLE + "( ID, NOME) VALUES (?,?)");
                    hashMap.keySet().forEach(p -> {
                        try {
                            pS2.setString(1, p.toString());
                            pS2.setInt(2, hashMap.get(p));
                            pS2.executeUpdate();
                        } catch (SQLException e) {
                            instance.getLogger().log(Level.SEVERE, "SQLException - StorageManager l51.");
                            e.printStackTrace();
                        }
                    });
                    connection.commit();
                    instance.getLogger().log(Level.INFO, "Armazenamento - Dados de usuarios salvos com sucesso.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public enum StorageType {
        YML,
        MYSQL,
        MARIADB,
        SQLITE;
    }
}
