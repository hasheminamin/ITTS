package model;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.util.Common;

public class Phrase {
	/**
	 * senteceModel which this Phrase belongs to.
	 */
	public SentenceModel _senteceModel;
	
	private Word _headWord = null;
	
	private ArrayList<Word> _words = new ArrayList<Word>();
	
	private int size = 0; 
	
	public Phrase(Word headWord, ArrayList<Word> words){
		this.set_headWord(headWord);
		this.set_words(words);
	}
	
	//-------------------- setter part --------------------------
	/**
	 * @param _headWord the _headWord to set
	 */
	public void set_headWord(Word _headWord) {
		this._headWord = _headWord;		
	}
	
	/**
	 * @param words the words to set
	 */
	public void set_words(ArrayList<Word> words) {
		if(!Common.isEmpty(words))
			for(Word w:words)
				add_word(w);
	}
	
	/**
	 * @param newWord the newWord to be added to this Phrase _words.
	 */
	public void add_word(Word newWord) {
		if(_words == null)
			_words = new ArrayList<Word>();
		
		if(!_words.contains(newWord)){
			_words.add(newWord);
			size++;
			newWord._phrase = this;
		}
	}

	//-------------------- getter part --------------------------
	
	/**
	 * @return the _headWord
	 */
	public Word get_headWord() {
		return _headWord;
	}

	
	public ArrayList<Word> get_words() {
		return _words;
	}

	public int getSize() {
		return size;
	}

	public Word get_wordWithSyntax(DependencyRelationType syntaxTag) {
		if(syntaxTag == null)
			return null;
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd._syntaxTag == syntaxTag)
					return wrd;
		return null;
	}

	
	@Override
	public String toString() {
		String str = "Phrase with head: " + _headWord + " \n";
		for(Word w:_words)
			str += w + "  ";
		str += " size: " + size;
		return str;
	}
}
