package com.feup.cmov.busphone_terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Entities.Ticket;
import android.annotation.SuppressLint;

public class RestAPI {
	private static String urlRest = "http://172.29.109.8:8080/WebServiceX/webresources/";
	private static int busNumber = 2;

	/**
	 * To be used as a testing function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		//Ticket t = getTicketFromId("04a28289-187d-4596-b7aa-a1fb5604b0f3");
		//useTheTicket("04a28289-187d-4596-b7aa-a1fb5604b0f3", "a");
		setBusNumber(1123);
	}

	/**
	 * Function that returns the JSON response form server
	 * 
	 * @param command
	 * @return
	 */
	public static String getJSONResponse(String entity, String command) {
		HttpURLConnection con = null;
		String payload = "Error";
		try {
			URL url = new URL(urlRest + entity + "/" + command);
			System.out.println("" + url);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			// con.setConnectTimeout(15000);
			// con.setRequestMethod("GET");
			con.setDoInput(true);
			try {
				con.connect();
			} catch (IOException e) {
				System.err.println("Failed to connect to server");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			payload = reader.readLine();
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.disconnect();
		}
		return payload;
	}

	/**
	 * Function that validates the login
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean validateLogin(String username, String password) {
		String serverResponse = getJSONResponse("entities.inspector", username);
		String returnedPassword = "FAILED_RESPONSE";
		if (serverResponse != "Error")
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(serverResponse);
				JSONObject jsonObject = (JSONObject) obj;
				returnedPassword = (String) jsonObject.get("password");
			} catch (Exception e) {
				returnedPassword = "JSON Error";
			}
		return (password.equals(returnedPassword) && username
				.equals("buscompany"));
	}

	/**
	 * Function that retrieves from server the ticket with a given id
	 * @param id
	 * @return
	 */
	public static Ticket getTicketFromId(String id) {
		String serverResponse = getJSONResponse("entities.ticket", "" + id);
		Ticket ticket = new Ticket();
		if (serverResponse != "Error")
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(serverResponse);
				JSONObject jsonObject = (JSONObject) obj;
				ticket.setIdticket((String) jsonObject.get("idticket"));
				ticket.setisChecked((Boolean) jsonObject.get("ischecked"));
				ticket.setTimeofvalidation((String) jsonObject
						.get("timeodvalidation"));
				ticket.setBusid((Long) jsonObject.get("idbus"));
				ticket.setType((String) jsonObject.get("type"));
				ticket.setIsvalidated((Boolean) jsonObject.get("isvalidated"));
			} catch (Exception e) {
				System.err.println("Error while parsing JSON object");
				e.printStackTrace();
			}
		return ticket;
	}
	
	/**
	 * Function that validates a users ticket in a bus terminal
	 * @param ticket
	 * @param busid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	public static boolean useTheTicket(String uuid, String username) {
		Ticket ticket = getTicketFromId(uuid);
		if (ticket.isIsvalidated())
			return false;
		// create the JSON object to send
		JSONObject obj = new JSONObject();
		obj.put("idticket", ticket.getIdticket());
		obj.put("type", ticket.getType());
		obj.put("isvalidated", true);
		obj.put("ischecked", ticket.isChecked());
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		obj.put("timeodvalidation", formatter.format(date).toString());
		obj.put("idbus", busNumber);
		
		JSONObject obj2 = new JSONObject();
		obj2.put("login", username);
		obj2.put("ticketid", uuid);

		// send the updated ticket to server
		HttpURLConnection con = null, con2 = null;
		try {
			URL url = new URL(urlRest + "entities.ticket");
			URL url2 = new URL(urlRest + "entities.lastticketused");

			con = (HttpURLConnection) url.openConnection();
			con2 = (HttpURLConnection) url2.openConnection();
			con.setReadTimeout(10000);
			con2.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con2.setConnectTimeout(15000);
			con.setRequestMethod("PUT");
			con2.setRequestMethod("PUT");
			con.setDoOutput(true);
			con2.setDoOutput(true);
			con.setDoInput(true);
			con2.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con2.setRequestProperty("Content-Type", "application/json");
			String payload = obj.toJSONString();
			String payload2 = obj2.toJSONString();
			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			OutputStreamWriter writer2 = new OutputStreamWriter(
					con2.getOutputStream(), "UTF-8");
			writer.write(payload, 0, payload.length());
			writer2.write(payload2, 0, payload2.length());
			writer.close();
			writer2.close();
			con.connect();
			con2.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					con2.getInputStream(), "UTF-8"));
			payload = reader.readLine();
			payload2 = reader.readLine();
			reader.close();
			reader2.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error while validating the ticket");
			return false;
		} finally {
			if (con != null)
				con.disconnect();
			if (con2 != null)
				con2.disconnect();
		}
		return true;
	}
	
	/**
	 * Setter for the Bus number
	 * @param number
	 */
	@SuppressWarnings("unchecked")
	public static void setBusNumber(int number){
		busNumber = number;
		// create the JSON object to send
		JSONObject obj = new JSONObject();
		obj.put("idbus", number);

		// send the updated ticket to server
		HttpURLConnection con = null;
		try {
			URL url = new URL(urlRest + "entities.bus");

			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("PUT");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			String payload = obj.toJSONString();
			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			writer.write(payload, 0, payload.length());
			writer.close();
			con.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			payload = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error while setting the bus number");
		} finally {
			if (con != null)
				con.disconnect();
		}
	}
}
