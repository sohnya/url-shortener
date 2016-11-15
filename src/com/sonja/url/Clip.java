package com.sonja.url;

/**
 * The Clip application is a simple URL shortener.
 *
 * @author Sonja Hiltunen
 * @version 1.0
 * @since 2016-11-15
 *
 *        This program is run from the command line, and provides two
 *        functionalities. 
 *        
 *        1. From a full URL, generate a short URL (shorten URL)
 *        
 *        2. From a previously generated short URL, provide the corresponding long
 *        URL (expand URL).
 *
 *        The shortened URLs survive an application restart. If I generate a
 *        short URL using the program, I am able to close the program, start it
 *        back up, and expand a previously shortened URL.
 */

public class Clip {

	public static void main(String[] args) {

		// Mysql implementation of the urlShortenerData
		// Must be initialized first, as shown in setup.sql.
		/*
		 * final String username = "newuser"; final String password = "secret";
		 * final String mysqlUrl = "jdbc:mysql://localhost:3306/urls"; //
		 * UrlShortenerData urlShortenerData = new
		 * UrlShortenerDataWithDatabase(mysqlUrl, username, password);
		 */

		// File implementation of the urlShortenerData
		final String fileName = "shorteners.bin";
		UrlShortenerData urlShortenerData = new UrlShortenerDataWithFile(fileName);

		String action;
		String address;
		UrlShortener urlShortener;

		StringAsker asker = new StringAsker(System.in, System.out);

		try {
			urlShortener = urlShortenerData.loadShorternerData();
			printMapContents("Data at beginning of program:", urlShortener);

			action = asker.ask("Enter an action ( shorten or expand ) and press enter. To quit, press anything and enter.");

			while (action.equals("shorten") || action.equals("expand")) {

				address = asker.ask("Enter the address that you want to shorten/expand");

				urlShortener.doAction(Action.valueOf(action), address);

				printMapContents("Data after action:", urlShortener);

				action = asker.ask(
						"Enter an action ( shorten or expand ) and press enter. To quit, press anything and enter.");

			}

			urlShortenerData.saveShorternerData(urlShortener);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		System.out.println("Terminating program.....");

	}

	private static void printMapContents(String message, UrlShortener urlShortener) {

		System.out.println(message +"\n");

		for (String url : urlShortener.getUrlShortenersMap().keySet()) {
			System.out.println("URL : " + url + ", Shortened URL : " + urlShortener.getUrlShortenersMap().get(url));
		}
		System.out.println();
	}

}
