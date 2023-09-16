package com.example.soundboardfx;

import java.io.IOException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
public class SoundFileManager
{
    private static final String TXT_FILE = "soundPackFolder.txt";
    public static boolean saveSoundToCompressedFile(SoundPack sound)
    {
        boolean result = true;
        try {
            FolderCompressor.compressFolder(sound.getFolderPath(), sound.getFolderPath() + ".zip");
        }

        catch  (IOException e) {
            result = false;
        }
        return result;
    }

    public static boolean decompressAllFolders(String sourceFolder, String outputFolder)
    {
        boolean result = true;
        try {
            FolderCompressor.decompressAllZipsInFolder(sourceFolder, outputFolder);
        }

        catch (IOException e)
        {
            result = false;
        }

        return result;
    }

    public static boolean saveSoundPackFolderPath(String filePath) {
        boolean saved = true;
        File soundTxtFile = new File(TXT_FILE);
        FileWriter writer;
        try {
            if (soundTxtFile.createNewFile())
                System.out.println("Sound Pack Folder txt created: " + soundTxtFile.getName());
            else
                System.out.println("Sound Pack Folder exists");
            writer = new FileWriter(TXT_FILE);
            writer.write(filePath);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            saved = false;
        }

        return saved;
    }
}
