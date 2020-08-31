package com.example.music.dto;



public class SongsDto {
	
	private Long id;

	private String audio_name;
	
	
	private String category;
	
	private String language;
	
	private String movie_name;
	
	private String artist;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getaudio_name() {
		return audio_name;
	}

	public void setaudio_name(String audio_name) {
		this.audio_name = audio_name;
	}
	
	public String getcategory() {
		return category;
	}

	public void setcategory(String category) {
		this.category = category;
	}
	
	public String getlanguage() {
		return language;
	}

	public void setlanguage(String language) {
		this.language = language;
	}
	
	public String getmovie_name() {
		return movie_name;
	}

	public void setmovie_name(String movie_name) {
		this.movie_name = movie_name;
	}
	
	public String getartist() {
		return artist;
	}

	public void setartist(String artist) {
		this.artist = artist;
	}

}
