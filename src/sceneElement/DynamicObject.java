package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import enums.ScenePart;
import model.SceneModel;
import model.Word;

public class DynamicObject extends SceneElement{
	
	private DynamicObjectState current_state = null;
	
	private ArrayList<DynamicObjectState> object_states = new ArrayList<DynamicObjectState>();
			
	private ArrayList<DynamicObjectAction> object_actions = new ArrayList<DynamicObjectAction>();
	
	protected boolean merged_in_child = false;		
	
	
	public DynamicObject(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT, word);
		this.object_states = new ArrayList<DynamicObjectState>();
		this.object_actions = new ArrayList<DynamicObjectAction>();
	}
	
	public DynamicObject(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT, node_name);
		this.object_states = new ArrayList<DynamicObjectState>();
		this.object_actions = new ArrayList<DynamicObjectAction>();
	}
	
	public DynamicObject(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT, node);
		this.object_states = new ArrayList<DynamicObjectState>();
		this.object_actions = new ArrayList<DynamicObjectAction>();
	}
	
	/**
	 * sets the current_state of this SceneObject and 
	 * adds this current_state to object_states via addObjectState. 
	 * 
	 * @param current_state
	 * @return
	 */
	public void setCurrent_state(DynamicObjectState current_state) {
		if(current_state == null)
			return;
		
		this.current_state = current_state;
		
		System.out.println("current DynamicObjectState of " + this._name + " set to " + current_state);
		
		addObject_state(current_state);

	}

	public DynamicObjectState getCurrent_state() {
		return current_state;
	}
	
	public DynamicObjectState getObject_state(Node state_node) {
		if(state_node == null)
			return null; 
		
		for(DynamicObjectState stat:this.object_states)
			if(stat._node.equalsRelaxed(state_node))
				return stat;
		
		MyError.error(this + " DynamicObject has no such a " + state_node + " DynamicObjectState.");
		return null;
	}
	
	
	public DynamicObjectState getObject_state(Word objectState_word){
		if(objectState_word == null)
			return null;
		
		if(!Common.isEmpty(object_states))
			for(DynamicObjectState objStat:object_states){
				if(objStat._mainWord != null && objStat._mainWord.equals(objectState_word))
					return objStat;
				
				if(objStat.get_otherWords() != null && objStat.get_otherWords().size() > 0)
					for(Word osWord: objStat.get_otherWords())
						if(osWord.equals(objectState_word))
							return objStat;
			}
		return null;
	}
	
	public ArrayList<DynamicObjectState> getObject_states() {
		return object_states;
	}
	
	public DynamicObjectAction getObject_action(Node action_node) {
		if(action_node == null)
			return null; 
		
		for(DynamicObjectAction act:this.object_actions)
			if(act._node.equalsRelaxed(action_node))
				return act;
		
		MyError.error(this + " DynamicObject has no such a " + action_node + " action.");
		return null;
	}
	
	public DynamicObjectAction getObject_action(Word objectAction_word) {

		if(objectAction_word == null)
			return null;
				
		if(!Common.isEmpty(object_actions))
			for(DynamicObjectAction oa:object_actions){
				
				if(oa._mainWord != null && oa._mainWord.equals(objectAction_word))
					return oa;
			
				if(oa.get_otherWords() != null && oa.get_otherWords().size() > 0)
					for(Word raWrd: oa.get_otherWords())
						if(raWrd.equals(objectAction_word))
							return oa;
			}
		return null;
	}

	
	public ArrayList<DynamicObjectAction> getObject_actions() {
		return object_actions;
	}
	
	/**
	 * adds state as ObjectState to this SceneObject or 
	 * merges it with the existing equivalent ObjectState of this SceneObject.
	 * 
	 * @param state
	 * @return
	 */
	public DynamicObjectState addObject_state(DynamicObjectState state){
		if(this.object_states == null)
			this.object_states = new ArrayList<DynamicObjectState>();
		
		if(state == null)
			return null;
		
		if(!hasObject_state(state)){
			this.object_states.add(state);
			System.out.println("DynamicObjectState " + state + " added to " + this._name);
			state.set_owningDynamicObject(this);
			return state;
		}
		else{
			DynamicObjectState exist = getObject_state(state._mainWord);
			if(exist != null){				
				System.out.println(this._name + " DynamicObject Merged this " + state + " DynamicObjectState with the the equal DynamicObjectState it had before!\n");
				exist.mergeWith(state);
				exist.set_owningDynamicObject(this);
			}
			else if(state.get_otherWords() != null){
				
				for(Word raWord: state.get_otherWords()){
					exist = getObject_state(raWord);
					if(exist != null){
						System.out.println(this._name + " DynamicObject Merged this " + state + " DynamicObjectState with the the equal DynamicObjectState it had before!\n");
						exist.mergeWith(state);
						exist.set_owningDynamicObject(this);
						break;
					}
				}
			}
			return exist;	
		}
		
	}

	/**
	 * adds action as ObjectAction to this DynamicObject or 
	 * merges it with the existing equivalent ObjectAction of this DyanamicObject.
	 * 
	 * @param action
	 * @return
	 */
	public DynamicObjectAction addObejct_action(DynamicObjectAction action) {
		if(action == null)
			return null;
		
		if(this.object_actions == null)
			this.object_actions = new ArrayList<DynamicObjectAction>();
			
		if(!hasObject_action(action)){
			this.object_actions.add(action);
			System.out.println("ObjectAction " + action + " added to " + this._name);
			action.set_owningDynamicObject(this);
			return action;
		}
		else{
			DynamicObjectAction exist = getObject_action(action._mainWord);
			if(exist != null){				
				System.out.println(this._name + " DynamicObject Merged this " + action + " ObjectAction with the the equal ObjectAction it had before!");
				exist.mergeWith(action);
				exist.set_owningDynamicObject(this);
			}
			else if(action.get_otherWords() != null){
				
				for(Word raWord: action.get_otherWords()){
					exist = getObject_action(raWord);
					if(exist != null){
						System.out.println(this._name + " DynamicObject Merged this " + action + " ObjectAction with the the equal ObjectAction it had before!\n");
						exist.mergeWith(action);
						exist.set_owningDynamicObject(this);
						break;
					}
				}
			}
			
			return exist;
		}
	
	}

	public boolean hasObject_state(DynamicObjectState objState){
		if(objState == null || object_states == null || object_states.size() == 0)
			return false;
		
		for(DynamicObjectState objectStat: this.object_states)
			if(objectStat.equals(objState))
				return true;
		return false;
	}

	public boolean hasObject_action(DynamicObjectAction action){
		if(action == null || object_actions == null || object_actions.size() == 0)
			return false;
	
		for(DynamicObjectAction act:object_actions)
			if(act.equals(action))
				return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "[" + _name + "=  " + _node_name + 
				" object_actions=  " + object_actions +
				" object_states=  " + object_states + "]";
	}

	
	/**
	 * 
	 * this method merges the DynamicObject which is called on with its parameter, dynObj.
	 * in merging the DynamicObject which is called on is prior to input parameter, dynObj.
	 * It means that only when a parameter in prior DynamicObject is null it is replaced with the dynObj parameter value.
	 * @param dynObj
	 */
	public void mergeDynamicObjectWith(DynamicObject dynObj) {
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(dynObj);
			merged_in_child = false;
		}
				
		if(dynObj == null)
			return;
		
		if(this.current_state == null && dynObj.current_state != null)
			this.current_state = dynObj.current_state;
		
		for(DynamicObjectState objState: dynObj.getObject_states())
			this.addObject_state(objState);
		
		for(DynamicObjectAction act:dynObj.getObject_actions())
			this.addObejct_action(act);			
	}
	
}
