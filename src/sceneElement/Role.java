/**
 * 
 */
package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;

import java.util.ArrayList;

import enums.ScenePart;
import model.SceneModel;
import model.Word;


/**
 * @author hashemi
 *
 */
public class Role extends SceneElement{	
		
	private ArrayList<RoleAction> role_actions = new ArrayList<RoleAction>();
	
	private ArrayList<RoleIntent> role_intents = new ArrayList<RoleIntent>();

	private ArrayList<RoleEmotion> role_emotions = new ArrayList<RoleEmotion>();
	
	private ArrayList<RoleState> role_states = new ArrayList<RoleState>();
	
	protected boolean merged_in_child = false;
	
	public Role(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.ROLE, word);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_intents = new ArrayList<RoleIntent>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
	public Role(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.ROLE, node_name);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_intents = new ArrayList<RoleIntent>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
	public Role(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE, node);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_intents = new ArrayList<RoleIntent>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
	public RoleAction getRole_action(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_actions))
			for(RoleAction ra:role_actions)
				if(ra._node.equalsRelaxed(node))
					return ra;
		return null;
	}
	
	public RoleAction getRole_action(Word roleAction_word) {

		if(roleAction_word == null)
			return null;
				
		if(!Common.isEmpty(role_actions))
			for(RoleAction ra:role_actions){
				
				if(ra._mainWord != null && ra._mainWord.equals(roleAction_word))
					return ra;
			
				if(ra.getWords() != null && ra.getWords().size() > 0)
					for(Word raWrd: ra.getWords())
						if(raWrd.equals(roleAction_word))
							return ra;
			}
		return null;
	}
	
	public ArrayList<RoleAction> getRole_actions() {
		return role_actions;
	}
	
	public RoleEmotion getRole_emotion(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_emotions))
			for(RoleEmotion re:role_emotions)
				if(re._node.equalsRelaxed(node))
					return re;
		return null;
	}
	
	public RoleEmotion getRole_emotion(Word roleEmotion_word) {

		if(roleEmotion_word == null)
			return null;
				
		if(!Common.isEmpty(role_emotions))
			for(RoleEmotion re:role_emotions){
				
				if(re._mainWord != null && re._mainWord.equals(roleEmotion_word))
					return re;
			
				if(re.getWords() != null && re.getWords().size() > 0)
					for(Word reWrd: re.getWords())
						if(reWrd.equals(roleEmotion_word))
					return re;
			}
		return null;
	}
	
	public ArrayList<RoleEmotion> getRole_emotions() {
		return role_emotions;
	}

	public RoleIntent getRole_intent(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_intents))
			for(RoleIntent rg:role_intents)
				if(rg._node.equalsRelaxed(node))
					return rg;
		return null;
	}
	
	public RoleIntent getRole_intent(Word roleIntent_word) {

		if(roleIntent_word == null)
			return null;
				
		if(!Common.isEmpty(role_intents))
			for(RoleIntent ri:role_intents){
				
				if(ri._mainWord != null && ri._mainWord.equals(roleIntent_word))
					return ri;
			
				if(ri.getWords() != null && ri.getWords().size() > 0)
					for(Word raWrd: ri.getWords())
						if(raWrd.equals(roleIntent_word))
					return ri;
			}
		return null;
	}
	
	public ArrayList<RoleIntent> getRole_intents() {		
		return role_intents;
	}
	
	public RoleState getRole_state(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_states))
			for(RoleState rm:role_states)
				if(rm._node.equalsRelaxed(node))
					return rm;
		return null;
	}

	public RoleState getRole_state(Word roleState_word) {

		if(roleState_word == null)
			return null;
				
		if(!Common.isEmpty(role_states))
			for(RoleState rs:role_states){
				
				if(rs._mainWord != null && rs._mainWord.equals(roleState_word))
					return rs;
			
				if(rs.getWords() != null && rs.getWords().size() > 0)
					for(Word raWrd: rs.getWords())
						if(raWrd.equals(roleState_word))
					return rs;
			}
		return null;
	}
	
	public ArrayList<RoleState> getRole_states(){
		return this.role_states;
	}
		
	public boolean hasRole_action(RoleAction role_action){
		if(this.role_actions != null)
			for(RoleAction ra:role_actions)
				if(ra.equals(role_action))
					return true;
		return false;
	}
	
	public boolean hasRole_emotion(RoleEmotion role_emotion){
		if(this.role_emotions != null)
			for(RoleEmotion re:role_emotions)
				if(re.equals(role_emotion))
					return true;
		return false;
	}
	
	public boolean hasRole_intent(RoleIntent role_intent){
		if(this.role_intents != null)
			for(RoleIntent rg:role_intents)
				if(rg.equals(role_intent))
					return true;
		return false;
	}	
	
	public boolean hasRole_state(RoleState role_state){
		if(this.role_states != null)
			for(RoleState rm:role_states)
				if(rm.equals(role_state))
					return true;
		return false;
	}
	
	/**
	 * adds role_action as RoleActione to this Role or 
	 * merges it with the existing equivalent RoleAction of this Role.
	 * 
	 * @param role_action
	 * @return
	 */
	public RoleAction addRole_action(RoleAction role_action) {
		if(role_action == null)
			return null;
		
		if(this.role_actions == null)
			this.role_actions = new ArrayList<RoleAction>();
	
		if(!hasRole_action(role_action)){
			this.role_actions.add(role_action);
			print("RoleAction " + role_action + " added to " + this._name);
			return role_action;
		}
		else{			
			RoleAction exist = getRole_action(role_action._mainWord);
			if(exist != null){				
				print(this._name + " role Merged this " + role_action + " RoleAction with the the equal roleAction it had before!\n");
				exist.mergeWith(role_action);
			}
			else if(role_action.getWords() != null){
				
				for(Word raWord: role_action.getWords()){
					exist = getRole_action(raWord);
					if(exist != null){
						print(this._name + " role Merged this " + role_action + " RoleAction with the the equal roleAction it had before!\n");
						exist.mergeWith(role_action);
						break;
					}
				}
			}
			return exist;
		}		
	}

	
	/**
	 * adds role_emotion as RoleEmotion to this Role or 
	 * merges it with the existing equivalent RoleEmotion of this Role.
	 * 
	 * @param role_emotion
	 * @return
	 */
	public RoleEmotion addRole_emotion(RoleEmotion role_emotion) {
		if(role_emotion == null)
			return null;
		
		if(this.role_emotions == null)
			this.role_emotions = new ArrayList<RoleEmotion>();
	
		if(!hasRole_emotion(role_emotion)){
			this.role_emotions.add(role_emotion);
			print("RoleEmotion " + role_emotion + " added to " + this._name);
			return role_emotion;
		}
		else{
			RoleEmotion exist = getRole_emotion(role_emotion._mainWord);
			if(exist != null){				
				print(this._name + " role Merged this " + role_emotion + " RoleEmotion with the the equal roleAEmotion it had before!\n");
				exist.mergeWith(role_emotion);
			}
			else if(role_emotion.getWords() != null){
				
				for(Word reWord: role_emotion.getWords()){
					exist = getRole_emotion(reWord);
					if(exist != null){
						print(this._name + " role Merged this " + role_emotion + " RoleEmotion with the the equal roleEmotion it had before!\n");
						exist.mergeWith(role_emotion);
						break;
					}
				}
			}
			return exist;			
		}	
	}
	
	/**
	 * adds role_intent as RoleIntent to this Role or 
	 * merges it with the existing equivalent RoleIntent of this Role.
	 * 
	 * @param role_intent
	 * @return
	 */
	public RoleIntent addRole_intent(RoleIntent role_intent) {
		if(role_intent == null)
			return null;
		
		if(this.role_intents == null)
			this.role_intents = new ArrayList<RoleIntent>();
		
		if(!hasRole_intent(role_intent)){
			this.role_intents.add(role_intent);
			System.out.println("RoleIntent " + role_intent + " added to " + this._name);
			return role_intent;
		}
		else{
			RoleIntent exist = getRole_intent(role_intent._mainWord);
			if(exist != null){				
				print(this._name + " role Merged this " + role_intent + " RoleIntent with the the equal roleIntent it had before!\n");
				exist.mergeWith(role_intent);
			}
			else if(role_intent.getWords() != null){
				
				for(Word riWord: role_intent.getWords()){
					exist = getRole_intent(riWord);
					if(exist != null){
						print(this._name + " role Merged this " + role_intent + " RoleIntent with the the equal roleIntent it had before!\n");
						exist.mergeWith(role_intent);
						break;
					}
				}
			}
			return exist;
		}		
	}

	/**
	 * adds role_state as RoleState to this Role or 
	 * merges it with the existing equivalent RoleState of this Role.
	 * 
	 * @param role_state to be added to this Role.
	 * @return the added RoleState.
	 */
	public RoleState addRole_State(RoleState role_state){
		if(role_state == null)
			return null;
		
		if(this.role_states == null)
			this.role_states = new ArrayList<RoleState>();
			
		if(!hasRole_state(role_state)){
			this.role_states.add(role_state);
			System.out.println("RoleState " + role_state + " added to " + this._name);
			return role_state;
		}
		else{
			RoleState exist = getRole_state(role_state._mainWord);
			if(exist != null){				
				print(this._name + " role Merged this " + role_state + " RoleState with the the equal roleState it had before!\n");
				exist.mergeWith(role_state);
			}
			else if(role_state.getWords() != null){
				
				for(Word rsWord: role_state.getWords()){
					exist = getRole_state(rsWord);
					if(exist != null){
						print(this._name + " role Merged this " + role_state + " RoleState with the the equal roleState it had before!\n");
						exist.mergeWith(role_state);
						break;
					}
				}
			}
			return exist;
		}
				
	}
	
	

	@Override
	public String toString() {
		return  "[" + _node_name + "= " + _name + 
				" role_actions=  " + role_actions + 
				" role_intents=    " + role_intents + 
				" role_emotions= " + role_emotions + 
				" role_state=    " + role_states + "]";
	}
	
	/**
	 * this method merges the Role which is called on with its parameter, role.
	 * in merging the Role which is called on is prior to input parameter, role.
	 * It means that only when a parameter in prior Role is null it is replaced with the role parameter value.
	 * @param role
	 */
	public void mergeRoleWith(Role role){
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(role);
			merged_in_child = false;
		}
			
		
		if(role == null)
			return;
	
		for(RoleAction ra:role.getRole_actions())
			this.addRole_action(ra);
				
		for(RoleIntent rg:role.getRole_intents())
			this.addRole_intent(rg);

		for(RoleEmotion re:role.getRole_emotions())
			this.addRole_emotion(re);	
		
		for(RoleState rm:role.getRole_states())
			this.addRole_State(rm);
	}
	
	private void print(String str){
		System.out.println(str);
	}

}
