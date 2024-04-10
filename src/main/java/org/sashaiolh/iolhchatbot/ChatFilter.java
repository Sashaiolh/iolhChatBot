package org.sashaiolh.iolhchatbot;

import com.mojang.logging.LogUtils;
import org.sashaiolh.iolhchatbot.Utils.BadWord;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFilter {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Pattern pattern;

    public ChatFilter(List<BadWord> badWords) {
        StringBuilder regexBuilder = new StringBuilder();
        regexBuilder.append("(");
        for (BadWord word : badWords) {
            String wordToLower = word.getWord().toLowerCase();
            String processedWord = wordToLower.replace("{", "(?:[^A-Яa-я]|^)");
            processedWord = processedWord.replace("}", "(?:[^A-Яa-я]|$)");
            regexBuilder.append(processedWord);
            regexBuilder.append("|");

        }


        regexBuilder.deleteCharAt(regexBuilder.toString().length() - 1); // Удаляем последнюю вертикальную черту
        regexBuilder.append(")");// Конец регекса
        String patternString = regexBuilder.toString();

        this.pattern = Pattern.compile(patternString);
    }

    public boolean containsBadWord(String message) {
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
    }
}
