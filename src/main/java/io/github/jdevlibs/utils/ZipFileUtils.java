package io.github.jdevlibs.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author supot.jdev
 * @version 1.0
 */
public final class ZipFileUtils {
    private static final String DEFAULT_NAME = "zip_file";
    private static final byte[] BUFFER = new byte[1024];

    private ZipFileUtils() {

    }

    public static void zip(byte[] contents, OutputStream out) throws IOException {
        zip(contents, out, DEFAULT_NAME);
    }

    public static void zip(byte[] contents, OutputStream out, String fileName) throws IOException {
        try (
                ZipOutputStream zipOut = new ZipOutputStream(out);
                ByteArrayInputStream fis = new ByteArrayInputStream(contents)
        ) {
            ZipEntry zipEntry = new ZipEntry(Validators.isEmpty(fileName) ? DEFAULT_NAME : fileName);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }

    public static void zip(List<FileZipInfo> fileContents, OutputStream out) throws IOException {
        try (
                ZipOutputStream zipOut = new ZipOutputStream(out)
        ) {

            for (FileZipInfo zipInfo : fileContents) {
                ByteArrayInputStream fis = new ByteArrayInputStream(zipInfo.getContents());
                ZipEntry zipEntry = new ZipEntry(zipInfo.getName());
                zipOut.putNextEntry(zipEntry);

                int length;
                while ((length = fis.read(BUFFER)) >= 0) {
                    zipOut.write(BUFFER, 0, length);
                }
                fis.close();
            }
        }
    }

    public static void zipByPath(List<String> filePaths, OutputStream out) throws IOException {
        try (
                ZipOutputStream zipOut = new ZipOutputStream(out)
        ) {

            for (String filePath : filePaths) {
                File fileToZip = new File(filePath);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);
                int length;
                while ((length = fis.read(BUFFER)) >= 0) {
                    zipOut.write(BUFFER, 0, length);
                }
                fis.close();
            }
        }
    }

    public static void zipByDir(File dir, OutputStream out) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(out);
        zipFile(dir, dir.getName(), zipOut);
        zipOut.close();
    }

    public static String unzipToPath(byte[] contents, String destDir) throws IOException {
        List<String> files = unzip(contents, destDir);
        return (Validators.isEmpty(files) ? null : files.get(0));
    }

    public static List<String> unzip(byte[] contents, String destDir) throws IOException {

        List<String> files = new ArrayList<>();
        try (
                ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(contents))
        ) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(new File(destDir), zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(BUFFER)) > 0) {
                        fos.write(BUFFER, 0, len);
                    }
                    fos.close();
                    files.add(newFile.getPath());
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();

            return files;
        }
    }

    private static void zipFile(File dir, String fileName, ZipOutputStream zipOut) throws IOException {
        if (dir.isHidden()) {
            return;
        }
        if (dir.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }

            File[] children = dir.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
            }
            return;
        }

        FileInputStream fis = new FileInputStream(dir);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public static final class FileZipInfo {
        private String name;
        private byte[] contents;

        public FileZipInfo(String name, byte[] contents) {
            this.name = name;
            this.contents = contents;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public byte[] getContents() {
            return contents;
        }

        public void setContents(byte[] contents) {
            this.contents = contents;
        }
    }
}
