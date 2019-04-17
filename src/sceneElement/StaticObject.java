package sceneElement;

import java.util.ArrayList;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

public class StaticObject extends SceneElement {
	
	private StaticObjectState current_state = null;
	
	private ArrayList<StaticObjectState> object_states = new ArrayList<StaticObjectState>();
	
	protected boolean merged_in_child = false;
	
	public StaticObject(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.STATIC_OBJECT, word);
		this.object_states = new ArrayList<StaticObjectState>();
	}

	public StaticObject(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.STATIC_OBJECT, node_name);
		this.object_states = new ArrayList<StaticObjectState>();
	}
		
	public StaticObject(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.STATIC_OBJECT, node);
		this.object_states = new ArrayList<StaticObjectState>();
	}

	/**
	 * sets the current_state of this SceneObject and 
	 * adds this current_state to object_states via addObjectState. 
	 * 
	 * @param current_state
	 * @return
	 */
	public void setCurrent_state(StaticObjectState current_state) {			
		if(current_state == null)
			return;
		
		this.current_state = current_state;
		
		System.out.println("current StaticObjectState of " + this._name + " set to " + current_state);
		
		addObject_state(current_state);				
		
	}

	public StaticObjectState getCurrent_state() {
		return current_state;
	}
	
	public StaticObjectState getObject_state(Node state_node) {
		if(state_node == null)
			return null; 
		
		for(StaticObjectState stat:this.object_states)
			if(stat._node.equalsRelaxed(state_node))
				return stat;
		
		MyError.error(this + " StaticObject has no such a " + state_node + " StaticObjectState.");
		return null;
	}
	
	public StaticObjectState getObject_state(Word objectState_word){
		if(objectState_word == null)
			return null;
		
		if(!Common.isEmpty(object_states))
			for(StaticObjectState objStat:object_states){
				if(objStat._mainWord != null && objStat._mainWord.equals(objectState_word))
					return objStat;
				
				if(objStat.getWords() != null && objStat.getWords().size() > 0)
					for(Word osWord: objStat.getWords())
						if(osWord.equals(objectState_word))
							return objStat;
			}
		return null;
	}
	
	public ArrayList<StaticObjectState> getObject_states() {
		return object_states;
	}
	
	/**
	 * adds state as ObjectState to this SceneObject or 
	 * merges it with the existing equivalent ObjectState of this SceneObject.
	 * 
	 * @param state
	 * @return
	 */
	public StaticObjectState addObject_state(StaticObjectState state){
		if(this.object_states == null)
			this.object_states = new ArrayList<StaticObjectState>();
		
		if(state == null)
			return null;
		
		if(!hasObject_state(state)){
			this.object_states.add(state);
			System.out.println("StaticObjectState " + state + " added to " + this._name);
			return state;
		}
		else{
			StaticObjectState exist = getObject_state(state._mainWord);
			if(exist != null){				
				System.out.println(this._name + " StaticObject Merged this " + state + " StaticObjectState with the the equal StaticObjectState it had before!\n");
				exist.mergeWith(state);
			}
			else if(state.getWords() != null){
				
				for(Word raWord: state.getWords()){
					exist = getObject_state(raWord);
					if(exist != null){
						System.out.println(this._name + " StaticObject Merged this " + state + " StaticObjectState with the the equal StaticObjectState it had before!\n");
						exist.mergeWith(state);
						break;
					}
				}
			}
			return exist;			
		}
	
	}

	public boolean hasObject_state(StaticObjectState objState){
		if(objState == null || object_states == null || object_states.size() == 0)
			return false;
		
		for(StaticObjectState objectStat: this.object_states)
			if(objectStat.equals(objState))
				return true;
		return false;
	}

	@Override
	public String toString() {
		return "[" + _name + "=  " + _node_name +				
				" object_states=  " + object_states + "]";
	}

	/**
	 * this method merges the StaticObject which is called on with its parameter, statObj.
	 * in merging the StaticObject which is called on is prior to input parameter, statObj.
	 * It means that only when a parameter in prior StaticObject is null it is replaced with the statObj parameter value.
	 * @param statObj
	 */
	public void mergeStaticObjectWith(StaticObject statObj) {
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(statObj);
			merged_in_child = false;
		}
		
		if(statObj == null)
			return;
		
		if(this.current_state == null && statObj.current_state != null)
			this.current_state = statObj.current_state;
		
		for(StaticObjectState stat:statObj.object_states)
			this.addObject_state(stat);		
	}
	
}
