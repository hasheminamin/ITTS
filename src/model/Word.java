package model;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import enums.POS;
import enums.ScenePart;
import enums.SemanticTag;

/**
 * Each Word object holds different information about itself.
 * sample: 4	برادر	N	OBJ	5	برادر§n-14090	نفر§n-13075	role	_	_	Arg1	_
 * information like its _number, _wordName, _pos, _syntaxTAg, _srcOfSynTag_number, _wsd_name, _super_wsd_name, sceneElement, ... _semanticTags ... 
 * 
 * @author ahmadi 
 */
public class Word {
	
	/**
	 * senteceModel which this Word belongs to.
	 */
	public SentenceModel _sentence;
	
	/**
	 * Phrase which this Word belongs to.
	 */
	public Phrase _phrase;
	
	/**
	 * number of this Word object in sentence.
	 */
	public int _number = -1;
	
	/**
	 * word String of this Word object.
	 */
	public String _wordName;
	
	/**
	 * great Part-Of-Speech of this Word object.
	 * we assume that it is the main POS that we have used before!
	 */
	public POS	_gPOS = null;
	
	/**
	 * this Word SyntaxTag 
	 */
	public DependencyRelationType _syntaxTag = null;
	
	/**
	 * the number of the source Word of this Word's _syntaxTag
	 */
	public int _srcOfSynTag_number = -1;	
	
	/**
	 * Word_Sense_Disambiguation of this Word object.. 
	 * It means mapping of this Word to KB concepts.
	 */
	public Node _wsd = null;
	
	/**
	 * the String name of Word_Sense_Disambiguation of this Word object. 
	 */
	public String _wsd_name;
	
	/**
	 * the String name of Word_Sense_Disambiguation of the father Word (one of main categories) of this Word object. 
	 */
	public String _super_wsd_name;
	
	/**
	 * the defined sceneElement of this Word object.
	 */
	public ScenePart _sceneElement = null;
	
	/**
	 * the sceneElements predicted for this Word object by LearningModel for each _SemanticTag.
	 */
	public ArrayList<ScenePart> _predictedSceneElements = new ArrayList<ScenePart>();
			
	/**
	 * determines if the defined and predicted sceneElements of this Word object are the same or not?
	 */
	private boolean _isTruelyPredicated = false;
	
	/**
	 * this Word Semantic-Role-Label tags.
	 */
	public ArrayList<SemanticTag> _semanticTags = new ArrayList<SemanticTag>();
	
	/**
	 * this is the number of the word that is the main reference of this word.
	 * the "-" sign attached to the number means the reference word is placed in the sentence before.
	 * the "--" sign attached to the number means the reference word is placed in the two sentence before.
	 * etc.
	 * the "+" sign attached to the number means the reference word is placed in the next sentence.
	 * the "++" sign attached to the number means the reference word is placed in the two next sentence.
	 * etc.
	 */
	public String _referenceWordNum = null;
	
	/**
	 * this is the word that is the main reference of this word.
	 */
	public Word _referenceWord = null;
	
	/**
	 * the verb assigned to this Word (as subject)
	 */
	private Word verb;
	
	/**
	 * if this Word is a verb, this parameter contains its subjects.
	 */
	private ArrayList<Word> subjects;
	
	/**
	 * The adjectives of this Word. 
	 */
	private ArrayList<Word> adjectives;
	
	/**
	 * The mosoof of this Word. 
	 */
	private Word mosoof;
		
	/**
	 * The mozaf_elaih of this Word. 
	 */	
	private ArrayList<Word> mozaf_elaih;

