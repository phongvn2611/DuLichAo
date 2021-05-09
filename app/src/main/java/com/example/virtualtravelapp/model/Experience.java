package com.example.virtualtravelapp.model;

public class Experience {
	private int id;
	private String name;
	private String image;
	private String detail;

	public Experience(){}
	public Experience(String name,String image, String detail){
		this.name = name;
		this.image = image;
		this.detail = detail;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
