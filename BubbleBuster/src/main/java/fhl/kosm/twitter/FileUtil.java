package fhl.kosm.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

	public static boolean write(String path, String data) {
		return write(new File(path), data);
	}
	
	public static boolean write(File file, String data) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
			writer.write(data);
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			System.err.println("Datei konnte nicht geschrieben werden, Pfad: " + file);
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean writeInCurrentDirectory(String name, String data) throws IOException {
		return write(fileInCurrentDirectory(name), data);
	}
	
	public static File createFile(String name, String suffix) {
		try {
			String path = new File(".").getCanonicalPath();
			File file = new File(path + File.separator + name + suffix);
			int i = 1;
			while (file.exists()) {
				file = new File(path + File.separator + name + "(" + i++ + ")" + suffix);
			}
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String read(File file) throws IOException {
		if (file == null || !file.exists() || file.isDirectory()) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8).lines().forEach(s ->builder.append(s).append("\n"));
		return builder.toString();
	}
	
	public static String readFileInCurrentDirectory(String name) {
		File file = null;
		try {
			file = fileInCurrentDirectory(name);
			return read(file);
		} catch (IOException e) {
			System.err.println("Datei konnte nicht gelesen werden, Pfad: " + (file != null ? file.getAbsolutePath() : name));
			e.printStackTrace();
			return null;
		}
	}
	
	public static File fileInCurrentDirectory(String name) throws IOException {
		return new File(new File(".").getCanonicalPath() + File.separator + name);
	}
	
}
