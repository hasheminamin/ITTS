package model;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class SentenceModel {
	
	/**
	 * the scene which this sentence belongs to. 
	 */
	public SceneModel _scene;
	
	/**
	 * original sentence in "natural language". 
	 */
	private String NLSentence;
	
	/**
	 * Words of this sentence.
	 */
	private ArrayList<Word> _words = new ArrayList<Word>();
	
	public SentenceModel(){
		this._words = new ArrayList<Word>();
	}
	
	public SentenceModel(SceneModel scene){
		this._words = new ArrayList<Word>();
		this._scene = scene;		
	}
	
	public SentenceModel(ArrayList<Word> words, SceneModel scene){
		this._words = words;
		this._scene = scene;
	}
	
	/**
	 * @return the _words
	 */
	public ArrayList<Word> getWords() {
		return _words;
	}
	
	/**
	 * 
	 * @param word_name the Word of this SentenceModel with _wordName word_name.
	 * @return 
	 */
	public Word getWord(String word_name) {
		if(!Common.isEmpty(_words))
			for(Word w:_words)
				if(w._wordName.equalsIgnoreCase(word_name))
					return w;
//		//TODO: is it correct to search in _prepared_words too?!
//		if(!Common.isEmpty(_prepared_words))
//			for(Word w:_prepared_words)
//				if(w._wordName.equalsIgnoreCase(word_name))
//					return w;
		return null;
	}
	
	
	/**
	 * 
	 * @param word_number the Word of this SentenceModel with _number word_number.
	 * @return 
	 */
	public Word getWord(int word_number) {
		if(!Common.isEmpty(_words))
			for(Word w:_words)
				if(w._number == word_number)
					return w;
		return null;
	}
	
	public Word getWord(Node wordNode) {		
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)					
				if(wrd != null && wrd._wsd != null && wrd._wsd.equalsRelaxed(wordNode))
					return wrd;
		
//		if(!Common.isEmpty(_prepared_words))
//			for(Word wrd:_prepared_words)					
//				if(wrd != null && wrd._wsd != null && wrd._wsd.equalsRelaxed(wordNode))
//					return wrd;

		return null;
	}
	
//	public Word getWord(SemanticTag semanticTag){
//		if(semanticTag == null)
//			return null;
//		
//		if(semanticTag.isMainSemanticTag())
//			return getWord(semanticTag.convertToMainSemanticTag());
//		
//		else if(semanticTag.isSubSemanticTag())
//			return getWord(semanticTag.convertToSubSemanticTag());
//					
//		return null;	
//	}

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
	
	public ArrayList<Word> calculateMultiSemanticTagWords(){
		if(_words == null || _words.size() == 0)
			return null;
		
		ArrayList<Word> multiSemTagwords = new ArrayList<Word>();
		
		for(Word wrd:_words)
			if(wrd.hasMultiSemanticTag())
				multiSemTagwords.add(wrd);
			
		return multiSemTagwords;
	}
	
	public ArrayList<Word> calculateWronglyPredictedWords(){
		if(_words == null || _words.size() == 0)
			return null;
		
		ArrayList<Word> wronglyPredictedWords = new ArrayList<Word>();
		
		for(Word wrd:_words)
			if(!wrd.get_isTruelyPredicted())
				wronglyPredictedWords.add(wrd);
			
		return wronglyPredictedWords;
	}
	
	public ArrayList<Word> calculateTruelyPredictedWords(){
		if(_words == null || _words.size() == 0)
			return null;
		
		ArrayList<Word> truelyPredictedWords = new ArrayList<Word>();
		
		for(Word wrd:_words)
			if(wrd.get_isTruelyPredicted())
				truelyPredictedWords.add(wrd);
			
		return truelyPredictedWords;
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
