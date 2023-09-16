package com.example.soundboardfx;

import java.util.Date;

public class SoundPack
{
    private final String author;
    private final String title;
    private final String folderPath;
    private final String soundFilePath;
    private final String creationDate;
    public SoundPack(String author, String title, String creationDate, String folderPath, String soundFilePath)
    {
        this.author = author;
        this.title = title;
        this.creationDate = creationDate;
        this.folderPath = folderPath;
        this.soundFilePath = soundFilePath;
    }

    public String getAuthor() {
        return this.author;
    }
    public String getCreationDate() {
        return this.creationDate;
    }
    public String getFolderPath() {
        return this.folderPath;
    }
    public String getSoundFilePath() {
        return this.soundFilePath;
    }
    public String getTitle() {
        return this.title;
    }
}
