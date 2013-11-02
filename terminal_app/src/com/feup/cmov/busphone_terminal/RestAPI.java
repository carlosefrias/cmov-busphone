package com.feup.cmov.busphone_terminal;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import Entities.Bus;
import Entities.Ticket;

public class RestAPI {
	private static String urlRest = "http://172.29.109.8:8080/WebServiceX/webresources/";
	private static int busNumber = 2;

	/**
	 * To be used as a testing function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		/*Ticket ticket = getTicketFromId("novoid");
		System.out.println(ticket);
		validateTicket(ticket, busNumber);
		ticket = getTicketFromId("novoid");
		System.out.println(ticket);*/
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
	public static boolean useTheTicket(Ticket ticket, int busid) {
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
		obj.put("idbus", busid);

		// send the updated ticket to server
		HttpURLConnection con = null;
		try {
			URL url = new URL(urlRest + "entities.ticket");

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
			System.err.println("Error while validating the ticket");
			return false;
		} finally {
			if (con != null)
				con.disconnect();
		}
		return true;
	}

}
