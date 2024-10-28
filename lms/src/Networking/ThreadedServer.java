package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import control.ANSIColor;

public class ThreadedServer extends Thread {
	Socket clientSocket=null;
	public ThreadedServer(Socket newServerSocket){
		clientSocket = newServerSocket;
	}
	public void run() {
		try {
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter sendData=new PrintWriter(clientSocket.getOutputStream(), true);
			String clientRequest;
			
			while((clientRequest=requestReader.readLine()) !=null) {
				sendData.println(clientRequest+ANSIColor.BLUE+"WOW"+ANSIColor.BLUE);
				if(clientRequest.equals("break")) break;
				 System.out.println("Received from client: " + clientRequest);
			}
			
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
