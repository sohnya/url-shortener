package com.sonja.url;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that performs simplified MySQL operations.  
 * 1. Retrieving data from a database and transforming it into a UrlShortener object
 * 2. Sending a URL shortener object and updating the database 
 * with the values that were not in the database before. 
 * */
public class UrlShortenerDataWithDatabase implements UrlShortenerData {

	private String username;
	private String password;
	private String mySqlUrl;

	public UrlShortenerDataWithDatabase(String mySqlUrl, String username, String password) {
		this.username = username;
		this.password = password;
		this.mySqlUrl = mySqlUrl;
	}

	/**
	 * Loads data from a database and puts it into a new
	 * UrlShortener Object
	 * @return an UrlShortener Object with database information
	 * */	
	@Override
	public UrlShortener loadShorternerData() throws Exception {

		try {
			
			Connection connection = DriverManager.getConnection(mySqlUrl, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from urls");

			UrlShortener urlShortener = transformDataToUrlShortener(rs);

			connection.close();

			return urlShortener;

		} catch (SQLException e) {
			throw new Exception("Cannot connect to database.");
		}
	}
/**
 * Transforms data from a sql ResultSet to an UrlShortener object.
 * @param rs ResultSet from a mySql 'select * from urls' query
 * @returns UrlShortener object filled with information from the 
 * database
 * */
	private UrlShortener transformDataToUrlShortener(ResultSet rs) {
		
		UrlShortener urlShortener = new UrlShortener();

		Map<String, String> urlShortenersMap = new HashMap<>();

		try {
			while (rs.next()) {
				urlShortenersMap.put(rs.getString("url"), rs.getString("shorturl"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		urlShortener.setUrlShortenersMap(urlShortenersMap);
		
		return urlShortener;
	}

	/**
	 * Transfers new data from an UrlShortener Object to a database.
	 * @param urlShortener containing all the data in the program
	 * */

	@Override
	public void saveShorternerData(UrlShortener urlShortener) {

		// Load info from database
		Connection connection;
		try {
			connection = DriverManager.getConnection(mySqlUrl, username, password);

			// Read all rows from database and put them in a ResultSet
			String query = "select * from urls";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			// Put database keys into a Set, for comparison with keys from
			// UrlShorterner
			Set<String> urlSqlSet = new HashSet<>();
			while (rs.next()) {
				urlSqlSet.add(rs.getString("url"));
			}

			Map<String, String> urlShortenersMap = urlShortener.getUrlShortenersMap();

			// Create a Set with urls that are not in the database
			Set<String> differenceSet = new HashSet<>(urlShortenersMap.keySet());
			differenceSet.removeAll(urlSqlSet);

			// and then put them into the database
			for (String url : differenceSet) {

				// Get the shortUrl that corresponds to the url
				String shortUrl = urlShortenersMap.get(url);

				// Update the database with the new key-value pair
				query = "insert into urls (url,shorturl) values (?,?);";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, url);
				preparedStatement.setString(2, shortUrl);
				preparedStatement.executeUpdate();

			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