	/**
	 * The Mozaf of this Word. 
	 */
	private Word mozaf;
	
	
	/**
	 * This constructor gets an input string in the format 
	 * "4	برادر	N	OBJ	5	برادر§n-14090	نفر§n-13075	role	_	_	Arg1	_"
	 * these parts are respectively:
	 * 
	 * number	name	gPOS	syntaxTag	_srcOfSynTag_number	wsd_name, super_wsd_name, sceneElement, ... semanticTags ...
	 * @param wStr the String definition of this word as in Corpora text.
	 */
	public Word(String wStr, SentenceModel sentence) {
//		print(wStr);
				
		String[] parts = wStr.split("(\t)+");
		
		if(parts.length < 8){			
			MyError.error("Bad sentence information format " + wStr + " parts-num " + parts.length);
			return;
		}
		this._sentence = sentence;
		
		this.set_number(parts[0].trim());
		
		this._wordName = parts[1].trim();
		
		this.set_gPOS(parts[2]);
				
		this.set_syntaxTag(parts[3]);
		
		this.set_srcOfSynTag_number(parts[4]);
		
		this.set_wsd_name(parts[5]);
		
		this.set_super_wsd_name(parts[6]);
		
		this.set_sceneElement(parts[7]);
		
		this.set_semanticTags(parts, 8);
		
		this.set_referenceWord_number(parts, 8);		
		
//		this.set
	}

	/**
	 * @param senteceModel
	 * @param phrase
	 * @param number
	 * @param wordName
	 * @param lem
	 * @param gPOS
	 * @param props
	 * @param syntaxTag
	 * @param srcOfSynTag_number
	 * @param semanticTag
	 * @param wsd
	 * @param wsd_name
	 */
	
	public Word(SentenceModel senteceModel, int number, String wordName, POS gPOS, DependencyRelationType syntaxTag, int srcOfSynTag_number, Node wsd, String wsd_name, String super_wsd_name, ScenePart sceneElement) {
		super();
		this._sentence = senteceModel;
		this._number = number;
		this._wordName = wordName;
		this._gPOS = gPOS;
		this._syntaxTag = syntaxTag;
		this._srcOfSynTag_number = srcOfSynTag_number;
		this._wsd = wsd;
		this._wsd_name = wsd_name;
		this._super_wsd_name = super_wsd_name;
		this._sceneElement = sceneElement;
	}	
		
//-------------------- setter part --------------------------
 
	public void set_number(String number) {
		if(number == null || number.equals("") || number.equals("-") || number.equals("_"))
			this._number = -1;
		else
			this._number = Integer.parseInt(number);
	}
	
	public void set_gPOS(String gPos) {
		if(gPos != null && !gPos.equals("") && !gPos.equals("-") && !gPos.equals("_"))
			this._gPOS = POS.fromString(gPos);
		
		if(_gPOS == null)
			MyError.error("bad gPOS name " + gPos);	
	}

	public void set_syntaxTag(String synTag) {
		if(synTag != null && !synTag.equals("") && !synTag.equals("-") && !synTag.equals("_"))
			this._syntaxTag = DependencyRelationType.fromString(synTag);
		
		if(_syntaxTag == null)
			MyError.error("bad syntaxTag name " + synTag);	
	}	
	
	public void set_srcOfSynTag_number(String srcOfSynTagNum) {
		if(srcOfSynTagNum == null || srcOfSynTagNum.equals("") || srcOfSynTagNum.equals("-")  || srcOfSynTagNum.equals("_"))
			this._srcOfSynTag_number = -1;
		else
			this._srcOfSynTag_number = Integer.parseInt(srcOfSynTagNum);
	}
	
	public void set_wsd(Node wsd) {
		this._wsd = wsd;
	}
		
	
	public void set_wsd_name(String wsd_name) {
		if(wsd_name != null && !wsd_name.equals("") && !wsd_name.equals("-") && !wsd_name.equals("_"))
			this._wsd_name = wsd_name;
	}
	
	public void set_super_wsd_name(String super_wsd_name) {
		if(super_wsd_name != null && !super_wsd_name.equals("") && !super_wsd_name.equals("-"))
			this._super_wsd_name = super_wsd_name;
	}	
	
	public void set_sceneElement(String sceneElem) {
		
//!!!	if(_semanticTags == null)
//			_semanticTags = new ArrayList<SemanticTag>();
			
		if(sceneElem != null && !sceneElem.equals("") && !sceneElem.equals("-") && !sceneElem.equals("_"))
			this._sceneElement = ScenePart.fromString(sceneElem);
		
		if(_sceneElement == null)
			MyError.error("bad sceneElement name " + sceneElem);	
	}
	
