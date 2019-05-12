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
	
	/**
	 * Phrases of this sentence.
	 */
	private ArrayList<Phrase> _phrases = new ArrayList<Phrase>();
	
	
	public SentenceModel(){
		this._words = new ArrayList<Word>();
	}
	
	public SentenceModel(SceneModel scene){
		this._words = new ArrayList<Word>();
		this._scene = scene;
		
		arrangeWords();
	}
	
	public SentenceModel(ArrayList<Word> words, SceneModel scene){
		this._words = words;
		this._scene = scene;
		
		arrangeWords();
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
	
	public Word getVerb(){
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isVerb())
					return wrd;
					
		MyError.error("This sentence has no verb!: " + this);
		return null;
	}
	
	private ArrayList<Word> getWordsWithSourceNumber(int number) {
		
		ArrayList<Word> allStartingWords = new ArrayList<Word>();		
		
		if(_words != null)
			for(Word wrd:_words)
				if(wrd != null && wrd._srcOfSynTag_number == number)
					allStartingWords.add(wrd);
			
		return allStartingWords;
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
	
	private void addPhrase(Phrase new_phrase){
		if(_phrases == null)
			_phrases = new ArrayList<Phrase>();
		
		if(new_phrase != null){
			_phrases.add(new_phrase);
			new_phrase._senteceModel = this;
		}
	}	
	
	@SuppressWarnings("unused")
	private boolean removePhrase(Phrase phrase){
		if(phrase == null)
			return false;
		
		if(_phrases == null){
			phrase._senteceModel = null;
			return true;
		}
		
		if(_phrases.contains(phrase)){
			phrase._senteceModel = null;
			_phrases.remove(phrase);
			return true;
		}
		return false;
	}
	
	private void makePhrases(Word phraseHead, ArrayList<Word> phraseWords){
		if(phraseHead == null)
			return;		
		
		if(!phraseWords.contains(phraseHead))
			phraseWords.add(phraseHead);
		
		ArrayList<Word> fromThis = getWordsWithSourceNumber(phraseHead._number);
		
		if(Common.isEmpty(fromThis))
			return;
		
		for(Word ph_w:fromThis){
			
			if(ph_w.isAdjective()){
				System.out.println("\nfirst here in makePhrase!");
				phraseHead.addAdjective(ph_w);
			}
			else if(ph_w.isMozaf_elaih()){
				System.out.println("\nfirst here in makePhrase!");
				phraseHead.addMozaf_elaih(ph_w);
			}
			
			makePhrases(ph_w, phraseWords);
		}
	}
	
	private void arrangeWords(){
		Word verb = getVerb();
		
		if(verb == null)
			return;
		
		int root_num = verb._number;
		
		ArrayList<Word> phraseHeads = getWordsWithSourceNumber(root_num);
			
		if(Common.isEmpty(phraseHeads))
			return;
					
		//------------ detect and generate phrases in this sentence ------------
		
		for(Word ph_h:phraseHeads){
			
			ArrayList<Word> phraseWords = new ArrayList<Word>();
			
			makePhrases(ph_h, phraseWords);
			
			Phrase ph = new Phrase(ph_h, phraseWords);
			
			this.addPhrase(ph);
			
			print(""+ ph);
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
