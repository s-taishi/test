package com.example.demo.entity;

import jakarta.validation.constraints.Pattern;

public class Word {

	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Pattern(regexp ="^[\\u3040-\\u309F]+$",message ="入力はひらがな")
	private String word;
	public long getId() {
		return id;
	}
	public String getWord() {
		return word;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
}
