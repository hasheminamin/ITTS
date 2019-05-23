package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


import sceneElement.DynamicObject;
import sceneElement.DynamicObjectState;
import sceneElement.Location;
import sceneElement.DynamicObjectAction;
import sceneElement.Role;
import sceneElement.RoleAction;
import sceneElement.RoleEmotion;
import sceneElement.RoleIntent;
import sceneElement.RoleState;
import sceneElement.SceneElement;
import sceneElement.SceneEmotion;
import sceneElement.SceneGoal;
import sceneElement.StaticObject;
import sceneElement.StaticObjectState;
import sceneElement.Time;
import enums.ScenePart;
import enums.POS;
import model.Phrase;
import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;
import model.Word;

public class SceneReasoner {

	/**
	 * It is assumed that referent are at most in two past scene not more! 	
	 * @param allStories
	 */
	public void arrangeWordReferences(ArrayList<StoryModel> allStories, POS anaphoraPOS) {
			
			int pronounIndex = 0;
		
			for(StoryModel storyModel: allStories){
				
	//			print("newStory*************************************");
				
				for(int sceneIndex = 0; sceneIndex < storyModel.scenes.size(); sceneIndex++){
					SceneModel sceneModel = storyModel.scenes.get(sceneIndex);
					
	//				print("newScene*************************************");
					
					for(int sentenceIndex = 0; sentenceIndex <sceneModel.sentences.size(); sentenceIndex++){
						
						SentenceModel sentence = sceneModel.sentences.get(sentenceIndex);
	
	//					print("");
						
						for(Word word: sentence.getWords()){
							
							if(word.hasReferenceWordPath()){
								
								if(anaphoraPOS != null)
									if(word._gPOS != anaphoraPOS)
										continue;
								
								pronounIndex++;
								
//								print(word + "");
								
								String path = "";
								
								String refWordNum = "";
								
								for(char c:word._referenceWordNum.toCharArray()){
									
									if(Character.isDigit(c))
										refWordNum += c;
									else
										path += c;
								}
								
								SceneModel referecneScene = sceneModel;
								SentenceModel referenceSentence = sentence;
								int referenceWordNumber = Integer.parseInt(refWordNum);
								
								boolean inPastScene = false;
								
								if(path.equals(""))
									referenceWordNumber = Integer.parseInt(refWordNum);							
								else{
									if(path.contains("*")){
										inPastScene = true;
										if( (sceneIndex-1) < storyModel.scenes.size())
											referecneScene = storyModel.scenes.get(sceneIndex - 1);
										else
											MyError.error("this is the first scene of the story, and has no past scene!");
	
										int starIndex = path.indexOf("*");
										
										if(starIndex + 1 < path.length())
											path = path.substring(0, starIndex) + path.substring(starIndex+1, path.length());
										else
											path = path.substring(0, starIndex);
										
										if(path.contains("*")){
											if((sceneIndex - 2) < storyModel.scenes.size())
												referecneScene = storyModel.scenes.get(sceneIndex - 2);
											else
												MyError.error("this is the second scene of the story, and has not 2 scene before!");
	
											
											starIndex = path.indexOf("*");
											
											if(starIndex + 1 < path.length())
												path = path.substring(0, starIndex) + path.substring(starIndex+1, path.length());
											else
												path = path.substring(0, starIndex);
										}
										if(path.contains("*")){
											
											if((sceneIndex - 3) < storyModel.scenes.size())
												referecneScene = storyModel.scenes.get(sceneIndex - 3);
											else
												MyError.error("this is the third scene of the story, and has not 3 scene before!");
											
											starIndex = path.indexOf("*");
											
											if(starIndex + 1 < path.length())
												path = path.substring(0, starIndex) + path.substring(starIndex+1, path.length());
											else
												path = path.substring(0, starIndex);
										}									
									}
									
									if(path.contains("-")){
										
										int refIndex = 0;
										if(inPastScene)
											refIndex = referecneScene.sentences.size() - path.length();
										else
											refIndex = sentenceIndex - path.length();
										
										if(refIndex >= 0 && refIndex < referecneScene.sentences.size())
											referenceSentence = referecneScene.sentences.get(refIndex);
										else
											MyError.error("the referenceWordNumber " + path + " for word " + word._wordName +  " dose not exist in this scene sentences!\nfor sentence " + sentence);										
									}
									else if(path.contains("+")){
										
										if(inPastScene)											
											MyError.error("how can reference word be placed in next sentences of the scene before!");
											
										int refIndex = sentenceIndex + path.length();
										
										if(refIndex >= 0 && refIndex < referecneScene.sentences.size())
											referenceSentence = referecneScene.sentences.get(refIndex);
										else
											MyError.error("the referenceWordNumber " + path + " for word " + word._wordName +  " dose not exist in this scene sentences!\nfor sentence " + sentence);									
									}
								}
								
								Word referent = referenceSentence.getWord(referenceWordNumber);		
								
								word.set_referenceWord(referent);
								
								if(referent == null) {
									print("%%%%%%%%%%%%%%%%%%\nreferent of \'" + word + "\' is null");
									MyError.error("referent not found!!!");
								}
//								print("referent:" + referent + "\n");
							}						
						}
					}
				}
			}
			
			print("\nTotal number of words with \'" + anaphoraPOS + "\' POS which have referent are: " + pronounIndex);
			
		}
	
	/**
	 * this method read each word of each sentence for every scene of every story and based 
	 * on its _sceneElement allocated that word to the corresponding part of sceneModel.
	 * when adding a SceneElement to a SceneModel if the _name and _node or _node_name 
	 * of a sceneElement be equal, SceneModel assumed they are equal and merges them 
	 * into each other.  
	 * @param allStories
	 */
	public void arrangeSceneModelsElements(ArrayList<StoryModel> allStories) {
		
		try{
//			PrintWriter writer = new PrintWriter("dataset/98-02-17story2Scenes.arff","utf-8");
			PrintWriter writer = new PrintWriter("dataset/98-02-22story2ScenesWithWords.arff","utf-8");
					
			int wordNum = 0;
						
			for(StoryModel storyModel: allStories){
				
				int sceneIndex = 0;
				
//				print("newStory*************************************");
				writer.write("newStory*************************************\n");
				
				for(SceneModel sceneModel:storyModel.scenes){
					
//					print("newScene"+ (sceneIndex) + "*************************************");
					writer.write("newScene"+ sceneIndex +"*************************************\n");
					sceneIndex++;
					
					for(SentenceModel sentence: sceneModel.sentences){
							
//						print("");
						writer.write("\n");
						
						for(Word word: sentence.getWords()){
							wordNum++;
							
//							print("" + word);
							writer.write(word + "\n");
							
							ScenePart scenePart = word._sceneElement;
							
							switch(scenePart){							
								case ROLE:
									Role role = new Role(sceneModel, word._wordName, word);
									sceneModel.addRole(role);
									break;
								case DYNAMIC_OBJECT:
									DynamicObject dynObj = new DynamicObject(sceneModel, word._wordName, word);
									sceneModel.addDynamic_object(dynObj);
									break;
								case STATIC_OBJECT:
									StaticObject statObj = new StaticObject(sceneModel, word._wordName, word);
									sceneModel.addStatic_object(statObj);
									break;
								case LOCATION:
									Location location = new Location(sceneModel, word._wordName, word);
									sceneModel.addAlternativeLocation(location);
									break;
								case TIME:
									Time time = new Time(sceneModel, word._wordName, word);
									sceneModel.addAlternativeTime(time);
									break;		
									
									
								case SCENE_EMOTION:
									SceneEmotion sceneEmotion = new SceneEmotion(sceneModel, word._wordName, word);
									sceneModel.addScene_emotion(sceneEmotion);
									break;
								case SCENE_GOAL:
									SceneGoal sceneGoal = new SceneGoal(sceneModel, word._wordName, word);
									sceneModel.addScene_goal(sceneGoal);
									break;
									
								case ROLE_ACTION:
									RoleAction roleAction = new RoleAction(sceneModel,  word._wordName, word);
									sceneModel.addRoleAction(roleAction);
									break;
								case ROLE_STATE:
									RoleState roleState = new RoleState(sceneModel,  word._wordName, word);
									sceneModel.addRoleState(roleState);
									break;
								case ROLE_INTENT:
									RoleIntent roleIntent = new RoleIntent(sceneModel,  word._wordName, word);
									sceneModel.addRoleIntent(roleIntent);
									break;
								case ROLE_EMOTION:
									RoleEmotion roleEmotion = new RoleEmotion(sceneModel,  word._wordName, word);
									sceneModel.addRoleEmotion(roleEmotion);
									break;
								case DYNAMIC_OBJECT_ACTION:
									DynamicObjectAction objectAction = new DynamicObjectAction(sceneModel,  word._wordName, word);
									sceneModel.addObjectAction(objectAction);
									break;
								case DYNAMIC_OBJECT_STATE:
									DynamicObjectState dynamicObejctState = new DynamicObjectState(sceneModel,  word._wordName, word);
									sceneModel.addDynamicObjectState(dynamicObejctState);
									break;
								case STATIC_OBJECT_STATE:
									StaticObjectState staticObejctState = new StaticObjectState(sceneModel,  word._wordName, word);
									sceneModel.addStaticObjectState(staticObejctState);
									break;							
								default:
									// case NO, JUNK
									break;							
							}
						}
						
	//					print("" + sceneModel + "\n");
					}
//					print("wordNum up to now: " + wordNum);
//					print("" + sceneModel + "\n");
					writer.write("" + sceneModel.toStringForGoldSceneModel() + "\n\n");
				}
			}
			print("total wordNum: " + wordNum);
			writer.close();
		}
		catch(Exception e){
			print("" + e);
		}
	}

