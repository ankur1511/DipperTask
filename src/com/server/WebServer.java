package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServer {

	Map<Integer, Long> map = new HashMap<Integer, Long>();
	List<Integer> connId = new ArrayList<Integer>();
	Map<String, Socket> clientMap = new HashMap<String, Socket>();
	Map<String, Thread> requestThreadMap = new HashMap<String, Thread>();

	/**
	 * This method is called when the request is GET api/request
	 * 
	 * Creates a connection with given connid
	 * 
	 * @param paramMap
	 * @param clientSocket
	 */

	public void implementSleepRequest(Map<String, String> paramMap, Socket clientSocket) throws Exception, IOException {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Long time1 = System.currentTimeMillis();

					System.out.println("Current Time is : " + time1);
					connId.add(Integer.parseInt(paramMap.get("connid")));
					map.put(Integer.parseInt(paramMap.get("connid")),
							(time1 / 1000) + Long.parseLong(paramMap.get("timeout")));
					System.out.println("Timeout value is : " + Long.parseLong(paramMap.get("timeout")));
					Thread.sleep(Long.parseLong(paramMap.get("timeout")) * 1000);
					System.out.println("NOw the Time is : " + System.currentTimeMillis());
					PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
					output.println("HTTP/1.1 200 OK\r\n");
					output.println("Date: Wed, 30 May 2017 23:59:59 GMT\r\n");
					output.println("Server: Apache/0.8.4\r\n");
					output.println("Content-Type: text/html\r\n");
					output.println("Content-Length: 0\r\n");
					output.println("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
					output.println("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
					output.println("\r\n");
					output.println("{\"status\":\"ok\"}");
					clientSocket.close();
					clientMap.remove(paramMap.get("connid"));

				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					clientMap.remove(paramMap.get("connid"));
				}
			}
		});
		requestThreadMap.put(paramMap.get("connid"), thread);
		thread.start();

	}

	/**
	 * This method is called when the request is GET api/serverStatus
	 * 
	 * returns all the connections with connid and the time remaining to sleep
	 * 
	 * @param clientSocket
	 */

	public void implementGetServerStatusRequest(Socket clientSocket) throws Exception {

		// Your code goes here.

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					int num = connId.size();
					for (int i = 0; i < num; i++) {
						Long prevTime = map.get(connId.get(i));
						Long newTime = (System.currentTimeMillis()) / 1000;
						System.out.println("New Time is : " + (prevTime - newTime));
						if ((prevTime - newTime) > 0)
							map.put(connId.get(i), prevTime - newTime);
						else
							map.put(connId.get(i), 0L);

					}
					PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

					output.println("HTTP/1.1 200 OK\r\n");
					output.println("Date: Wed, 30 May 2017 23:59:59 GMT\r\n");
					output.println("Server: Apache/0.8.4\r\n");
					output.println("Content-Type: text/html\r\n");
					output.println("Content-Length: 0\r\n");
					output.println("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
					output.println("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
					output.println("\r\n");

					for (int j = 0; j < num; j++)
						output.println("{" + connId.get(j) + ":" + map.get(connId.get(j)) + "}");
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

	/**
	 * This method is called when the request is PUT api/kill
	 * 
	 * Kills a given connection id
	 * 
	 * @param requestBody
	 * @param clientSocket
	 */
	public void implementKillConnectionRequest(String requestBody, Socket clientSocket) throws Exception {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (clientMap.get(requestBody.replaceAll("\\D+", "")) != null) {
						Socket socketToClose = clientMap.get(requestBody.replaceAll("\\D+", ""));

						System.out.println("Closing socket with id >> " + requestBody.replaceAll("\\D+", ""));
						PrintWriter output = new PrintWriter(socketToClose.getOutputStream(), true);
						output.println("HTTP/1.1 200 OK\r\n");
						output.println("Date: Wed, 30 May 2017 23:59:59 GMT\r\n");
						output.println("Server: Apache/0.8.4\r\n");
						output.println("Content-Type: text/html\r\n");
						output.println("Content-Length: 0\r\n");
						output.println("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
						output.println("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
						output.println("\r\n");
						output.println("{\"status\":\"killed\"}");
						output.close();
						socketToClose.close();

						// removing the conection entry from
						// connection_store_map

						clientMap.remove(requestBody.replaceAll("\\D+", ""));

						requestThreadMap.get(requestBody.replaceAll("\\D+", "")).interrupt();
						PrintWriter outputForKill = new PrintWriter(clientSocket.getOutputStream(), true);
						outputForKill.println("HTTP/1.1 200 OK\r\n");
						outputForKill.println("Date: Wed, 30 May 2017 23:59:59 GMT\r\n");
						outputForKill.println("Server: Apache/0.8.4\r\n");
						outputForKill.println("Content-Type: text/html\r\n");
						outputForKill.println("Content-Length: 0\r\n");
						outputForKill.println("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
						outputForKill.println("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
						outputForKill.println("\r\n");
						outputForKill.println("{\"status\":\"OK\"}");
						outputForKill.close();
						clientSocket.close();
					} else {
						PrintWriter outputForKill = new PrintWriter(clientSocket.getOutputStream(), true);
						outputForKill.println("HTTP/1.1 200 OK\r\n");
						outputForKill.println("Date: Wed, 30 May 2017 23:59:59 GMT\r\n");
						outputForKill.println("Server: Apache/0.8.4\r\n");
						outputForKill.println("Content-Type: text/html\r\n");
						outputForKill.println("Content-Length: 0\r\n");
						outputForKill.println("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
						outputForKill.println("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
						outputForKill.println("\r\n");
						outputForKill.println("{\"status \": \"invalid connection Id : < "
								+ requestBody.replaceAll("\\D+", "") + " >\" }");
						outputForKill.close();
						clientSocket.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

	public void callMethod(WebServerRequestBody webServerRequestBody, Socket clientSocket) throws Exception {

		if (WebServerUtils.isPutRequest(webServerRequestBody)) {
			if (webServerRequestBody.getRequestUri() != null && webServerRequestBody.getRequestUri().contains("kill")) {
				implementKillConnectionRequest(webServerRequestBody.getRequestBody(), clientSocket);
			}
		} else if (WebServerUtils.isGetRequest(webServerRequestBody)) {

			if (webServerRequestBody.getRequestUri() != null
					&& webServerRequestBody.getRequestUri().contains("request")) {
				clientMap.put(webServerRequestBody.getRequestParams().get("connid"), clientSocket);
				implementSleepRequest(webServerRequestBody.getRequestParams(), clientSocket);
			} else {
				implementGetServerStatusRequest(clientSocket);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		WebServer webServer = new WebServer();
		int port = 8080;
		ServerSocket server = new ServerSocket(port);
		System.out.println("Listening for connections on port no 8080... ");
		while (true) {

			Socket clientSocket = server.accept();
			InputStream input = clientSocket.getInputStream();
			WebServerRequestBody webServerRequestBody = WebServerUtils.parseRequest(input);

			// Handle the Web request
			webServer.callMethod(webServerRequestBody, clientSocket);
		}

	}

}
