package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class Sentence {
	
	/**
	 * the scene which this sentence belongs to. 
	 */
	public Scene _scene;
	
	/**
	 * original sentence in "natural language". 
	 */
	private String NLSentence;
	
	/**
	 * Words of this sentence.
	 */
	private ArrayList<Word> _words = new ArrayList<Word>();
	
	public Sentence(){
		this._words = new ArrayList<Word>();
	}
	
	public Sentence(Scene scene){
		this._words = new ArrayList<Word>();
		this._scene = scene;		
	}
	
	public Sentence(ArrayList<Word> words, Scene scene){
		this._words = words;
		this._scene = scene;
	}
	
	public void addWord(Word newWord){
		if(_words == null)
			_words = new ArrayList<Word>();
		
		if(newWord == null || _words.contains(newWord))
			return;
	
		_words.add(newWord);
		
		if(newWord._sentece == null || _words.contains(newWord))
			newWord._sentece = this;
		
		else if(newWord._sentece != this){
			MyError.error("\n!!!! change in sentence of a word from " + newWord._sentece + " to " + this + " for \"" + newWord + "\" word.\n");
			newWord._sentece = this;
		}		
	}
	
	public void makeNaturalLanguage() {
		if(_words == null)
			return;
		String NL = "";
		for(Word wrd:_words)
			NL += wrd._wordName + " ";
		
		this.NLSentence = NL.trim();
	}
	
	@Override
	public String toString() {
		return NLSentence;
	}
	
	@SuppressWarnings("unused")
	private void print(String s){
		System.out.println(s);
	}	
	
}