	public void printForGoldSceneModel(ArrayList<StoryModel> allStories, String printFileName) {
	
		try{
			PrintWriter writer = new PrintWriter(printFileName, "utf-8");
					
			for(StoryModel storyModel: allStories){
				
				int sceneIndex = -1;

				writer.write("newStory*************************************\n");
				
				for(SceneModel sceneModel:storyModel.scenes){
					
					sceneIndex++;
					
					writer.write("newScene"+ sceneIndex +"*************************************\n");
					
//					for(SentenceModel sentence: sceneModel.sentences){
//								
//						writer.write("\n");
//						
//						for(Word word: sentence.getWords())
//							writer.write(word + "\n");
//					}
					
					writer.write("" + sceneModel.toStringForGoldSceneModel() + "\n\n");
				}
			}			
			writer.close();
		}
		catch(Exception e){
			print("" + e);
		}
	}
	
	public void OldCompleteSceneModelsElements(ArrayList<StoryModel> allStories) {
		
		for(StoryModel storyModel: allStories){
			
			print("newStory*************************************");
			
			for(SceneModel sceneModel:storyModel.scenes){
				
				print("newScene*************************************");
				
				for(RoleAction roleAction: sceneModel.roleActions){
						
					print("--------roleActions----------");
					
					if(roleAction._mainWord != null){
						
						if(roleAction._mainWord._referenceWord != null){
						
							Word raRefWord = roleAction._mainWord._referenceWord;
							
							Role refRole = sceneModel.getRole(raRefWord);
							
							if(refRole == null)
								MyError.error("this sceneModel dosen't contain the reference Role of this roleAction " + roleAction + " with refrenceWord " + raRefWord);
							
							else{
								refRole.addRole_action(roleAction);
//								roleAction.set_owningRole(refRole);
								continue;
							}							
						}
					}
					if(roleAction.get_otherWords() == null || roleAction.get_otherWords().size() == 0)
						continue;
										
					for(Word roleActionWord: roleAction.get_otherWords()){
					
						if(roleActionWord._referenceWord == null)
							continue;
						
						Word raRefWord = roleActionWord._referenceWord;
						
						Role refRole = sceneModel.getRole(raRefWord);
						
						if(refRole == null){
							MyError.error("this sceneModel dosen't contain the reference Role of this roleAction " + roleAction + " with refrenceWord " + raRefWord);
							continue;
						}
						//if(refRole != null)
						refRole.addRole_action(roleAction);
//						roleAction.set_owningRole(refRole);
					}
				}
				//TODO: code for processing *1- in input path and check the correct work of finding references.
							/*
							 * 	case SCENE_EMOTION:
								SceneEmotion sceneEmotion = new SceneEmotion(sceneModel, word._wordName, word);
								sceneModel.addScene_emotion(sceneEmotion);
								break;
							case SCENE_GOAL:
								SceneGoal sceneGoal = new SceneGoal(sceneModel, word._wordName, word);
								sceneModel.addScene_goal(sceneGoal);
								break;
							case ROLE_ACTION:
								RoleAction roleAction = new RoleAction(sceneModel,  word._wordName, word);
								sceneModel.addRoleAction(roleAction);
								break;
							case ROLE_STATE:
								RoleState roleState = new RoleState(sceneModel,  word._wordName, word);
								sceneModel.addRoleState(roleState);
								break;
							case ROLE_INTENT:
								RoleIntent roleIntent = new RoleIntent(sceneModel,  word._wordName, word);
								sceneModel.addRoleIntent(roleIntent);
								break;
							case ROLE_EMOTION:
								RoleEmotion roleEmotion = new RoleEmotion(sceneModel,  word._wordName, word);
								sceneModel.addRoleEmotion(roleEmotion);
								break;
							case DYNAMIC_OBJECT_ACTION:
								ObjectAction objectAction = new ObjectAction(sceneModel,  word._wordName, word);
								sceneModel.addObjectAction(objectAction);
								break;
							case DYNAMIC_OBJECT_STATE:
								DynamicObjectState dynamicObejctState = new DynamicObjectState(sceneModel,  word._wordName, word);
								sceneModel.addDynamicObjectState(dynamicObejctState);
								break;
							case STATIC_OBJECT_STATE:
								StaticObjectState staticObejctState = new StaticObjectState(sceneModel,  word._wordName, word);
								sceneModel.addStaticObjectState(staticObejctState);
								break;		
							 * همه role_state, rola_action, ...
							 * همه اینا رو چک کنه و متناسب رفرنس وردشون کاملشون کنه هم اینا رو هم اون کلمات رفرنس رو
							 * 
							 * فقط ضمیرها این وسط میمونه
							 * همین طور
							 * main location and time of scene  
							 */
					
				
			}
		}
	}

	public ArrayList<Word> calculateMultiSemanticTagWords(ArrayList<StoryModel> allStories){
		if(allStories == null || allStories.size() == 0)
			return null;
		
		ArrayList<Word> multiSemTagwords = new ArrayList<Word>();
		
		int index = 0;
//		print("\nWords with multi semantic Tags:");
		
		int duplacateNum = 0;
		
		for(StoryModel stry:allStories) {
			
			ArrayList<Word> strMultiSemTagWrds = stry.calculateMultiSemanticTagWords();
			
				if(strMultiSemTagWrds != null) {
				
					for(Word wrd:strMultiSemTagWrds) {
						index++;
//						print(index + ": " + wrd._wordName + " " + wrd._semanticTags + " ||| " + wrd._predictedSceneElements.toString());						
						duplacateNum += wrd._semanticTags.size();
					}
					
					multiSemTagwords.addAll(strMultiSemTagWrds);
			}
		}
		print("\nWords with multi semantic Tags: " + index);
		print("\nDataset records with multi semantic Tags: " + duplacateNum);
		
		return multiSemTagwords;
	}
	
	public ArrayList<Word> calculateWronglyPredictedWords(ArrayList<StoryModel> allStories){
		if(allStories == null || allStories.size() == 0)
			return null;
		
		ArrayList<Word> wronglyPredictedWords = new ArrayList<Word>();
		
		int index = 0;
//		print("\nwrongly predicted words:");
		
		for(StoryModel stry:allStories) {
			ArrayList<Word> wrds = stry.calculateWronglyPredictedWords();
			
			if(wrds != null) {
				
				for(@SuppressWarnings("unused") Word wrd:wrds) {
					index++;
//					print(index + ": " + wrd._wordName + " " + wrd._sceneElement + " ||| " + wrd._predictedSceneElements.toString());
					
				}
			
				wronglyPredictedWords.addAll(wrds);
			}
		}
		
		print("\nwrongly predicated words num: " + index);
		
		return wronglyPredictedWords;
	}
	
	public ArrayList<Word> calculateTruelyPredictedWords(ArrayList<StoryModel> allStories){
		if(allStories == null || allStories.size() == 0)
			return null;
		
		ArrayList<Word> truelyPredicatedWords = new ArrayList<Word>();
		
		int index = 0;
				
		for(StoryModel stry:allStories) {
			ArrayList<Word> wrds = stry.calculateTruelyPredicatedWords();
			if(wrds != null) {
				
				for(@SuppressWarnings("unused") Word wrd:wrds) {
					index++;
//					print(index + ": " + wrd + " ||| " + wrd._predictedSceneElements.toString());					
				}
	
				truelyPredicatedWords.addAll(wrds);
			}
		}
		
		print("\ntruely predicated words num: " + index);
		
		return truelyPredicatedWords;
	}
	
	public void calculateRepeatedWords(ArrayList<StoryModel> allStories){
		
		if(allStories == null || allStories.size() == 0)
			return;
		
		int totalRepeatedWords = 0;			
		
		for(StoryModel stry:allStories)			
			totalRepeatedWords += stry.calculateRepeatedWords();		
				
		print("\nThe number of words which are repeated in all scenes are: " + totalRepeatedWords + "\n");
	}
	
