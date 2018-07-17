package userInterface;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Sentence;
import model.Scene;
import model.Story;
import model.Word;

public class UI {
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
	private static String inputCorporaPath = "dataset/97-04-17CRFcorpora-junk-sceneMarker.arff";
	
	public static ArrayList<Story> importInputCorpora(String filename)
	{
		ArrayList<Story> allStories = new ArrayList<Story>();
		
		Story currentStory = null;
		Scene currentScene = null;
		Sentence currentSentence = null;
		int sentenceNumber = 0;
		
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
					
					currentStory = new Story();
					print("newStory*************************************");
					continue;
				}
				//TODO: check correct assignemtn of scene of a story and sentcens of a scene
				if (line.startsWith("#scene#")){ // beginning of new scene in the corpora
					if(currentStory != null){
						//TODO: check here
						currentScene = new Scene(currentStory);
						currentStory.addScene(currentScene);						
						
						currentSentence = null;
					}
					else
						MyError.error("currentStory should not be null!!!");					
					print("newScene^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
					continue;
				}
				
				if(line.equals("")){
					
					if(currentStory != null){
						if(currentScene != null){
							if(currentSentence == null){// beginning of new sentence in the corpora
								currentSentence = new Sentence(currentScene);								
							}
							else{// end of currentSentence
								currentSentence.makeNaturalLanguage();
								currentScene.addSentence(currentSentence);//we have done before this assignment!								
								currentSentence = new Sentence(currentScene);
								sentenceNumber++;
								print("-"+sentenceNumber + "-");																
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
				
				if(currentSentence == null)
					MyError.error("currentStory should not be null!!!");
				else
					currentSentence.addWord(currentWord);
				
				print("" + currentWord);
			}
			print("sentece number is " + sentenceNumber);
			stream.close();		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return allStories;
	}

	private static void print(String s){
		System.out.println(s);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		print("بسم الله الرحمن الرحیم و توکلت علی الله");
		ArrayList<Story> allStories = importInputCorpora(inputCorporaPath);
		print("الحمدلله رب العالمین");
	}

}