	public void add_predictedSceneElement(String predictedSceneElem) {
		
		if(_predictedSceneElements == null)
			_predictedSceneElements = new ArrayList<ScenePart>();
		
		if(predictedSceneElem != null && !predictedSceneElem.equals("") && !predictedSceneElem.equals("-") && !predictedSceneElem.equals("_"))
			_predictedSceneElements.add(ScenePart.fromString(predictedSceneElem));
		else			
			MyError.error("bad sceneElement name " + predictedSceneElem + " is predicted!");
		
		checkIfItIsTruelyPredicted();		
	}
	
	public void set_isTruelyPredicted(boolean _isTruelyPredicted) {
		this._isTruelyPredicated = _isTruelyPredicted;
	}
		
	/**
	 * this methods sets the semanticTags of the current word and returns the start index of the non semanticTag element in the parts array.
	 * it escapes these String too: "Y" and "ديدن.47"
	 * 
	 * @param parts
	 * @param startIndex
	 * @return
	 */
	public void set_semanticTags(String[] parts, int startIndex) {
		
		if(parts == null || parts.length <= startIndex)
			return;
		
		for(int i = startIndex; i < parts.length; i++)
			if(parts[i] != null && !parts[i].equals("") && !parts[i].equals("-") && !parts[i].equals("_")){
				
				if(SemanticTag.isSemanticTag(parts[i])){
					
					SemanticTag nSemTag = SemanticTag.fromString(parts[i]);
					if(nSemTag != null)
						this._semanticTags.add(nSemTag);
				}
			}		
	}
	
	private void set_referenceWord_number(String[] parts, int startIndex) {
		
		if(parts == null || parts.length <= startIndex)
			return;
		
		for(int i = startIndex; i < parts.length; i++)
			
			if(parts[i] != null && !parts[i].equals("") && !parts[i].equals("-") && !parts[i].equals("_")){
				
				if(!SemanticTag.isSemanticTag(parts[i]) && !parts[i].equalsIgnoreCase("Y") && !parts[i].contains("."))
			
					this._referenceWordNum = parts[i];
			}		
	}
	
	public void set_referenceWord_number(String referenceWordnumber) {
		if(referenceWordnumber == null || referenceWordnumber.equals("") || referenceWordnumber.equals("-") || referenceWordnumber.equals("_"))
			this._referenceWordNum = null;
		else
			this._referenceWordNum = referenceWordnumber;
	}
	
	public void set_referenceWord(Word referenceWord){
		
		this._referenceWord = referenceWord;
		
		this.mergeWith(referenceWord, true);
		
	}
	
	//-------------------- is part --------------------------
	
		/**
		 * checks weather this Word object _syntaxTag is SBJ or not?
		 * @return
		 */
		public boolean isSubject(){
			if(_syntaxTag == DependencyRelationType.SBJ)
				return true;
			return false;		
		}
		
		/**
		 * checks weather this Word object _syntaxTag is OBJ or not?
		 * @return
		 */
		public boolean isObject(){
			if(_syntaxTag == DependencyRelationType.OBJ)
				return true;
			return false;		
		}
		
		/**
		 * checks weather this Word object _syntaxTag is MOS or not?
		 * @return
		 */
		public boolean isMosnad(){
			if(_syntaxTag == DependencyRelationType.MOS)
				return true;
			return false;		
		}
		
		/**
		 * checks weather this Word _syntaxTag is ROOT or its _srcOfSynTag_number is 0?
		 * @return
		 */
		public boolean isRootVerb(){
			if(_syntaxTag == DependencyRelationType.ROOT || _srcOfSynTag_number == 0)
				return true;
			return false;		
		}
		
		/**
		 * checks weather this Word object _syntaxTag is V; it means it is a verb or not?
		 * @return
		 */
		public boolean isVerb(){
			if(_syntaxTag == DependencyRelationType.ROOT || _srcOfSynTag_number == 0)
				return true;
			return false;		
		}
		
		/**
		 * checks weather this Word object _syntaxTag is ADVERB or not?
		 * ADVRB: to constraint verbs, nouns, adjectives or ...
		 * NADV: prepositional complements of nouns
		 * ADVC: adverbial complements of verbs
		 * AVCONJ: adverbs with equal positions
		 * @return
		 */
		public boolean isAdverb(){
			if(_syntaxTag == DependencyRelationType.ADVRB || _syntaxTag == DependencyRelationType.NADV ||
					_syntaxTag == DependencyRelationType.ADVC || _syntaxTag == DependencyRelationType.AVCONJ) // || maybe DependenctRelationType.VPP
				return true;
			return false;		
		}
		
