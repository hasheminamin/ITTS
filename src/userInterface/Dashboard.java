package userInterface;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import enums.SemanticTag;
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
//	public static String inputCorporaPath = "dataset/97-05-22CRFcorpora-junk-sceneMarker-corrections-story2.arff";
//	public static String inputCorporaPath = "dataset/98-02-03CRFcorpora-junk-sceneMarker-corrections-corr-test-sync.arff";
//	public static String inputCorporaPath = "dataset/98-02-03CRFcorpora-junk-sceneMarker-corrections-corr-test-sync2.arff";
//	public static String inputCorporaPath = "dataset/98-02-03CRFcorpora-junk-sceneMarker-corrections-corr-test-sync2.arff";
//	public static String inputCorporaPath = "dataset/98-02-08CRFcorpora-junk-sceneMarker-corrections-corr-test-sync2-PRcorrect.arff";
	public static String inputCorporaPath = "dataset/98-02-09CRFcorpora-junk-sceneMarker-corrections-corr-test-sync-PRcorrect.arff";
	
	
	
//	public static String inputSystemOutpurPath = "dataset/97-12-21out.arff";
	public static String inputSystemOutpurPath = "dataset/98-02-05out.arff";
	
	public static SceneReasoner sceneReasoner = new SceneReasoner(); 
	

	public static ArrayList<StoryModel> importInputCorpora(String corporaFilename)
	{
		ArrayList<StoryModel> allStories = new ArrayList<StoryModel>();
		
		StoryModel currentStory = null;
		SceneModel currentScene = null;
		SentenceModel currentSentence = null;
		int storyNumber = 0;
		int sceneNumber = 0;
		int sentenceNumber = 0;
		int wordNumber = 0;
		
		BufferedReader corporaStream = null;
		
		try
		{
			corporaStream = new BufferedReader(new InputStreamReader(new FileInputStream(corporaFilename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening \'" + corporaFilename + "\' for reading input corpora!");
			e.printStackTrace();
		}
		
		String line = "";
		try{
		
			while (line != null)
			{				
				line = corporaStream.readLine();
				
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
					MyError.error("currentSentence should not be null!!!");
				else
					currentSentence.addWord(currentWord);
				
//				print("" + currentWord);
			}
			if(currentStory != null)
				allStories.add(currentStory);
			
			print("story-num: " + storyNumber +" scene-num: " + sceneNumber + " sentence-num: " + sentenceNumber + " word-num:" + wordNumber);
				
			corporaStream.close();		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return allStories;
	}
	
	
	public static void importSystemOutput(String systemOutputFilename, ArrayList<StoryModel> corporaStories)
	{
		if (corporaStories == null || corporaStories.size() == 0) {
			MyError.error("corporaStories should not be null!!!");
			return;
		}
		
		BufferedReader systemOutputStream = null;
		
		try
		{
			systemOutputStream = new BufferedReader(new InputStreamReader(new FileInputStream(systemOutputFilename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening \'" + systemOutputFilename + "\' for reading input corpora!");
			e.printStackTrace();
		}
		
		String line = "";
		try{
		
			Word currentWord = null;
			int truelyPredictedWords = 0;
			int wronglyPredictedWords = 0;
				
			for(int storyIndex = 0; storyIndex < corporaStories.size(); storyIndex++) {
					
				StoryModel currentStory = corporaStories.get(storyIndex);
				
				if(currentStory == null) {
					MyError.error("current Story should not be null!!!");
					continue;
				}
				
				for(int sceneIndex = 0; currentStory.scenes != null && sceneIndex < currentStory.scenes.size(); sceneIndex++) {

					SceneModel currentScene = currentStory.scenes.get(sceneIndex);
					
					if(currentScene == null) {
						MyError.error("current Scene should not be null!!!");
						continue;
					}
										
					for(int sentenceIndex = 0; currentScene.sentences != null && sentenceIndex < currentScene.sentences.size(); sentenceIndex++) {
					
						SentenceModel currentSentence = currentScene.sentences.get(sentenceIndex);
											
						if(currentSentence == null) {
							MyError.error("current Sentence should not be null!!!");
							continue;
						}
						
						if(currentWord != null) {//it is the last word of the past sentence
							
							line = systemOutputStream.readLine();
							
							if (line == null) {
								MyError.error("unexpected end of file!!!");								
								break;
							}
							
							line = line.trim();
							
							if(!line.equals(""))//it must be a blank line after the past sentence.
								MyError.error("it must be a blank line after the past sentence !!!");
													
							currentWord = null;
						}
						
						for(int wordIndex = 0; currentSentence.getWords() != null && wordIndex < currentSentence.getWords().size();) {
						
							currentWord =  currentSentence.getWords().get(wordIndex);
//							print("curWord: " + currentWord);
						
							if(currentWord == null)
								break;
							
							if(!currentWord.hasMultiSemanticTag()) {
								
								line = systemOutputStream.readLine();
								
								if (line == null) {
									MyError.error("unexpected end of file!!!");								
									break;
								}
								
								line = line.trim();
								
								if(!line.equals("")) //it is a sceneElement for a word
									currentWord.add_predictedSceneElement(line);
								else 
									MyError.error("there is no predicted sceneElement in output File!!!");								
							}
							else{//this word has multi SemanticTags so it has multi predicted sceneElement in dataset too!
								
								for(@SuppressWarnings("unused") SemanticTag semTag:currentWord._semanticTags) {
									
									line = systemOutputStream.readLine();
									
									if (line == null) {
										MyError.error("unexpected end of file!!!");								
										break;
									}
									
									line = line.trim();
									
									if(!line.equals("")) //it is a sceneElement for a word
										currentWord.add_predictedSceneElement(line);
										
									else 
										MyError.error("there is no predicted sceneElement in output File!!!");									
								}								
							}
							if(currentWord.get_isTruelyPredicted())
								truelyPredictedWords++;
							else
								wronglyPredictedWords++;
							
							wordIndex++;
						}
					}
				}				
			}
			
			print("truely predicted words:" + truelyPredictedWords);
			print("wrongly predicted words:" + wronglyPredictedWords);
			print("accuracy:" + (((truelyPredictedWords*1.0)/1203)*100) + "%");
			systemOutputStream.close();		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
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
