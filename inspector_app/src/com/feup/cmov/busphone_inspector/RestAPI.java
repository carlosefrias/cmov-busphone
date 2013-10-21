package com.feup.cmov.busphone_inspector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Entities.Bus;
import Entities.Ticket;

public class RestAPI {
	private static String urlRest = "http://192.168.56.1:8080/WebServiceX/webresources/";

	/**
	 * To be used as a testing function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		//System.out.println(validateLogin("insp", "coiso"));
		//System.out.println(validateLogin("insp", "pass2"));
		//System.out.print(loadBusFromServer());
		//System.out.println(loadTicketListInBus(2));
		Ticket tck = getTicketFromId("12345");
		System.out.println("" + tck);
		validateTicket(tck);
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
		System.out.println(payload);
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
		return (password.equals(returnedPassword));
	}

	/**
	 * Function that returns the list of busses from database
	 * 
	 * @return
	 */
	public static ArrayList<Bus> loadBusFromServer() {
		ArrayList<Bus> result = new ArrayList<Bus>();

		String serverResponse = getJSONResponse("entities.bus", "");

		System.out.println("***" + serverResponse);

		if (serverResponse != "Error")
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(serverResponse);
				JSONArray jsonArray = (JSONArray) obj;
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject item = iterator.next();
					long idbus = (Long) item.get("idbus");
					Bus bus = new Bus((int) idbus);
					result.add(bus);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("error while parsing JSON object");
			}
		return result;
	}

	/**
	 * Function that returns the list of used tickets in a given bus
	 * 
	 * @param idbus
	 * @return
	 */
	public static ArrayList<Ticket> loadTicketListInBus(int idbus) {
		ArrayList<Ticket> list = new ArrayList<Ticket>();
		String allTickets = getJSONResponse("entities.ticket", "");
		if (allTickets != "Error")
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(allTickets);
				JSONArray jsonArray = (JSONArray) obj;
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject item = iterator.next();
					// Building the ticket
					Ticket ticket = new Ticket();
					ticket.setIdticket((String) item.get("idticket"));
					ticket.setType((String) item.get("type"));
					ticket.setBusid((Long) item.get("idbus"));
					ticket.setIsused((Boolean) item.get("isused"));
					ticket.setIsvalidated((Boolean) item.get("isvalidated"));
					ticket.setTimeofvalidation((Date) item
							.get("timeodvalidation"));
					// TODO: Acrescentar a condição do tempo...
					if (ticket.getBusid() == idbus && ticket.isIsused()) {
						// Adding the ticket to list
						list.add(ticket);
					}
				}
			} catch (Exception e) {
				System.err.println("error while parsing json object");
			}
		return list;
	}

	public static Ticket getTicketFromId(String id){
		String serverResponse = getJSONResponse("entities.ticket",
				"" + id);
		Ticket ticket = new Ticket();
		if (serverResponse != "Error")
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(serverResponse);
				JSONObject jsonObject = (JSONObject) obj;
				ticket.setIdticket((String) jsonObject.get("idticket"));
				ticket.setIsused((Boolean) jsonObject.get("isused"));
				ticket.setTimeofvalidation((Date) jsonObject.get("timeodvalidation"));
				ticket.setBusid((Long) jsonObject.get("idbus"));
				ticket.setType((String) jsonObject.get("type"));
				ticket.setIsvalidated((Boolean) jsonObject.get("isvalidated"));
			}catch(Exception e){
				System.err.println("Error while parsing JSON object");
				e.printStackTrace();
			}
		return ticket;		
	}
	@SuppressWarnings("unchecked")
	public static void validateTicket(Ticket ticketToUpdate) {
		ticketToUpdate.setIsvalidated(true);
		System.out.println("****" + ticketToUpdate);
		// create the JSON object to send
		JSONObject obj2 = new JSONObject();
		obj2.put("idticket", ticketToUpdate.getIdticket());
		obj2.put("type", ticketToUpdate.getType());
		obj2.put("isvalidated", ticketToUpdate.isIsvalidated());
		obj2.put("isused", ticketToUpdate.isIsused());
		obj2.put("timeodvalidation", ticketToUpdate.getTimeofvalidation());
		obj2.put("idbus", ticketToUpdate.getBusid());
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
			String payload = obj2.toJSONString();
			System.out.println("payload: " + payload);
			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			writer.write(payload, 0, payload.length());
			writer.close();
			con.connect();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			payload = reader.readLine();
			reader.close();
		} catch (IOException e) {
		} finally {
			if (con != null)
				con.disconnect();
		}
	}

}
