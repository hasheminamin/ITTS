package evaluation;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.ac.itrc.qqa.semantic.util.MyError;
import model.SceneModel;
import model.StoryModel;
import model.Word;
import sceneElement.DynamicObject;
import sceneElement.DynamicObjectAction;
import sceneElement.DynamicObjectState;
import sceneElement.Location;
import sceneElement.Role;
import sceneElement.RoleAction;
import sceneElement.RoleEmotion;
import sceneElement.RoleIntent;
import sceneElement.RoleState;
import sceneElement.SceneElement;
import sceneElement.StaticObject;
import sceneElement.StaticObjectState;
import sceneElement.Time;

public class Evaluator {
	
	ArrayList<StoryModel> goldStoryModels = null;
	
	int allWordsNumber = 1203;
	
	
	public Evaluator(ArrayList<StoryModel> goldStoryModels){
		
		this.goldStoryModels = goldStoryModels;
		
		if(goldStoryModels == null || goldStoryModels.size() == 0)
			MyError.exit("Gold StoryModels of the Evaluator class should not be null!!!");
	}	
	
	public void evaluateAllSceneElementPrimaryAllocation(ArrayList<StoryModel> allocatedStoryModels) {
	
		print("\n\n>>>>>>>>>-------- evaluate primary allocation of all sceneElements in StoryModels --------<<<<<<<<<");
		
		if(allocatedStoryModels == null || allocatedStoryModels.size() == 0)
			return;
		
		ArrayList<Word> roleTP = new ArrayList<Word>();
		ArrayList<Word> roleFN = new ArrayList<Word>();		
		ArrayList<Word> roleFP = new ArrayList<Word>();
		int roleClassifiedAsRoleWord_nums = 0;
		int roleTN = 0;
		
		ArrayList<Word> dynTP = new ArrayList<Word>();
		ArrayList<Word> dynFN = new ArrayList<Word>();		
		ArrayList<Word> dynFP = new ArrayList<Word>();
		int dynClassifiedAsDynWord_nums = 0;
		int dynTN = 0;
		
		ArrayList<Word> statTP = new ArrayList<Word>();
		ArrayList<Word> statFN = new ArrayList<Word>();		
		ArrayList<Word> statFP = new ArrayList<Word>();
		int statClassifiedAsStatWord_nums = 0;
		int statTN = 0;
		
		ArrayList<Word> altLocTP = new ArrayList<Word>();
		ArrayList<Word> altLocFN = new ArrayList<Word>();		
		ArrayList<Word> altLocFP = new ArrayList<Word>();
		int altLocClassifiedAsAltLocWord_nums = 0;
		int altLocTN = 0;
		
		ArrayList<Word> altTimTP = new ArrayList<Word>();
		ArrayList<Word> altTimFN = new ArrayList<Word>();		
		ArrayList<Word> altTimFP = new ArrayList<Word>();
		int altTimClassifiedAsAltTimWord_nums = 0;
		int altTimTN = 0;
		
		ArrayList<Word> roleActTP = new ArrayList<Word>();
		ArrayList<Word> roleActFN = new ArrayList<Word>();		
		ArrayList<Word> roleActFP = new ArrayList<Word>();
		int roleActClassifiedAsRoleActWord_nums = 0;
		int roleActTN = 0;
		
		ArrayList<Word> roleStatTP = new ArrayList<Word>();
		ArrayList<Word> roleStatFN = new ArrayList<Word>();		
		ArrayList<Word> roleStatFP = new ArrayList<Word>();
		int roleStatClassifiedAsRoleStatWord_nums = 0;
		int roleStatTN = 0;
		
		ArrayList<Word> roleIntTP = new ArrayList<Word>();
		ArrayList<Word> roleIntFN = new ArrayList<Word>();		
		ArrayList<Word> roleIntFP = new ArrayList<Word>();
		int roleIntClassifiedAsRoleIntWord_nums = 0;
		int roleIntTN = 0;
		
		ArrayList<Word> roleEmoTP = new ArrayList<Word>();
		ArrayList<Word> roleEmoFN = new ArrayList<Word>();		
		ArrayList<Word> roleEmoFP = new ArrayList<Word>();
		int roleEmoClassifiedAsRoleEmoWord_nums = 0;
		int roleEmoTN = 0;
		
		ArrayList<Word> dynActTP = new ArrayList<Word>();
		ArrayList<Word> dynActFN = new ArrayList<Word>();		
		ArrayList<Word> dynActFP = new ArrayList<Word>();
		int dynActClassifiedAsDynActWord_nums = 0;
		int dynActTN = 0;
		
		ArrayList<Word> dynStatTP = new ArrayList<Word>();
		ArrayList<Word> dynStatFN = new ArrayList<Word>();		
		ArrayList<Word> dynStatFP = new ArrayList<Word>();
		int dynStatClassifiedAsDynStatWord_nums = 0;
		int dynStatTN = 0;
		
		ArrayList<Word> stStatTP = new ArrayList<Word>();
		ArrayList<Word> stStatFN = new ArrayList<Word>();		
		ArrayList<Word> stStatFP = new ArrayList<Word>();
		int stStatClassifiedAsDynStatWord_nums = 0;
		int stStatTN = 0;

				
		for (int storyIndex = 0; storyIndex < goldStoryModels.size(); storyIndex++) {
			
//			print("newStory"+ storyIndex +"*************************************");
			
			StoryModel currentGoldStory = goldStoryModels.get(storyIndex);
			
			if(currentGoldStory.scenes == null || currentGoldStory.scenes.size() == 0)
				continue;
			
			for(int sceneIndex = 0; sceneIndex < currentGoldStory.scenes.size();  sceneIndex++) {
				
//				print("newScene" + sceneIndex + "*************************************");
				
				SceneModel currentGoldScene = currentGoldStory.scenes.get(sceneIndex);
				
				StoryModel allocatedStory = null;
				SceneModel allocatedScene = null;
				
				if(storyIndex >= allocatedStoryModels.size())
					return;
					
				allocatedStory = allocatedStoryModels.get(storyIndex);
				
				if(allocatedStory == null || allocatedStory.scenes == null || sceneIndex >= allocatedStory.scenes.size()) {
					MyError.error("allocatedStory is null or didn't have the required SceneModel");
					continue;
				}
				
				allocatedScene = allocatedStory.scenes.get(sceneIndex);
								
				if(allocatedScene == null) {
					MyError.error("SceneIndex " + sceneIndex + " is null in allocatedStoryModels!!!");
					continue;
				}
				//------------------------- evaluate Roles -----------------------------
				ArrayList<SceneElement> goldRoleElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoles() != null)
					for(Role r: currentGoldScene.getRoles())
						goldRoleElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoles() != null)
					for(Role r: allocatedScene.getRoles())
						allocatedRoleElements.add(r);
				
				roleClassifiedAsRoleWord_nums += evaluatePrimaryAllocationForSceneElement(goldRoleElements, allocatedRoleElements, roleTP, roleFN, roleFP);
				
				//------------------------- evaluate DynamicObjects -----------------------------
				ArrayList<SceneElement> goldDynElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getDynamic_objects() != null)
					for(DynamicObject r: currentGoldScene.getDynamic_objects())
						goldDynElements.add(r);
				
				ArrayList<SceneElement> allocatedDynElements = new ArrayList<SceneElement>();
				if(allocatedScene.getDynamic_objects() != null)
					for(DynamicObject r: allocatedScene.getDynamic_objects())
						allocatedDynElements.add(r);
				
