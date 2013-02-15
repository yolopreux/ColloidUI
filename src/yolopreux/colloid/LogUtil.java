package yolopreux.colloid;

import java.io.File;
import java.io.RandomAccessFile;

public class LogUtil {

    public static String tail(File file) {
        try {
            @SuppressWarnings("resource")
            RandomAccessFile fileHandler = new RandomAccessFile(file, "r");
            long fileLength = file.length() - 1;
            StringBuilder sb = new StringBuilder();

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA) {
                    if (filePointer == fileLength) {
                        continue;
                    } else {
                        break;
                    }
                } else if (readByte == 0xD) {
                    if (filePointer == fileLength - 1) {
                        continue;
                    } else {
                        break;
                    }
                }

                sb.append((char) readByte);
            }

            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // fileHandler.close();
        }
    }

    public static String tail(File file, int lines) {
        try {
            @SuppressWarnings("resource")
            java.io.RandomAccessFile fileHandler = new java.io.RandomAccessFile(
                    file, "r");
            long fileLength = file.length() - 1;
            StringBuilder sb = new StringBuilder();
            int line = 0;

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA) {
                    if (line == lines) {
                        if (filePointer == fileLength) {
                            continue;
                        } else {
                            break;
                        }
                    }
                } else if (readByte == 0xD) {
                    line = line + 1;
                    if (line == lines) {
                        if (filePointer == fileLength - 1) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                sb.append((char) readByte);
            }

            sb.deleteCharAt(sb.length() - 1);
            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // fileHandler.close();
        }
    }
}
