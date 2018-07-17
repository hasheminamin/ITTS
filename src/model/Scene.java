package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class Scene {
	
	public Story story = null;
	
	public ArrayList<Sentence> sentences = new ArrayList<Sentence>();
	
	public Scene(){
		sentences = new ArrayList<Sentence>();
	}
	
	public Scene(Story story){
		this.sentences = new ArrayList<Sentence>();
		this.story = story;
	}
	
	public Scene(ArrayList<Sentence> sentences, Story story){
		this.sentences = sentences;
		this.story = story;
	}

	public int getSentenceNumber() {
		if(sentences != null)
			return sentences.size();
		return 0;
	}
	
	public void addSentence(Sentence newSentence) {
		if(sentences == null)
			sentences = new ArrayList<Sentence>();
		
		if(newSentence == null || sentences.contains(newSentence))
			return;
		
		sentences.add(newSentence);

		if(newSentence._scene == null)
			newSentence._scene = this;
		else if(newSentence._scene != this){			
			MyError.error("\n!!!! change in scene of a sentence from " +  newSentence._scene + " to " + this + " for \"" + newSentence + "\" sentence.\n");
			newSentence._scene = this;
		}
			
	}	

}
