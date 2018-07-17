package model;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;






import java.util.ArrayList;

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
	public Sentence _sentece;
	
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
	 * this Word Semantic-Role-Label tags.
	 */
	public ArrayList<SemanticTag> _semanticTags = new ArrayList<SemanticTag>();

	/**
	 * This constructor gets an input string in the format 
	 * "4	برادر	N	OBJ	5	برادر§n-14090	نفر§n-13075	role	_	_	Arg1	_"
	 * these parts are respectively:
	 * 
	 * number	name	gPOS	syntaxTag	_srcOfSynTag_number	wsd_name, super_wsd_name, sceneElement, ... semanticTags ...
	 * @param wStr the String definition of this word as in Corpora text.
	 */
	public Word(String wStr, Sentence sentence) {
//		print(wStr);
				
		String[] parts = wStr.split("(\t)+");
		
		if(parts.length < 8){			
			MyError.error("Bad sentence information format " + wStr + " parts-num " + parts.length);
			return;
		}
		this._sentece = sentence;
		
		this.set_number(parts[0].trim());
		
		this._wordName = parts[1].trim();
		
		this.set_gPOS(parts[2]);
				
		this.set_syntaxTag(parts[3]);
		
		this.set_srcOfSynTag_number(parts[4]);
		
		this.set_wsd_name(parts[5]);
		
		this.set_super_wsd_name(parts[6]);
		
		this.set_sceneElement(parts[7]);
		
		this.set_semanticTags(parts, 8);		
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
	
	public Word(Sentence senteceModel, int number, String wordName, POS gPOS, DependencyRelationType syntaxTag, int srcOfSynTag_number, Node wsd, String wsd_name, String super_wsd_name, ScenePart sceneElement) {
		super();
		this._sentece = senteceModel;
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
		
		if(_semanticTags == null)
			_semanticTags = new ArrayList<SemanticTag>();
			
		if(sceneElem != null && !sceneElem.equals("") && !sceneElem.equals("-") && !sceneElem.equals("_"))
			this._sceneElement = ScenePart.fromString(sceneElem);
		
		if(_sceneElement == null)
			MyError.error("bad sceneElement name " + sceneElem);	
	}
	
	public void set_semanticTags(String[] parts, int startIndex) {
		
		if(parts == null || parts.length <= startIndex)
			return;
		for(int i = startIndex; i < parts.length; i++)
			if(parts[i] != null && !parts[i].equals("") && !parts[i].equals("-") && !parts[i].equals("_")){
				SemanticTag nSemTag = SemanticTag.fromString(parts[i]);
				if(nSemTag != null)
					this._semanticTags.add(nSemTag);
			}
	}
	
	public String get_semanticTagsStr(){
		String semStr = "";
		if(_semanticTags != null)
			for(SemanticTag st: _semanticTags)
				semStr += st.toString() + " ";		
		return semStr;
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
//		rs += "\n";
		return rs;
 	}
 	 
 	@SuppressWarnings("unused")
	private void print(String s){
		System.out.println(s);
	}
 	 
}
