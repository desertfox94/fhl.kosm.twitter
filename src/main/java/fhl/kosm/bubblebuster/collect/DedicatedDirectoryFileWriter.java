package fhl.kosm.bubblebuster.collect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DedicatedDirectoryFileWriter {

    private final String directory;

    public DedicatedDirectoryFileWriter(String directory) {
        this.directory = directory;
    }

    public void write(String name, String data) {
        if (data == null) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(fileWithName(name));
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public File fileWithName(String name) {
        return new File(directory + File.separator + name);
    }

    public boolean exists(String name) {
        return fileWithName(name).exists();
    }

}
