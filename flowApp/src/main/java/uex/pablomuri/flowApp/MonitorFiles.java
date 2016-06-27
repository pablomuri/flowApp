package uex.pablomuri.flowApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que contiene los metodos para escribir en un fichero
 * @author pablomuri
 *
 */
public class MonitorFiles {
	private String path;
	
	/**
	 * Constructor principal
	 * @param path ruta donde se guardaran los ficheros
	 */
	public MonitorFiles(String path) {
		this.path = path;
	}

	/**
	 * Anade una linea de texto a un fichero 
	 * @param name nombre del fichero
	 * @param line string a anadir al fichero
	 * @throws IOException
	 */
	public void addLine(String name, String line) throws IOException {
		File file = new File(path + name);
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(file, true));
		bw.write(line);
		bw.newLine();
		bw.close();
	}

}