		public boolean isAdjective(){
			if(_gPOS == POS.ADJ || _syntaxTag == DependencyRelationType.NPREMOD || _syntaxTag == DependencyRelationType.NPOSTMOD)
				return true;
			return false;
		}
		
		public boolean isMozaf_elaih(){
			if(_syntaxTag == DependencyRelationType.MOZ)
				return true;
			return false;
		}
	
	public boolean hasReferenceWordPath(){
		if(_referenceWordNum != null && !_referenceWordNum.equalsIgnoreCase(""))
			if(_referenceWordNum.matches(".*\\d+.*"))
				return true;
		return false;
	}
	
	public boolean hasMultiSemanticTag() {
		if(_semanticTags != null && _semanticTags.size() > 1)
			return true;
				
		return false;
	}
	
	public boolean hasSemaniticTag(SemanticTag semTag) {
		if(semTag == null)
			return false;
		
		if(_semanticTags == null || _semanticTags.size() == 0)
			return false;
		
		for(SemanticTag sem: _semanticTags)
			if(sem == semTag)
				return true;
		return false;
	}
	
	//-------------------- getter part --------------------------
	
	public Word getVerb() {
		return verb;
	}
	
	public Word getMosoof() {
		return mosoof;
	}
	
	public ArrayList<Word> getSubjects() {
		return subjects;
	}
	
	public Word getSubject(String sbj_node_name){
		if(!Common.isEmpty(subjects))
			for(Word sbj:subjects)
				if(sbj._wsd_name != null && sbj._wsd_name.equals(sbj_node_name))
					return sbj;
		return null;		
	}
	
	public ArrayList<Word> getAdjectives() {
		return adjectives;
	}
 	 
 	public Word getAdjective(String adj_node_name){
		if(!Common.isEmpty(adjectives))
			for(Word adj:adjectives)
				if(adj._wsd_name != null && adj._wsd_name.equals(adj_node_name))
					return adj;
		return null;		
	}
	
 	public Word getMozaf() {
 		return mozaf;
 	}
 	
	public ArrayList<Word> getMozaf_elaih() {
		return mozaf_elaih;
	}
	
	public Word getMozaf_elaih(String moz_node_name){
		if(!Common.isEmpty(mozaf_elaih))
			for(Word moz:mozaf_elaih)
				if(moz._wsd_name != null && moz._wsd_name.equals(moz_node_name))
					return moz;
		return null;		
	}
	
	public SemanticTag getFirstSemanticTag() {
		if(_semanticTags != null || _semanticTags.size() > 0)
			return _semanticTags.get(0);
				
		return null;
	}
	
	public String get_semanticTagsStr(){
		String semStr = "";
		if(_semanticTags != null)
			for(SemanticTag st: _semanticTags)
				semStr += st.toString() + "\t";		
		return semStr;
	}
	
	//-------------------- add part -----------------------------
	/**
 	 * 
 	 * @param sbj
 	 * @return an integer, 1 means adj added, 0 means the Word own adjective has merged with adj, and -1 means nothing happened! 
 	 */
 	public int addSubject(Word sbj){
 		if(sbj == null)
 			return -1;
 		
 		sbj.verb = this;
 		
 		if(subjects == null)
 			subjects = new ArrayList<Word>();
 		
 		if(!hasSubject(sbj._wsd_name)){
// 			print(sbj._wsd + " sbj added to " + this._wordName + "\n");
 			subjects.add(sbj);
 			return 1;
 		}
 		else{
// 			print(this._wordName + " has this " + sbj._wsd + " sbj before! so they will merge \n");
 			
 			Word oldSbj = getSubject(sbj._wsd_name);
 			
 			if(oldSbj != null){
 				oldSbj.mergeWith(sbj);			
 				return 0;
 			}
 			return -1;
 		}
 	}
	
