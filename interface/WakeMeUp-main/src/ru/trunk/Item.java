package ru.trunk;

public class Item{
	String Id;
	String time;
	String address;
	String discipline;
	String type;
	String tutor;
	Item(String Id, String time, String address, String discipline,	String type,String tutor){
		this.Id = Id;
		this.time = time;
		this.address = address;
		this.discipline = discipline;
		this.type = type;
		this.tutor = tutor;
	}
}