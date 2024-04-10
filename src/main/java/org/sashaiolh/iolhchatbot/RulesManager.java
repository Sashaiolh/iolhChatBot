package org.sashaiolh.iolhchatbot;
import org.sashaiolh.iolhchatbot.Utils.Rule;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RulesManager {
    private List<Rule> rules;
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(BadWordsManager.class.getName());
    static String rulePath = "config/"+ IolhChatBot.MODID+"/rules.txt";

    public RulesManager() {
        this.rules = new ArrayList<>();
    }

    // Метод для загрузки правил из файла
    public void loadRulesFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            loadRulesFromReader(reader);
        }
    }

    private void loadRulesFromReader(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("***")) {
                // Пропускаем разделитель
                continue;
            }
            // Создаем новый пункт правил и добавляем его в список
            String id = line;
            String content = reader.readLine();
            // Используем кодировку UTF-8 при чтении строк
            if (content != null) {
                content = new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            }
            rules.add(new Rule(id, content.toString()));
        }
    }

    // Метод для получения всех правил
    public List<Rule> getAllRules() {
        return rules;
    }

    // Метод для получения пункта правил по его ID
    public Rule getRuleById(String id) {
        for (Rule rule : rules) {
            if (rule.getId().equals(id)) {
                return rule;
            }
        }
        return null; // Если пункт правил с указанным ID не найден
    }

    // Метод для создания файла конфигурации с шаблоном, если он не существует
    private static void createDefaultConfig(File configFile) {
        try {
            // Создание объекта Gson
            FileWriter writer = new FileWriter(configFile, StandardCharsets.UTF_8);

            // Создание шаблона конфигурации

            List<String> defaultLines = new ArrayList<>();

            defaultLines.add("1.1\n");
            defaultLines.add("правило 1.1\n");
            defaultLines.add("***\n");

            defaultLines.add("1.2\n");
            defaultLines.add("правило 1.2\n");
            defaultLines.add("***");

            for(String line : defaultLines){
                writer.write(line);
            }
            writer.close();

        } catch (IOException e) {
            LOGGER.warning("Ошибка создания файла конфигурации: " + e.getMessage());
        }
    }

    public static void main(RulesManager rulesManager) {

        File configFile = new File(rulePath);
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        try {
            rulesManager.loadRulesFromFile(rulePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