	 	/**
	 	 * 
	 	 * @param adj
	 	 * @return an integer, 1 means adj added, 0 means the Word own adjective has merged with adj, and -1 means nothing happened! 
	 	 */
	 	public int addAdjective(Word adj){
	 		if(adj == null)
	 			return -1;
	 		
	 		adj.mosoof = this;
	 		
	 		if(adjectives == null)
	 			adjectives = new ArrayList<Word>();
	 		
	 		if(!hasAdjective(adj._wsd_name)){
//	 			print(adj._wsd + " adj added to " + this._wordName + "\n");
	 			adjectives.add(adj);
	 			return 1;
	 		}
	 		else{
//	 			print(this._wordName + " has this " + adj._wsd + " adj before! so they will merge \n");
	 			
	 			Word oldAdj = getAdjective(adj._wsd_name);
	 			
	 			if(oldAdj != null){
	 				oldAdj.mergeWith(adj);			
	 				return 0;
	 			}
	 			return -1;
	 		}
	 	}
	 	/**
	 	 * 
	 	 * @param moz
	 	 * @return  an integer, 1 means moz added, 0 means the Word own mozaf_elaih has merged with moz, and -1 means nothing happened!
	 	 */
	 	public int addMozaf_elaih(Word moz){
	 		if(moz == null)
	 			return -1;
	 		
	 		moz.mozaf = this;
	 		
	 		if(mozaf_elaih == null)
	 			mozaf_elaih = new ArrayList<Word>();
	 		
	 		if(!hasMozaf_elaih(moz._wsd_name)){
//	 			print(moz._wsd + " mozaf added to " + this._wordName + "\n");
	 			mozaf_elaih.add(moz);
	 			return 1;
	 		}
	 		else{
//	 			print(this._wordName + " has this " + moz._wsd + " mozaf before! so they will merge \n");
	 			
	 			Word oldMoz = getMozaf_elaih(moz._wsd_name);
	 			
	 			if(oldMoz != null){
	 				oldMoz.mergeWith(moz);			
	 				return 0;
	 			}					
	 			return -1;
	 		}
	 	}
	 	 
	 	//-------------------- has part -----------------------------
	 	
	 	public boolean hasAnySubjects(){
			return !Common.isEmpty(subjects);
		}
	 			
	 	public boolean hasSubject(String sbj_node_name){
			if(!Common.isEmpty(subjects))
				for(Word sbj:subjects)
					if(sbj._wsd_name != null && sbj._wsd_name.equals(sbj_node_name))
						return true;
			return false;		
		}
	 	
	 	public boolean hasAnyAdjectives(){
			return !Common.isEmpty(adjectives);
		}
	 	
		public boolean hasAdjective(String adj_node_name){
			if(!Common.isEmpty(adjectives))
				for(Word adj:adjectives)
					if(adj._wsd_name != null && adj._wsd_name.equals(adj_node_name))
						return true;
			return false;		
		}
		
		public boolean hasAnyMozaf_elaihs(){
			return !Common.isEmpty(mozaf_elaih);
		}
		
		public boolean hasMozaf_elaih(String moz_node_name){
			if(!Common.isEmpty(mozaf_elaih))
				for(Word moz:mozaf_elaih)
					if(moz._wsd_name != null && moz._wsd_name.equals(moz_node_name))
						return true;
			return false;		
		}
		
	 	//-------------------- end of part --------------------------
		
	
	public boolean get_isTruelyPredicted() {
		this._isTruelyPredicated = checkIfItIsTruelyPredicted();
		return this._isTruelyPredicated;
	}
	
	public boolean checkIfItIsTruelyPredicted() {
		if(_predictedSceneElements == null || _predictedSceneElements.size() == 0)
			return false;
		
		boolean flag = false;
		
		for(ScenePart sp:_predictedSceneElements)
			if(_sceneElement != sp) {
				this.set_isTruelyPredicted(false);
				flag = true;
				break;
			}
		if(!flag)
			this.set_isTruelyPredicted(true);
		
		return _isTruelyPredicated;
	}
	
	public String get_predictedSceneElementsStr(){
		String predStr = "";
		if(_predictedSceneElements != null &&  _predictedSceneElements.size() != 0)
			for(ScenePart scPrt: _predictedSceneElements)
				predStr += scPrt.toString() + "\t";		
		return predStr;
	}
	
