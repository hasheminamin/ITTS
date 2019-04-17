package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class Time  extends SceneElement{

	protected boolean merged_in_child = false;

	public Time(SceneModel scene, String name, Word word) {
		 super(scene, name, ScenePart.TIME, word);
	}
	
	public Time(SceneModel scene, String name, Node node) {
		 super(scene, name, ScenePart.TIME, node);
	}

	@Override
	public String toString() {
		return  "[" + _node_name + "=  " + _name + "]";
	}	
	
	public void mergeTimeWith(Time time){
		
		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(time);
			merged_in_child = false;
		}
		
		if(time == null)
			return;
	}

}
