package SoundToSoundPack;

import com.example.soundboardfx.SoundFileManager;
import com.example.soundboardfx.SoundPack;
import src.Sound;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Converter
{
    private static HashMap<String, Sound> sSoundMap;
    private static HashMap<String, File> rSoundMap;
    public static void main(String[] args)
    {
        sSoundMap = new HashMap<>();
        rSoundMap = new HashMap<>();

        String rSoundPath = "C:/Users/daveb/Desktop/r";
        String sSoundPath = "C:/Users/daveb/Desktop/s";

        String folderPath = "C:/Users/daveb/Desktop/SoundBoardTest";

        listFilesForFolder(new File(sSoundPath));
        rSoundMap = loadWavFiles(rSoundPath);

        for (Map.Entry<String, Sound> entry : sSoundMap.entrySet()) {
            String key = entry.getKey();
            Sound value = entry.getValue();
            if (rSoundMap.containsKey(key)) {
                File currentWavFile = rSoundMap.get(key);
                File folder = new File(folderPath + "/" +key);
                if (!folder.exists() && !folder.mkdirs()) {
                    System.err.println("Failed to create folder for key: " + key);
                    continue; // Skip processing this entry if folder creation fails
                }

                // Create the SoundPack with the modified soundFilePath
                SoundPack newPack = new SoundPack(
                        value.getCreator(),
                        value.getName(),
                        value.getDate(),
                        value.getDesc(),
                        Paths.get(currentWavFile.getAbsolutePath()).getFileName().toString()

                );

                // Write SoundPack to the created folder
                SoundFileManager.writeSoundPackToFile(newPack, folderPath + "/" +key +"/" + key);
                System.out.println(key + " Added ");

                // Copy the currentWavFile to the same folder
                File wavFileInFolder = new File(folder, currentWavFile.getName());
                try {
                    // Use Java NIO to copy the .wav file
                    Files.copy(currentWavFile.toPath(), wavFileInFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle any potential exceptions
                }
            }

            else {
                System.err.println("No matching .wav file found for key: " + key);
            }
        }
    }

    private static void listFilesForFolder(final File folder) {

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
                Sound newSound = readSoundFromFile(fileEntry);
                sSoundMap.put(newSound.getName(), newSound);
            }
        }

        System.out.println("Sounds put in map");
    }

    public static HashMap<String, File> loadWavFiles(String directoryPath) {
        HashMap<String, File> wavFilesMap = new HashMap<>();
        File directory = new File(directoryPath);
        File[] wavFiles = directory.listFiles((dir, name) -> name.endsWith(".wav"));

        if (wavFiles != null) {
            for (File wavFile : wavFiles) {
                String fileName = wavFile.getName();
                String key = fileName.substring(0, fileName.lastIndexOf('.'));
                wavFilesMap.put(key, wavFile);
            }
        }

        return wavFilesMap;
    }

    public static Sound readSoundFromFile(File file) {
        Sound obj = null;
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            obj = (Sound) oi.readObject();

            System.out.println(obj.getName());

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
