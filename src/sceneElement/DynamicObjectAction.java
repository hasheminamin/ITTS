package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class DynamicObjectAction extends SceneElement {

	private DynamicObject owningDynamicObject = null;
	
	protected boolean merged_in_child = false;
	
	public DynamicObjectAction(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT_ACTION, word);		
	}
		
	public DynamicObjectAction(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT_ACTION, node_name);		
	}
	
	public DynamicObjectAction(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.DYNAMIC_OBJECT_ACTION, node);		
	}

	public DynamicObject get_owningDynamicObject() {
		return owningDynamicObject;
	}

	public void set_owningDynamicObject(DynamicObject owningDynamicObject) {
		this.owningDynamicObject = owningDynamicObject;
	}

	public void mergeDynamicObjectActionWith(DynamicObjectAction dynObjAction) {

		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(dynObjAction);
			merged_in_child = false;
		}
				
		if(dynObjAction == null)
			return;
		
		if(this.owningDynamicObject == null && dynObjAction.owningDynamicObject != null)
				this.owningDynamicObject = dynObjAction.owningDynamicObject;
	}

}