	public ArrayList<StoryModel> readGoldStoryModels(String goldStoryModelsFilename) {
		
		BufferedReader goldModelStream = null;
		
		ArrayList<StoryModel> goldStoryModels = new ArrayList<>();
		
		try
		{
			goldModelStream = new BufferedReader(new InputStreamReader(new FileInputStream(goldStoryModelsFilename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening \'" + goldStoryModelsFilename + "\' for reading input corpora!");
			e.printStackTrace();
		}
		
		ArrayList<String> goldModelLines = new ArrayList<String>();
		
		try {		
			
			String line = goldModelStream.readLine();			
			
			while (line != null)
			{	
				goldModelLines.add(line);
				line = goldModelStream.readLine();		
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		StoryModel currentStory = null;
		SceneModel currentScene = null;
		
		int storyNumber = 0;
		int sceneNumber = 0;
		int wordNumber = 0;
		
		String line = null;
		
		for(int lineIndex = 0; lineIndex < goldModelLines.size();)
		{				
			line = goldModelLines.get(lineIndex);
		
			if (line == null)
				break;
						
			if (line.startsWith("newStory")){ // beginning of new story in the goldStoryModelsFile
							
				currentStory = new StoryModel();
				goldStoryModels.add(currentStory);
				
				storyNumber++;
//				print("newStory" + storyNumber + "*************************************");
				
				lineIndex++;
				continue;
			}

			if (line.startsWith("newScene")){ // beginning of new scene in the goldStoryModelsFile
				
				sceneNumber++;
				
				if(currentStory != null){

					currentScene = new SceneModel(currentStory);
					currentStory.addScene(currentScene);								
				}
				else
					MyError.error("currentStory should not be null!!!");					
//				print("newScene"+ sceneNumber +"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//				print("goldModel wordsNumbers up to now" + wordNumber);
				lineIndex++;
				continue;
			}
			
			if(currentScene == null) {
				MyError.error("currentScene should not be null!!!");
				lineIndex++;
				continue;
			}
			
			if(line.equals(""))
				lineIndex++;
			
			if(line.startsWith("roles")){
									
				ArrayList<SceneElement> roleElems = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.ROLE, currentScene);
				
				if(roleElems == null || roleElems.size() == 0)
					continue;
								
				for(SceneElement rlElm:roleElems) {
					lineIndex += rlElm.getWordNumbers();
					Role nRole = new Role(currentScene,rlElm.getName(), rlElm._mainWord);
					nRole.addWord(rlElm.get_otherWords());
					currentScene.addRole(nRole);
					wordNumber += rlElm.getWordNumbers();
				}					
			}
			if(line.startsWith("DynamicObjects")){
				
				ArrayList<SceneElement> dynObjElems = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.DYNAMIC_OBJECT, currentScene);
				
				if(dynObjElems == null || dynObjElems.size() == 0)
					continue;
								
				for(SceneElement dynElm:dynObjElems) {
					lineIndex += dynElm.getWordNumbers();
					DynamicObject nDynObj = new DynamicObject(currentScene,dynElm.getName(), dynElm._mainWord);
					nDynObj.addWord(dynElm.get_otherWords());
					currentScene.addDynamic_object(nDynObj);
					wordNumber += dynElm.getWordNumbers();
				}					
			}
			if(line.startsWith("StaticObjects")){
				
				ArrayList<SceneElement> statObjElems = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.STATIC_OBJECT, currentScene);
				
				if(statObjElems == null || statObjElems.size() == 0)
					continue;
								
				for(SceneElement staElm:statObjElems) {
					lineIndex += staElm.getWordNumbers();
					StaticObject nStatObj = new StaticObject(currentScene,staElm.getName(), staElm._mainWord);
					nStatObj.addWord(staElm.get_otherWords());
					currentScene.addStatic_object(nStatObj);
					wordNumber += staElm.getWordNumbers();
				}					
			}
			if(line.startsWith("location")){
				
				ArrayList<SceneElement> location = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.LOCATION, currentScene);
				
				if(location == null || location.size() == 0)
					continue;
								
				for(SceneElement locElm:location) {
					lineIndex += locElm.getWordNumbers();
					Location nLoc = new Location(currentScene,locElm.getName(), locElm._mainWord);
					nLoc.addWord(locElm.get_otherWords());
					currentScene.setLocation(nLoc);
					wordNumber += locElm.getWordNumbers();
				}					
			}
			if(line.startsWith("alternativeLocations")){
				
				ArrayList<SceneElement> altLocs = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.LOCATION, currentScene);
				
				if(altLocs == null || altLocs.size() == 0)
					continue;
								
				for(SceneElement locElm:altLocs) {
					lineIndex += locElm.getWordNumbers();
					Location nLoc = new Location(currentScene,locElm.getName(), locElm._mainWord);
					nLoc.addWord(locElm.get_otherWords());
					currentScene.addAlternativeLocation(nLoc);
					wordNumber += locElm.getWordNumbers();
				}					
			}
			if(line.startsWith("time")){
				
				ArrayList<SceneElement> time = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.TIME, currentScene);
				
				if(time == null || time.size() == 0)
					continue;
								
				for(SceneElement timElm:time) {
					lineIndex += timElm.getWordNumbers();
					Time nTime = new Time(currentScene,timElm.getName(), timElm._mainWord);
					nTime.addWord(timElm.get_otherWords());
					currentScene.setTime(nTime);
					wordNumber += timElm.getWordNumbers();
				}					
			}
			if(line.startsWith("alternativeTimes")){
				
				ArrayList<SceneElement> time = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.TIME, currentScene);
				
				if(time == null || time.size() == 0)
					continue;
								
				for(SceneElement timElm:time) {
					lineIndex += timElm.getWordNumbers();
					Time nTime = new Time(currentScene,timElm.getName(), timElm._mainWord);
					nTime.addWord(timElm.get_otherWords());
					currentScene.addAlternativeTime(nTime);
					wordNumber += timElm.getWordNumbers();
				}					
			}
			if(line.startsWith("scene_goals")){
				
				ArrayList<SceneElement> sceGols = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.SCENE_GOAL, currentScene);
				
				if(sceGols == null || sceGols.size() == 0)
					continue;
								