	/**
	 * It returns one String with the dataset format for each SemanticTag
	 * dataset format:
	 * N ADVRB  ArgM_TMP دوره_زماني§n-12603 time
	 * _gPOS _syntaxTag _semanticTag _super_wsd_name sceneElement 
	 * @return
	 */
	public ArrayList<String> getWordDatasetStrs(){
		
		ArrayList<String> strs = new ArrayList<String>();
		
		if(_semanticTags == null || _semanticTags.size() == 0)
			strs.add(_gPOS + " " + _syntaxTag + " null " + _super_wsd_name + " " + _sceneElement.toString().toLowerCase());
					
		String str = "";
		for(SemanticTag semTag: _semanticTags){
			str += _gPOS + " " + _syntaxTag + " " + semTag + " " + _super_wsd_name + " " + _sceneElement.toString().toLowerCase();
			strs.add(str);
			str = "";
		}
		return strs;
	}
	

//	@Override
//	public String toString() {
//		return _wordName;
//	}
	
//	@Override
//	public String toString() {
 	public String getFullStr() {
 		String rs = "number=" + _number;		
		rs += " name=";
		if(_wordName != null) rs += "" + _wordName; 
		else rs += "-";
		rs += " POS=";
		if(_gPOS != null) rs += "" + _gPOS;
		else rs += "-";
		rs += " Syn=";
		if(_syntaxTag != null) rs += "" + _syntaxTag;
		else rs += "-";
		rs += " SynSrc=" + _srcOfSynTag_number;
		rs += " Wsd_name=";
		if(_wsd_name != null) rs += _wsd_name;
		else rs += "-";
		rs += " Super_wsd_name=";
		if(_super_wsd_name != null) rs += _super_wsd_name;
		else rs += "-";
		rs += " SceneElement=";
		if(_sceneElement != null) rs += _sceneElement;
		else rs += "-";
		rs += " SemanticTags=";
		if(_semanticTags != null) rs += get_semanticTagsStr();
		else rs += "-";
		rs += "\n";
		return rs;
	}	

 	@Override
 	public String toString() {
// 	public String getStr(){
 		String rs = "" + _number;		
		rs += "\t";
		if(_wordName != null) rs += "" + _wordName; 
		else rs += "%%%%";
		rs += "\t";
		if(_gPOS != null) rs += "" + _gPOS;
		else rs += "%%%%";
		rs += "\t";
		if(_syntaxTag != null) rs += "" + _syntaxTag;
		else rs += "%%%%";
		rs += "\t" + _srcOfSynTag_number + "\t";
		if(_wsd_name != null) rs += _wsd_name;
		else rs += "null";
		rs += "\t";
		if(_super_wsd_name != null) rs += _super_wsd_name;
		else rs += "null";
		rs += "\t";
		if(_sceneElement != null) rs += "" + _sceneElement.toString().toLowerCase();
		else rs += "%%%%";
		rs += "\t";
		if(_semanticTags != null) rs += get_semanticTagsStr();
		else rs += "%%%%";
		rs += "\t";
		if(_referenceWordNum != null) rs += _referenceWordNum;
//		rs += "\n";
		rs += "||| ";
		if(_predictedSceneElements != null && _predictedSceneElements.size() != 0)
			rs += get_predictedSceneElementsStr();
		else rs += "%%%%";
		return rs;
 	}
 	 
 	@SuppressWarnings("unused")
	private void print(String s){
		System.out.println(s);
	}
 	
 	/**
	 * this method merges called Word with the input Word, newWord.
	 * in merging the called Word is main, it means that only when a parameter in 
	 * called Word is null it is replaced with the newWord Word.
	 * newWord _semanticTags will not add to called Word _semantic_tags. 
	 * @param newWord the Word is to be merged with this Word. 
	 */
	public void mergeWith(Word newWord){
		mergeWith(newWord, true);
	}

