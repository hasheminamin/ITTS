package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleState extends SceneElement {
	
	protected boolean merged_in_child = false;
	
	public RoleState(SceneModel scene, String name, Word word){
		super(scene, name, ScenePart.ROLE_STATE, word);			
	}
	
	public RoleState(SceneModel scene, String name, String node_name){
		super(scene, name, ScenePart.ROLE_STATE, node_name);		
	}
	
	public RoleState(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE_STATE, node);
	}

	public void mergeRoleStateWith(RoleState roleState) {
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(roleState);
			merged_in_child = false;
		}
				
		if(roleState == null)
			return;
		
	}
}