				dynClassifiedAsDynWord_nums += evaluatePrimaryAllocationForSceneElement(goldDynElements, allocatedDynElements, dynTP, dynFN, dynFP);	
				
				//------------------------- evaluate StaticObjects -----------------------------
				ArrayList<SceneElement> goldStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getStatic_objects() != null)
					for(StaticObject r: currentGoldScene.getStatic_objects())
						goldStatElements.add(r);
				
				ArrayList<SceneElement> allocatedStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getStatic_objects() != null)
					for(StaticObject r: allocatedScene.getStatic_objects())
						allocatedStatElements.add(r);
				
				statClassifiedAsStatWord_nums += evaluatePrimaryAllocationForSceneElement(goldStatElements, allocatedStatElements, statTP, statFN, statFP);
				
				//------------------------- evaluate AlternativeLocations -----------------------------
				ArrayList<SceneElement> goldAltLocElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getAlternativeLocations() != null)
					for(Location r: currentGoldScene.getAlternativeLocations())
						goldAltLocElements.add(r);
				
				ArrayList<SceneElement> allocatedAltLocElements = new ArrayList<SceneElement>();
				if(allocatedScene.getAlternativeLocations() != null)
					for(Location r: allocatedScene.getAlternativeLocations())
						allocatedAltLocElements.add(r);
				
				altLocClassifiedAsAltLocWord_nums += evaluatePrimaryAllocationForSceneElement(goldAltLocElements, allocatedAltLocElements, altLocTP, altLocFN, altLocFP);
				
				//------------------------- evaluate AlternativeTimes -----------------------------
				ArrayList<SceneElement> goldAltTimElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getAlternativeTimes() != null)
					for(Time r: currentGoldScene.getAlternativeTimes())
						goldAltTimElements.add(r);
				
				ArrayList<SceneElement> allocatedAltTimElements = new ArrayList<SceneElement>();
				if(allocatedScene.getAlternativeTimes() != null)
					for(Time r: allocatedScene.getAlternativeTimes())
						allocatedAltTimElements.add(r);
				
				altTimClassifiedAsAltTimWord_nums += evaluatePrimaryAllocationForSceneElement(goldAltTimElements, allocatedAltTimElements, altTimTP, altTimFN, altTimFP);

				//------------------------- evaluate RoleActions -----------------------------
				ArrayList<SceneElement> goldRoleActElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleActions() != null)
					for(RoleAction r: currentGoldScene.getRoleActions())
						goldRoleActElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleActElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleActions() != null)
					for(RoleAction r: allocatedScene.getRoleActions())
						allocatedRoleActElements.add(r);
				
				roleActClassifiedAsRoleActWord_nums += evaluatePrimaryAllocationForSceneElement(goldRoleActElements, allocatedRoleActElements, roleActTP, roleActFN, roleActFP);

				//------------------------- evaluate RoleStates -----------------------------
				ArrayList<SceneElement> goldRoleStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleStates() != null)
					for(RoleState r: currentGoldScene.getRoleStates())
						goldRoleStatElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleStates() != null)
					for(RoleState r: allocatedScene.getRoleStates())
						allocatedRoleStatElements.add(r);
				
				roleStatClassifiedAsRoleStatWord_nums += evaluatePrimaryAllocationForSceneElement(goldRoleStatElements, allocatedRoleStatElements, roleStatTP, roleStatFN, roleStatFP);
				
				//------------------------- evaluate RoleIntents -----------------------------
				ArrayList<SceneElement> goldRoleIntElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleIntents() != null)
					for(RoleIntent r: currentGoldScene.getRoleIntents())
						goldRoleIntElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleIntElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleIntents() != null)
					for(RoleIntent r: allocatedScene.getRoleIntents())
						allocatedRoleIntElements.add(r);
				
				roleIntClassifiedAsRoleIntWord_nums += evaluatePrimaryAllocationForSceneElement(goldRoleIntElements, allocatedRoleIntElements, roleIntTP, roleIntFN, roleIntFP);
				
				//------------------------- evaluate RoleEmotions -----------------------------
				ArrayList<SceneElement> goldRoleEmotionElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleEmotions() != null)
					for(RoleEmotion r: currentGoldScene.getRoleEmotions())
						goldRoleEmotionElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleEmotionElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleEmotions() != null)
					for(RoleEmotion r: allocatedScene.getRoleEmotions())
						allocatedRoleEmotionElements.add(r);
				
				roleEmoClassifiedAsRoleEmoWord_nums += evaluatePrimaryAllocationForSceneElement(goldRoleEmotionElements, allocatedRoleEmotionElements, roleEmoTP, roleEmoFN, roleEmoFP);

				//------------------------- evaluate DynamicObjectActions -----------------------------
				ArrayList<SceneElement> goldDynActElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getObjectActions() != null)
					for(DynamicObjectAction r: currentGoldScene.getObjectActions())
						goldDynActElements.add(r);
				
				ArrayList<SceneElement> allocatedDynActElements = new ArrayList<SceneElement>();
				if(allocatedScene.getObjectActions() != null)
					for(DynamicObjectAction r: allocatedScene.getObjectActions())
						allocatedDynActElements.add(r);
				
				dynActClassifiedAsDynActWord_nums += evaluatePrimaryAllocationForSceneElement(goldDynActElements, allocatedDynActElements, dynActTP, dynActFN, dynActFP);	
				
				//------------------------- evaluate DynamicObjectStates -----------------------------
				ArrayList<SceneElement> goldDynStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getDynamicObjectStates() != null)
					for(DynamicObjectState r: currentGoldScene.getDynamicObjectStates())
						goldDynStatElements.add(r);
				
				ArrayList<SceneElement> allocatedDynStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getDynamicObjectStates() != null)
					for(DynamicObjectState r: allocatedScene.getDynamicObjectStates())
						allocatedDynStatElements.add(r);
				
				dynStatClassifiedAsDynStatWord_nums += evaluatePrimaryAllocationForSceneElement(goldDynStatElements, allocatedDynStatElements, dynStatTP, dynStatFN, dynStatFP);
				
				//------------------------- evaluate StaticObjectStates -----------------------------
				ArrayList<SceneElement> goldStStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getStaticObjectStates() != null)
					for(StaticObjectState r: currentGoldScene.getStaticObjectStates())
						goldStStatElements.add(r);
				
				ArrayList<SceneElement> allocatedStnStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getStaticObjectStates() != null)
					for(StaticObjectState r: allocatedScene.getStaticObjectStates())
						allocatedStnStatElements.add(r);
				
				stStatClassifiedAsDynStatWord_nums += evaluatePrimaryAllocationForSceneElement(goldStStatElements, allocatedStnStatElements, stStatTP, stStatFN, stStatFP);
				
			}
		}
		print("\nRole evaluation:");
		roleTN = allWordsNumber - roleClassifiedAsRoleWord_nums - roleFN.size();
		double rolePrecision = calculatePrecision(roleTP.size(), roleFN.size(), roleFP.size(), roleTN);		
		double roleRecall = calculateRecall(roleTP.size(), roleFN.size(), roleFP.size(), roleTN);
		double roleF1 = calculateF1(rolePrecision, roleRecall);
		int roleNum = roleTP.size() + roleFN.size();
		print("TP:" + roleTP.size() + " FN:" + roleFN.size() + " FP:" + roleFP.size() + " TN:" + roleTN);		
		print("precision:" + rolePrecision + " recall: " + roleRecall + " F1_score: " + roleF1);
		print("total number: " + (roleTP.size() + roleFN.size() + roleFP.size() + roleTN) + " role Number: " + roleNum);
		
		print("\nDynamicObjects evaluation:");		
		dynTN = allWordsNumber - dynClassifiedAsDynWord_nums - dynFN.size();
		double dynPrecision = calculatePrecision(dynTP.size(), dynFN.size(), dynFP.size(), dynTN);		
		double dynRecall = calculateRecall(dynTP.size(), dynFN.size(), dynFP.size(), dynTN);
		double dynF1 = calculateF1(dynPrecision, dynRecall);
		int dynNum = dynTP.size() + dynFN.size();
		print("TP:" + dynTP.size() + " FN:" + dynFN.size() + " FP:" + dynFP.size() + " TN:" + dynTN);		
		print("precision:" + dynPrecision + " recall: " + dynRecall + " F1_score: " + dynF1);
		print("total number: " + (dynTP.size() + dynFN.size() + dynFP.size() + dynTN) + " dynamic_obejcts Number: " + dynNum);
		
		print("\nStaticObjects evaluation:");		
		statTN = allWordsNumber - statClassifiedAsStatWord_nums - statFN.size();
		double statPrecision = calculatePrecision(statTP.size(), statFN.size(), statFP.size(), statTN);		
		double statRecall = calculateRecall(statTP.size(), statFN.size(), statFP.size(), statTN);
		double statF1 = calculateF1(statPrecision, statRecall);
		int statNum = statTP.size() + statFN.size();
		print("TP:" + statTP.size() + " FN:" + statFN.size() + " FP:" + statFP.size() + " TN:" + statTN);		
		print("precision:" + statPrecision + " recall: " + statRecall + " F1_score: " + statF1);
		print("total number: " + (statTP.size() + statFN.size() + statFP.size() + statTN) + " static_obejcts Number: " + statNum);
		
		print("\nAlternativeLocations evaluation:");		
		altLocTN = allWordsNumber - altLocClassifiedAsAltLocWord_nums - altLocFN.size();
		double altLocPrecision = calculatePrecision(altLocTP.size(), altLocFN.size(), altLocFP.size(), altLocTN);		
		double altLocRecall = calculateRecall(altLocTP.size(), altLocFN.size(), altLocFP.size(), altLocTN);
		double altLocF1 = calculateF1(altLocPrecision, altLocRecall);
		int altLocNum = altLocTP.size() + altLocFN.size();
		print("TP:" + altLocTP.size() + " FN:" + altLocFN.size() + " FP:" + altLocFP.size() + " TN:" + altLocTN);		
		print("precision:" + altLocPrecision + " recall: " + altLocRecall + " F1_score: " + altLocF1);
		print("total number: " + (altLocTP.size() + altLocFN.size() + altLocFP.size() + altLocTN) + " alternative Locations Number: " + altLocNum);
		
		print("\nAlternativeTimes evaluation:");		
		altTimTN = allWordsNumber - altTimClassifiedAsAltTimWord_nums - altTimFN.size();
		double altTimPrecision = calculatePrecision(altTimTP.size(), altTimFN.size(), altTimFP.size(), altTimTN);		
		double altTimRecall = calculateRecall(altTimTP.size(), altTimFN.size(), altTimFP.size(), altTimTN);
		double altTimF1 = calculateF1(altTimPrecision, altTimRecall);
		int altTimNum = altTimTP.size() + altTimFN.size();
		print("TP:" + altTimTP.size() + " FN:" + altTimFN.size() + " FP:" + altTimFP.size() + " TN:" + altTimTN);		
		print("precision:" + altTimPrecision + " recall: " + altTimRecall + " F1_score: " + altTimF1);
		print("total number: " + (altTimTP.size() + altTimFN.size() + altTimFP.size() + altTimTN) + " alternative Times Number: " + altTimNum);
		
		print("\nRoleAction evaluation:");
		roleActTN = allWordsNumber - roleActClassifiedAsRoleActWord_nums - roleActFN.size();
		double roleActPrecision = calculatePrecision(roleActTP.size(), roleActFN.size(), roleActFP.size(), roleActTN);		
		double roleActRecall = calculateRecall(roleActTP.size(), roleActFN.size(), roleActFP.size(), roleActTN);
		double roleActF1 = calculateF1(rolePrecision, roleRecall);
		int roleActNum = roleActTP.size() + roleActFN.size();
		print("TP:" + roleActTP.size() + " FN:" + roleActFN.size() + " FP:" + roleActFP.size() + " TN:" + roleActTN);		
		print("precision:" + roleActPrecision + " recall: " + roleActRecall + " F1_score: " + roleActF1);		
		print("total number: " + (roleActTP.size() + roleActFN.size() + roleActFP.size() + roleActTN) + " roleAction Number: " + roleActNum);
		
		print("\nRoleState evaluation:");
		roleStatTN = allWordsNumber - roleStatClassifiedAsRoleStatWord_nums - roleStatFN.size();
		double roleStatPrecision = calculatePrecision(roleStatTP.size(), roleStatFN.size(), roleStatFP.size(), roleStatTN);		
		double roleStatRecall = calculateRecall(roleStatTP.size(), roleStatFN.size(), roleStatFP.size(), roleStatTN);
		double roleStatF1 = calculateF1(roleStatPrecision, roleStatRecall);
		int roleStatNum = roleStatTP.size() + roleStatFN.size();
		print("TP:" + roleStatTP.size() + " FN:" + roleStatFN.size() + " FP:" + roleStatFP.size() + " TN:" + roleStatTN);		
		print("precision:" + roleStatPrecision + " recall: " + roleStatRecall + " F1_score: " + roleStatF1);
		print("total number: " + (roleStatTP.size() + roleStatFN.size() + roleStatFP.size() + roleStatTN) + " roleState Number: " + roleStatNum);
		
		print("\nRoleIntent evaluation:");
		roleIntTN = allWordsNumber - roleIntClassifiedAsRoleIntWord_nums - roleIntFN.size();
		double roleIntPrecision = calculatePrecision(roleIntTP.size(), roleIntFN.size(), roleIntFP.size(), roleIntTN);		
		double roleIntRecall = calculateRecall(roleIntTP.size(), roleIntFN.size(), roleIntFP.size(), roleIntTN);
		double roleIntF1 = calculateF1(roleIntPrecision, roleIntRecall);
		int roleIntNum = roleIntTP.size() + roleIntFN.size();
		print("TP:" + roleIntTP.size() + " FN:" + roleIntFN.size() + " FP:" + roleIntFP.size() + " TN:" + roleIntTN);		
		print("precision:" + roleIntPrecision + " recall: " + roleIntRecall + " F1_score: " + roleIntF1);
		print("total number: " + (roleIntTP.size() + roleIntFN.size() + roleIntFP.size() + roleIntTN) + " roleIntent Number: " + roleIntNum);
		
		print("\nRoleEmoent evaluation:");
		roleEmoTN = allWordsNumber - roleEmoClassifiedAsRoleEmoWord_nums - roleEmoFN.size();
		double roleEmoPrecision = calculatePrecision(roleEmoTP.size(), roleEmoFN.size(), roleEmoFP.size(), roleEmoTN);		
		double roleEmoRecall = calculateRecall(roleEmoTP.size(), roleEmoFN.size(), roleEmoFP.size(), roleEmoTN);
		double roleEmoF1 = calculateF1(roleEmoPrecision, roleEmoRecall);
		int roleEmoNum = roleEmoTP.size() + roleEmoFN.size();
		print("TP:" + roleEmoTP.size() + " FN:" + roleEmoFN.size() + " FP:" + roleEmoFP.size() + " TN:" + roleEmoTN);		
		print("precision:" + roleEmoPrecision + " recall: " + roleEmoRecall + " F1_score: " + roleEmoF1);
		print("total number: " + (roleEmoTP.size() + roleEmoFN.size() + roleEmoFP.size() + roleEmoTN) + " roleEmotion Number: " + roleEmoNum);
		
		print("\nDynamicObjectActions evaluation:");		
		dynActTN = allWordsNumber - dynActClassifiedAsDynActWord_nums - dynActFN.size();
		double dynActPrecision = calculatePrecision(dynActTP.size(), dynActFN.size(), dynActFP.size(), dynActTN);		
		double dynActRecall = calculateRecall(dynActTP.size(), dynActFN.size(), dynActFP.size(), dynActTN);
		double dynActF1 = calculateF1(dynActPrecision, dynActRecall);
		int dynActNum = dynActTP.size() + dynActFN.size();
		print("TP:" + dynActTP.size() + " FN:" + dynActFN.size() + " FP:" + dynActFP.size() + " TN:" + dynActTN);		
		print("precision:" + dynActPrecision + " recall: " + dynActRecall + " F1_score: " + dynActF1);
		print("total number: " + (dynActTP.size() + dynActFN.size() + dynActFP.size() + dynActTN) + " dynamic_obejct_Actions Number: " + dynActNum);
				
		print("\nDynamicObjectStates evaluation:");		
		dynStatTN = allWordsNumber - dynStatClassifiedAsDynStatWord_nums - dynStatFN.size();
		double dynStatPrecision = calculatePrecision(dynStatTP.size(), dynStatFN.size(), dynStatFP.size(), dynStatTN);		
		double dynStatRecall = calculateRecall(dynStatTP.size(), dynStatFN.size(), dynStatFP.size(), dynStatTN);
		double dynStatF1 = calculateF1(dynStatPrecision, dynStatRecall);
		int dynStatNum = dynStatTP.size() + dynStatFN.size();
		print("TP:" + dynStatTP.size() + " FN:" + dynStatFN.size() + " FP:" + dynStatFP.size() + " TN:" + dynStatTN);		
		print("precision:" + dynStatPrecision + " recall: " + dynStatRecall + " F1_score: " + dynStatF1);
		print("total number: " + (dynStatTP.size() + dynStatFN.size() + dynStatFP.size() + dynStatTN) + " dynmic_obejcts_state Number: " + dynStatNum);
		
		print("\nStaticObjectStates evaluation:");		
		stStatTN = allWordsNumber - stStatClassifiedAsDynStatWord_nums - stStatFN.size();
		double stStatPrecision = calculatePrecision(stStatTP.size(), stStatFN.size(), stStatFP.size(), stStatTN);		
		double stStatRecall = calculateRecall(stStatTP.size(), stStatFN.size(), stStatFP.size(), stStatTN);
		double stStatF1 = calculateF1(stStatPrecision, stStatRecall);
		int stStatNum = stStatTP.size() + stStatFN.size();
		print("TP:" + stStatTP.size() + " FN:" + stStatFN.size() + " FP:" + stStatFP.size() + " TN:" + stStatTN);		
		print("precision:" + stStatPrecision + " recall: " + stStatRecall + " F1_score: " + stStatF1);
		print("total number: " + (stStatTP.size() + stStatFN.size() + stStatFP.size() + stStatTN) + " static_obejct_states Number: " + stStatNum);
		
		double weighted_accuracy = roleNum*roleF1 + dynNum*dynF1 + statNum*statF1+ altLocNum*altLocF1 + altTimNum*altTimF1;
		weighted_accuracy += roleActNum*roleActF1 + roleStatNum*roleStatF1 + roleIntNum*roleIntF1;// + roleEmoNum*roleEmoF1;
		weighted_accuracy += dynActNum*dynActF1 + dynStatNum*dynStatF1 + stStatNum*stStatF1;
		
		int allGoldWords = roleNum +  roleActNum + roleStatNum + roleIntNum + roleEmoNum + dynNum + dynActNum + dynStatNum + statNum + stStatNum + altLocNum + altTimNum;
		weighted_accuracy = weighted_accuracy/allGoldWords;
		
		print("\ntotal (not NO and JUNK) word nums: " + allGoldWords);
		print("weighted accuracies: " + weighted_accuracy);		
	}
	

	private int evaluatePrimaryAllocationForSceneElement (ArrayList<SceneElement> goldSceneElems, ArrayList<SceneElement> allocatedSceneElems, ArrayList<Word> TP, ArrayList<Word> FN, ArrayList<Word> FP) {
		
		if(goldSceneElems == null || goldSceneElems.size() == 0 || allocatedSceneElems == null || allocatedSceneElems.size() == 0)
			return 0;
						
		Hashtable<String, ArrayList<Word>> assessed = new Hashtable<String, ArrayList<Word>>();
		
		for(SceneElement goldSceElm:goldSceneElems) {
			
//			print("Gold SceneElement: " + goldSceElm._name + "*************************************");
			
			ArrayList<Word> goldScElmWords = goldSceElm.getAllWords();
			
			if(goldScElmWords == null || goldScElmWords.size() == 0)
				continue;
			
			for(Word goldSceElmWrd: goldScElmWords) {
				
				SceneElement allocatedSceElm = null;
				
				for(SceneElement curAllocScElm: allocatedSceneElems) {
					
					ArrayList<Word> allocScELmWords =  curAllocScElm.getAllWords();

					if(allocScELmWords == null || allocScELmWords.size() == 0)
						continue;
					
					for(Word wrd : allocScELmWords)
						if(wrd.equals(goldSceElmWrd))
							allocatedSceElm = curAllocScElm;	
				}	
				
				if(allocatedSceElm != null && goldSceElm.equals(allocatedSceElm)) {
					TP.add(goldSceElmWrd);
					if(assessed.containsKey(goldSceElmWrd._wordName)) {
						assessed.get(goldSceElmWrd._wordName).add(goldSceElmWrd);
//						print("added to assessed " + goldRoleWrd._wordName);
					}
					else {
						ArrayList<Word> newList = new ArrayList<Word>();
						newList.add(goldSceElmWrd);
						assessed.put(goldSceElmWrd._wordName, newList);
//						print("new to assessed " + goldRoleWrd._wordName);
					}
//					print("TP: " + goldRoleWrd._wordName);
					continue;
				}
				FN.add(goldSceElmWrd);						
//				print("FN: " + goldRoleWrd._wordName);
			}
		}
		
		int positiveWord_nums = 0;
		
		if(allocatedSceneElems == null || allocatedSceneElems.size() == 0)
//			continue;
			return positiveWord_nums;
		
		for(SceneElement allocSceElm:allocatedSceneElems) {
		
			ArrayList<Word> allocatedWords = allocSceElm.getAllWords();
			
			if(allocatedWords == null || allocatedWords.size() == 0)
				continue;
			
			positiveWord_nums += allocatedWords.size();
							
			for(Word allocWord:allocatedWords) {
				boolean flag = false;
				if(assessed.containsKey(allocWord._wordName)) {
					ArrayList<Word> assList = assessed.get(allocWord._wordName);
					for(Word wr:assList)
						if(wr.equals(allocWord)) {
							flag = true;
							break;
						}							
				}
				if(!flag) {
					FP.add(allocWord);
					print(">>>>>>>>>>>>>>>>>>>>>>> ----------- FP added" + allocWord);
				}
			}
		}
		return positiveWord_nums;
	}

	/**
	 * this method evaluates the nonRundancy detection of all sceneElement in all StoryModel.
	 * It means in all words of (the all sentences of) a sceneModel, some words which refer 
	 * to a unique sceneElement, must be detected as redundant.
	 * for example in the secene with these 3 sentences:
	 * بعداز چند وقت يونس جهت مشاهده عذاب وارد شهر شد اما ديد که اتفاق خاصي نيفتاده ،
	 * وقتي قضيه را از يکي‌از اهالي پرسيد مرد که او را نمي‌شناخت ماجراي نفرين يونس و توبه مردم را براي ش توضيح داد .
	 * يونس با شنيدن اين خبر خشمگين شد 
	 *  all of these words are redundant:
	 *  [4	يونس	N	SBJ	10	يونس§n-23943	نفر§n-13075	role	Arg0	Arg0		||| ROLE	ROLE	]
	 *  	[10	او	PR	PREDEP	11	يونس§n-23943	نفر§n-13075	role		-4||| JUNK	]
	 *  	[15	يونس	N	MOZ	14	يونس§n-23943	نفر§n-13075	role		||| ROLE	]
	 *  	[21	ش	PR	POSDEP	20	يونس§n-23943	نفر§n-13075	role		-4||| ROLE	]
	 *  	[1	يونس	N	SBJ	7	يونس§n-23943	نفر§n-13075	role	Arg1	Arg0	Arg0	Arg0		||| ROLE	ROLE	ROLE	ROLE	]
	 * and other words are non redundant.		
	 * 
	 * @param allocatedStoryModels
	 */
	public void evaluateAllSceneElementNonRedundancyDetection(ArrayList<StoryModel> allocatedStoryModels) {
		
		print("\n\n>>>>>>>>>-------- evaluate nonRedundancy detection of sceneElements in StoryModels --------<<<<<<<<<");
		
		if(allocatedStoryModels == null || allocatedStoryModels.size() == 0)
			return;
		
		ArrayList<Word> roleTP = new ArrayList<Word>();
		ArrayList<Word> roleFN = new ArrayList<Word>();		
		ArrayList<Word> roleFP = new ArrayList<Word>();
		ArrayList<Word> roleTN = new ArrayList<Word>();
						
		ArrayList<Word> dynTP = new ArrayList<Word>();
		ArrayList<Word> dynFN = new ArrayList<Word>();		
		ArrayList<Word> dynFP = new ArrayList<Word>();
		ArrayList<Word> dynTN = new ArrayList<Word>();
				
		ArrayList<Word> statTP = new ArrayList<Word>();
		ArrayList<Word> statFN = new ArrayList<Word>();		
		ArrayList<Word> statFP = new ArrayList<Word>();
		ArrayList<Word> statTN = new ArrayList<Word>();
		
		ArrayList<Word> altLocTP = new ArrayList<Word>();
		ArrayList<Word> altLocFN = new ArrayList<Word>();		
		ArrayList<Word> altLocFP = new ArrayList<Word>();
		ArrayList<Word> altLocTN = new ArrayList<Word>();
						
		ArrayList<Word> altTimTP = new ArrayList<Word>();
		ArrayList<Word> altTimFN = new ArrayList<Word>();		
		ArrayList<Word> altTimFP = new ArrayList<Word>();
		ArrayList<Word> altTimTN = new ArrayList<Word>();
						
		ArrayList<Word> roleActTP = new ArrayList<Word>();
		ArrayList<Word> roleActFN = new ArrayList<Word>();		
		ArrayList<Word> roleActFP = new ArrayList<Word>();
		ArrayList<Word> roleActTN = new ArrayList<Word>();
						
		ArrayList<Word> roleStatTP = new ArrayList<Word>();
		ArrayList<Word> roleStatFN = new ArrayList<Word>();		
		ArrayList<Word> roleStatFP = new ArrayList<Word>();
		ArrayList<Word> roleStatTN = new ArrayList<Word>();
						
		ArrayList<Word> roleIntTP = new ArrayList<Word>();
		ArrayList<Word> roleIntFN = new ArrayList<Word>();		
		ArrayList<Word> roleIntFP = new ArrayList<Word>();
		ArrayList<Word> roleIntTN = new ArrayList<Word>();
						
		ArrayList<Word> roleEmoTP = new ArrayList<Word>();
		ArrayList<Word> roleEmoFN = new ArrayList<Word>();		
		ArrayList<Word> roleEmoFP = new ArrayList<Word>();
		ArrayList<Word> roleEmoTN = new ArrayList<Word>();
						
		ArrayList<Word> dynActTP = new ArrayList<Word>();
		ArrayList<Word> dynActFN = new ArrayList<Word>();		
		ArrayList<Word> dynActFP = new ArrayList<Word>();
		ArrayList<Word> dynActTN = new ArrayList<Word>();
						
		ArrayList<Word> dynStatTP = new ArrayList<Word>();
		ArrayList<Word> dynStatFN = new ArrayList<Word>();		
		ArrayList<Word> dynStatFP = new ArrayList<Word>();
		ArrayList<Word> dynStatTN = new ArrayList<Word>();
						
		ArrayList<Word> stStatTP = new ArrayList<Word>();
		ArrayList<Word> stStatFN = new ArrayList<Word>();		
		ArrayList<Word> stStatFP = new ArrayList<Word>();
		ArrayList<Word> stStatTN = new ArrayList<Word>();
								
		for (int storyIndex = 0; storyIndex < goldStoryModels.size(); storyIndex++) {
			
//			print("newStory"+ storyIndex +"*************************************");
			
			StoryModel currentGoldStory = goldStoryModels.get(storyIndex);
			
			if(currentGoldStory.scenes == null || currentGoldStory.scenes.size() == 0)
				continue;
			
			for(int sceneIndex = 0; sceneIndex < currentGoldStory.scenes.size();  sceneIndex++) {
				
//				print("newScene" + sceneIndex + "*************************************");
				
				SceneModel currentGoldScene = currentGoldStory.scenes.get(sceneIndex);
				
				StoryModel allocatedStory = null;
				SceneModel allocatedScene = null;
				
				if(storyIndex >= allocatedStoryModels.size())
					return;
					
				allocatedStory = allocatedStoryModels.get(storyIndex);
				
				if(allocatedStory == null || allocatedStory.scenes == null || sceneIndex >= allocatedStory.scenes.size()) {
					MyError.error("allocatedStory is null or didn't have the required SceneModel");
					continue;
				}
				
				allocatedScene = allocatedStory.scenes.get(sceneIndex);
								
				if(allocatedScene == null) {
					MyError.error("SceneIndex " + sceneIndex + " is null in allocatedStoryModels!!!");
					continue;
				}
				//------------------------- evaluate Roles -----------------------------
				ArrayList<SceneElement> goldRoleElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoles() != null)
					for(Role r: currentGoldScene.getRoles())
						goldRoleElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoles() != null)
					for(Role r: allocatedScene.getRoles())
						allocatedRoleElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldRoleElements, allocatedRoleElements, roleTP, roleFN, roleFP, roleTN);
				
				//------------------------- evaluate DynamicObjects -----------------------------
				ArrayList<SceneElement> goldDynElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getDynamic_objects() != null)
					for(DynamicObject r: currentGoldScene.getDynamic_objects())
						goldDynElements.add(r);
				
				ArrayList<SceneElement> allocatedDynElements = new ArrayList<SceneElement>();
				if(allocatedScene.getDynamic_objects() != null)
					for(DynamicObject r: allocatedScene.getDynamic_objects())
						allocatedDynElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldDynElements, allocatedDynElements, dynTP, dynFN, dynFP, dynTN);	
				
				//------------------------- evaluate StaticObjects -----------------------------
				ArrayList<SceneElement> goldStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getStatic_objects() != null)
					for(StaticObject r: currentGoldScene.getStatic_objects())
						goldStatElements.add(r);
				
				ArrayList<SceneElement> allocatedStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getStatic_objects() != null)
					for(StaticObject r: allocatedScene.getStatic_objects())
						allocatedStatElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldStatElements, allocatedStatElements, statTP, statFN, statFP, statTN);
				
				//------------------------- evaluate AlternativeLocations -----------------------------
				ArrayList<SceneElement> goldAltLocElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getAlternativeLocations() != null)
					for(Location r: currentGoldScene.getAlternativeLocations())
						goldAltLocElements.add(r);
				
				ArrayList<SceneElement> allocatedAltLocElements = new ArrayList<SceneElement>();
				if(allocatedScene.getAlternativeLocations() != null)
					for(Location r: allocatedScene.getAlternativeLocations())
						allocatedAltLocElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldAltLocElements, allocatedAltLocElements, altLocTP, altLocFN, altLocFP, altLocTN);
				
				//------------------------- evaluate AlternativeTimes -----------------------------
				ArrayList<SceneElement> goldAltTimElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getAlternativeTimes() != null)
					for(Time r: currentGoldScene.getAlternativeTimes())
						goldAltTimElements.add(r);
				
				ArrayList<SceneElement> allocatedAltTimElements = new ArrayList<SceneElement>();
				if(allocatedScene.getAlternativeTimes() != null)
					for(Time r: allocatedScene.getAlternativeTimes())
						allocatedAltTimElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldAltTimElements, allocatedAltTimElements, altTimTP, altTimFN, altTimFP, altTimTN);

				//------------------------- evaluate RoleActions -----------------------------
				ArrayList<SceneElement> goldRoleActElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleActions() != null)
					for(RoleAction r: currentGoldScene.getRoleActions())
						goldRoleActElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleActElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleActions() != null)
					for(RoleAction r: allocatedScene.getRoleActions())
						allocatedRoleActElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldRoleActElements, allocatedRoleActElements, roleActTP, roleActFN, roleActFP, roleActTN);

				//------------------------- evaluate RoleStates -----------------------------
				ArrayList<SceneElement> goldRoleStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleStates() != null)
					for(RoleState r: currentGoldScene.getRoleStates())
						goldRoleStatElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleStates() != null)
					for(RoleState r: allocatedScene.getRoleStates())
						allocatedRoleStatElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldRoleStatElements, allocatedRoleStatElements, roleStatTP, roleStatFN, roleStatFP, roleStatTN);
				
				//------------------------- evaluate RoleIntents -----------------------------
				ArrayList<SceneElement> goldRoleIntElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleIntents() != null)
					for(RoleIntent r: currentGoldScene.getRoleIntents())
						goldRoleIntElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleIntElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleIntents() != null)
					for(RoleIntent r: allocatedScene.getRoleIntents())
						allocatedRoleIntElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldRoleIntElements, allocatedRoleIntElements, roleIntTP, roleIntFN, roleIntFP, roleIntTN);
				
				//------------------------- evaluate RoleEmotions -----------------------------
				ArrayList<SceneElement> goldRoleEmotionElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getRoleEmotions() != null)
					for(RoleEmotion r: currentGoldScene.getRoleEmotions())
						goldRoleEmotionElements.add(r);
				
				ArrayList<SceneElement> allocatedRoleEmotionElements = new ArrayList<SceneElement>();
				if(allocatedScene.getRoleEmotions() != null)
					for(RoleEmotion r: allocatedScene.getRoleEmotions())
						allocatedRoleEmotionElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldRoleEmotionElements, allocatedRoleEmotionElements, roleEmoTP, roleEmoFN, roleEmoFP, roleEmoTN);

				//------------------------- evaluate DynamicObjectActions -----------------------------
				ArrayList<SceneElement> goldDynActElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getObjectActions() != null)
					for(DynamicObjectAction r: currentGoldScene.getObjectActions())
						goldDynActElements.add(r);
				
				ArrayList<SceneElement> allocatedDynActElements = new ArrayList<SceneElement>();
				if(allocatedScene.getObjectActions() != null)
					for(DynamicObjectAction r: allocatedScene.getObjectActions())
						allocatedDynActElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldDynActElements, allocatedDynActElements, dynActTP, dynActFN, dynActFP, dynActTN);	
				
				//------------------------- evaluate DynamicObjectStates -----------------------------
				ArrayList<SceneElement> goldDynStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getDynamicObjectStates() != null)
					for(DynamicObjectState r: currentGoldScene.getDynamicObjectStates())
						goldDynStatElements.add(r);
				
				ArrayList<SceneElement> allocatedDynStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getDynamicObjectStates() != null)
					for(DynamicObjectState r: allocatedScene.getDynamicObjectStates())
						allocatedDynStatElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldDynStatElements, allocatedDynStatElements, dynStatTP, dynStatFN, dynStatFP, dynStatTN);
				
				//------------------------- evaluate StaticObjectStates -----------------------------
				ArrayList<SceneElement> goldStStatElements = new ArrayList<SceneElement>();
				if(currentGoldScene.getStaticObjectStates() != null)
					for(StaticObjectState r: currentGoldScene.getStaticObjectStates())
						goldStStatElements.add(r);
				
				ArrayList<SceneElement> allocatedStnStatElements = new ArrayList<SceneElement>();
				if(allocatedScene.getStaticObjectStates() != null)
					for(StaticObjectState r: allocatedScene.getStaticObjectStates())
						allocatedStnStatElements.add(r);
				
				evaluateNonRedundancyDetectionForSceneElement(goldStStatElements, allocatedStnStatElements, stStatTP, stStatFN, stStatFP, stStatTN);
				
			}
		}
		print("\nRole evaluation:");
		double rolePrecision = calculatePrecision(roleTP.size(), roleFN.size(), roleFP.size(), roleTN.size());		
		double roleRecall = calculateRecall(roleTP.size(), roleFN.size(), roleFP.size(), roleTN.size());
		double roleF1 = calculateF1(rolePrecision, roleRecall);
		int roleNum = roleTP.size() + roleFN.size();
		print("TP:" + roleTP.size() + " FN:" + roleFN.size() + " FP:" + roleFP.size() + " TN:" + roleTN.size());		
		print("precision:" + rolePrecision + " recall: " + roleRecall + " F1_score: " + roleF1);
		print("total number: " + (roleTP.size() + roleFN.size() + roleFP.size() + roleTN.size()) + " role Number: " + roleNum);
		
		print("\nDynamicObjects evaluation:");		
		double dynPrecision = calculatePrecision(dynTP.size(), dynFN.size(), dynFP.size(), dynTN.size());		
		double dynRecall = calculateRecall(dynTP.size(), dynFN.size(), dynFP.size(), dynTN.size());
		double dynF1 = calculateF1(dynPrecision, dynRecall);
		int dynNum = dynTP.size() + dynFN.size();
		print("TP:" + dynTP.size() + " FN:" + dynFN.size() + " FP:" + dynFP.size() + " TN:" + dynTN.size());		
		print("precision:" + dynPrecision + " recall: " + dynRecall + " F1_score: " + dynF1);
		print("total number: " + (dynTP.size() + dynFN.size() + dynFP.size() + dynTN.size()) + " dynamic_obejcts Number: " + dynNum);
		
		print("\nStaticObjects evaluation:");		
		double statPrecision = calculatePrecision(statTP.size(), statFN.size(), statFP.size(), statTN.size());		
		double statRecall = calculateRecall(statTP.size(), statFN.size(), statFP.size(), statTN.size());
		double statF1 = calculateF1(statPrecision, statRecall);
		int statNum = statTP.size() + statFN.size();
		print("TP:" + statTP.size() + " FN:" + statFN.size() + " FP:" + statFP.size() + " TN:" + statTN.size());		
		print("precision:" + statPrecision + " recall: " + statRecall + " F1_score: " + statF1);
		print("total number: " + (statTP.size() + statFN.size() + statFP.size() + statTN.size()) + " static_obejcts Number: " + statNum);
		
		print("\nAlternativeLocations evaluation:");		
		double altLocPrecision = calculatePrecision(altLocTP.size(), altLocFN.size(), altLocFP.size(), altLocTN.size());		
		double altLocRecall = calculateRecall(altLocTP.size(), altLocFN.size(), altLocFP.size(), altLocTN.size());
		double altLocF1 = calculateF1(altLocPrecision, altLocRecall);
		int altLocNum = altLocTP.size() + altLocFN.size();
		print("TP:" + altLocTP.size() + " FN:" + altLocFN.size() + " FP:" + altLocFP.size() + " TN:" + altLocTN.size());		
		print("precision:" + altLocPrecision + " recall: " + altLocRecall + " F1_score: " + altLocF1);
		print("total number: " + (altLocTP.size() + altLocFN.size() + altLocFP.size() + altLocTN.size()) + " alternative Locations Number: " + altLocNum);
		
		print("\nAlternativeTimes evaluation:");		
		double altTimPrecision = calculatePrecision(altTimTP.size(), altTimFN.size(), altTimFP.size(), altTimTN.size());		
		double altTimRecall = calculateRecall(altTimTP.size(), altTimFN.size(), altTimFP.size(), altTimTN.size());
		double altTimF1 = calculateF1(altTimPrecision, altTimRecall);
		int altTimNum = altTimTP.size() + altTimFN.size();
		print("TP:" + altTimTP.size() + " FN:" + altTimFN.size() + " FP:" + altTimFP.size() + " TN:" + altTimTN.size());		
		print("precision:" + altTimPrecision + " recall: " + altTimRecall + " F1_score: " + altTimF1);
		print("total number: " + (altTimTP.size() + altTimFN.size() + altTimFP.size() + altTimTN.size()) + " alternative Times Number: " + altTimNum);
		
		print("\nRoleAction evaluation:");
		double roleActPrecision = calculatePrecision(roleActTP.size(), roleActFN.size(), roleActFP.size(), roleActTN.size());		
		double roleActRecall = calculateRecall(roleActTP.size(), roleActFN.size(), roleActFP.size(), roleActTN.size());
		double roleActF1 = calculateF1(rolePrecision, roleRecall);
		int roleActNum = roleActTP.size() + roleActFN.size();
		print("TP:" + roleActTP.size() + " FN:" + roleActFN.size() + " FP:" + roleActFP.size() + " TN:" + roleActTN.size());		
		print("precision:" + roleActPrecision + " recall: " + roleActRecall + " F1_score: " + roleActF1);		
		print("total number: " + (roleActTP.size() + roleActFN.size() + roleActFP.size() + roleActTN.size()) + " roleAction Number: " + roleActNum);
		
		print("\nRoleState evaluation:");
		double roleStatPrecision = calculatePrecision(roleStatTP.size(), roleStatFN.size(), roleStatFP.size(), roleStatTN.size());		
		double roleStatRecall = calculateRecall(roleStatTP.size(), roleStatFN.size(), roleStatFP.size(), roleStatTN.size());
		double roleStatF1 = calculateF1(roleStatPrecision, roleStatRecall);
		int roleStatNum = roleStatTP.size() + roleStatFN.size();
		print("TP:" + roleStatTP.size() + " FN:" + roleStatFN.size() + " FP:" + roleStatFP.size() + " TN:" + roleStatTN.size());		
		print("precision:" + roleStatPrecision + " recall: " + roleStatRecall + " F1_score: " + roleStatF1);
		print("total number: " + (roleStatTP.size() + roleStatFN.size() + roleStatFP.size() + roleStatTN.size()) + " roleState Number: " + roleStatNum);
		
		print("\nRoleIntent evaluation:");
		double roleIntPrecision = calculatePrecision(roleIntTP.size(), roleIntFN.size(), roleIntFP.size(), roleIntTN.size());		
		double roleIntRecall = calculateRecall(roleIntTP.size(), roleIntFN.size(), roleIntFP.size(), roleIntTN.size());
		double roleIntF1 = calculateF1(roleIntPrecision, roleIntRecall);
		int roleIntNum = roleIntTP.size() + roleIntFN.size();
		print("TP:" + roleIntTP.size() + " FN:" + roleIntFN.size() + " FP:" + roleIntFP.size() + " TN:" + roleIntTN.size());		
		print("precision:" + roleIntPrecision + " recall: " + roleIntRecall + " F1_score: " + roleIntF1);
		print("total number: " + (roleIntTP.size() + roleIntFN.size() + roleIntFP.size() + roleIntTN.size()) + " roleIntent Number: " + roleIntNum);
		
		print("\nRoleEmoent evaluation:");
		double roleEmoPrecision = calculatePrecision(roleEmoTP.size(), roleEmoFN.size(), roleEmoFP.size(), roleEmoTN.size());		
		double roleEmoRecall = calculateRecall(roleEmoTP.size(), roleEmoFN.size(), roleEmoFP.size(), roleEmoTN.size());
		double roleEmoF1 = calculateF1(roleEmoPrecision, roleEmoRecall);
		int roleEmoNum = roleEmoTP.size() + roleEmoFN.size();
		print("TP:" + roleEmoTP.size() + " FN:" + roleEmoFN.size() + " FP:" + roleEmoFP.size() + " TN:" + roleEmoTN.size());		
		print("precision:" + roleEmoPrecision + " recall: " + roleEmoRecall + " F1_score: " + roleEmoF1);
		print("total number: " + (roleEmoTP.size() + roleEmoFN.size() + roleEmoFP.size() + roleEmoTN.size()) + " roleEmotion Number: " + roleEmoNum);
		
		print("\nDynamicObjectActions evaluation:");		
		double dynActPrecision = calculatePrecision(dynActTP.size(), dynActFN.size(), dynActFP.size(), dynActTN.size());		
		double dynActRecall = calculateRecall(dynActTP.size(), dynActFN.size(), dynActFP.size(), dynActTN.size());
		double dynActF1 = calculateF1(dynActPrecision, dynActRecall);
		int dynActNum = dynActTP.size() + dynActFN.size();
		print("TP:" + dynActTP.size() + " FN:" + dynActFN.size() + " FP:" + dynActFP.size() + " TN:" + dynActTN.size());		
		print("precision:" + dynActPrecision + " recall: " + dynActRecall + " F1_score: " + dynActF1);
		print("total number: " + (dynActTP.size() + dynActFN.size() + dynActFP.size() + dynActTN.size()) + " dynamic_obejct_Actions Number: " + dynActNum);
				
		print("\nDynamicObjectStates evaluation:");		
		double dynStatPrecision = calculatePrecision(dynStatTP.size(), dynStatFN.size(), dynStatFP.size(), dynStatTN.size());		
		double dynStatRecall = calculateRecall(dynStatTP.size(), dynStatFN.size(), dynStatFP.size(), dynStatTN.size());
		double dynStatF1 = calculateF1(dynStatPrecision, dynStatRecall);
		int dynStatNum = dynStatTP.size() + dynStatFN.size();
		print("TP:" + dynStatTP.size() + " FN:" + dynStatFN.size() + " FP:" + dynStatFP.size() + " TN:" + dynStatTN.size());		
		print("precision:" + dynStatPrecision + " recall: " + dynStatRecall + " F1_score: " + dynStatF1);
		print("total number: " + (dynStatTP.size() + dynStatFN.size() + dynStatFP.size() + dynStatTN.size()) + " dynmic_obejcts_state Number: " + dynStatNum);
		
		print("\nStaticObjectStates evaluation:");		
		double stStatPrecision = calculatePrecision(stStatTP.size(), stStatFN.size(), stStatFP.size(), stStatTN.size());		
		double stStatRecall = calculateRecall(stStatTP.size(), stStatFN.size(), stStatFP.size(), stStatTN.size());
		double stStatF1 = calculateF1(stStatPrecision, stStatRecall);
		int stStatNum = stStatTP.size() + stStatFN.size();
		print("TP:" + stStatTP.size() + " FN:" + stStatFN.size() + " FP:" + stStatFP.size() + " TN:" + stStatTN.size());		
		print("precision:" + stStatPrecision + " recall: " + stStatRecall + " F1_score: " + stStatF1);
		print("total number: " + (stStatTP.size() + stStatFN.size() + stStatFP.size() + stStatTN.size()) + " static_obejct_states Number: " + stStatNum);
		
		double weighted_accuracy = roleNum*roleF1 + dynNum*dynF1 + statNum*statF1+ altLocNum*altLocF1 + altTimNum*altTimF1;
		weighted_accuracy += roleActNum*roleActF1 + roleStatNum*roleStatF1 + roleIntNum*roleIntF1;// + roleEmoNum*roleEmoF1;
		weighted_accuracy += dynActNum*dynActF1 + dynStatNum*dynStatF1 + stStatNum*stStatF1;
		
		int allGoldWords = roleNum +  roleActNum + roleStatNum + roleIntNum + roleEmoNum + dynNum + dynActNum + dynStatNum + statNum + stStatNum + altLocNum + altTimNum;
		weighted_accuracy = weighted_accuracy/allGoldWords;
		
		print("\ntotal (not NO and JUNK) word nums: " + allGoldWords);
		print("weighted accuracies: " + weighted_accuracy);
			
	}
	
	private void evaluateNonRedundancyDetectionForSceneElement (ArrayList<SceneElement> goldSceneElems, ArrayList<SceneElement> allocatedSceneElems, ArrayList<Word> TP, ArrayList<Word> FN, ArrayList<Word> FP, ArrayList<Word> TN) {
		
		if(goldSceneElems == null || goldSceneElems.size() == 0 || allocatedSceneElems == null)
			return;
						
		Hashtable<String, ArrayList<Word>> assessed = new Hashtable<String, ArrayList<Word>>();
		
		for(SceneElement goldSceElm:goldSceneElems) {
			
//			print("Gold SceneElement: " + goldSceElm._name + "*************************************");
			
			if(goldSceElm.get_otherWords() == null || goldSceElm.get_otherWords().size() == 0) {
				//this is a True unique (nonRedundant) word
				
				Word uniqueWord = goldSceElm._mainWord;
				
				SceneElement equalAllocatedSceElm = null;
				
				for(SceneElement curAllocScElm: allocatedSceneElems) {
					
					ArrayList<Word> curAllocScELmWords =  curAllocScElm.getAllWords();

					if(curAllocScELmWords == null || curAllocScELmWords.size() == 0)
						continue;
					
					for(Word wrd : curAllocScELmWords)
						if(wrd.equals(uniqueWord))
							equalAllocatedSceElm = curAllocScElm;	
				}
				if(equalAllocatedSceElm == null) {
					MyError.error("the sceneElement with word \'" + uniqueWord+ "\' didn't find!");
					TP.add(uniqueWord);
					print("TP PR: " + uniqueWord._wordName + "+++++++++++++++++++++++++++++");
					continue;
				}
				if(equalAllocatedSceElm.get_otherWords() == null || equalAllocatedSceElm.get_otherWords().size() == 0) {
					//this word is Truly allocated as unique word.
					TP.add(uniqueWord);
					if(assessed.containsKey(uniqueWord._wordName)) {
						assessed.get(uniqueWord._wordName).add(uniqueWord);
//						print("added to assessed " + uniqueWord._wordName);
					}
					else {
						ArrayList<Word> newList = new ArrayList<Word>();
						newList.add(uniqueWord);
						assessed.put(uniqueWord._wordName, newList);
//						print("new to assessed " + uniqueWord._wordName);
					}
					print("TP: " + uniqueWord._wordName);
					continue;
				}
				else {//this word is Falsely allocated as redundant.
					FN.add(uniqueWord);						
					print("FN: " + uniqueWord._wordName);
				}
			}
			else {//this is a True redundant word
				
				ArrayList<Word> allRedundantWords = goldSceElm.getAllWords();
				
				if(allocatedSceneElems == null || allRedundantWords.size() <= 1) {
					MyError.error("inconsistent sceneElement " + goldSceElm);
					continue;
				}
				
				for(Word redunWrd: allRedundantWords) {
					
					SceneElement equalAllocatedSceElm = null;
					
					for(SceneElement curAllocScElm: allocatedSceneElems) {
						
						ArrayList<Word> curAllocScELmWords =  curAllocScElm.getAllWords();
	
						if(curAllocScELmWords == null || curAllocScELmWords.size() == 0)
							continue;
						
						for(Word wrd : curAllocScELmWords)
							if(wrd.equals(redunWrd))
								equalAllocatedSceElm = curAllocScElm;	
					}
					if(equalAllocatedSceElm == null) {
						MyError.error("the sceneElement with word \'" + redunWrd + "\' didn't find!");
						FP.add(redunWrd);
						print("FP PR: " + redunWrd._wordName + "+++++++++++++++++++++++++++++");
						continue;
					}
					if(equalAllocatedSceElm.get_otherWords() == null || equalAllocatedSceElm.get_otherWords().size() == 0) {
						//this word is wrongly allocated as unique word.
						FP.add(redunWrd);
//						if(assessed.containsKey(uniqueWord._wordName)) {
//							assessed.get(uniqueWord._wordName).add(uniqueWord);
////							print("added to assessed " + uniqueWord._wordName);
//						}
//						else {
//							ArrayList<Word> newList = new ArrayList<Word>();
//							newList.add(uniqueWord);
//							assessed.put(uniqueWord._wordName, newList);
////							print("new to assessed " + uniqueWord._wordName);
//						}
						print("FP: " + redunWrd._wordName);
						continue;
					}
					else {//this word is Truly allocated as redundant.
						TN.add(redunWrd);						
						print("TN: " + redunWrd._wordName);
					}				
				}
			}
		}
	}

	
	private double calculatePrecision(int TP, int FN, int FP, int TN) {
		double precision = (TP * 1.0)/((TP + FP));
		return precision * 100;
	}
	
	private double calculateRecall(int TP, int FN, int FP, int TN) {
		double precision = (TP * 1.0)/((TP + FN));
		return precision * 100;
	}
	
	private double calculateF1(double precision, double recall) {
		double F1 = (2 * precision * recall)/(precision + recall);
		return F1;
	}
	
	private static void print(String s){
		System.out.println(s);
	}
	

}