				for(SceneElement scGolElm:sceGols) {
					lineIndex += scGolElm.getWordNumbers();
					SceneGoal nScGol = new SceneGoal(currentScene,scGolElm.getName(), scGolElm._mainWord);
					nScGol.addWord(scGolElm.get_otherWords());
					currentScene.addScene_goal(nScGol);
					wordNumber += scGolElm.getWordNumbers();
				}					
			}
			if(line.startsWith("scene_emotions")){
				
				ArrayList<SceneElement> sceEmos = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.SCENE_EMOTION, currentScene);
				
				if(sceEmos == null || sceEmos.size() == 0)
					continue;
								
				for(SceneElement scEmoElm:sceEmos) {
					lineIndex += scEmoElm.getWordNumbers();
					SceneEmotion nScEmo = new SceneEmotion(currentScene,scEmoElm.getName(), scEmoElm._mainWord);
					nScEmo.addWord(scEmoElm.get_otherWords());
					currentScene.addScene_emotion(nScEmo);
					wordNumber += scEmoElm.getWordNumbers();
				}					
			}				
			if(line.startsWith("role_actions")){
				
				ArrayList<SceneElement> rolActs = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.ROLE_ACTION, currentScene);
				
				if(rolActs == null || rolActs.size() == 0)
					continue;
								
				for(SceneElement rolActElm:rolActs) {
					lineIndex += rolActElm.getWordNumbers();
					RoleAction nRolAct = new RoleAction(currentScene,rolActElm.getName(), rolActElm._mainWord);
					nRolAct.addWord(rolActElm.get_otherWords());
					currentScene.addRoleAction(nRolAct);
					wordNumber += rolActElm.getWordNumbers();
				}					
			}
			if(line.startsWith("role_states")){
				
				ArrayList<SceneElement> rolstats = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.ROLE_STATE, currentScene);
				
				if(rolstats == null || rolstats.size() == 0)
					continue;
								
				for(SceneElement rolStatElm:rolstats) {
					lineIndex += rolStatElm.getWordNumbers();
					RoleState nRolStat = new RoleState(currentScene,rolStatElm.getName(), rolStatElm._mainWord);
					nRolStat.addWord(rolStatElm.get_otherWords());
					currentScene.addRoleState(nRolStat);
					wordNumber += rolStatElm.getWordNumbers();
				}					
			}
			if(line.startsWith("role_intents")){
				
				ArrayList<SceneElement> rolsIntents = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.ROLE_INTENT, currentScene);
				
				if(rolsIntents == null || rolsIntents.size() == 0)
					continue;
								
				for(SceneElement rolIntElm:rolsIntents) {
					lineIndex += rolIntElm.getWordNumbers();
					RoleIntent nRolInt = new RoleIntent(currentScene,rolIntElm.getName(), rolIntElm._mainWord);
					nRolInt.addWord(rolIntElm.get_otherWords());
					currentScene.addRoleIntent(nRolInt);
					wordNumber += rolIntElm.getWordNumbers();
				}					
			}
			if(line.startsWith("role_emotions")){
				
				ArrayList<SceneElement> rolEmos = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.ROLE_EMOTION, currentScene);
				
				if(rolEmos == null || rolEmos.size() == 0)
					continue;
								
				for(SceneElement rolEmoElm:rolEmos) {
					lineIndex += rolEmoElm.getWordNumbers();
					RoleEmotion nRolEmo = new RoleEmotion(currentScene,rolEmoElm.getName(), rolEmoElm._mainWord);
					nRolEmo.addWord(rolEmoElm.get_otherWords());
					currentScene.addRoleEmotion(nRolEmo);
					wordNumber += rolEmoElm.getWordNumbers();
				}					
			}
			if(line.startsWith("dynamic_object_actions")){
				
				ArrayList<SceneElement> dynObjActs = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.DYNAMIC_OBJECT_ACTION, currentScene);
				
				if(dynObjActs == null || dynObjActs.size() == 0)						
					continue;
								
				for(SceneElement dynActElm:dynObjActs) {
					lineIndex += dynActElm.getWordNumbers();
					DynamicObjectAction nDynAct = new DynamicObjectAction(currentScene,dynActElm.getName(), dynActElm._mainWord);
					nDynAct.addWord(dynActElm.get_otherWords());
					currentScene.addObjectAction(nDynAct);
					wordNumber += dynActElm.getWordNumbers();
				}					
			}
			if(line.startsWith("dynamic_object_states")){
				
				ArrayList<SceneElement> dynObjStats = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.DYNAMIC_OBJECT_STATE, currentScene);
				
				if(dynObjStats == null || dynObjStats.size() == 0)						
					continue;
								
				for(SceneElement dynStatElm:dynObjStats) {
					lineIndex += dynStatElm.getWordNumbers();
					DynamicObjectState nDynStat = new DynamicObjectState(currentScene,dynStatElm.getName(), dynStatElm._mainWord);
					nDynStat.addWord(dynStatElm.get_otherWords());
					currentScene.addDynamicObjectState(nDynStat);
					wordNumber += dynStatElm.getWordNumbers();
				}					
			}
			if(line.startsWith("static_object_states")){
				
				ArrayList<SceneElement> statObjStats = generateSceneElements(goldModelLines, ++lineIndex, ScenePart.STATIC_OBJECT_STATE, currentScene);
				
				if(statObjStats == null || statObjStats.size() == 0)						
					continue;
								
				for(SceneElement staStatElm:statObjStats) {
					lineIndex += staStatElm.getWordNumbers();
					StaticObjectState nstaStat = new StaticObjectState(currentScene,staStatElm.getName(), staStatElm._mainWord);
					nstaStat.addWord(staStatElm.get_otherWords());
					currentScene.addStaticObjectState(nstaStat);
					wordNumber += staStatElm.getWordNumbers();
				}
			}				
		}
		print("Gold StoryModels story-num: " + storyNumber +" scene-num: " + sceneNumber + " word-num:" + wordNumber);
		print("583 (junk words) + 200 (no words) " + wordNumber + " (words in gold StoryModels) - 77 (PR words which was calculated twice) = " + (583 + 200 + wordNumber - 77));
		return goldStoryModels;
	}
	
	private ArrayList<SceneElement> generateSceneElements(ArrayList<String> goldModelLines, Integer lineIndex, ScenePart scenePart, SceneModel currentScene) {
		
//		print(scenePart + ":");
		
		if(goldModelLines == null || lineIndex < 0 || lineIndex >= goldModelLines.size() || scenePart == null)
			return null;
		
		ArrayList<SceneElement> sceneElems = new ArrayList<SceneElement>();

		//[1	يونس	N	SBJ	8	يونس§n-23943	نفر§n-13075	role	Arg1 Arg0 	||| ROLE ROLE ]
		String line = goldModelLines.get(lineIndex);
				
		while(line.startsWith("\t")) {
			
//			print(line);
					
			String wordLine = line.substring((line.indexOf("[") + 1), line.indexOf("|||"));
						
			Word currentWord = new Word(wordLine, null);
						
			String predicLine = line.substring((line.indexOf("|||") + 3), (line.length()-2));
			
			String[] parts = predicLine.split("(\t)+");
			if(parts != null)
				for(String pat:parts)
					currentWord.add_predictedSceneElement(pat.trim());
						
			if(line.startsWith("\t\t\t")) {//it means it is an equal
				
			 	SceneElement lastScenElem = sceneElems.get(sceneElems.size() - 1);
				lastScenElem.addWord(currentWord);
			}
			else {
				
				SceneElement scELem = new SceneElement(currentScene, currentWord._wordName, scenePart, currentWord);
				
				sceneElems.add(scELem);			
			}
			
			lineIndex++;
			
			line = goldModelLines.get(lineIndex);
		}
//		print("here");
		return sceneElems;
	}
	
	public void correct_mainWord_otherWords_places(ArrayList<StoryModel> goldStoryModels) {
		if(goldStoryModels == null || goldStoryModels.size() == 0)
			return;
		
		for(StoryModel storyModel: goldStoryModels){
			
			@SuppressWarnings("unused")
			int sceneIndex = 0;
			
//			print("newStory*************************************");
			
			for(SceneModel sceneModel:storyModel.scenes){
				
//				print("newScene"+ (++sceneIndex) + "*************************************");
				
				if(sceneModel.getRoles() != null)					
					for(Role r:sceneModel.getRoles()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}				
				if(sceneModel.getDynamic_objects() != null)					
					for(DynamicObject r:sceneModel.getDynamic_objects()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}
				if(sceneModel.getStatic_objects() != null)					
					for(StaticObject r:sceneModel.getStatic_objects()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}							
					
				if(sceneModel.getLocation() != null) {					
					Location r = sceneModel.getLocation(); 
					if(r._mainWord._gPOS == POS.PR)
						if(r.get_otherWords() != null) {
							
							ArrayList<Word> newOtherWords = new ArrayList<>();
							
							boolean flag = false;
							
							for(Word ow:r.get_otherWords())
								if(ow._gPOS != POS.PR) {
									if(!flag) {
										Word swapWord = r._mainWord;
										r._mainWord = ow;
										r._name = ow._wordName;
										flag = true;
										newOtherWords.add(swapWord);
									}
									else
										newOtherWords.add(ow);
								}
								else
									newOtherWords.add(ow);
							r.set_otherWords(newOtherWords);
						}							
				}
				if(sceneModel.getAlternativeLocations() != null)					
					for(Location r:sceneModel.getAlternativeLocations()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}	
				if(sceneModel.getTime() != null) {					
					Time r = sceneModel.getTime(); 
					if(r._mainWord._gPOS == POS.PR)
						if(r.get_otherWords() != null) {
							
							ArrayList<Word> newOtherWords = new ArrayList<>();
							
							boolean flag = false;
							
							for(Word ow:r.get_otherWords())
								if(ow._gPOS != POS.PR) {
									if(!flag) {
										Word swapWord = r._mainWord;
										r._mainWord = ow;
										r._name = ow._wordName;
										flag = true;
										newOtherWords.add(swapWord);
									}
									else
										newOtherWords.add(ow);
								}
								else
									newOtherWords.add(ow);
							r.set_otherWords(newOtherWords);
						}
				}
				if(sceneModel.getAlternativeTimes() != null)					
					for(Time r:sceneModel.getAlternativeTimes()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}
				if(sceneModel.getScene_goals() != null)					
					for(SceneGoal r:sceneModel.getScene_goals()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}							
				if(sceneModel.getScene_emotions() != null)					
					for(SceneEmotion r:sceneModel.getScene_emotions()) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}
				if(sceneModel.roleActions != null)					
					for(RoleAction r:sceneModel.roleActions) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}
				if(sceneModel.roleStates != null)					
					for(RoleState r:sceneModel.roleStates) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}							
				if(sceneModel.roleIntents != null)					
					for(RoleIntent r:sceneModel.roleIntents) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}							
				if(sceneModel.roleEmotions != null)					
					for(RoleEmotion r:sceneModel.roleEmotions) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}		
				if(sceneModel.object_actions != null)					
					for(DynamicObjectAction r:sceneModel.object_actions) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}
				if(sceneModel.dynamic_object_states != null)					
					for(DynamicObjectState r:sceneModel.dynamic_object_states) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}				
				if(sceneModel.static_object_states != null)					
					for(StaticObjectState r:sceneModel.static_object_states) 
						if(r._mainWord._gPOS == POS.PR)
							if(r.get_otherWords() != null) {
								
								ArrayList<Word> newOtherWords = new ArrayList<>();
								
								boolean flag = false;
								
								for(Word ow:r.get_otherWords())
									if(ow._gPOS != POS.PR) {
										if(!flag) {
											Word swapWord = r._mainWord;
											r._mainWord = ow;
											r._name = ow._wordName;
											flag = true;
											newOtherWords.add(swapWord);
										}
										else
											newOtherWords.add(ow);
									}
									else
										newOtherWords.add(ow);
								r.set_otherWords(newOtherWords);
							}							
					
					
			}
