package com.example.virtualtravelapp.model;

public class Weather {
	private int code;
	private String day;
	private int image;
	private String cencius;
	private String text;
	private String date;

	public Weather(){}
//	public Weather(String name, int image, String cencius) {
//		this.name = name;
//		this.image = image;
//		this.cencius = cencius;
//	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getCencius() {
		return cencius;
	}

	public void setCencius(String cencius) {
		this.cencius = cencius;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
