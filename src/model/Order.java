package model;

import java.io.Serializable;

public class Order implements Serializable{
	private boolean isHardcore;
	private byte classes[];
	private byte lvl_group;
	private byte type_2[];
	private int maxBuyout;
	private int[] property_type;
	private int[] property_value;
	private String name;
	private long duration;
	private int main_attr;
	private String id;
	private byte money_type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte getMoney_type() {
		return money_type;
	}
	public void setMoney_type(byte money_type) {
		this.money_type = money_type;
	}
	public int getMain_attr() {
		return main_attr;
	}
	public void setMain_attr(int main_attr) {
		this.main_attr = main_attr;
	}
	public byte getLvl_group() {
		return lvl_group;
	}
	public void setLvl_group(byte lvl_group) {
		this.lvl_group = lvl_group;
	}
	public int[] getProperty_type() {
		return property_type;
	}
	public void setProperty_type(int[] property_type) {
		this.property_type = property_type;
	}
	public int[] getProperty_value() {
		return property_value;
	}
	public void setProperty_value(int[] property_value) {
		this.property_value = property_value;
	}
	public boolean isHardcore() {
		return isHardcore;
	}
	public void setHardcore(boolean isHardcore) {
		this.isHardcore = isHardcore;
	}
	public byte[] getClasses() {
		return classes;
	}
	public void setClasses(byte classes[]) {
		this.classes = classes;
	}
	public byte[] getType_2() {
		return type_2;
	}
	public void setType_2(byte type_2[]) {
		this.type_2 = type_2;
	}
	public int getMaxBuyout() {
		return maxBuyout;
	}
	public void setMaxBuyout(int maxBuyout) {
		this.maxBuyout = maxBuyout;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
}
