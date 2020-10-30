package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import models.Entity;



public class JSONImplementation extends API{


	public Object importFileToObject(String path, Class<?> classOf) {

		String json = fileToString(path);
		String[] input = json.split("},");
		//for (String s : input) {
			System.out.println(input[0]);
		//}
		Gson gson = new Gson();
		Object object = gson.fromJson(json, classOf);

		return null;
	}
	
	private String fileToString(String filePath) {

		final StringBuilder contentBuilder = new StringBuilder();
		File file = new File("");
		//Paths.get(file.getAbsolutePath() + filePath)
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(new Consumer<String>() {
				public void accept(String s) {
					contentBuilder.append(s).append("\n");
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
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
	
	
	
	

}
