package com.sonja.url;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UrlShortenerDataWithFile implements UrlShortenerData {

	private String fileName;

	/**
	 * Constructor
	 * @param fileName the name of the file where data should be saved
	 * */
	public UrlShortenerDataWithFile(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public UrlShortener loadShorternerData() {

		UrlShortener urlShortener;
		
		File file = new File(fileName);
		if (!file.exists() || (file.length() == 0)) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Error in creating new file " + fileName);
			}
		} else {

			// Read from this file.
			try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(file))) {

				urlShortener = (UrlShortener) os.readObject();

				os.close();

				return urlShortener;

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		return new UrlShortener();

	}

	/*
	 * 
	 * */
	@Override
	public void saveShorternerData(UrlShortener urlShortener) {		
		
		
		// If for some reason the file has been removed between starting and 
		// closing the application
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Error in creating new file " + fileName);
			}
		}

		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName))) {

			os.writeObject(urlShortener);
			os.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
