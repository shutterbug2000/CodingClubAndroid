package multiplayer.server;

import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.PrintWriter;

import multiplayer.Config;

public class Server {
	
	void runServer() throws IOException {
		
		int port = Integer.parseInt(Config.load("serverPort"));
		
		ServerSocket server = new ServerSocket(port);
		Socket socket = null;
		
		BufferedReader in;
		PrintWriter out;
		
		while(true) {
			
			socket = server.accept();
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
		}
		
	}

}
