package multiplayer.client;

import java.io.IOException;
import java.net.UnknownHostException;

import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.PrintWriter;

public class Client {
	
	void runClient(String server, int port) throws UnknownHostException, IOException {
		
		Socket socket = new Socket(server, port);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		while(true) {
			
			
			
		}
		
	}

}
