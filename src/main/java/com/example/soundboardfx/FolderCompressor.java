package com.example.soundboardfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
public class FolderCompressor
{
    public static void compressFolder(String sourceFolder, String outputZipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputZipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        File folder = new File(sourceFolder);
        addToZip(folder, folder.getName(), zos);

        zos.close();
        fos.close();
    }

    private static void addToZip(File file, String entryName, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                addToZip(subFile, entryName + "/" + subFile.getName(), zos);
            }
        } else {
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            fis.close();
            zos.closeEntry();
        }
    }

    public static void decompressAllZipsInFolder(String sourceFolder, String outputFolder) throws IOException {
        File sourceDirectory = new File(sourceFolder);
        File[] zipFiles = sourceDirectory.listFiles((dir, name) -> name.endsWith(".zip"));

        if (zipFiles != null) {
            for (File zipFile : zipFiles) {
                decompressZip(zipFile.getAbsolutePath(), outputFolder);
            }
        }
    }

    public static void decompressZip(String zipFilePath, String outputFolder) throws IOException {
        byte[] buffer = new byte[1024];

        // Create the output folder if it doesn't exist
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                File outputFile = new File(outputFolder + File.separator + entryName);

                // Ensure parent directories are created
                File parent = outputFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                if (zipEntry.isDirectory()) {
                    // If it's a directory, create it
                    outputFile.mkdirs();
                } else {
                    // If it's a file, write it to the output folder
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }

                zipInputStream.closeEntry();
            }
        }
    }
}
