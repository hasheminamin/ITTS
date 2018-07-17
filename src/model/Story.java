package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class Story {
	
	public ArrayList<Scene> scenes = new ArrayList<Scene>();
	
	public Story(){
		scenes = new ArrayList<Scene>();
	}

	public void addScene(Scene newScene) {
		if(scenes == null)
			scenes = new ArrayList<Scene>();
		
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
