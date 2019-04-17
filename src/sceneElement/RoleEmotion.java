package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleEmotion extends SceneElement {

	protected boolean merged_in_child = false;

	public RoleEmotion(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.ROLE_EMOTION, word);

	}
	
	public RoleEmotion(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.ROLE_EMOTION, node_name);

	}
	
	public RoleEmotion(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE_EMOTION, node);

	}

	public void mergeRoleEmotionWith(RoleEmotion roleEmotion) {
		
		if(merged_in_father == false){
			merged_in_child  = true;
			super.mergeWith(roleEmotion);
			merged_in_child = false;
		}
				
		if(roleEmotion == null)
			return;		
	}

}
