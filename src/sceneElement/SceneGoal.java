package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class SceneGoal extends SceneElement {
	
	protected boolean merged_in_child = false;

	public SceneGoal(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.SCENE_GOAL, word);
		
	}

	public SceneGoal(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.SCENE_GOAL, node_name);
		
	}
	
	public SceneGoal(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.SCENE_GOAL, node);

	}

	public void mergeSceneGoalWith(SceneGoal sceneGoal) {

		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(sceneGoal);
			merged_in_child = false;
		}
				
		if(sceneGoal == null)
			return;
		
	}
}
