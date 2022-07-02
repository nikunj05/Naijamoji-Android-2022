package com.naijamojiapp.app.sqlitedb;

import java.io.Serializable;

public class DataModel implements Serializable {

	String Image;
	String Id;
	String character_limit;
	String is_studiomode;

	public DataModel(String Image, String Id,String character_limit,String is_studiomode) {
		this.Image = Image;
		this.Id = Id;
		this.character_limit = character_limit;
		this.is_studiomode = is_studiomode;
	}

	public String getCharacter_limit() {
		return character_limit;
	}

	public void setCharacter_limit(String character_limit) {
		this.character_limit = character_limit;
	}

	public String getIs_studiomode() {
		return is_studiomode;
	}

	public void setIs_studiomode(String is_studiomode) {
		this.is_studiomode = is_studiomode;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	// Getter
	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}
}
