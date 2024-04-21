package org.sashaiolh.iolhchatbot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sashaiolh.iolhchatbot.Utils.ConfigFile;

public class ConfigFilesManager {
    static final String CONFIG_FOLDER = "config/"+IolhChatBot.MODID + "/";
    private static final String CONFIG_FILE = "iolhConfigFiles.txt";
    private static final Logger LOGGER = Logger.getLogger(BadWordsManager.class.getName());

    public static List<ConfigFile> configFiles = new ArrayList<ConfigFile>();


    // Метод для загрузки правил из файла
    public static void loadConfigFilesFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            loadConfigFilesFromReader(reader);
        }
    }

    private static void loadConfigFilesFromReader(BufferedReader reader) throws IOException {
        configFiles.clear();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("")) {
                continue;
            }
            // Создаем новый пункт правил и добавляем его в список
            // Используем кодировку UTF-8 при чтении строк
//            word = new String(word.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            ConfigFile configFile = new ConfigFile(line);
            configFiles.add(configFile);
        }
    }


    public List<ConfigFile> getConfigFiles() {
        return configFiles;
    }



    // Метод для создания файла конфигурации с шаблоном, если он не существует
    private static void createDefaultConfig(File configFile) {
        try {
            // Создание объекта Gson
            FileWriter writer = new FileWriter(configFile, StandardCharsets.UTF_8);

            // Создание шаблона конфигурации

            List<String> defaultLines = new ArrayList<>();

            defaultLines.add("iolhchatbot/rules.txt\n");
            defaultLines.add("iolhchatbot/questions.txt\n");
            defaultLines.add("iolhchatbot/badwords.txt\n");
            defaultLines.add("iolhchatbot/ChatBotConfig.cfg\n");

//            defaultLines.add("iolhchatbot/iolhConfigFiles.txt\n"); Безопасность уровень хлебушек

            for(String line : defaultLines){
                writer.write(line);
            }
            writer.close();

        } catch (IOException e) {
            LOGGER.warning("Ошибка создания файла конфигурации: " + e.getMessage());
        }
    }
    public static void init(){
        // Путь к файлу конфигурации
        String filePath = CONFIG_FOLDER + CONFIG_FILE;

        // Проверка существования файла и его создание при необходимости
        File configFile = new File(filePath);
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }
        try {
            loadConfigFilesFromFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



