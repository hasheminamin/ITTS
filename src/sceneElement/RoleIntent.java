package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class RoleIntent extends SceneElement {
	
	protected boolean merged_in_child = false;

	public Role owningRole = null;
	
	public RoleIntent(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.ROLE_INTENT, word);

	}
	
	public RoleIntent(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.ROLE_INTENT, node_name);

	}

	public RoleIntent(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE_INTENT, node);

	}

	public void mergeRoleIntentWith(RoleIntent roleIntent) {
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(roleIntent);
			merged_in_child = false;
		}
				
		if(roleIntent == null)
			return;		
		
		if(owningRole == null && roleIntent.owningRole != null)
			owningRole = roleIntent.owningRole;
		
	}
		
	public void set_owningRole(Role owningRole) {
		if(owningRole == null)
			return;
		this.owningRole = owningRole;
//		owningRole.addRole_intent(this);
	}

}
