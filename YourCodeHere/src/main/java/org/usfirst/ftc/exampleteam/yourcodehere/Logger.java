package org.usfirst.ftc.exampleteam.yourcodehere;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by matt on 12/16/15.
 */
public class Logger {

	File file;

	public void newLog(String filepath, String filename) {
		file = new File(filepath,filename);
	}

	public void write(String line) {
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file,false);
			line += "\n";
			outputStream.write(line.getBytes());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
