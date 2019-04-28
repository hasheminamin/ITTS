package sceneReasoner;

import ir.ac.itrc.qqa.semantic.util.MyError;

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
import sceneElement.SceneEmotion;
import sceneElement.SceneGoal;
import sceneElement.StaticObject;
import sceneElement.StaticObjectState;
import sceneElement.Time;
import enums.ScenePart;
import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;
import model.Word;

public class SceneReasoner {

	
public void arrangeWordReferences(ArrayList<StoryModel> allStories) {
		
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
									referecneScene = storyModel.scenes.get(sceneIndex - 1);
									path = path.replace("*", "");
									
									if(path.contains("*")){
										referecneScene = storyModel.scenes.get(sceneIndex - 2);
										path = path.replace("*", "");
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
															
							word.set_referenceWord(referenceSentence.getWord(referenceWordNumber));
						}						
					}
				}
			}
		}
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
			PrintWriter writer = new PrintWriter("dataset/98-02-07story2Scenes.arff","utf-8");
					
			int wordNum = 0;
			
			for(StoryModel storyModel: allStories){
				
				print("newStory*************************************");
				writer.write("newStory*************************************\n");
				
				for(SceneModel sceneModel:storyModel.scenes){
					
					print("newScene*************************************");
					writer.write("newScene*************************************\n");
					
					for(SentenceModel sentence: sceneModel.sentences){
							
						print("");
						writer.write("\n");
						
						for(Word word: sentence.getWords()){
							wordNum++;
							
							print("" + word);
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
					print("" + sceneModel + "\n");
					writer.write("" + sceneModel + "\n\n");
				}
			}
			print("total wordNum: " + wordNum);
			writer.close();
		}
		catch(Exception e){
			print("" + e);
		}
	}
	
	public void completeSceneModelsElements(ArrayList<StoryModel> allStories) {
		
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
								roleAction.setActor(refRole);
								continue;
							}							
						}
					}
					if(roleAction.getWords() == null || roleAction.getWords().size() == 0)
						continue;
										
					for(Word roleActionWord: roleAction.getWords()){
					
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
						roleAction.setActor(refRole);
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
					
		for(StoryModel stry:allStories)			
			stry.calculateRepeatedWords();		
				
	}
	
	private void print(String toPrint){
		System.out.println(toPrint);		
	}	
}
