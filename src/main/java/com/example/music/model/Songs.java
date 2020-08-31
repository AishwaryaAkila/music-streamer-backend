package com.example.music.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "SONGS")
public class Songs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "AUDIO_NAME", length = 50)
	private String audio_name;
	
	@Lob
	@Column(name = "AUDIO_IMAGE",length = 1000)
	private byte[] audio_image;
	
	@Lob
	@Column(name = "AUDIO_FILE",length = 1000)
	private byte[] audio_file;
	
	@Column(name = "CATEGORY", length=100)
	private String category;
	
	@Column(name = "LANGUAGE", length=100)
	private String language;
	
	@Column(name = "MOVIE_NAME", length=100)
	private String movie_name;
	
	@Column(name = "ARTIST", length=100)
	private String artist;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public byte[] getaudio_image() {
		return audio_image;
	}

	public void setaudio_image(byte[] data) {
		this.audio_image = data;
	}
	
	public byte[] getaudio_file() {
		return audio_file;
	}

	public void setaudio_file(byte[] data) {
		this.audio_file = data;
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
