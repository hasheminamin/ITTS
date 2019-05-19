package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import model.SceneModel;
import model.Word;
import enums.ScenePart;

public class StaticObjectState extends SceneElement {
	
	private StaticObject owningStaticObject = null;
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
	
	public StaticObject get_owningStaticObject() {
		return owningStaticObject;
	}

	public void set_owningStaticObject(StaticObject actor) {
		this.owningStaticObject = actor;
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
