package com.feup.cmov.busphone_passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.annotation.SuppressLint;
import Entities.Passenger;
import Entities.Ticket;


public class RestAPI {
	private static String urlRest = "http://172.29.109.8:8080/WebServiceX/webresources/";
	/**
	 * To be used as a testing function
	 * @param args
	 */
	public static void main(String args[]) {
		//System.out.println(validateLogin("insp", "coiso"));
		//System.out.println(validateLogin("insp", "pass2"));
		//System.out.print(loadBusFromServer());
		
		//System.out.println(loadTicketListInBus(2));
		Passenger p = new Passenger("user123", "pass", "full name", "VISA", 13377413, "11/666");
		addUser(p);
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
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean validateLogin(String username, String password) {
		String serverResponse = getJSONResponse("entities.passenger",username);
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
	 * Function that adds an user to the server database
	 * 
	 * @param passenger
	 * @return success/fail
	 */
	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	public static boolean addUser(Passenger passenger) {
		// create the JSON object to send
		JSONObject obj = new JSONObject();
		obj.put("login", passenger.getLogin());
		obj.put("password", passenger.getPassword());
		obj.put("name", passenger.getFullName());
		obj.put("surname", "");
		obj.put("creditcardtype", passenger.getCreditCardType());
		obj.put("creditcardnumber", passenger.getCreditCardNumber());
		obj.put("creditcardvalidity", passenger.getCreditCardValidity());

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
			System.err.println("Unable to create a new passenger");
			return false;
		} finally {
			if (con != null)
				con.disconnect();
		}
		return true;
	}

	@SuppressWarnings("unused")
	private static Passenger getPassenger(String username) {
		String serverresponse = getJSONResponse("entities.passenger", username);
		if (!serverresponse.equals("Error")) {
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(serverresponse);
				JSONObject jsonObject = (JSONObject) obj;
				Passenger p = new Passenger((String) jsonObject.get("login"),
						(String) jsonObject.get("password"),
						(String) jsonObject.get("name"),
						(String) jsonObject.get("creditcardtype"),
						(Long) jsonObject.get("creditcardnumber"),
						(String) jsonObject.get("creditcardvalidity"));
				return p;
			} catch (Exception e) {
				System.err.println("Unable to return passenger from server");
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}

	/**
	 * Function that allows an user to by tickets
	 * 
	 * @param type
	 * @param numberOfTickets
	 * @param passenger
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean buyTickets(String type, int numberOfTickets,
			Passenger passenger) {
		if (numberOfTickets > 10 || numberOfTickets < 0)
			return false;
		for (int i = 0; i < numberOfTickets; i++) {
			// create the JSON object to send
			JSONObject objTicket = new JSONObject();
			String ticketUniqueID = UUID.randomUUID().toString();
			objTicket.put("idticket", ticketUniqueID);
			objTicket.put("type", type);
			objTicket.put("isvalidated", false);
			objTicket.put("ischecked", false);
			objTicket.put("timeodvalidation", "1990/01/01 00:00:00");// default date
			objTicket.put("idbus", -1);// default bus

			JSONObject obj = new JSONObject();
			obj.put("ticketid", ticketUniqueID);
			obj.put("loginpassenger", passenger.getLogin());
			JSONObject objPassengerTicket = new JSONObject();
			objPassengerTicket.put("passengerticketsPK", obj);

			System.out.println(objPassengerTicket.toJSONString());

			// send the updated ticket to server
			HttpURLConnection conTicket = null, conPassegerTicket = null;
			try {
				URL urlTicket = new URL(urlRest + "entities.ticket");
				URL urlPassengerTicket = new URL(urlRest
						+ "entities.passengertickets");

				conTicket = (HttpURLConnection) urlTicket.openConnection();
				conPassegerTicket = (HttpURLConnection) urlPassengerTicket
						.openConnection();

				conTicket.setReadTimeout(10000);
				conPassegerTicket.setReadTimeout(10000);

				conTicket.setConnectTimeout(15000);
				conPassegerTicket.setConnectTimeout(15000);

				conTicket.setRequestMethod("POST");
				conPassegerTicket.setRequestMethod("POST");

				conTicket.setDoOutput(true);
				conPassegerTicket.setDoOutput(true);

				conTicket.setDoInput(true);
				conPassegerTicket.setDoInput(true);

				conTicket
						.setRequestProperty("Content-Type", "application/json");
				conPassegerTicket.setRequestProperty("Content-Type",
						"application/json");

				String payloadTicket = objTicket.toJSONString();
				String payloadPassengerTicket = objPassengerTicket
						.toJSONString();

				OutputStreamWriter writerTicket = new OutputStreamWriter(
						conTicket.getOutputStream(), "UTF-8");
				OutputStreamWriter writerPassengerTicket = new OutputStreamWriter(
						conPassegerTicket.getOutputStream(), "UTF-8");

				writerTicket.write(payloadTicket, 0, payloadTicket.length());
				writerPassengerTicket.write(payloadPassengerTicket, 0,
						payloadPassengerTicket.length());

				writerTicket.close();
				writerPassengerTicket.close();

				conTicket.connect();
				conPassegerTicket.connect();

				BufferedReader readerTicket = new BufferedReader(
						new InputStreamReader(conTicket.getInputStream(),
								"UTF-8"));
				BufferedReader readerPassengerTicket = new BufferedReader(
						new InputStreamReader(
								conPassegerTicket.getInputStream(), "UTF-8"));

				payloadTicket = readerTicket.readLine();
				payloadPassengerTicket = readerPassengerTicket.readLine();

				readerTicket.close();
				readerPassengerTicket.close();

			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error while trying to buy tickets");
				return false;
			} finally {
				if (conTicket != null)
					conTicket.disconnect();
				if (conPassegerTicket != null)
					conPassegerTicket.disconnect();
			}
		}
		return true;
	}

	/**
	 * Function that returns the passenger list of unused tickets
	 * 
	 * @param passenger
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Ticket> getPassengerUnusedTickets(
			Passenger passenger) {
		ArrayList<Ticket> list = new ArrayList<Ticket>();

		String allPassengerTickets = getJSONResponse(
				"entities.passengertickets", "");

		if (!allPassengerTickets.equals("Error"))
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(allPassengerTickets);
				JSONArray jsonArray = (JSONArray) obj;
				Iterator<JSONObject> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject item = iterator.next();
					JSONObject passticketObj = (JSONObject) item
							.get("passengerticketsPK");
					String p = (String) passticketObj.get("loginpassenger");
					String ticketid = (String) passticketObj.get("ticketid");
					// If the login passenger from the passenger tickets row
					// equals the passenger login
					if (passenger.getLogin().equals(p)) {
						Ticket ticket = getTicketFromId(ticketid);
						// and the ticket is unused, then add it to the list
						if (!ticket.isIsvalidated())
							list.add(ticket);
					}
				}
			} catch (Exception e) {
				System.err
						.println("error while retrieving passenger unused tickets");
				e.printStackTrace();
			}

		return list;
	}
	/**
	 * Function that returns the ticket given it's id
	 * 
	 * @param id
	 * @return
	 */
	private static Ticket getTicketFromId(String id) {
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
}
