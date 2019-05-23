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
	
	public SentenceModel getPastSentence(SentenceModel currentSentence) {
		
		if(currentSentence == null)
			return null;	
		
		for(int sceneIndex = 0; sceneIndex < this.scenes.size(); sceneIndex++) {
			
			SceneModel curScene = this.scenes.get(sceneIndex);
					
			for(int sentIndex = 0; sentIndex < curScene.sentences.size(); sentIndex++) {
				
				SentenceModel curSent = curScene.sentences.get(sentIndex);
				
				if(curSent != null && curSent.equals(currentSentence) && (sentIndex-1) >= 0)
					return curScene.sentences.get(sentIndex - 1);
				
				else if(curSent != null && curSent.equals(currentSentence) && (sentIndex - 1 < 0) && (sceneIndex - 1) >= 0) {
					SceneModel pastScene = this.scenes.get(sceneIndex - 1);
					
					if(pastScene != null && pastScene.sentences != null && pastScene.sentences.size() != 0)
						return pastScene.sentences.get(pastScene.sentences.size() - 1);
				}
			}
		}
		return null;
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
	
	@SuppressWarnings("unused")
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
					
					repeatedWords += repList.size();
									
//					print("this \'" + keyElem._name + "=" + keyElem._node_name + "\' and these \'(" + repList.get(0)._name + "=" + repList.get(0)._node_name + ")*" + repList.size() + "\' are repeated in secene" + sceneIndex);		
					
				}
			}
		}
		return repeatedWords;
	}
	
	@SuppressWarnings("unused")
	private void print(String toPrint){
		System.out.println(toPrint);		
	}

	
}
