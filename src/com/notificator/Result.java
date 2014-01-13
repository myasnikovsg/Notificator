package com.notificator;

import java.io.Serializable;

public class Result implements Serializable {
	private String name;
	private int array[];
	private int buyout;
	private int bid;
	private int main_attr;
	private int time;
	private int property_number;
	private int socket_number;
	
	public int getProperty_number() {
		return property_number;
	}
	public void setProperty_number(int property_number) {
		this.property_number = property_number;
	}
	public int getSocket_number() {
		return socket_number;
	}
	public void setSocket_number(int socket_number) {
		this.socket_number = socket_number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getArray() {
		return array;
	}
	public void setArray(int[] array) {
		this.array = array;
	}
	public int getBuyout() {
		return buyout;
	}
	public void setBuyout(int buyout) {
		this.buyout = buyout;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getMain_attr() {
		return main_attr;
	}
	public void setMain_attr(int main_attr) {
		this.main_attr = main_attr;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
