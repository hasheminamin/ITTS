package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import model.SceneModel;
import model.Word;
import enums.ScenePart;

public class DynamicObjectState extends SceneElement {
	
	protected boolean merged_in_child = false;
	
	public DynamicObjectState(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT_STATE, word);
	}
	
	public DynamicObjectState(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT_STATE, node_name);
	}

	public DynamicObjectState(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT_STATE, node);
	}

	public void mergeDynamicObjectStateWith(DynamicObjectState dynObjState) {

		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(dynObjState);
			merged_in_child = false;
		}
		
		if(dynObjState == null)
			return;
	}	
}
