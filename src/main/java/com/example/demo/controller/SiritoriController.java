package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Word;
import com.example.demo.mapper.WordMapper;

import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@Data
@RequiredArgsConstructor
public class SiritoriController {
	
	private final WordMapper wordMapper;
	private ArrayList<String> tango= new ArrayList<>();
	private char fc;				//初期化.最初の文字。比較用

	private char lc ='り';			//しりとりスタートのため

	
	
	@GetMapping("/index")			//最初の入力画面
	public String showIndex(Model model,HttpSession session) {
		session.invalidate();					//httpのセッションをリセットする
		wordMapper.deleteAll();					//データベース消去
		tango.clear();							//リストの単語を消去
		lc='り';								//lcの初期化。ｆｃは単語入力後に更新
		tango.add("しりとり");					//最初の文字としてしりとり設定
		model.addAttribute("lc","り");
		model.addAttribute("inputword","最初の文字：しりとり");
		return "submit";
	}
	@PostMapping("/submit")				//2回目以降の入力画面
	public String showSubmit( @RequestParam("word") @Validated String word,Model model,RedirectAttributes redirectAttributes) {
		if (!word.matches("^[\\u3040-\\u309F\\u30A0-\\u30FFー]+$")) {				//ひらがなであるかどうかチェック
			model.addAttribute("errorMessage", "ひらがなかカタカナで入力してください。");
			model.addAttribute("inputword",word);
			model.addAttribute("lc",lc);
			return "submit";
		}else {}
		Word words = new Word();					//ワード型を新しく作成
		String chengeWord =convertKatakanaToHiragana(word);	//wordをひらがなに
		char fc =chengeWord.charAt(0);					//今回の単語の最初の文字と 
		if(fc != lc) {								//前回の単語の最後の文字を比較
			return "fault";							//
		}else {		}
		char lc = chengeWord.charAt(chengeWord.length()-1);		//単語の最後の文字を変更する	
		if(lc==('ー')) {										//ーがあった場合は２つ前の文字に変更
			lc = chengeWord.charAt(chengeWord.length()-2);		//
		}else {}

		setFc(fc);									//フィールドfcを変更
	
		setLc(lc);									//フィールドlcを変更
	
		boolean bl = tango.contains(chengeWord);			//データベースに登録した単語と入力した単語を比較
		tango.add(chengeWord);							//単語に登録したリストに入力した単語を登録
		words.setWord(chengeWord);						//wordをセット。ひらがなに変換したものを登録
		model.addAttribute("inputword",word);		//入力した単語をmodel型に格納。inputwordとしてページに表示する
		model.addAttribute("lc",lc);				//最後の文字を切り取ってlcへ格納。submitで最後の文字として表示する
		wordMapper.insertWord(words);				//入力した原型をデータベースに登録
		if(word.endsWith("ん")||bl==true) {			//入力した最後の文字が”ん”、または登録している単語と同じとき終了
		return "result";
	}else {
		lc=word.charAt(chengeWord.length()-1);			//単語の最後の文字を登録
		return "submit";							//最後が”ん”または1度目の入力の時
	}
	}     
	  private static char convertToHiragana(char katakana) {
	        // 長音符号「ー」の場合はそのまま返す
	        if (katakana == 'ー') {
	            return katakana;
	        }
	        // カタカナの範囲内であれば変換
	        if (katakana >= 'ァ' && katakana <= 'ヶ') {
	            return (char) (katakana - 0x60);		//
	        } else {
	            return katakana; // カタカナ以外はそのまま返す
	        }
	    }
	  public static String convertKatakanaToHiragana(String input) {
	        StringBuilder result = new StringBuilder();
	        for (char ch : input.toCharArray()) {
	            result.append(convertToHiragana(ch));
	        }
	        return result.toString();
	    }
}