package com.example.virtualtravelapp.model;

public class DiaDanh {
	private int idDiaDanh;
	private String nameDiaDanh;
	private String imDiaDanh;
	private String image_int;
	private String latlng;
	private String city;
	private int regions;
	private int favotite;

	public DiaDanh() {

	}

	public DiaDanh(int idDiaDanh, String nameDiaDanh, String imDiaDanh, String image_int, String latlng, String city, int regions, int favotite) {
		this.idDiaDanh = idDiaDanh;
		this.nameDiaDanh = nameDiaDanh;
		this.imDiaDanh = imDiaDanh;
		this.image_int = image_int;
		this.latlng = latlng;
		this.city = city;
		this.regions = regions;
		this.favotite = favotite;
	}

	public int getIdDiaDanh() {
		return idDiaDanh;
	}

	public void setIdDiaDanh(int idDiaDanh) {
		this.idDiaDanh = idDiaDanh;
	}

	public String getNameDiaDanh() {
		return nameDiaDanh;
	}

	public void setNameDiaDanh(String nameDiaDanh) {
		this.nameDiaDanh = nameDiaDanh;
	}

	public String getImDiaDanh() {
		return imDiaDanh;
	}

	public void setImDiaDanh(String imDiaDanh) {
		this.imDiaDanh = imDiaDanh;
	}

	public String getImage_int() {
		return image_int;
	}

	public void setImage_int(String image_int) {
		this.image_int = image_int;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public int getRegions() {
		return regions;
	}

	public void setRegions(int regions) {
		this.regions = regions;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getFavotite() {
		return favotite;
	}

	public void setFavotite(int favotite) {
		this.favotite = favotite;
	}
}
