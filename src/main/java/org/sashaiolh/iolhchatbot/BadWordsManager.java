package org.sashaiolh.iolhchatbot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sashaiolh.iolhchatbot.Utils.BadWord;

public class BadWordsManager {
    static final String CONFIG_FOLDER = "config/" + IolhChatBot.MODID + "/";
    private static final String CONFIG_FILE = "badwords.txt";
    private static final Logger LOGGER = Logger.getLogger(BadWordsManager.class.getName());

    static List<BadWord> badWords = new ArrayList<BadWord>();


    // Метод для загрузки правил из файла
    public static void loadBedWordsFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            loadBedWordsFromReader(reader);
        }
    }

    private static void loadBedWordsFromReader(BufferedReader reader) throws IOException {
        badWords.clear();
        String penaltyType = "warn";
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("***warn***")) {
                penaltyType = "warn";
                continue;
            }
            if (line.equals("***mute***")) {
                penaltyType = "mute";
                continue;
            }
            if (line.equals("")) {
                continue;
            }
            // Создаем новый пункт правил и добавляем его в список
            String word = line;
            // Используем кодировку UTF-8 при чтении строк
//            word = new String(word.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            BadWord badWord = new BadWord(word, penaltyType);
            badWords.add(badWord);
        }
    }


    public List<BadWord> getBadWords() {
        return badWords;
    }



    // Метод для создания файла конфигурации с шаблоном, если он не существует
    private static void createDefaultConfig(File configFile) {
        try {
            // Создание объекта Gson
            FileWriter writer = new FileWriter(configFile, StandardCharsets.UTF_8);

            // Создание шаблона конфигурации

            List<String> defaultLines = new ArrayList<>();

            defaultLines.add("***warn***\n");
            defaultLines.add("{блять}\n");
            defaultLines.add("{сука}\n");

            defaultLines.add("***mute***\n");
            defaultLines.add("{хуйня}\n");
            defaultLines.add("пизд\n");
            defaultLines.add("{ебать}\n");

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
            loadBedWordsFromFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


