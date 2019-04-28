package model;

import ir.ac.itrc.qqa.semantic.util.MyError;
import sceneElement.SceneElement;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

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
	
	public ArrayList<Word> calculateMultiSemanticTagWords(){
		if(scenes == null || scenes.size() == 0)
			return null;
		
		ArrayList<Word> multiSemTagwords = new ArrayList<Word>();
		
		for(SceneModel scn:scenes) {
			ArrayList<Word> wrds = scn.calculateMultiSemanticTagWords();
			if(wrds != null)
				multiSemTagwords.addAll(scn.calculateMultiSemanticTagWords());
		}
		
		return multiSemTagwords;
	}
	
	public ArrayList<Word> calculateWronglyPredictedWords(){
		if(scenes == null || scenes.size() == 0)
			return null;
		
		ArrayList<Word> wronglyPredictedwords = new ArrayList<Word>();
		
		for(SceneModel scn:scenes) {
			
			ArrayList<Word> wrds = scn.calculateWronglyPredictedWords();
			
			if(wrds != null)
				wronglyPredictedwords.addAll(wrds);
		}
		
		return wronglyPredictedwords;
	}

	public ArrayList<Word> calculateTruelyPredicatedWords(){
		if(scenes == null || scenes.size() == 0)
			return null;
		
		ArrayList<Word> truelyPredictedwords = new ArrayList<Word>();
		
		for(SceneModel scn:scenes) {
			
			ArrayList<Word> wrds = scn.calculateTruelyPredictedWords();
			
			if(wrds != null)
				truelyPredictedwords.addAll(wrds);
		}
		
		return truelyPredictedwords;
	}
	
	public int calculateRepeatedWords(){
		if(scenes == null || scenes.size() == 0)
			return 0;
		
		int sceneIndex = 0;		
		int repeatedWords = 0;
		
		for(SceneModel scn:scenes) {
			
			sceneIndex++;
			 
			Hashtable<SceneElement, ArrayList<SceneElement>> sceneRepeatedWrds = scn.repeatedSceneElements;
			
			if(sceneRepeatedWrds != null) {
			
				Set<SceneElement> scneKetSet = sceneRepeatedWrds.keySet();
					
				for (SceneElement keyElem:scneKetSet) {
					
					ArrayList<SceneElement> repList = sceneRepeatedWrds.get(keyElem);
					
					for(SceneElement rep: repList) {
						
						repeatedWords++;						
	
						print("this \'" + keyElem._name + "=" + keyElem._node_name + "\' and these \'" + rep._name + "=" + rep._node_name + "\' are repeated in secene" + sceneIndex);		
					}
				}
			}
		}
		return repeatedWords;
	}
	
	private void print(String toPrint){
		System.out.println(toPrint);		
	}

	
}