//			print("here");
		}		
	}

	/**
	 * only _mainWord of RoleStates are assessed to find the allocated Role.
	 * because almost all of RoleStates just have one Word; _mainWord; and 
	 * their _otherWords list is empty.  
	 * 
	 * @param allStories
	 */
	public void allocateRoleStates(ArrayList<StoryModel> allStories) {
		
		print("\n>>>>>>>>>>---------- allocation of RoleStates ----------<<<<<<<<<<");
		
		if(Common.isEmpty(allStories))
			return;

		int allRoleStateNums = 0;
		int adjRoleStateNums = 0;
		int corAdjRoleStateNums = 0;
		int mosnadRoleStateNums = 0;
		int corMosnadRoleStateNums = 0;		
		int mozRoleStateNums = 0;		
		int corMozafRoleStateNums = 0;
		int mozElaRoleStateNums = 0;
		int corMozafElaRoleStateNums = 0;
		int NVERoleStateNums = 0;
		int corNVERoleStateNums = 0;
		int chainRoleStateNums = 0;
		int corChainRoleStateNums = 0;
		int correctAllocationNUms = 0;
		int faultAllocationNUms = 0;
		int nonAllocatedNUms = 0;
		
		for(StoryModel storyModel: allStories){
			
//			print("newStory*************************************");
			
			for(SceneModel currentSceneModel:storyModel.scenes){
				
//				print("newScene*************************************");
				
				for(RoleState roleState: currentSceneModel.roleStates){
					 
//					print(roleState._mainWord._sentence + "");
//					print("\nRoleState: " + roleState);
					
					allRoleStateNums++;
					
					Word roleStateWord = roleState._mainWord;
					
					if(roleStateWord == null || roleStateWord._sentence == null) {
						MyError.error("null Word or SentenceMocel for RoleState!!!");
						nonAllocatedNUms++;
						continue;					
					}
					
					Word roleWord = null;					
					
					if(roleStateWord.isAdjective() && roleStateWord.getMosoof() != null) {
						
						Word tempWord = roleStateWord.getMosoof();
						
						if(tempWord._sceneElement == ScenePart.ROLE) {
							roleWord = tempWord;
//							print("111111111111111111111 Adj referent role: " + roleWord._wordName);
							adjRoleStateNums++;							
							
							if(roleWord.equals(roleStateWord._referenceWord))
								corAdjRoleStateNums++;
							
						}						
					}
					//if it is not allocated yet.
					if(roleWord == null) {
					
						if(roleStateWord.hasAnyMozaf_elaihs()) {
							
							ArrayList<Word> mozaf_elaihs = roleStateWord.getMozaf_elaih();
							
							for(Word me: mozaf_elaihs)
								
								if(me._sceneElement == ScenePart.ROLE) {									
									roleWord = me;							
//									print("222222222222222222222 Moz referent role: " + roleWord._wordName);
									
									mozRoleStateNums++;
									
									if(roleWord.equals(roleStateWord._referenceWord))
										corMozafRoleStateNums++;
									
									break;
							}						
						}
					}
					//if it is not allocated yet.
					if(roleWord == null) {
					
						if(roleStateWord.isMozaf_elaih()) {
																				
							Word mozaf = roleStateWord.getMozaf();
															
							if(mozaf != null && mozaf._sceneElement == ScenePart.ROLE) {									
								roleWord = mozaf;					
//								print("333333333333333333333 Moz_elaih referent role: " + roleWord._wordName);															
								mozElaRoleStateNums++;
								
								if(roleWord.equals(roleStateWord._referenceWord))
									corMozafElaRoleStateNums++;
							}
						}
					}
					//if it is not allocated yet.
					if(roleWord == null) {
						if(roleStateWord.isMosnad()) {
							
							SentenceModel RSsent = roleStateWord._sentence;
							
							ArrayList<Word> allSbjs = RSsent.getAllSubjects();
							
							if(allSbjs != null)
								for(Word sbj:allSbjs)
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
										roleWord = sbj;
//										print("444444444444444444444 Mosnad referent role: " + roleWord._wordName);
										mosnadRoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corMosnadRoleStateNums++;
										
										break;
									}
						}
					}	
					//if it is not allocated yet.
					if(roleWord == null) {
						if(roleStateWord._syntaxTag == DependencyRelationType.NVE) {
							
//							print(roleStateWord + "");
							
							Word verb = roleStateWord._sentence.getWord(roleStateWord._srcOfSynTag_number);
							
							
							
							if(verb != null && verb.getObjects() != null) {
								
								for(Word obj:verb.getObjects()) {
									if(obj != null && obj._sceneElement == ScenePart.ROLE) {
										roleWord = obj;										
//										print("555555555555555555555 NVE OBJ referent role: " + roleWord._wordName);
										NVERoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corNVERoleStateNums++;
										break;										
									}
								}								
							}
							
							if(roleWord == null && verb != null && verb.getSubjects() != null) {
								
								for(Word sbj:verb.getSubjects()) {
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
										roleWord = sbj;										
//										print("555555555555555555555 NVE Self SBJ referent role: " + roleWord._wordName);
										NVERoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corNVERoleStateNums++;
										break;										
									}
								}								
							}
							
							if(roleWord == null && roleStateWord._sentence.getAllSubjects() != null) {
							
								for(Word sbj:roleStateWord._sentence.getAllSubjects()) {
									
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
										roleWord = sbj;										
//										print("555555555555555555555 NVE All SBJ referent role: " + roleWord._wordName);
										NVERoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corNVERoleStateNums++;
										break;										
									}
								}								
							}
						}
					}
					//if it is not allocated yet.
					if(roleWord == null) {
					
						Phrase rsPhrase = roleStateWord._phrase;							
						
						if(rsPhrase != null && rsPhrase.get_words() != null && rsPhrase.get_words().size() != 0) {
							
//							print("RoleState _phrase: " + rsPhrase);
							
							for(Word phW:rsPhrase.get_words()) {									
							
								if(phW._sceneElement == ScenePart.ROLE) {
									roleWord = phW;
									chainRoleStateNums++;
//									print("666666666666666666666 chain referent role: " + roleWord._wordName);
																																					
									if(roleWord.equals(roleStateWord._referenceWord))
										corChainRoleStateNums++;
									
									break;
								}
							}
						}
					}			
					
					if(roleWord != null) {
						
						Role role = currentSceneModel.getRole(roleWord);
						
						if(role != null) {
							role.addRole_state(roleState);
//							roleState.set_owningRole(role);
						}							
					}
					
					//TODO: these line and all ifs which checks roleStateWord._referenceWord must be deleted!
					//these are only for checking phase
					if(roleWord != null && roleWord.equals(roleStateWord._referenceWord)) {
						correctAllocationNUms++;
//							print("##################### correct allocation for " + roleStateWord._wordName + " role: " + roleWord._wordName);
//							print("referent role: " + roleWord._wordName);
					}
					else if(roleWord != null) {
						faultAllocationNUms++;
//							print("===================== fault allocation for " + roleStateWord._wordName + " role: " + roleWord._wordName);
//							print("referent role: " + roleWord._wordName);
					}
					else {
						nonAllocatedNUms++;
//						print("))))))))))))))))))))) not allocated: " + roleState);
					}					
				}
			}
		}
		
		print("\nall RoleStateNumbers: " + allRoleStateNums);
		print("Adj RoleStateNumbers: " + adjRoleStateNums + " which " + corAdjRoleStateNums + " are correct: " + ((corAdjRoleStateNums*1.0*100)/adjRoleStateNums));
		print("Moz RoleStateNumbers: " + mozRoleStateNums + " which " + corMozafRoleStateNums + " are correct: " + ((corMozafRoleStateNums*1.0*100)/mozRoleStateNums));
		print("MozEla RoleStateNumbers: " + mozElaRoleStateNums + " which " + corMozafElaRoleStateNums + " are correct: " + ((corMozafElaRoleStateNums*1.0*100)/mozElaRoleStateNums));
		print("Mosnad RoleStateNumbers: " + mosnadRoleStateNums  + " which " + corMosnadRoleStateNums + " are correct: " + ((corMosnadRoleStateNums*1.0*100)/mosnadRoleStateNums));
		print("NVE RoleStateNumbers: " + NVERoleStateNums  + " which " + corNVERoleStateNums + " are correct: " + ((corNVERoleStateNums*1.0*100)/NVERoleStateNums));
		print("chain RoleStateNumbers: " + chainRoleStateNums + " which " + corChainRoleStateNums + " are correct: " + ((corChainRoleStateNums*1.0*100)/chainRoleStateNums));
		int allocatedNums = adjRoleStateNums + mozRoleStateNums + mozElaRoleStateNums + mosnadRoleStateNums + NVERoleStateNums +chainRoleStateNums;
		print("allocated RoleStateNumbers: " + allocatedNums);		
		print("non-allocated RoleStateNumbers: " + nonAllocatedNUms);		
		print("\ncorrect allocations: " + correctAllocationNUms + " and fault allocations: " + faultAllocationNUms + " from: " + allocatedNums + " allocations.");
		print("correct alloation in allocated RoleStates: " + ((correctAllocationNUms*1.0)/(correctAllocationNUms+faultAllocationNUms))*100 + "%");
		print("correct alloation in all RoleStates: " + ((correctAllocationNUms*1.0)/allRoleStateNums)*100 + "%");
		
	}

	/**
	 * only _mainWord of RoleStates are assessed to find the allocated Role.
	 * because almost all of RoleStates just have one Word; _mainWord; and 
	 * their _otherWords list is empty.
	 * 
	 * 	در این مرجله چون میخواهیم که تست کنیم که کدوم روش برای پیدا کردن تخصیص بهتر جواب میده. در هر لول با جواب اصلی که در
	 * _referenceWord
	 * هست مقایسه کردیم که ببینیم جواب درست بوده یا نه. 
	 * والا که باید این مقایسه ها رو برداریم چون فرض بر این نیست که جواب درست رو در 
	 * _referenceWord
	 * داریم!!!
	 * @param allStories
	 */	
	public void allocateRoleStatesWithChecking(ArrayList<StoryModel> allStories) {
		
		print("\n>>>>>>>>>>---------- allocation of RoleStates ----------<<<<<<<<<<");
		
		if(Common.isEmpty(allStories))
			return;

		int allRoleStateNums = 0;
		int adjRoleStateNums = 0;
		int corAdjRoleStateNums = 0;
		int mosnadRoleStateNums = 0;
		int corMosnadRoleStateNums = 0;		
		int mozRoleStateNums = 0;		
		int corMozafRoleStateNums = 0;
		int mozElaRoleStateNums = 0;
		int corMozafElaRoleStateNums = 0;
		int NVERoleStateNums = 0;
		int corNVERoleStateNums = 0;		
		int chainRoleStateNums = 0;
		int corChainRoleStateNums = 0;
		int correctAllocationNUms = 0;
		int faultAllocationNUms = 0;
		int nonAllocatedNUms = 0;
		
		for(StoryModel storyModel: allStories){
			
//			print("newStory*************************************");
			
			for(SceneModel currentSceneModel:storyModel.scenes){
				
//				print("newScene*************************************");
				
				for(RoleState roleState: currentSceneModel.roleStates){
					 
//					print(roleState._mainWord._sentence + "");
//					print("\nRoleState: " + roleState);
					
					allRoleStateNums++;
					
					Word roleStateWord = roleState._mainWord;
					
					if(roleStateWord == null) {
						MyError.error("null Word for RoleState!!!");
						nonAllocatedNUms++;
						continue;					
					}
					
					Word roleWord = null;					
					
					if(roleStateWord.isAdjective() && roleStateWord.getMosoof() != null) {
						
						Word tempWord = roleStateWord.getMosoof();
						
						if(tempWord._sceneElement == ScenePart.ROLE) {
							roleWord = tempWord;							
//							print("$$$$$$$$$$$$$$$$$$$$$ Adj referent role: " + roleWord._wordName);
							adjRoleStateNums++;
							
							if(roleWord.equals(roleStateWord._referenceWord))
								corAdjRoleStateNums++;
						}						
					}
					//it is not allocated or it has a fault allocation!
					if(roleWord == null || (roleWord != null && !roleWord.equals(roleStateWord._referenceWord))) {
					
						if(roleStateWord.hasAnyMozaf_elaihs()) {
							
							ArrayList<Word> mozaf_elaihs = roleStateWord.getMozaf_elaih();
							
							for(Word me: mozaf_elaihs)
								if(me._sceneElement == ScenePart.ROLE) {									
									roleWord = me;							
//									print(">>>>>>>>>>>>>>>>>>>>> Moz referent role: " + roleWord._wordName);
									mozRoleStateNums++;
																		
									if(roleWord.equals(roleStateWord._referenceWord))
										corMozafRoleStateNums++;
									
									break;
							}						
						}
					}
					//it is not allocated or it has a fault allocation!
					if(roleWord == null || (roleWord != null && !roleWord.equals(roleStateWord._referenceWord))) {
					
						if(roleStateWord.isMozaf_elaih()) {
							
							print(roleStateWord + "");
							
							Word mozaf = roleStateWord.getMozaf();
															
							if(mozaf != null && mozaf._sceneElement == ScenePart.ROLE) {									
								roleWord = mozaf;					
//								print("333333333333333333333 Moz_elaih referent role: " + roleWord._wordName);															
								mozElaRoleStateNums++;
								
								if(roleWord.equals(roleStateWord._referenceWord))
									corMozafElaRoleStateNums++;
							}
						}
					}
					//it is not allocated or it has a fault allocation!
					if(roleWord == null || (roleWord != null && !roleWord.equals(roleStateWord._referenceWord))) {
						if(roleStateWord.isMosnad()) {
							
							SentenceModel RSsent = roleStateWord._sentence;
							
							ArrayList<Word> allSbjs = RSsent.getAllSubjects();
							
							if(allSbjs != null)
								for(Word sbj:allSbjs)
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
										roleWord = sbj;
//										print("********************* Mosnad referent role: " + roleWord._wordName);
										mosnadRoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corMosnadRoleStateNums++;
										
										break;
									}
						}
					}						
					//it is not allocated or it has a fault allocation!
					if(roleWord == null || (roleWord != null && !roleWord.equals(roleStateWord._referenceWord))) {
						if(roleStateWord._syntaxTag == DependencyRelationType.NVE) {
							
//							print(roleStateWord + "");
							
							Word verb = roleStateWord._sentence.getWord(roleStateWord._srcOfSynTag_number);
														
							if(verb != null && verb.getObjects() != null) {
								
								for(Word obj:verb.getObjects()) {
									if(obj != null && obj._sceneElement == ScenePart.ROLE) {
										roleWord = obj;										
//										print("555555555555555555555 NVE OBJ referent role: " + roleWord._wordName);
										NVERoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corNVERoleStateNums++;
										break;										
									}
								}								
							}
							
							if(roleWord == null && verb != null && verb.getSubjects() != null) {
								
								for(Word sbj:verb.getSubjects()) {
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
										roleWord = sbj;										
//										print("555555555555555555555 NVE Self SBJ referent role: " + roleWord._wordName);
										NVERoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corNVERoleStateNums++;
										break;										
									}
								}								
							}
							
							if(roleWord == null && roleStateWord._sentence.getAllSubjects() != null) {
							
								for(Word sbj:roleStateWord._sentence.getAllSubjects()) {
									
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
										roleWord = sbj;										
//										print("555555555555555555555 NVE All SBJ referent role: " + roleWord._wordName);
										NVERoleStateNums++;
										
										if(roleWord.equals(roleStateWord._referenceWord))
											corNVERoleStateNums++;
										break;										
									}
								}								
							}
						}
					}
					//it is not allocated or it has a fault allocation!
					if(roleWord == null || (roleWord != null && !roleWord.equals(roleStateWord._referenceWord))) {
					
						Phrase rsPhrase = roleStateWord._phrase;							
						
						if(rsPhrase != null && rsPhrase.get_words() != null && rsPhrase.get_words().size() != 0) {
							
//							print("RoleState _phrase: " + rsPhrase);
							
							for(Word phW:rsPhrase.get_words()) {									
							
								if(phW._sceneElement == ScenePart.ROLE) {
									roleWord = phW;
									chainRoleStateNums++;
//									print("@@@@@@@@@@@@@@@@@@@@@ chain referent role: " + roleWord._wordName);
									
									if(roleWord.equals(roleStateWord._referenceWord))
										corChainRoleStateNums++;
									
									break;
								}
							}
						}
					}			
					
					if(roleWord != null) {
						Role role = currentSceneModel.getRole(roleWord);
						if(role != null) {
							role.addRole_state(roleState);
//							roleState.set_owningRole(role);
						}							
					}
					
					//TODO: these line and all ifs which checks roleStateWord._referenceWord must be deleted!
					//these are only for checking phase
					if(roleWord != null && roleWord.equals(roleStateWord._referenceWord)) {
						correctAllocationNUms++;
//							print("##################### correct allocation for " + roleStateWord._wordName + " role: " + roleWord._wordName);
//							print("referent role: " + roleWord._wordName);
					}
					else if(roleWord != null) {
						faultAllocationNUms++;
//							print("===================== fault allocation for " + roleStateWord._wordName + " role: " + roleWord._wordName);
//							print("referent role: " + roleWord._wordName);
					}
					else {
						nonAllocatedNUms++;
	//					print("))))))))))))))))))))) not allocated: " + roleState);
					}					
				}
			}
		}
		
		print("\nall RoleStateNumbers: " + allRoleStateNums);
		print("Adj RoleStateNumbers: " + adjRoleStateNums + " which " + corAdjRoleStateNums + " are correct: " + ((corAdjRoleStateNums*1.0*100)/adjRoleStateNums));
		print("Moz RoleStateNumbers: " + mozRoleStateNums + " which " + corMozafRoleStateNums + " are correct: " + ((corMozafRoleStateNums*1.0*100)/mozRoleStateNums));
		print("MozEla RoleStateNumbers: " + mozElaRoleStateNums + " which " + corMozafElaRoleStateNums + " are correct: " + ((corMozafElaRoleStateNums*1.0*100)/mozElaRoleStateNums));
		print("Mosnad RoleStateNumbers: " + mosnadRoleStateNums  + " which " + corMosnadRoleStateNums + " are correct: " + ((corMosnadRoleStateNums*1.0*100)/mosnadRoleStateNums));
		print("NVE RoleStateNumbers: " + NVERoleStateNums  + " which " + corNVERoleStateNums + " are correct: " + ((corNVERoleStateNums*1.0*100)/NVERoleStateNums));
		print("chain RoleStateNumbers: " + chainRoleStateNums + " which " + corChainRoleStateNums + " are correct: " + ((corChainRoleStateNums*1.0*100)/chainRoleStateNums));
		int allocatedNums = adjRoleStateNums + mozRoleStateNums + mozElaRoleStateNums + mosnadRoleStateNums + NVERoleStateNums +chainRoleStateNums;
		print("allocated RoleStateNumbers: " + allocatedNums);		
		print("non-allocated RoleStateNumbers: " + nonAllocatedNUms);		
		print("\ncorrect allocations: " + correctAllocationNUms + " and fault allocations: " + faultAllocationNUms + " from: " + allocatedNums + " allocations.");
		print("correct alloation in allocated RoleStates: " + ((correctAllocationNUms*1.0)/(correctAllocationNUms+faultAllocationNUms))*100 + "%");
		print("correct alloation in all RoleStates: " + ((correctAllocationNUms*1.0)/allRoleStateNums)*100 + "%");
		
	}

	
	/**
	 * only _mainWord of RoleActions are assessed to find the allocated Role.
	 * because almost all of RoleActions just have one Word; _mainWord; and 
	 * their _otherWords list is empty.
	 */
	public void allocateRoleActions(ArrayList<StoryModel> allStories) {
		
		print("\n>>>>>>>>>>---------- allocation of RoleActions ----------<<<<<<<<<<");
		
		if(Common.isEmpty(allStories))
			return;

		int allRoleActionNums = 0;
		int selfSbjRoleActionNums = 0;
		int corSelfSbjRoleActionNums = 0;
		int selfPhraseSbjRoleActionNums = 0;
		int corSelfPhraseSbjRoleActionNums = 0;		
		int allSbjCurrRoleActionNums = 0;		
		int corAllSbjCurrRoleActionNums = 0;
		int allSbjPastRoleActionNums = 0;
		int corAllSbjPastRoleActionNums = 0;
		int correctAllocationNUms = 0;
		int faultAllocationNUms = 0;
		int nonAllocatedNums = 0;
		
		for(StoryModel storyModel: allStories){
			
//			print("newStory*************************************");
			
			for(SceneModel currentSceneModel:storyModel.scenes){
				
//				print("newScene*************************************");
				
				for(RoleAction roleAction: currentSceneModel.roleActions){
					 
//					print(roleAction._mainWord._sentece + "");
					print("\nRoleAction: " + roleAction);
					
					allRoleActionNums++;
					
					Word roleActionWord = roleAction._mainWord;
					
					if(roleActionWord == null) {
						MyError.error("null Word for RoleAction!!!");
						nonAllocatedNums++;
						continue;					
					}
					
					Word roleWord = null;
					
					if(roleActionWord.isVerb() && roleActionWord.getSubjects() != null && roleActionWord.getSubjects().size() != 0) {
						
						ArrayList<Word> allSbjs = roleActionWord.getSubjects();
						
						if(allSbjs != null)							
							for(Word sbj:allSbjs)
								
								if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
							
									roleWord = sbj;									
									print("111111111111111111111 self Sbj referent role: " + roleWord._wordName);
									selfSbjRoleActionNums++;
									
									if(roleWord.equals(roleActionWord._referenceWord))
										corSelfSbjRoleActionNums++;
									
									break;
								}
								else {
									if(sbj != null && sbj._phrase != null) {
										
										ArrayList<Word> phraseWords = sbj._phrase.get_words();
										
										if(phraseWords == null)
											continue;
										
										for(Word ph_w: phraseWords)
											if(ph_w != null && ph_w._sceneElement == ScenePart.ROLE) {
												
												roleWord = ph_w;												
//												print("222222222222222222222 phrase Self Sbj referent role: " + roleWord._wordName);
												
												selfPhraseSbjRoleActionNums++;
												
												if(roleWord.equals(roleActionWord._referenceWord))
													corSelfPhraseSbjRoleActionNums++;
												
												break;	
											}										
									}									
								}				
					}
					
					SentenceModel owningSentence = roleActionWord._sentence;
					
					//if it is not allocated yet.
					if(roleWord == null) {
											
						if(owningSentence != null && owningSentence.hasSubject()) { 						
							
							ArrayList<Word> allSbjWords = owningSentence.getAllSubjects();
							
							if(allSbjWords != null)
								for(Word sbjWord:allSbjWords)						
								
									if(sbjWord != null && sbjWord._sceneElement == ScenePart.ROLE) {
										
										roleWord = sbjWord;
//										print("333333333333333333333 allSbjs currSent referent role: " + roleWord._wordName);
																				
										allSbjCurrRoleActionNums++;
																				
										if(roleWord.equals(roleActionWord._referenceWord))
											corAllSbjCurrRoleActionNums++;
																				
										break;
									}							
						}
					}
					//if it is not allocated yet.
					if(roleWord == null) {
						
							SentenceModel pastSent = currentSceneModel.getPastSentence(owningSentence);
							
							if(pastSent == null || !pastSent.hasSubject())
								pastSent = storyModel.getPastSentence(owningSentence);
							
							if(pastSent != null && pastSent.hasSubject()){
								
								ArrayList<Word> allSbjWords = pastSent.getAllSubjects();
								
								if(allSbjWords != null)
									for(Word sbjWord:allSbjWords)						
									
										if(sbjWord != null && sbjWord._sceneElement == ScenePart.ROLE) {
											
											roleWord = sbjWord;
//											print("444444444444444444444 allSbjs pastSent referent role: " + roleWord._wordName);
																						
											allSbjPastRoleActionNums++;
																					
											if(roleWord.equals(roleActionWord._referenceWord))
												corAllSbjPastRoleActionNums++;
																					
											break;
										}		
							}							
					}
					
					if(roleWord != null) {
						Role role = currentSceneModel.getRole(roleWord);

						if(role != null) {
							role.addRole_action(roleAction);
//							roleAction.set_owningRole(role);
						}
					}
					
					//TODO: these line and all ifs which checks roleActionWord._referenceWord must be deleted!
					//these are only for checking phase					
					if(roleWord != null && roleWord.equals(roleActionWord._referenceWord)) {
						correctAllocationNUms++;
						print("##################### correct allocation for " + roleActionWord._wordName + " role: " + roleWord._wordName);					
					}
					else if(roleWord != null) {
						faultAllocationNUms++;
						print("===================== fault allocation for " + roleActionWord._wordName + " role: " + roleWord._wordName);							
					}
					else {
						nonAllocatedNums++;
//						print("))))))))))))))))))))) not allocated: " + roleAction);
					}					
				}
			}
		}		
		
		print("\nall RoleActionNumbers: " + allRoleActionNums);
		print("Self sbj RoleActionNumbers: " + selfSbjRoleActionNums + " which " + corSelfSbjRoleActionNums + " are correct: " + ((corSelfSbjRoleActionNums*1.0*100)/selfSbjRoleActionNums));
		print("Self phrase sbj RoleActionNumbers: " + selfPhraseSbjRoleActionNums + " which " + corSelfPhraseSbjRoleActionNums + " are correct: " + ((corSelfPhraseSbjRoleActionNums*1.0*100)/selfPhraseSbjRoleActionNums));
		print("All sbjs of currentSent RoleActionNumbers: " + allSbjCurrRoleActionNums + " which " + corAllSbjCurrRoleActionNums + " are correct: " + ((corAllSbjCurrRoleActionNums*1.0*100)/allSbjCurrRoleActionNums));
		print("All sbjs of pastSent RoleActionNumbers: " + allSbjPastRoleActionNums + " which " + corAllSbjPastRoleActionNums + " are correct: " + ((corAllSbjPastRoleActionNums*1.0*100)/allSbjPastRoleActionNums));
		int allocatedNum = selfSbjRoleActionNums + selfPhraseSbjRoleActionNums + allSbjCurrRoleActionNums + allSbjPastRoleActionNums;
		print("allocated RoleActionNumbers: " + allocatedNum);
		print("non-allocated RoleActionNumbers: " + nonAllocatedNums);		
		print("\ncorrect allocations: " + correctAllocationNUms + " and fault allocations: " + faultAllocationNUms + " from: " + allocatedNum + " allocations.");
		print("correct alloation in allocated RoleActions: " + ((correctAllocationNUms*1.0)/(correctAllocationNUms+faultAllocationNUms))*100 + "%");
		print("correct alloation in all RoleAction: " + ((correctAllocationNUms*1.0)/allRoleActionNums)*100 + "%");
		
	}
	
	/**
	 * only _mainWord of RoleActions are assessed to find the allocated Role.
	 * because almost all of RoleActions just have one Word; _mainWord; and 
	 * their _otherWords list is empty.
	 *  
	 * در این مرجله چون میخواهیم که تست کنیم که کدوم روش برای پیدا کردن تخصیص بهتر جواب میده. در هر لول با جواب اصلی که در
	 * _referenceWord
	 * هست مقایسه کردیم که ببینیم جواب درست بوده یا نه. 
	 * والا که باید این مقایسه ها رو برداریم چون فرض بر این نیست که جواب درست رو در 
	 * _referenceWord
	 * داریم!!!
	 */
	public void allocateRoleActionsWithChecking(ArrayList<StoryModel> allStories) {
			
			print("\n>>>>>>>>>>---------- allocation of RoleActions ----------<<<<<<<<<<");
			
			if(Common.isEmpty(allStories))
				return;
	
			int allRoleActionNums = 0;
			int selfSbjRoleActionNums = 0;
			int corSelfSbjRoleActionNums = 0;
			int selfPhraseSbjRoleActionNums = 0;
			int corSelfPhraseSbjRoleActionNums = 0;		
			int allSbjCurrRoleActionNums = 0;		
			int corAllSbjCurrRoleActionNums = 0;
			int allSbjPastRoleActionNums = 0;
			int corAllSbjPastRoleActionNums = 0;
			int correctAllocationNUms = 0;
			int faultAllocationNUms = 0;
			int nonAllocatedNums = 0;
			
			for(StoryModel storyModel: allStories){
				
	//				print("newStory*************************************");
				
				for(SceneModel currentSceneModel:storyModel.scenes){
					
	//					print("newScene*************************************");
					
					for(RoleAction roleAction: currentSceneModel.roleActions){
						 
	//						print(roleAction._mainWord._sentece + "");
//						print("\nRoleAction: " + roleAction);
						
						allRoleActionNums++;
						
						Word roleActionWord = roleAction._mainWord;
						
						if(roleActionWord == null) {
							MyError.error("null Word for RoleAction!!!");
							nonAllocatedNums++;
							continue;					
						}
						
						Word roleWord = null;
						
						if(roleActionWord.isVerb() && roleActionWord.getSubjects() != null && roleActionWord.getSubjects().size() != 0) {
							
							ArrayList<Word> allSbjs = roleActionWord.getSubjects();
							
							if(allSbjs != null)							
								for(Word sbj:allSbjs)
									
									if(sbj != null && sbj._sceneElement == ScenePart.ROLE) {
								
										roleWord = sbj;									
//										print("111111111111111111111 self Sbj referent role: " + roleWord._wordName);
										selfSbjRoleActionNums++;
										
										if(roleWord.equals(roleActionWord._referenceWord))
											corSelfSbjRoleActionNums++;
										
										break;
									}
									else {
										if(sbj != null && sbj._phrase != null) {
											
											ArrayList<Word> phraseWords = sbj._phrase.get_words();
											
											if(phraseWords == null)
												continue;
											
											for(Word ph_w: phraseWords)
												if(ph_w != null && ph_w._sceneElement == ScenePart.ROLE) {
													
													roleWord = ph_w;												
//													print("222222222222222222222 phrase Self Sbj referent role: " + roleWord._wordName);
													
													selfPhraseSbjRoleActionNums++;
													
													if(roleWord.equals(roleActionWord._referenceWord))
														corSelfPhraseSbjRoleActionNums++;
													
													break;	
												}										
										}									
									}				
						}
						
						SentenceModel owningSentence = roleActionWord._sentence;
						
						//it is not allocated or it has a fault allocation!
						if(roleWord == null || (roleWord != null && !roleWord.equals(roleActionWord._referenceWord))) {
												
							if(owningSentence != null && owningSentence.hasSubject()) { 						
								
								ArrayList<Word> allSbjWords = owningSentence.getAllSubjects();
								
								if(allSbjWords != null)
									for(Word sbjWord:allSbjWords)						
									
										if(sbjWord != null && sbjWord._sceneElement == ScenePart.ROLE) {
											
											roleWord = sbjWord;
//											print("333333333333333333333 allSbjs currSent referent role: " + roleWord._wordName);
																					
											allSbjCurrRoleActionNums++;
																					
											if(roleWord.equals(roleActionWord._referenceWord))
												corAllSbjCurrRoleActionNums++;
																					
											break;
										}							
							}
						}
						//it is not allocated or it has a fault allocation!
						if(roleWord == null || (roleWord != null && !roleWord.equals(roleActionWord._referenceWord))) {
							
								SentenceModel pastSent = currentSceneModel.getPastSentence(owningSentence);
								
								if(pastSent == null || !pastSent.hasSubject())
									pastSent = storyModel.getPastSentence(owningSentence);
								
								if(pastSent != null && pastSent.hasSubject()){
									
									ArrayList<Word> allSbjWords = pastSent.getAllSubjects();
									
									if(allSbjWords != null)
										for(Word sbjWord:allSbjWords)						
										
											if(sbjWord != null && sbjWord._sceneElement == ScenePart.ROLE) {
												
												roleWord = sbjWord;
//												print("444444444444444444444 allSbjs pastSent referent role: " + roleWord._wordName);
																							
												allSbjPastRoleActionNums++;
																						
												if(roleWord.equals(roleActionWord._referenceWord))
													corAllSbjPastRoleActionNums++;
																						
												break;
											}		
								}							
						}			
						
						if(roleWord != null) {
							Role role = currentSceneModel.getRole(roleWord);
	
							if(role != null) {
								role.addRole_action(roleAction);
	//								roleAction.set_owningRole(role);
							}
						}
						
						//TODO: these line and all ifs which checks roleActionWord._referenceWord must be deleted!
						//these are only for checking phase					
						if(roleWord != null && roleWord.equals(roleActionWord._referenceWord)) {
							correctAllocationNUms++;
//							print("##################### correct allocation for " + roleActionWord._wordName + " role: " + roleWord._wordName);					
						}
						else if(roleWord != null) {
							faultAllocationNUms++;
//							print("===================== fault allocation for " + roleActionWord._wordName + " role: " + roleWord._wordName);							
						}
						else {
							nonAllocatedNums++;
//							print("))))))))))))))))))))) not allocated: " + roleAction);
						}					
					}
				}
			}

		
		print("\nall RoleActionNumbers: " + allRoleActionNums);
		print("Self sbj RoleActionNumbers: " + selfSbjRoleActionNums + " which " + corSelfSbjRoleActionNums + " are correct: " + ((corSelfSbjRoleActionNums*1.0*100)/selfSbjRoleActionNums));
		print("Self phrase sbj RoleActionNumbers: " + selfPhraseSbjRoleActionNums + " which " + corSelfPhraseSbjRoleActionNums + " are correct: " + ((corSelfPhraseSbjRoleActionNums*1.0*100)/selfPhraseSbjRoleActionNums));
		print("All sbjs of currentSent RoleActionNumbers: " + allSbjCurrRoleActionNums + " which " + corAllSbjCurrRoleActionNums + " are correct: " + ((corAllSbjCurrRoleActionNums*1.0*100)/allSbjCurrRoleActionNums));
		print("All sbjs of pastSent RoleActionNumbers: " + allSbjPastRoleActionNums + " which " + corAllSbjPastRoleActionNums + " are correct: " + ((corAllSbjPastRoleActionNums*1.0*100)/allSbjPastRoleActionNums));
		int allocatedNum = selfSbjRoleActionNums + selfPhraseSbjRoleActionNums + allSbjCurrRoleActionNums + allSbjPastRoleActionNums;
		print("allocated RoleActionNumbers: " + allocatedNum);
		print("non-allocated RoleActionNumbers: " + nonAllocatedNums);		
		print("\ncorrect allocations: " + correctAllocationNUms + " and fault allocations: " + faultAllocationNUms + " from: " + allocatedNum + " allocations.");
		print("correct alloation in allocated RoleActions: " + ((correctAllocationNUms*1.0)/(correctAllocationNUms+faultAllocationNUms))*100 + "%");
		print("correct alloation in all RoleAction: " + ((correctAllocationNUms*1.0)/allRoleActionNums)*100 + "%");
		
	}
	
	private void print(String toPrint){
		System.out.println(toPrint);		
	}	
}



