package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Word;

@Mapper
public interface WordMapper {
	List<Word> getAllWords();
	void insertWord(Word word);
@Delete("DELETE FROM words")
public void deleteAll();
}
