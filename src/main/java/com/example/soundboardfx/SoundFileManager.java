package com.example.soundboardfx;

import java.io.*;

public class SoundFileManager
{
    public static void saveSoundPackFolderPath(String textFilePath, String filePath) {
        File soundTxtFile = new File(textFilePath);
        FileWriter writer;
        try {
            if (soundTxtFile.createNewFile())
                System.out.println("Sound Pack Folder txt created: " + soundTxtFile.getName());
            else
                System.out.println("Sound Pack Folder exists");
            writer = new FileWriter(textFilePath);
            writer.write(filePath);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readSoundPackFolderPath(String textFilePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(textFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    public static void writeSoundPackToFile(SoundPack obj, String soundFilePath) {
        try {
            FileOutputStream fo = new FileOutputStream(soundFilePath + ".spo");
            ObjectOutputStream oo = new ObjectOutputStream(fo);
            oo.writeObject(obj);
            fo.close();
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SoundPack readSoundPackFromFile(File file) {
        SoundPack obj = null;
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            obj = (SoundPack) oi.readObject();

            oi.close();
            fi.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing stream");
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
