package com.kabu.graph.viewer;

import java.io.*;
import javax.swing.*;

import com.kabu.graph.Graph;

/**
 * Übersetzung und Darstellung einer Graphiz-Datei auf dem Bildschirm.
 */
public class Viewer {
	private Converter DOTConverter = new Converter();
	private String executableLocation = "dot.exe";
	private String windowTitle = "";

	public Viewer() {
	}

	/**
	 * Setzen des Pfades der Datei dot.exe. Dies ist sehr wichtig, da sonst
	 * keine Graphik erzeugt werden kann. Backslashes beachten!
	 */
	public void setExecutable(String executableLocation) {
		this.executableLocation = executableLocation;
	}

	/**
	 * Schreiben des Strings, der die dot-Repräsentation enthält in eine
	 * temporäre Datei.
	 */
	private File write2File(String str) throws IOException {
		File graphFile = null;

		try {
			// Die temporäre Datei nennen wir einfach "graph.dot". Sie wird in
			// das automatisch in das auf dem jeweiligem System übliche
			// Temp-Verzeichnis geschrieben.
			graphFile = File.createTempFile("graph", ".dot");
			FileWriter fw = new FileWriter(graphFile);
			fw.write(str);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return graphFile;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	private File runDot(File f) throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		String cmd = executableLocation + " -Tgif " + f.getAbsolutePath()
				+ " -o" + f.getAbsolutePath() + ".gif";
		Process p = rt.exec(cmd);
		p.waitFor();

		return new File(f.getAbsolutePath() + ".gif");
	}

	/**
	 * Starten des Übersetzungsvorgangs mit anschließender Darstellung auf dem
	 * Bildschirm.
	 */
	public void display(Graph graph, boolean useLabels, boolean useWeights) {
		DOTConverter.setUseLabels(useLabels);
		DOTConverter.setUseWeights(useWeights);
		display(graph);
	}
	
	public void display(Graph graph) {
		DOTConverter.convert(graph);
		String dotRep = DOTConverter.getResult();

		File dotFile = null;
		File img = null;

		try {
			dotFile = write2File(dotRep);
			img = runDot(dotFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}

		Display display = new Display(img);
		display.setTitle(windowTitle);
		display.setVisible(true);
	}
}

/**
 * Hilfsklasse, die zur Darstellung der fertigen Graphik verwendet wird.
 */
class Display extends JFrame {

	public Display(File f) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Icon icon = new ImageIcon(f.getAbsolutePath());
		getContentPane().add(new JLabel(icon));
		setSize(icon.getIconWidth(), icon.getIconHeight());
	}
}
