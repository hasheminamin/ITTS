package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleAction extends SceneElement {
	
	public Role owningRole = null;
	
	private RoleEmotion emotion_in_action = null;
	
	private RoleState state_in_action = null;
	
	protected boolean merged_in_child = false;

	public RoleAction(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.ROLE_ACTION, word);		
	}
	
	public RoleAction(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.ROLE_ACTION, node_name);		
	}

	public RoleAction(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE_ACTION, node);		
	}
	
	public void set_owningRole(Role owningRole) {
		if(owningRole == null)
			return;
		this.owningRole = owningRole;
//		owningRole.addRole_action(this);
	}
	
	
	public void setEmotion_in_action(RoleEmotion emotion_in_action) {
		this.emotion_in_action = emotion_in_action;
	}
	
	public void setState_in_action(RoleState state_in_action) {
		this.state_in_action = state_in_action;
	}
	
	public RoleEmotion getEmotion_in_action() {
		return emotion_in_action;
	}

	public RoleState getState_in_action() {
		return state_in_action;
	}

	/**
	 * this method merges the RoleAction which is called on with its parameter, roleAction.
	 * in merging the RoleAction which is called on is prior to input parameter, roleAction.
	 * It means that only when a parameter in prior RoleAction is null it is replaced with the roleAction parameter value.
	 * @param roleAction
	 */
	public void mergeRoleActionWith(RoleAction roleAction) {
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(roleAction);
			merged_in_child = false;
		}
				
		if(roleAction == null)
			return;
		
		if(owningRole == null && roleAction.owningRole != null)
			owningRole = roleAction.owningRole;
		
//		if(actor == null && roleAction.actor != null)
//			actor = roleAction.actor;
		
		if(emotion_in_action == null && roleAction.emotion_in_action != null)
			emotion_in_action = roleAction.emotion_in_action;
		
		if(state_in_action == null && roleAction.state_in_action != null)
			state_in_action = roleAction.state_in_action;
	}
}
