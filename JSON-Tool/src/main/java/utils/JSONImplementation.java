package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import models.Entity;
import utils.API;



public class JSONImplementation extends API{

	static {
		ToolManager.registerManager(new JSONImplementation(), ".json");
	}
	
	public JSONImplementation() {
		super();
	}
	
	@Override
	public void openFile(File file){
		
		JsonReader jr;
		try {
			jr = new JsonReader(new FileReader(file));
			jr.beginArray();
			while(jr.hasNext()) {
				Entity e = new Entity();
				
				jr.beginObject();
				while(jr.hasNext()) {
					
					String name = jr.nextName();
					
					if (jr.peek() == JsonToken.BEGIN_OBJECT) {
						Entity nested = new Entity();
						jr.beginObject();
						while (jr.hasNext()) {
							String name1 = jr.nextName();
							if (name1.equals("naziv")) {
								nested.setName(jr.nextString());
							}else if (name1.equals("id")) {
								nested.setId(jr.nextString());
							}else {
								nested.getProperties().put(name1, jr.nextString());
							}
						} e.getEntities().put(name, nested);
						jr.endObject();
					}else {
						String value = jr.nextString();
						if (name.equals("naziv")) {
							e.setName(value);
						}else if (name.equals("id")) {
							e.setId(value);
						}else {
							e.getProperties().put(name, value);
						}
						
					}
				}
				entities.add(e);
				jr.endObject();
				
			}
			jr.endArray();
			System.out.println(entities);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (JsonSyntaxException e) {
			e.printStackTrace();
		}catch (JsonIOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override	/// pokrece se u GUI za instancu API-a
	public void save(List<Entity> data, String fileName) {
		try {
            FileWriter fileWriter = new FileWriter(new File(fileName));
            JsonArray sArray = new JsonArray();
            for(Entity e:data) {
                JsonObject json = new JsonObject();
                json.addProperty("id",e.getId());
                json.addProperty("name", e.getName());
                for (Map.Entry<String,Object> property : e.getProperties().entrySet()) {
                	json.addProperty(property.getKey(), (String) property.getValue());
                }
                for (Map.Entry<String,Entity> entity : e.getEntities().entrySet()) {
                	json.addProperty(entity.getKey(),  entity.getValue().toString());
                }
                sArray.add(json); 
            }
            fileWriter.write(sArray.toString());            
            fileWriter.close();       
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
	}

	
	
	
	
	

}