 	/**
	 * this method merges called Word with the input Word, newWord.
	 * in merging the called Word is main, it means that only when a parameter in 
	 * called Word is null it is replaced with the newWord Word.
	 * only newWord _semanticTags are added to called Word _semantic_tags. 
	 * @param newWord the Word is to be merged with this Word.
	 * @param excludeSemanticTag if this parameter is true, the _semanticTags will not be merged.
	 */
	public void mergeWith(Word newWord, boolean excludeSemanticTag){
		
//		print("newWord to merge: " + newWord);
//		print("word Itself:      " + this);		
		
		if(newWord == null)
			return;
		
		if(_sentence == null)
			if(newWord._sentence != null)
				_sentence = newWord._sentence;
					
		if(_number < 0)
			if(newWord._number >= 0)
				_number = newWord._number;
		
		if(_wordName == null || _wordName.equals(""))
			if(newWord._wordName != null && !newWord._wordName.equals(""))
				_wordName = newWord._wordName;
						
		if(_gPOS == null || _gPOS == POS.UNKNOWN)
			if(newWord._gPOS == null && newWord._gPOS != POS.UNKNOWN)
				_gPOS = newWord._gPOS;
		
		if(_syntaxTag == null || _syntaxTag == DependencyRelationType.ANY)
			if(newWord._syntaxTag != null && newWord._syntaxTag != DependencyRelationType.ANY)
				_syntaxTag = newWord._syntaxTag;
		
		if(_srcOfSynTag_number < 0)
			if(newWord._srcOfSynTag_number >= 0)
				_srcOfSynTag_number = newWord._srcOfSynTag_number;
			
		if(_wsd == null)
			if(newWord._wsd != null)
				_wsd = newWord._wsd;
		
		if(_wsd_name == null || _wsd_name.equals("") || _wsd_name.equalsIgnoreCase("null"))
			if(newWord._wsd_name != null && !newWord._wsd_name.equals("") && !newWord._wsd_name.equalsIgnoreCase("null"))
				_wsd_name = newWord._wsd_name;
		
		if(_super_wsd_name == null || _super_wsd_name.equals("") || _super_wsd_name.equalsIgnoreCase("null"))
			if(newWord._super_wsd_name != null && !newWord._super_wsd_name.equals("") && !newWord._super_wsd_name.equalsIgnoreCase("null"))
				_super_wsd_name = newWord._super_wsd_name;

		if(_sceneElement == null || _sceneElement == ScenePart.JUNK || _sceneElement == ScenePart.NO)
			if(newWord._sceneElement  != null && newWord._sceneElement != ScenePart.JUNK && newWord._sceneElement != ScenePart.NO)
				_sceneElement = newWord._sceneElement;
			
		if(_semanticTags == null)
			_semanticTags = new ArrayList<SemanticTag>();

		if(!excludeSemanticTag)		
			if(newWord._semanticTags != null)
				for(SemanticTag nSemTag: newWord._semanticTags)
					this._semanticTags.add(nSemTag);				
			
		
//		print("word after merge: " + this + "\n");
	}		
	
	/** 
	 * this method checks the equality of two words based on the equality of their
	 * _sentence, _number, _wordName, _gPOS, _syntaxTag, _srcOfSynTag_number, _wsd,
	 * _wsd_name, _srcOfSynTag_number, _sceneElement and _semanticTags.
	 *  in other words _referenceWordNum, _referenceWordNum, _predictedSceneElements and _isTruelyPredicated
	 *  are not checked for equality.
	 * 
	 * @param word
	 * @return
	 */
	public boolean equals(Word word){
		if(word == null)
			return false;
		
//		if(this._sentece != word._sentece)
//			return false;
					
		if(this._number != word._number)
			return false;
		
		if(!this._wordName.equalsIgnoreCase(word._wordName))			
			return false;
						
		if(this._gPOS != word._gPOS)
			return false;
		
		if(this._syntaxTag != word._syntaxTag)
			return false;
		
		if(this._srcOfSynTag_number != word._srcOfSynTag_number)
			return false;
			
		if(this._wsd != word._wsd)
			return false;
		
		if(!this._wsd_name.equalsIgnoreCase(word._wsd_name))
			return false;
		
		if(!this._super_wsd_name.equalsIgnoreCase(word._super_wsd_name))
			return false;

		if(this._sceneElement != word._sceneElement)
			return false;
			
		if(this._semanticTags == null && (word._semanticTags != null || word._semanticTags.size() > 0))
			return false;
		
		for(SemanticTag nSemTag: word._semanticTags)
			if(!word.hasSemaniticTag(nSemTag))
				return false;
	
		return true;
	}	 	 
}
