package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class StoryModel {
	
	public ArrayList<SceneModel> scenes = new ArrayList<SceneModel>();
	
	
	
	public StoryModel(){
		scenes = new ArrayList<SceneModel>();
	}

	
	
	public void addScene(SceneModel newScene) {
		if(scenes == null)
			scenes = new ArrayList<SceneModel>();
		
		if(newScene == null || scenes.contains(newScene))
			return;
				
		scenes.add(newScene);
		
		if(newScene.story == null)
			newScene.story = this;
		else if(newScene.story != this){
			MyError.error("\n!!!! change in story of a scene from " +  newScene.story + " to " + this + " for \"" + newScene + "\" scene.\n");
			newScene.story = this;
		}		
	}

}
