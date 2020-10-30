package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

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
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
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
				
				jr.beginObject();
				while(jr.hasNext()) {
					String name = jr.nextName();
					System.out.println(name);
					if (jr.nextString().contains("{")) {
						jr.skipValue();
					}
					
				}
				
				
				
//				jr.beginObject();
//				while (jr.peek() != JsonToken.END_OBJECT) {
//					System.out.println(jr.nextName());
//					System.out.println(jr.nextString());
//					if (jr.peek() == JsonToken.BEGIN_OBJECT) { /// Kako da proverimo da li je trenutni BeginObject
//						System.out.println("USAO");
//						jr.beginObject();
//						while (jr.peek() != JsonToken.END_OBJECT) {
//							System.out.println(jr.nextName());
//							System.out.println(jr.nextString());
//						}
//					}
//					
//					
//				}
//				jr.endObject();
			}
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
