package org.sashaiolh.iolhchatbot.Utils;

public class BadWord {
    private String word;
    private String penaltyType;

    public BadWord(String word, String penaltyType) {
        this.word = word;
        this.penaltyType = penaltyType;
    }

    public String getWord() {
        return word;
    }

    public String getpenaltyType() {
        return penaltyType;
    }
}
