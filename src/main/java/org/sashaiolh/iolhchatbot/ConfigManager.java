package org.sashaiolh.iolhchatbot;
import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_PATH = "config/"+IolhChatBot.MODID+"/ChatBotConfig.cfg";
    private static Properties properties;

    public ConfigManager() {
        properties = new Properties(); // Всегда создаем новый экземпляр Properties
        loadConfig(); // Загружаем конфигурацию
    }

    private void loadConfig() {
        try (InputStream inputStream = new FileInputStream(CONFIG_PATH)) {
            // Загрузка конфигурации с использованием кодировки UTF-8
            properties.load(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            // Если файл конфигурации не существует, создаем его с шаблонным содержимым
            createDefaultConfig();
            // Повторно загружаем конфигурацию после создания файла
            loadConfig();
        }
    }

    private static void saveConfig() {
        try {
            File configFile = new File(CONFIG_PATH);
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            try (OutputStream outputStream = new FileOutputStream(CONFIG_PATH)) {
                // Сохранение конфигурации с использованием кодировки UTF-8
                properties.store(new OutputStreamWriter(outputStream, "UTF-8"), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConfig(String key) {
        return properties.getProperty(key);
    }

    public void setConfig(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }

    private static void createDefaultConfig() {
        // Установка шаблонных значений конфигурации
        properties.setProperty("botNickname", "§dНастя");
        properties.setProperty("botPrefix", "§8[§3Помощница§8]");
        properties.setProperty("badWordsRuleNum", "4.2");
        properties.setProperty("autoModerationEnabled", "true");
//        properties.setProperty("time", "20");
        saveConfig(); // Сохраняем шаблонную конфигурацию в файл
    }

    public static void init(){
        // Проверяем существование файла и его создание при необходимости
        File configFile = new File(CONFIG_PATH);
        if (!configFile.exists()) {
            createDefaultConfig();
        }
    }
}
