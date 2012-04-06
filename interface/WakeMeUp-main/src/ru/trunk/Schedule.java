package ru.trunk;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	List<Item> items; 
	public Schedule(List <Item> items) {
		this.items = items;
	}
	public Schedule(){
		this.items = new ArrayList<Item>(); 
	}
}
