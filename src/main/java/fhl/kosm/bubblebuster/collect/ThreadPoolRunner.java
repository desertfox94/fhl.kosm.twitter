package fhl.kosm.bubblebuster.collect;

import fhl.kosm.bubblebuster.analyse.WordcloudCreator;
import fhl.kosm.bubblebuster.model.Hashtag;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ThreadPoolRunner {

    private static int THREADS = 5;

    private static Queue<Hashtag> pool;

    static String DIRECTORY = "D:\\dev\\data\\clouds\\inverted";

    public ThreadPoolRunner(List<Hashtag> pool) {
        this.pool = new LinkedList<>(pool);
    }

    public ThreadPoolRunner(Hashtag... pool) {
        this(Arrays.asList(pool));
    }

    static Hashtag next() {
        synchronized (pool) {
            if (pool.isEmpty()) {
                return null;
            }
            synchronized (pool) {
                return pool.poll();
            }
        }
    }

    public void execute() {
        execute(THREADS);
    }

    public void execute(int numOfThreads) {
        for (int i = 0; i < numOfThreads; i++) {
            new Thread(new Runner(String.valueOf(i + 1))).start();
        }
    }
}

class Runner implements Runnable {

    WordcloudCreator creator = new WordcloudCreator();

    DedicatedDirectoryFileWriter writer = new DedicatedDirectoryFileWriter(ThreadPoolRunner.DIRECTORY);

    private String id;

    public  Runner(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println(id +": started");
        Hashtag hashtag;
        String tag;
        while ((hashtag = ThreadPoolRunner.next()) != null) {
            tag = hashtag.getTag();
            if (writer.exists(tag)) {
                continue;
            }
            writer.write(tag, creator.createAsBase64(hashtag));
            System.out.println(id +": created word cloud for " + tag);
        }
    }
}