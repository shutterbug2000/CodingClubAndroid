package multiplayer;

import java.io.IOException;

import java.io.OutputStream;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.FileInputStream;

import java.util.Properties;

public class Config {
	
	static Properties config = new Properties();
	
	static OutputStream configFileO;
	static InputStream configFileI;
	
	public static void set(String property, String data) {
		
		try {
			configFileO = new FileOutputStream("dawnoftheimmortals.properties");
		
			config.setProperty(property, data);
			
			config.store(configFileO, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String load(String property) {
		
		try {
			configFileI = new FileInputStream("dawnoftheimmortals.properties");
		
			config.load(configFileI);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return config.getProperty(property);
		
	}

}
