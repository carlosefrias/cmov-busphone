package com.feup.cmov.busphone_inspector;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Entities.Bus;
import Entities.Passenger;
import Entities.Ticket;

public class RestAPI {
	private static String urlRest = "http://172.29.109.8:8080/WebServiceX/webresources/";

	/**192.168.1.71
	 * To be used as a testing function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		
		Ticket ticket = getTicketFromId("novoid");
		System.out.println("antes: " + ticket);
		//inspectTicket(ticket);
		ticket = getTicketFromId("novoid");
		System.out.println("depois: " + ticket);		
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
			//System.out.println("" + url);
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
		//System.out.println(payload);
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
					ticket.setisChecked((Boolean) item.get("ischecked"));
					ticket.setIsvalidated((Boolean) item.get("isvalidated"));
					ticket.setTimeofvalidation((String) item.get("timeodvalidation"));
					//calculating the time difference in minutes
					long difMinutes = getDiffMinutes(ticket.getTimeofvalidation());
					int maxMinutesAlowed;
					if(ticket.getType().equalsIgnoreCase("T3")) maxMinutesAlowed = 60;
					else if(ticket.getType().equalsIgnoreCase("T2")) maxMinutesAlowed = 30;
					else maxMinutesAlowed = 15;
					if (ticket.getBusid() == idbus && ticket.isIsvalidated() && difMinutes <= maxMinutesAlowed) {
						// If the bus is right and it's a validated ticket and the time travel is right them lets add the ticket to the list
						list.add(ticket);
					}
				}
			} catch (Exception e) {
				System.err.println("error while parsing json object");
			}
		return list;
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
				ticket.setTimeofvalidation((String) jsonObject.get("timeodvalidation"));
				ticket.setBusid((Long) jsonObject.get("idbus"));
				ticket.setType((String) jsonObject.get("type"));
				ticket.setIsvalidated((Boolean) jsonObject.get("isvalidated"));
			} catch (Exception e) {
				System.err.println("Error while parsing JSON object");
				e.printStackTrace();
			}
		return ticket;
	}

	// MOVER função para o passenger_app/RestApi
	@SuppressWarnings("unchecked")
	public static boolean buyTickets(String type, int numberOfTickets) {
		if (numberOfTickets > 10 || numberOfTickets < 0) return false;
		for (int i = 0; i < numberOfTickets; i++) {
			// create the JSON object to send
			JSONObject obj = new JSONObject();
			obj.put("idticket", UUID.randomUUID().toString());
			obj.put("type", type);
			obj.put("isvalidated", false);
			obj.put("ischecked", false);
			obj.put("timeodvalidation", "1990/01/01 00:00:00");
			obj.put("idbus", -1);
			//TODO: colocar o registo na tabela passanger tickets
			// send the updated ticket to server
			HttpURLConnection con = null;
			try {
				URL url = new URL(urlRest + "entities.ticket");

				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(10000);
				con.setConnectTimeout(15000);
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestProperty("Content-Type", "application/json");
				String payload = obj.toJSONString();
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
				e.printStackTrace();
				System.err.println("ERRO na compra dos bilhetes...");
				return false;
			} finally {
				if (con != null)
					con.disconnect();
			}
		}
		return true;
	}
	
	//Mover para passenger_app
	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	public static boolean addUser(Passenger passenger) {
		// create the JSON object to send
		JSONObject obj = new JSONObject();
		obj.put("login", passenger.getLogin());
		obj.put("password", passenger.getPassword());
		obj.put("name", passenger.getFullName());
		obj.put("surname", "surname");
		obj.put("creditcardtype", passenger.getCreditCardType());
		obj.put("creditcardnumber", passenger.getCreditCardNumber());
		obj.put("creditcardvalidity", null);

		// send the updated ticket to server
		HttpURLConnection con = null;
		try {
			URL url = new URL(urlRest + "entities.passenger");

			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			String payload = obj.toJSONString();
			System.out.println("payload: " + payload);
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
			System.err.println("ERRO criação de um novo utilizador.");
			return false;
		} finally {
			if (con != null)
				con.disconnect();
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private static long getDiffMinutes(String ticketDateString){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date  ticketDate = null;
	    long timeDifMinutes = -1;
	    try {
        	ticketDate = df.parse(ticketDateString);
    		Date actualDate = new Date();
    		
            long milliSecAnterior = ticketDate.getTime();
            long milliSecAtual = actualDate.getTime();
     
            long timeDifInMilliSec;
            if(milliSecAtual >= milliSecAnterior)
            {
                timeDifInMilliSec = milliSecAtual - milliSecAnterior;
            }
            else
            {
                return -1;
            }
            timeDifMinutes = timeDifInMilliSec / (60 * 1000);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return timeDifMinutes;
	}
	@SuppressWarnings("unchecked")
	public static boolean inspectTicket(String ticketid, ArrayList<Ticket> ticketList) {
		//get the ticket from server
		Ticket ticket = getTicketFromId(ticketid);
		if(!containsTicket(ticketList, ticket)) 
			return false;
		// create the JSON object to send
		JSONObject obj = new JSONObject();
		obj.put("idticket", ticket.getIdticket());
		obj.put("type", ticket.getType());
		obj.put("isvalidated", ticket.isIsvalidated());
		obj.put("ischecked", true);
		obj.put("timeodvalidation", ticket.getTimeofvalidation());
		obj.put("idbus", ticket.getBusid());

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
			System.err.println("ERRO validação do bilhete");
			return false;
		} finally {
			if (con != null)
				con.disconnect();
		}
		return true;
	}

	private static boolean containsTicket(ArrayList<Ticket> ticketList, Ticket ticket){
		boolean contains = false;
		for(int i = 0; i < ticketList.size(); i++){
			if(ticketList.get(i).getIdticket().equals(ticket.getIdticket())){
				contains = true;
				break;
			}										
		}
		return contains;
	}
}
