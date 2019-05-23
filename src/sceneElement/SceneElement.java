package sceneElement;

import java.util.ArrayList;

import enums.POS;
import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

@SuppressWarnings("unused")
public class SceneElement {
	
	public SceneModel _scene = null;
	
	public String _name = "";
	
	public ScenePart scenePart;
	
	/**
	 * this is the main word of this SceneElement.
	 */
	public Word _mainWord = null;
	
	/**
	 * if this SceneElement has more that one word, the extra words are placed in _extraWords array. 
	 */
	private ArrayList<Word> _otherWords;
	
	public Node _node = null;	
	
	public String _node_name = "";
	
	protected boolean merged_in_father = false;
	
	
	public SceneElement(SceneModel scene){
		super();
		this._scene = scene;
	}
	
	public SceneElement(SceneModel scene, String name, ScenePart scenePart, Word word){
		super();
		this._scene = scene;
		this._name = name;
		this.scenePart = scenePart;
		this._mainWord = word;
		if(word != null)
			this._node_name = word._wsd_name; 
	}

	
	public SceneElement(SceneModel scene, String name, ScenePart scenePart, String node_name){
		super();
		this._scene = scene;
		this._name = name;
		this.scenePart = scenePart;
		this._node_name = node_name;		
	}
	
	public SceneElement(SceneModel scene, String name, ScenePart scenePart, Node node) {
		super();
		this._scene = scene;
		this._name = name;
		this.scenePart = scenePart;
		this._node = node;		
	}
		

	public void setName(String _name) {
		this._name = _name;	
	}
	
	public void setNode(Node _node) {
		this._node = _node;
	}
	
	public void setWords(ArrayList<Word> words){
		if(words == null || words.size() == 0)
			return;
		
		int index = 0;
		if(this._mainWord == null){
			MyError.error(this + " SceneElement has no _mainWord, so the first word in thar array is set as its _mainWord");
			this._mainWord = words.get(0);
			index = 1;
		}
		if(this._otherWords == null)
			this._otherWords = new ArrayList<Word>();
		
		for(;index < words.size(); index++)
			this._otherWords.add(words.get(index));			
	}
	public void set_otherWords(ArrayList<Word> otherWords) {
		this._otherWords = otherWords;
	}
	
	public void addWord(Word word){
		if(word == null)
			return;
		
		if(this._mainWord == null)
			this._mainWord = word;
		else{
			if(this._otherWords == null)
				this._otherWords = new ArrayList<Word>();
		
			this._otherWords.add(word);
		}
	}
	
	public void addWord(ArrayList<Word> words) {
		if(words == null)
			return;
		
		for(Word wrd:words)		
			this.addWord(wrd);		
	}
	
	public String getName() {
		return _name;
	}
	
	public Node getNode() {
		return _node;
	}
	
	public ArrayList<Word> get_otherWords(){
		return _otherWords;
	}
	
	public ArrayList<Word> getAllWords(){
		ArrayList<Word> allWords = new ArrayList<Word>();
		
		if(_mainWord != null)
			allWords.add(_mainWord);
		if(_otherWords != null)
			allWords.addAll(_otherWords);
		
		return allWords;
	}
	
	public int getWordNumbers() {
		int num = 0;
		if(_mainWord != null)
			num++;
		if(_otherWords != null)
			num += _otherWords.size();
		return num;
	}
	
