package game.xfy9326.catchspy.Methods;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOMethod {

    @SuppressWarnings("SameParameterValue")
    public static boolean renameFile(String path, String name) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            File file_to = new File(file.getParent() + File.separator + name);
            return file.renameTo(file_to);
        }
        return false;
    }

    @SuppressWarnings("SameParameterValue")
    static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeFile(String content, String path) {
        File file = new File(path);
        try {
            if (CheckFile(file, true)) {
                return false;
            }
            OutputStream writer = new FileOutputStream(file);
            byte[] Bytes = content.getBytes();
            writer.write(Bytes);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readFile(String path) {
        File file = new File(path);
        try {
            if (CheckFile(file, false)) {
                return null;
            }
            InputStream file_stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file_stream));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
            file_stream.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean createPath(File file) {
        if (file.getParent().trim().length() != 1) {
            File filepath = file.getParentFile();
            if (!filepath.exists()) {
                return filepath.mkdirs();
            }
        }
        return true;
    }

    private static boolean CheckFile(File file, boolean delete) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (file.canWrite() && file.canRead() && delete) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
                return false;
            }
        } else {
            if (!createPath(file)) {
                return true;
            }
            if (file.createNewFile()) {
                return false;
            }
        }
        return true;
    }

}
