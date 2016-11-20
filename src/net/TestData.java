package net;

import java.util.Date;

public class TestData {
	String bla;
	int x;
	int y;
	boolean jo;
	Date date;
	
	public TestData()
	{
		bla = "abc";
		x = 10;
		y = 12;
		jo = false;
		date = new Date(System.currentTimeMillis());
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getBla() {
		return bla;
	}

	public void setBla(String bla) {
		this.bla = bla;
	}

	public boolean isJo() {
		return jo;
	}

	public void setJo(boolean jo) {
		this.jo = jo;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