	public boolean hasMoreThanOneWord(){
		if(_otherWords != null && _otherWords.size() > 0)
			return true;
		return false;
	}
	
//	/**
//	 * creates a RoleAction and calls addRole_action for this Role. 
//	 *  
//	 * @param name
//	 * @param node
//	 * @return
//	 */
//	
//	private RoleAction addRoleActionToRole(String name, Node node){
//		
//		try{
//			Role role = (Role)this;
//			RoleAction roleAct = new RoleAction(this._scene, name, node); 
//			return role.addRole_action(roleAct);			
//		}
//		catch(Exception e){
//			print("" + e);
//			return null;
//		}
//	}
//	
//	/**
//	 * creates a ObjectAction and calls addObject_action for this ObjectAction.
//	 * 
//	 * @param name
//	 * @param node
//	 */
//	
//	private ObjectAction addObjectActionToDynmicAction(String name, Node node){		
//		try{
//			DynamicObject dynObj = (DynamicObject)this;
//			ObjectAction objAct = new ObjectAction(this._scene, name, node); 
//			return dynObj.addObejct_action(objAct);
//		}
//		catch(Exception e){
//			print("" + e);
//			return null;
//		}
//	}
//	
//	/**
//	 * creates a RoleMood and calls addRole_mood for this Role.
//	 * 
//	 * @param name
//	 * @param node
//	 * @return
//	 */
//	private RoleState addRoleMoodToRole(String name, Node node){		
//		try{
//			Role role = (Role)this;
//			RoleState rm = new RoleState(this._scene, name, node);
//			return role.addRole_mood(rm);
//		}
//		catch(Exception e){			
//			print("" + e);
//			return null;
//		}
//	}
//	
//	/**
//	 * creates a RoleMood and calls addRole_mood for this Role.
//	 *TODO: check why private or public ?!
//	 * @param name
//	 * @param node
//	 * @return
//	 */
//	private RoleEmotion addRoleEmotionToRole(String name, Node node){		
//		try{
//			Role role = (Role)this;
//			RoleEmotion rEmo = new RoleEmotion(this._scene, name, node);
//			return role.addRole_emotion(rEmo);
//		}
//		catch(Exception e){			
//			print("" + e);
//			return null;
//		}
//	}
//	
//	/**
//	 * creates a SceneObject and calls setCurrent_state for this SceneObject. 
//	 * 
//	 * @param name
//	 * @param node
//	 * @return
//	 */
//	private ObjectState addStateToSceneObject(String name, Node node) {
//		try{
//			SceneObject sceObj = (SceneObject)this;			
//			ObjectState objState = new ObjectState(this._scene, name, node);	
//			return sceObj.setCurrent_state(objState);				
//		}
//		catch(Exception e){			
//			print("" + e);
//			return null;
//		}	
//	}

	@Override
	public String toString() {
		return  _node_name + "=  " + _name;
	}

	public ArrayList<String> getElementDatasetStrs(){
	
		ArrayList<String> elementStrs = new ArrayList<String>();
	
		if(_otherWords != null)
			for(Word wrd:_otherWords)
				if(wrd.getWordDatasetStrs() != null)
					elementStrs.addAll(wrd.getWordDatasetStrs());
	
			
		return elementStrs;
		
	}
	
