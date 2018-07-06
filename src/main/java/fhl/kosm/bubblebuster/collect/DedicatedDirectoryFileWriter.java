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
        try {
            FileWriter writer = new FileWriter(directory + File.separator + name);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
