package userInterface;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sceneReasoner.SceneReasoner;
import model.SentenceModel;
import model.SceneModel;
import model.StoryModel;
import model.Word;

public class Dashboard {
	/**
	 * format:
	 * in this corpora each word appears in a separate line and  
	 * sentences are separated by one and only one blank line. 
	 * there is also one and only one blank line before and after each scene marker.
	 * 
	 * #scene#......
	 * 
	 * one and only one blank line exists before story marker and its next scene marker is attached to it (without a blank line)
	 * 
	 * #story#......
	 * #scene#......
	 * 
	 */
//	public static String inputCorporaPath = "dataset/97-04-17CRFcorpora-junk-sceneMarker.arff";
//	public static String inputCorporaPath = "dataset/97-04-31CRFcorpora-junk-sceneMarker-corrections.arff";
//	public static String inputCorporaPath = "dataset/97-04-17CRFcorpora-junk-sceneMarker-story1-corrections.arff";
//	public static String inputCorporaPath = "dataset/97-04-31CRFcorpora-junk-sceneMarker-corrections.arff";
//	public static String inputCorporaPath = "dataset/97-05-14CRFcorpora-junk-sceneMarker-corrections-story1_role_state_action.arff";												     
//	public static String inputCorporaPath = "dataset/97-05-17CRFcorpora-junk-sceneMarker-corrections-story1_role_state_action_ statobj_state_dynobj_state_role_intent_check_PR_loc_time.arff";
	public static String inputCorporaPath = "dataset/97-05-22CRFcorpora-junk-sceneMarker-corrections-story2.arff";
	
	public static SceneReasoner sceneReasoner = new SceneReasoner(); 
	

	
	public static ArrayList<StoryModel> importInputCorpora(String filename)
	{
		ArrayList<StoryModel> allStories = new ArrayList<StoryModel>();
		
		StoryModel currentStory = null;
		SceneModel currentScene = null;
		SentenceModel currentSentence = null;
		int storyNumber = 0;
		int sceneNumber = 0;
		int sentenceNumber = 0;
		int wordNumber = 0;
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening `" + filename + "` for reading input corpora!");
			e.printStackTrace();
		}
		
		String line = "";
		try{
		
			while (line != null)
			{				
				line = stream.readLine();
				
				if (line == null)
					break;
				
				line = line.trim();				
							
				if (line.startsWith("#story#")){ // beginning of new story in the corpora
					
					if(currentStory != null)
						allStories.add(currentStory);
					
					currentStory = new StoryModel();
					
					storyNumber++;
//					print("newStory*************************************");
					
					continue;
				}

				if (line.startsWith("#scene#")){ // beginning of new scene in the corpora
					
					sceneNumber++;
					
					if(currentStory != null){

						currentScene = new SceneModel(currentStory);
						currentStory.addScene(currentScene);						
						
						currentSentence = null;
					}
					else
						MyError.error("currentStory should not be null!!!");					
//					print("newScene^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
					continue;
				}
				
				if(line.equals("")){
					
					if(currentStory != null){
						if(currentScene != null){
							if(currentSentence == null){// beginning of new sentence in the corpora
								currentSentence = new SentenceModel(currentScene);								
							}
							else{// end of currentSentence
								currentSentence.makeNaturalLanguage();
								currentScene.addSentence(currentSentence);//we have done before this assignment!								
								currentSentence = new SentenceModel(currentScene);
								
								sentenceNumber++;
//								print("-"+sentenceNumber + "-");
							}
						}
						else{
							MyError.error("currentScene should not be null!!!");	 
						}
					}
					else
						MyError.error("currentStory should not be null!!!");
					continue;
				}
				//when reach here it means it is a word:
				Word currentWord = new Word(line, currentSentence);
				wordNumber++;
							
				if(currentSentence == null)
					MyError.error("currentStory should not be null!!!");
				else
					currentSentence.addWord(currentWord);
				
//				print("" + currentWord);
			}
			if(currentStory != null)
				allStories.add(currentStory);
			
			print("story number: " + storyNumber +" scene number:" + sceneNumber + " sentence number: " + sentenceNumber + " word number:" + wordNumber);
				
			stream.close();		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return allStories;
	}
	
//	public static void ConvertTextToSceneModel(){
//		
//		ArrayList<StoryModel> allStories = importInputCorpora(inputCorporaPath);
//		
//		sceneReasoner.arrangeWordReferences(allStories);
//		
////		sceneReasoner.arrangeSceneModelsElements(allStories);
//	}

	private static void print(String s){
		System.out.println(s);
	}
	
//	public static void main(String[] args) {
//		print("بسم الله الرحمن الرحیم و توکلت علی الله");
//
//		ConvertTextToSceneModel();		
//		
//		print("الحمدلله رب العالمین");
//	}

}
