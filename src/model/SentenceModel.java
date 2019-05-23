package model;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import enums.POS;

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
			
	}
	
	public SentenceModel(ArrayList<Word> words, SceneModel scene){
		this._words = words;
		this._scene = scene;
		
		arrangeNounPhraseAsLarge();
//		arrangeNounPhraseAsSmall();
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
	
	public Word getWord(Node wordNode, String wordNodeName) {		
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words) {					
				if(wrd != null && wrd._wsd != null && wrd._wsd.equalsRelaxed(wordNode))
					return wrd;
				if(wrd!= null && wrd._wsd_name != null && wrd._wsd_name.equals(wordNodeName))
					return wrd;
			}
						
//		if(!Common.isEmpty(_prepared_words))
//			for(Word wrd:_prepared_words)					
//				if(wrd != null && wrd._wsd != null && wrd._wsd.equalsRelaxed(wordNode))
//					return wrd;

		return null;
	}
	
	/**
	 * return the Word in a sentence which has ROOT as _syntaxTag or 0 as _srcOfSynTag_number
	 * @return
	 */
	public Word getRootWord(){
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isRootWord())
					return wrd;
					
		MyError.error("This sentence has no verb!: " + this);
		return null;
	}
	
	/**
	 * return all Words in a sentence which has V as _gPOS
	 * @return
	 */
	public ArrayList<Word> getAllVerbs(){
		
		ArrayList<Word> allVerbs = new ArrayList<Word>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isVerb())
					allVerbs.add(wrd);
				
		return allVerbs;
	}
	
//	public Word getSubject(){
//		if(!Common.isEmpty(_words))
//			for(Word wrd:_words)
//				if(wrd != null && wrd.isSubject())
//					return wrd;
//					
//		MyError.error("This sentence has no Subject!: " + this);
//		return null;
//	}
	
	/**
	 * return all Words in a sentence which has SBJ as _syntaxTag
	 * @return
	 */
	public ArrayList<Word> getAllSubjects(){
		
		ArrayList<Word> allSbjs = new ArrayList<Word>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isSubject())
					allSbjs.add(wrd);
				
		return allSbjs;
	}
	
	public boolean hasSubject(){
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isSubject())
					return true;					

		return false;
	}
	
	private ArrayList<Word> getWordsWithSourceNumber(int number) {
		
		ArrayList<Word> allStartingWords = new ArrayList<Word>();		
		
		if(_words != null)
			for(Word wrd:_words)
				if(wrd != null && wrd._srcOfSynTag_number == number)
					allStartingWords.add(wrd);
			
		return allStartingWords;
	}
	
	public Word getVerbWithOutSourceNumber(){
		if(Common.isEmpty(_words))
			return null;
			
		for(Word wrd:_words)			
			if(wrd._srcOfSynTag_number > _words.get(_words.size() - 1)._number)
				if(wrd.isVerb())
					return wrd;
				else {
					ArrayList<Word> startingWords = getWordsWithSourceNumber(wrd._number);
					if(!Common.isEmpty(startingWords))
						for(Word start:startingWords)
							if(start.isVerb())
								return start;					
				}
		for(Word wrd:_words)
			if(wrd._srcOfSynTag_number < _words.get(0)._number)
				if(wrd.isVerb())
					return wrd;
				else {
					ArrayList<Word> startingWords = getWordsWithSourceNumber(wrd._number);
					if(!Common.isEmpty(startingWords))
						for(Word start:startingWords)
							if(start.isVerb())
								return start;					
				}
		
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
		
		if(newWord._sentence == null || _words.contains(newWord))
			newWord._sentence = this;
		
		else if(newWord._sentence != this){
			MyError.error("\n!!!! change in sentence of a word from " + newWord._sentence + " to " + this + " for \"" + newWord + "\" word.\n");
			newWord._sentence = this;
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
	
	private void assignWordToWord(Word root, Word dependent) {
		if(root == null ||dependent == null)
			return;
		
		if(dependent.isAdjective()){
//			print("\nfirst here in makePhrase!");
			root.addAdjective(dependent);
		}
		else if(dependent.isMozaf_elaih()){
//			print("\nfirst here in makePhrase!");
			root.addMozaf_elaih(dependent);
		}
		else if(dependent.isSubject()) {
			root.addSubject(dependent);
		}
		else if(dependent.isObject())
			root.addObject(dependent);
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
			
			assignWordToWord(phraseHead, ph_w);
							
			makePhrases(ph_w, phraseWords);
		}
	}	
	
	/**
	 * this method generates phrases for each noun-phrase which is depended to main verb or
	 * each verb which exists in the sentence (words with POS "V")
	 * noun-phrase in this method are very small. 
	 */
	public void arrangeNounPhraseAsSmall(){
		Word root = getRootWord();
		
		if(root == null) {
			root = getVerbWithOutSourceNumber();
			if(root == null)
				return;	
		}			
		
		int root_num = root._number;

		print("root: " + root._wordName);
		
		ArrayList<Word> phraseHeads = getWordsWithSourceNumber(root_num);
				
		if(Common.isEmpty(phraseHeads))
			return;
		
		ArrayList<Word> rootOfPhraseHeads = new ArrayList<Word>();
		
		for(@SuppressWarnings("unused") Word h:phraseHeads)
			rootOfPhraseHeads.add(root);
		
					
		//------------ detect and generate phrases in this sentence ------------
		
		while(!phraseHeads.isEmpty()) {
			
			Word ph_h = phraseHeads.remove(0);
			Word rootOfPh_h = rootOfPhraseHeads.remove(0);
			
			
			assignWordToWord(rootOfPh_h, ph_h);
				
			ArrayList<Word> phraseWords = new ArrayList<Word>();
			
			makePhrases(ph_h, phraseWords);
								
			for(int index = 0; index < phraseWords.size();) {				
				Word ph_w = phraseWords.get(index);
				
				int delimIndex = 0;
				if(ph_w._gPOS == POS.V) {
					
					print("\nsub roots: " + ph_w);
					
					ArrayList<Word> subPhraseHeads = getWordsWithSourceNumber(ph_w._number);
					
					if(!Common.isEmpty(subPhraseHeads)) {
						phraseHeads.addAll(subPhraseHeads);
						
						for(@SuppressWarnings("unused") Word h:subPhraseHeads)
							rootOfPhraseHeads.add(ph_w);
					}
					
					delimIndex = index;
					index++;
					while(index <= phraseWords.size()) 		
						phraseWords.remove(delimIndex);

					break;
				}
				index++;
			}					
			
			Phrase ph = new Phrase(ph_h, phraseWords);
			
			this.addPhrase(ph);			
			
			print(""+ ph);			
		}
	}	
			
	/**
	 * this method generates phrases for each noun-phrase which is depended to main verb.
	 * noun-phrase in this method are very large. 
	 */
	public void arrangeNounPhraseAsLarge(){
		Word root = getRootWord();
		
		if(root == null) {
			root = getVerbWithOutSourceNumber();
			if(root == null)
				return;			
		}
		
		int root_num = root._number;
		
		ArrayList<Word> phraseHeads = getWordsWithSourceNumber(root_num);
			
		if(Common.isEmpty(phraseHeads))
			return;
					
		//------------ detect and generate phrases in this sentence ------------
			
		for(Word ph_h:phraseHeads){
			
			assignWordToWord(root, ph_h);
				
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
	
	private void print(String s){
		System.out.println(s);
	}	
	
}
