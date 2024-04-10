package org.sashaiolh.iolhchatbot.Utils;

public class Rule {
    private String id;
    private String content;

    public Rule(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