	/**
	 * this method assessed the equality of two sceneElement based on the equality of
	 * their _name and their _node or _node_name.
	 * if the _main_word of this SceneELement is PR (a pronoun) the equality of their
	 * _node_name means they are equal.
	 * @param sceneElement
	 * @return
	 */	
	public boolean equals(SceneElement sceneElement) {
		if(sceneElement == null)
			return false;
					
		if(this._name != null && !this._name.equalsIgnoreCase(sceneElement._name))
			return false;
		else{			
			if(this._node != null) {
				if(!this._node.equalsRelaxed(sceneElement._node))
					return false;
			}
			else//if(this._node == null) 
				if(sceneElement._node != null)
					return false;
			
			if(this._node_name != null) {
				if(!this._node_name.equalsIgnoreCase(sceneElement._node_name))
					return false;
			}
			else //this._node_name == null
				if(sceneElement._node_name != null)
					return false;
		}
		
		if((this._mainWord != null && this._mainWord._gPOS == POS.PR) || (sceneElement._mainWord != null && sceneElement._mainWord._gPOS == POS.PR))
			if(this._node_name != null) {
				if(!this._node_name.equalsIgnoreCase(sceneElement._node_name))
					return false;
			}
			else //this._node_name == null
				if(sceneElement._node_name != null)
					return false;
		
		return true;
	}
	
		
	/**
	 * this method merges the SceneElement which is called on with its parameter, element.
	 * in merging the SceneElement which is called on is prior to input parameter, element.
	 * It means that only when a parameter in prior SceneElement is null it is replaced with the element parameter value.
	 * @param element
	 */
	public void mergeWith(SceneElement element){
		
		merged_in_father = true;
			
		if(element == null){
			MyError.error("can not merge null with " + this + " !");
			return;
		}
		
		if(this._scene == null)
			if(element._scene != null)
				this._scene = element._scene;		
		
		if(this._name == null || this._name.equals(""))
			if(element._name != null && !element._name.equals(""))
				this._name = element._name;
				
		if(this._mainWord == null && element._mainWord != null)
			this._mainWord = element._mainWord;
				
		if(this._otherWords == null)
			this._otherWords = new ArrayList<Word>();
		
		if(element._mainWord != null)
			this._otherWords.add(element._mainWord);
		
		if(element._otherWords != null && element._otherWords.size() > 0)
			this._otherWords.addAll(element._otherWords);		
		
		if(this._node == null && element._node != null)
			this._node = element._node;
		
		if(this._node_name == null || this._node_name.equals(""))
			if(element._node_name != null && !element._node_name.equals(""))
				this._node_name = element._node_name;
						
		if(this.scenePart == ScenePart.ROLE && element.scenePart == ScenePart.ROLE){
			Role role = (Role) this;
			if(!role.merged_in_child)
				role.mergeRoleWith((Role)element);	
		}
		else if(this.scenePart == ScenePart.DYNAMIC_OBJECT && element.scenePart == ScenePart.DYNAMIC_OBJECT){			
			DynamicObject dynObj = (DynamicObject) this;
			if(!dynObj.merged_in_child)
				dynObj.mergeDynamicObjectWith((DynamicObject)element);			
		}
		else if(this.scenePart == ScenePart.STATIC_OBJECT && element.scenePart == ScenePart.STATIC_OBJECT){
			StaticObject staObj = (StaticObject)this;
			if(!staObj.merged_in_child)
				staObj.mergeStaticObjectWith((StaticObject)element);				
		}
		
		
		
		
		else if(this.scenePart == ScenePart.ROLE_ACTION && element.scenePart == ScenePart.ROLE_ACTION){
			RoleAction roleAction = (RoleAction) this;
			if(!roleAction.merged_in_child)
				roleAction.mergeRoleActionWith((RoleAction)element);	
		}
		else if(this.scenePart == ScenePart.ROLE_STATE && element.scenePart == ScenePart.ROLE_STATE){
			RoleState roleState = (RoleState) this;
			if(!roleState.merged_in_child)
				roleState.mergeRoleStateWith((RoleState)element);	
		}
		else if(this.scenePart == ScenePart.ROLE_EMOTION && element.scenePart == ScenePart.ROLE_EMOTION){
			RoleEmotion roleEmotion = (RoleEmotion) this;
			if(!roleEmotion.merged_in_child)
				roleEmotion.mergeRoleEmotionWith((RoleEmotion)element);	
		}
		else if(this.scenePart == ScenePart.ROLE_INTENT && element.scenePart == ScenePart.ROLE_INTENT){
			RoleIntent roleIntent = (RoleIntent) this;
			if(!roleIntent.merged_in_child)
				roleIntent.mergeRoleIntentWith((RoleIntent)element);	
		}
		else if(this.scenePart == ScenePart.STATIC_OBJECT_STATE && element.scenePart == ScenePart.STATIC_OBJECT_STATE){
			StaticObjectState statObjState = (StaticObjectState) this;
			if(!statObjState.merged_in_child)
				statObjState.mergeStaticObjectStateWith((StaticObjectState)element);	
		}
		else if(this.scenePart == ScenePart.DYNAMIC_OBJECT_STATE && element.scenePart == ScenePart.DYNAMIC_OBJECT_STATE){
			DynamicObjectState dynObjState = (DynamicObjectState) this;
			if(!dynObjState.merged_in_child)
				dynObjState.mergeDynamicObjectStateWith((DynamicObjectState)element);	
		}		
		else if(this.scenePart == ScenePart.DYNAMIC_OBJECT_ACTION && element.scenePart == ScenePart.DYNAMIC_OBJECT_ACTION){
			DynamicObjectAction dynObjAct = (DynamicObjectAction) this;
			if(!dynObjAct.merged_in_child)
				dynObjAct.mergeDynamicObjectActionWith((DynamicObjectAction)element);	
		}
		
		else if(this.scenePart == ScenePart.SCENE_EMOTION && element.scenePart == ScenePart.SCENE_EMOTION){
			SceneEmotion sceneEmo = (SceneEmotion) this;
			if(!sceneEmo.merged_in_child)
				sceneEmo.mergeSceneEmotionWith((SceneEmotion)element);	
		}
		else if(this.scenePart == ScenePart.SCENE_GOAL && element.scenePart == ScenePart.SCENE_GOAL){
			SceneGoal dynObjState = (SceneGoal) this;
			if(!dynObjState.merged_in_child)
				dynObjState.mergeSceneGoalWith((SceneGoal)element);	
		}		
		else if(this.scenePart == ScenePart.LOCATION && element.scenePart == ScenePart.LOCATION){
			Location location = (Location) this;
			if(!location.merged_in_child)
				location.mergeLocationWith((Location)location);			
		}
		else if(this.scenePart == ScenePart.TIME && element.scenePart == ScenePart.TIME){
			Time time = (Time)this;
			if(!time.merged_in_child)
				time.mergeTimeWith((Time)element);
		}
		merged_in_father = false;
	}
	
	private void print(String toPrint){
		System.out.println(toPrint);
		
	}

}
