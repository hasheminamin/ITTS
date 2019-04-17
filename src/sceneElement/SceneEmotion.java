package sceneElement;

import enums.ScenePart;
import model.SceneModel;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;



public class SceneEmotion  extends SceneElement{
	
	protected boolean merged_in_child = false;

	public SceneEmotion(SceneModel scene, String name, Word word) {
		super(scene, name, ScenePart.SCENE_EMOTION, word);

	}
	
	public SceneEmotion(SceneModel scene, String name, String node_name) {
		super(scene, name, ScenePart.SCENE_EMOTION, node_name);

	}

	public SceneEmotion(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.SCENE_EMOTION, node);

	}

	public void mergeSceneEmotionWith(SceneEmotion sceneEmotion) {

		if(merged_in_father == false){
			merged_in_child = true;
			super.mergeWith(sceneEmotion);
			merged_in_child = false;
		}
				
		if(sceneEmotion == null)
			return;
		
	}
}
