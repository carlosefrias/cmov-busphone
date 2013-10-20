package com.feup.cmov.busphone_inspector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private static String urlRest = "http://192.168.1.100:8080/WebServiceX/webresources/";
	/**
	 * To be used as a testing function
	 * @param args
	 */
	public static void main(String args[]) {
		//System.out.println(validateLogin("insp", "coiso"));
		//System.out.println(validateLogin("insp", "pass2"));
		//System.out.print(loadBusFromServer());
		
		System.out.println(loadTicketListInBus(2));
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
		String serverResponse = getJSONResponse("entities.inspector",username);
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
	 * @return
	 */
	public static ArrayList<Bus> loadBusFromServer(){
		ArrayList<Bus> result = new ArrayList<Bus>();
		
		String serverResponse = getJSONResponse("entities.bus", "");
		
		System.out.println("***"+serverResponse);
		
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
				//System.err.println("error while parsing json object");
			}		
		return result;	
	}
	/**
	 * Function that returns the list of used tickets in a given bus
	 * @param idbus
	 * @return
	 */
	public static ArrayList<Ticket> loadTicketListInBus(int idbus){
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
						//Building the ticket
						Ticket ticket = new Ticket();
						ticket.setIdticket((String)item.get("idticket"));
						ticket.setType((String)item.get("type"));
						ticket.setBusid((Long)item.get("idbus"));
						ticket.setIsused((Boolean)item.get("isused"));
						ticket.setIsvalidated((Boolean)item.get("isvalidated"));
						ticket.setTimeofvalidation((Date)item.get("timeodvalidation"));
						//TODO: Acrescentar a condição do tempo...
						if(ticket.getBusid() == idbus && ticket.isIsused()){
							//Adding the ticket to list
							list.add(ticket);
						}
					}				
			} catch (Exception e) {
				System.err.println("error while parsing json object");
			}				
		return list;		
	}
	
	public static void validateTicket(Ticket ticket){
		//TODO:Metodo put para validat bilhete
		/*
		String payload = "Error";
        HttpURLConnection con = null;
        try {
          URL url = new URL(urlRest + );

          con = (HttpURLConnection) url.openConnection();
          con.setReadTimeout(10000);      
          con.setConnectTimeout(15000);   
          con.setRequestMethod("PUT");
          con.setDoOutput(true);
          con.setDoInput(true);
          con.setRequestProperty("Content-Type", "application/json");

          payload = "{\"ClinicNr\":" + et1.getText().toString() + ",\"Name\":\"" + et2.getText().toString() + "\"}";
          OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
          writer.write(payload, 0, payload.length());
          writer.close();
          con.connect();
          BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8" ));
          payload = reader.readLine();
          reader.close();
        }
        catch (IOException e) {
        }
        finally {
          if (con != null)
            con.disconnect();
        }*/
	}
	
}
