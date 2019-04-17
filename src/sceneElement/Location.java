package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;



public class Location extends SceneElement{
	
	protected boolean merged_in_child = false;

	public Location(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.LOCATION, word);
	}
	
	public Location(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.LOCATION, node_name);
	}
	
	public Location(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.LOCATION, node);
	}

	@Override
	public String toString() {
		return  "[" + _node_name + "=  " + _name + "]";
	}
	
	public void mergeLocationWith(Location location){
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(location);
			merged_in_child = false;
		}
				
		if(location == null)
			return;
	}
}
