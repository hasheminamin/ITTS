package userInterface;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.sun.xml.internal.ws.client.dispatch.DataSourceDispatch;

import enums.POS;
import evaluation.Evaluator;
import sceneElement.DynamicObject;
import sceneElement.DynamicObjectState;
import sceneElement.Role;
import sceneElement.RoleAction;
import sceneElement.RoleIntent;
import sceneElement.RoleState;
import sceneElement.StaticObject;
import sceneElement.StaticObjectState;
import model.SceneModel;
import model.StoryModel;
import model.Word;

@SuppressWarnings("unused")
public class LearningUI {
	
//	private static String inputCorporaPath = "dataset/97-04-17CRFcorpora-junk-sceneMarker.arff";
//	private static String inputCorporaPath = "dataset/97-05-03CRFcorpora-junk-sceneMarker-corrections-story1.arff";	
//	private static String inputCorporaPath = "dataset/97-04-31CRFcorpora-junk-sceneMarker-corrections.arff";
//	private static String inputCorporaPath = "dataset/97-05-10CRFcorpora-junk-sceneMarker-corrections-story1_role_state_action.arff";
	
	
//	private static String roleActionDataset = "dataset/97-05-08roleActionDataset.arff";
////	private static String roleStateDataset = "dataset/97-05-08roleStateDataset.arff";
//	private static String roleStateDataset = "dataset/97-05-14roleStateDataset.arff";
//	private static String roleIntentDataset = "dataset/97-05-08roleIntentDataset.arff";
//	private static String staticObjectStateDataset = "dataset/97-05-08staticObjectStateDataset.arff";
//	private static String dynamicObjectStateDataset = "dataset/97-05-08dynamicObjectStateDataset.arff";
	
	
//	/**
//	 * produce a dataSet file for learning the association between each RoleAction sceneElement of a scene and Roles of that scene.
//	 * each word features:
//	 * V VCONJ null رخداد§n-13136 role_action 
//	 * produced dataset sample features:
//	 * V ROOT null رخداد§n-13136 role_action N SBJ Arg0 نفر§n-13075 role yes
//	 * V PRD null رخداد§n-13136 role_action N NPP Arg3 نفر§n-13075 no 
//	 * @param allStories
//	 */
//	public static void produceRoleActionDataset(ArrayList<StoryModel> allStories){
//		
//		try{		
//		
//			PrintWriter writer = new PrintWriter(roleActionDataset, "UTF-8");
//			
//			for(StoryModel storyModel: allStories){
//				
//				print("newStory*************************************");
//				
//				for(SceneModel sceneModel:storyModel.scenes){
//					
//					print("newScene*************************************");
//					
//					//format: V VCONJ null رخداد§n-13136 role_action N SBJ  Arg0 null role yes
//					String newRoleActionSample = "";
//					
//					for(RoleAction roleAction: sceneModel.roleActions){				
//						
//						if(roleAction == null || roleAction.getElementDatasetStrs() == null || roleAction.getElementDatasetStrs().size() == 0)
//							continue;
//							
//						for(String roleActiondatasetStr: roleAction.getElementDatasetStrs()){
//							
//							newRoleActionSample =  roleActiondatasetStr + " ";
//							
//							for(Role role: sceneModel.getRoles()){
//							
//								if(role == null || role.getElementDatasetStrs() == null || role.getElementDatasetStrs().size() == 0)
//									continue;
//																								
//								for(String roleDatasetStr: role.getElementDatasetStrs()){
//							
//									newRoleActionSample +=  roleDatasetStr;
//									
//									print(newRoleActionSample);
//									
//									writer.println(newRoleActionSample);
//									
//									newRoleActionSample = roleActiondatasetStr + " ";
//								}
//								
//								newRoleActionSample = roleActiondatasetStr + " ";								
//							}
//							
////							newRoleActionSample = "";
//						}
//					}
//					print("");
//					writer.println();					
//				}
//			}
//			writer.close();
//		}
//		catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	
//		
//		
//	}
//	
//	/**
//	 * produce a dataSet file for learning the association between each RoleState sceneElement of a scene and Roles of that scene.
//	 * each word features:
//	 * ADJ NPOSTMOD null نفر§n-13075 role_state 
//	 * produced dataset sample features:
//	 * ADJ NPOSTMOD null نفر§n-13075 role_state N AJPP null نفر§n-13075 role yes
//	 * ADJ AJCONJ null نفر§n-13075 role_state PR SBJ Arg0 null role no 
//	 * @param allStories
//	 */
//	public static void produceRoleStateDataset(ArrayList<StoryModel> allStories){
//		
//		try{		
//		
//			PrintWriter writer = new PrintWriter(roleStateDataset, "UTF-8");
//			
//			for(StoryModel storyModel: allStories){
//				
//				print("newStory*************************************");
//				
//				for(SceneModel sceneModel:storyModel.scenes){
//					
//					print("newScene*************************************");
//					
//					//format: ADJ NPOSTMOD null نفر§n-13075 role_state N AJPP null نفر§n-13075 role yes
//					String newRoleStateSample = "";
//					
//					for(RoleState roleState: sceneModel.roleStates){				
//						
//						if(roleState == null || roleState.getElementDatasetStrs() == null || roleState.getElementDatasetStrs().size() == 0)
//							continue;
//							
//						for(String roleStatedatasetStr: roleState.getElementDatasetStrs()){
//							
//							newRoleStateSample =  roleStatedatasetStr + " ";
//							
//							for(Role role: sceneModel.getRoles()){
//							
//								if(role == null || role.getElementDatasetStrs() == null || role.getElementDatasetStrs().size() == 0)
//									continue;
//																								
//								for(String roleDatasetStr: role.getElementDatasetStrs()){
//							
//									newRoleStateSample +=  roleDatasetStr;
//									
//									print(newRoleStateSample);
//									
//									writer.println(newRoleStateSample);
//									
//									newRoleStateSample = roleStatedatasetStr + " ";
//								}
//								
//								newRoleStateSample = roleStatedatasetStr + " ";								
//							}
//							
////							newRoleActionSample = "";
//						}
//					}
//					print("");
//					writer.println();					
//				}
//			}
//			writer.close();
//		}
//		catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}	
//		
//	}
//
//
//	/**
//	 * produce a dataSet file for learning the association between each RoleIntent sceneElement of a scene and Roles of that scene.
//	 * each word features:
//	 * ADJ MOS Arg2 مشخصه§n-12756 role_intent 
//	 * produced dataset sample features:
//	 * ADJ MOS Arg2 مشخصه§n-12756 role_intent PR SBJ Arg0 نفر§n-13075 role yes
//	 * N NVE null خبر§n-12766 role_intent N MOZ null نفر§n-13075 role no
//	 * @param allStories
//	 */
//	public static void produceRoleIntentDataset(ArrayList<StoryModel> allStories){
//		
//		try{		
//		
//			PrintWriter writer = new PrintWriter(roleIntentDataset, "UTF-8");
//			
//			for(StoryModel storyModel: allStories){
//				
//				print("newStory*************************************");
//				
//				for(SceneModel sceneModel:storyModel.scenes){
//					
//					print("newScene*************************************");
//					
//					//format: ADJ MOS Arg2 مشخصه§n-12756 role_intent PR SBJ Arg0 نفر§n-13075 role yes
//					String newRoleIntentSample = "";
//					
//					for(RoleIntent roleIntent: sceneModel.roleIntents){				
//						
//						if(roleIntent == null || roleIntent.getElementDatasetStrs() == null || roleIntent.getElementDatasetStrs().size() == 0)
//							continue;
//							
//						for(String roleIntentdatasetStr: roleIntent.getElementDatasetStrs()){
//							
//							newRoleIntentSample =  roleIntentdatasetStr + " ";
//							
//							for(Role role: sceneModel.getRoles()){
//							
//								if(role == null || role.getElementDatasetStrs() == null || role.getElementDatasetStrs().size() == 0)
//									continue;
//																								
//								for(String roleDatasetStr: role.getElementDatasetStrs()){
//							
//									newRoleIntentSample +=  roleDatasetStr;
//									
//									print(newRoleIntentSample);
//									
//									writer.println(newRoleIntentSample);
//									
//									newRoleIntentSample = roleIntentdatasetStr + " ";
//								}
//								
//								newRoleIntentSample = roleIntentdatasetStr + " ";								
//							}
//							
////							newRoleActionSample = "";
//						}
//					}
//					print("");
//					writer.println();					
//				}
//			}
//			writer.close();
//		}
//		catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}	
//		
//	}
//
//	/** 
//	 * produce a dataSet file for learning the association between each StaticObjectState sceneElement of a scene and Static_object of that scene.
//	 * each word features:
//	 * ADJ NPOSTMOD null مشخصه§n-12756 static_object_state 
//	 * produced dataset sample features:
//	 * ADJ NPOSTMOD null مشخصه§n-12756 static_object_state N OBJ Arg1 شيء§n-12703 static_object yes
//	 * ADJ NPOSTMOD null مشخصه§n-12756 static_object_state N MOZ null شيء§n-12703 static_object no 
//	 * @param allStories
//	 */
//	public static void produceStaticObjectDataset(ArrayList<StoryModel> allStories){
//		
//		try{		
//		
//			PrintWriter writer = new PrintWriter(staticObjectStateDataset, "UTF-8");
//			
//			for(StoryModel storyModel: allStories){
//				
//				print("newStory*************************************");
//				
//				for(SceneModel sceneModel:storyModel.scenes){
//					
//					print("newScene*************************************");
//					
//					//format: ADJ NPOSTMOD null مشخصه§n-12756 static_object_state N OBJ Arg1 شيء§n-12703 static_object yes
//					String newStaticOjectStateSample = "";
//					
//					for(StaticObjectState stat_obj_state: sceneModel.static_object_states){				
//						
//						if(stat_obj_state == null || stat_obj_state.getElementDatasetStrs() == null || stat_obj_state.getElementDatasetStrs().size() == 0)
//							continue;
//							
//						for(String stat_obj_state_datasetStr: stat_obj_state.getElementDatasetStrs()){
//							
//							newStaticOjectStateSample =  stat_obj_state_datasetStr + " ";
//							
//							for(StaticObject static_object: sceneModel.getStatic_objects()){
//							
//								if(static_object == null || static_object.getElementDatasetStrs() == null || static_object.getElementDatasetStrs().size() == 0)
//									continue;
//																								
//								for(String static_objectDatasetStr: static_object.getElementDatasetStrs()){
//							
//									newStaticOjectStateSample +=  static_objectDatasetStr;
//									
//									print(newStaticOjectStateSample);
//									
//									writer.println(newStaticOjectStateSample);
//									
//									newStaticOjectStateSample = stat_obj_state_datasetStr + " ";
//								}
//								
//								newStaticOjectStateSample = stat_obj_state_datasetStr + " ";								
//							}
//							
////							newRoleActionSample = "";
//						}
//					}
//					print("");
//					writer.println();					
//				}
//			}
//			writer.close();
//		}
//		catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}	
//		
//	}
//		
//	/** 
//	 * produce a dataSet file for learning the association between each DynamicObjectState sceneElement of a scene and Static_object of that scene.
//	 * each word features:
//	 * ADJ NPOSTMOD null مشخصه§n-12756 dynamic_object_state 
//	 * produced dataset sample features:
//	 * ADJ NPOSTMOD null مشخصه§n-12756 dynamic_object_state N OBJ Arg1 شيء§n-12703 dynamic_object yes
//	 * ADJ NPOSTMOD null مشخصه§n-12756 dynamic_object_state N MOZ null شيء§n-12703 dynamic_object no
//	 * @param allStories
//	 */
//	public static void produceDynamicObjectDataset(ArrayList<StoryModel> allStories){
//		
//		try{		
//		
//			PrintWriter writer = new PrintWriter(dynamicObjectStateDataset, "UTF-8");
//			
//			for(StoryModel storyModel: allStories){
//				
//				print("newStory*************************************");
//				
//				for(SceneModel sceneModel:storyModel.scenes){
//					
//					print("newScene*************************************");
//					
//					//format: ADJ NPOSTMOD null مشخصه§n-12756 dynamic_object_state N OBJ Arg1 شيء§n-12703 dynamic_object yes
//					String newDynamicOjectStateSample = "";
//					
//					for(DynamicObjectState dyn_obj_state: sceneModel.dynamic_object_states){				
//						
//						if(dyn_obj_state == null || dyn_obj_state.getElementDatasetStrs() == null || dyn_obj_state.getElementDatasetStrs().size() == 0)
//							continue;
//							
//						for(String dyn_obj_state_datasetStr: dyn_obj_state.getElementDatasetStrs()){
//							
//							newDynamicOjectStateSample =  dyn_obj_state_datasetStr + " ";
//							
//							for(DynamicObject dynamic_object: sceneModel.getDynamic_objects()){
//							
//								if(dynamic_object == null || dynamic_object.getElementDatasetStrs() == null || dynamic_object.getElementDatasetStrs().size() == 0)
//									continue;
//																								
//								for(String dynamic_objectDatasetStr: dynamic_object.getElementDatasetStrs()){
//							
//									newDynamicOjectStateSample +=  dynamic_objectDatasetStr;
//									
//									print(newDynamicOjectStateSample);
//									
//									writer.println(newDynamicOjectStateSample);
//									
//									newDynamicOjectStateSample = dyn_obj_state_datasetStr + " ";
//								}
//								
//								newDynamicOjectStateSample = dyn_obj_state_datasetStr + " ";								
//							}
//							
////							newRoleActionSample = "";
//						}
//					}
//					print("");
//					writer.println();					
//				}
//			}
//			writer.close();
//		}
//		catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}	
//		
//	}
//		
	public static void evaluate(ArrayList<StoryModel> allStories) {
		
		/**these 4 line were for producing the "gold StoryModels" and are produced based on the allStories produced after
		 * ArrayList<StoryModel> allStories = Dashboard.importInputCorpora(Dashboard.inputCorporaPath);
		 * Dashboard.importSystemOutput(Dashboard.inputSystemOutpurPath, allStories);
		 * Dashboard.sceneReasoner.arrangeWordReferences(allStories, POS.PR);
		 * Dashboard.sceneReasoner.arrangeSceneModelsElements(allStories);
		 */
		/*
		Dashboard.sceneReasoner.printForGoldSceneModel(allStories, Dashboard.goldModelfile);
		
		ArrayList<StoryModel> goldStoryModels = Dashboard.sceneReasoner.readGoldStoryModels(Dashboard.goldModelfile);
		
		Dashboard.sceneReasoner.correct_main_otherWordsPlaces(goldStoryModels);
		
		Dashboard.sceneReasoner.printForGoldSceneModel(goldStoryModels, Dashboard.goldModelCorrectedfile);
		*/

		//the file "Dashboard.goldModelCorrectedfile" contain the gold StoryModels and can be imported for evaluation.
		ArrayList<StoryModel> goldStoryModels = Dashboard.sceneReasoner.readGoldStoryModels(Dashboard.goldModelCorrectedfile);
		
		Evaluator evaluator = new Evaluator(goldStoryModels);
		
		evaluator.evaluateAllSceneElementPrimaryAllocation(allStories);
		
		evaluator.evaluateAllSceneElementNonRedundancyDetection(allStories);

	}
			
	
	public static void main(String[] args){		
		print("بسم الله الرحمن الرحیم و توکلت علی الله");
				
		ArrayList<StoryModel> allStories = Dashboard.importInputCorpora(Dashboard.inputCorporaPath);
		
		Dashboard.importSystemOutput(Dashboard.inputSystemOutpurPath, allStories);
		
//		Dashboard.sceneReasoner.calculateMultiSemanticTagWords(allStories);
		
//		Dashboard.sceneReasoner.calculateWronglyPredictedWords(allStories);
		
//		Dashboard.sceneReasoner.calculateTruelyPredictedWords(allStories);
	
		Dashboard.sceneReasoner.arrangeWordReferences(allStories, POS.PR);
		
		Dashboard.sceneReasoner.arrangeSceneModelsElements(allStories);
		
//		Dashboard.sceneReasoner.calculateRepeatedWords(allStories);
		
//		evaluate(allStories);		
		
		Dashboard.sceneReasoner.arrangeWordReferences(allStories, null);//all referents will be set.
		
		Dashboard.sceneReasoner.allocateRoleStates(allStories);
//		Dashboard.sceneReasoner.allocateRoleStatesWithChecking(allStories);
		
//		Dashboard.sceneReasoner.allocateRoleActions(allStories);
//		Dashboard.sceneReasoner.allocateRoleActionsWithChecking(allStories);
		
//		Dashboard.sceneReasoner.completeSceneModelsElements(allStories);
		
//		produceRoleActionDataset(allStories);		
		
//		produceRoleStateDataset(allStories);
		
//		produceRoleIntentDataset(allStories);
	
//		produceStaticObjectDataset(allStories);
		
//		produceDynamicObjectDataset(allStories);
		
		//here main elements of SceneModels of these stories are allocated correctly.
		//and depended elements are stored in arrays to be processed and got allocated here!
		
		print("الحمدلله رب العالمین");
		
	}
	
	private static void print(String toPrint){
		System.out.println(toPrint);		
	}	


}

