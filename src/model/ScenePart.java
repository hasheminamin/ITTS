package model;

public enum ScenePart {
	ROLE,
	
//	SCENE_OBJECT,
	DYNAMIC_OBJECT,
	STATIC_OBJECT,
	
	LOCATION,
	
	TIME,
	
//	ACTION,
//	OBJECT_ACTION,
	DYNAMIC_OBJECT_ACTION,
	ROLE_ACTION,
	
//	EMOTION,
	ROLE_EMOTION,
	SCENE_EMOTION,
	
//	GOAL,
//	ROLE_GOAL,
	ROLE_INTENT,	
	SCENE_GOAL,	
	
//	ROAL_MOOD,
	ROLE_STATE,	
//	OBJECT_STATE,
	STATIC_OBJECT_STATE,
	DYNAMIC_OBJECT_STATE,
		
	NO, 
	JUNK;
//	UNKNOWN
	
	public static ScenePart fromString(String scenePart){
		if (scenePart != null)
			for (ScenePart sp : ScenePart.values()) 
				if (scenePart.equalsIgnoreCase(sp.name())) 
					return sp;
       return NO;
	}
}