package com.feup.cmov.busphone_passenger;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TicketData extends SQLiteOpenHelper {

	public TicketData(Context context) {
		super(context, "Ticket.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Ticket(idticket VARCHAR(100),"
				+ "type VARCHAR(4),"
				+ "ischecked TINYINT,"
				+ "isvalidated TINYINT,"
				+ "timeofvalidation VARCHAR(20),"
				+ "idbus INT(4));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Ticket");
		onCreate(db);
	}

	public Cursor getAllTickets(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * from Ticket", null);
		return cursor;
	}	
	
	public void addTicket(int nTickets, String[] uuids, String[] types){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues content = new ContentValues();
		
		for(int i = 0; i < nTickets; i++){
			content.put("idticket", uuids[i]);
			content.put("type", types[i]);
			content.put("ischecked", 0);
			content.put("isvalidated", 0);
			content.put("timeofvalidation", "");
			content.put("idbus", "");
			db.insert("Ticket", null, content);
		}
	}
	
	public void validateTicket(String uuid){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("isvalidated", 1);
		ArrayList<String> whereClauseArrayList = new ArrayList<String>();
		whereClauseArrayList.add(uuid);
		String[] whereClause = new String[whereClauseArrayList.size()];
		whereClauseArrayList.toArray(whereClause);
		db.update("Ticket", content, "WHERE idticket=?", whereClause);
	}
}
