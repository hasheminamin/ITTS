package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import model.SceneModel;
import model.Word;
import enums.ScenePart;

public class StaticObjectState extends SceneElement {
	
	protected boolean merged_in_child = false;
	
	public StaticObjectState(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.STATIC_OBJECT_STATE, word);
	}
	
	public StaticObjectState(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.STATIC_OBJECT_STATE, node_name);
	}

	public StaticObjectState(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.STATIC_OBJECT_STATE, node);
	}

	public void mergeStaticObjectStateWith(StaticObjectState statObjState) {
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(statObjState);
			merged_in_child = false;
		}
				
		if(statObjState == null)
			return;		
	}

}
