package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import utils.ToolManager;
import utils.JSONImplementation;


public class JSONImplementation extends API {
	public String a;
	static {
		ToolManager.registerManager(new JSONImplementation(), ".json");
	}

	public JSONImplementation() {
		super();
	}

	@Override
	public void openFile(File file) {

		JsonReader jr;
		try {
			jr = new JsonReader(new FileReader(file));
			jr.beginArray();
			while (jr.hasNext()) {
				Entity e = new Entity();

				jr.beginObject();
				while (jr.hasNext()) {

					String name = jr.nextName();

					if (jr.peek() == JsonToken.BEGIN_OBJECT) {
						Entity nested = new Entity();
						jr.beginObject();
						while (jr.hasNext()) {
							String name1 = jr.nextName();
							System.out.println(name1 + " !@#!@#!@##!@");
							if (name1.equals("name")) {
								nested.setName(jr.nextString());
							} else if (name1.equals("id")) {
								nested.setId(jr.nextString());
							} else {
								System.out.println("usao");
								nested.getProperties().put(name1, jr.nextString());
							}
						}
						e.getEntities().put(name, nested);
						jr.endObject();
					} else {
						String value = jr.nextString();
						if (name.equals("name")) {
							e.setName(value);
						} else if (name.equals("id")) {
							e.setId(value);
						} else {
							e.getProperties().put(name, value);
						}

					}
				}
				entities.add(e);
				jr.endObject();
				
			}
			jr.endArray();
			jr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		}

	}

	@Override /// pokrece se u GUI za instancu API-a
	public void save(List<Entity> data, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(new File(fileName));
			JsonArray sArray = new JsonArray();
			for (Entity e : data) {
				JsonObject json = new JsonObject();
				json.addProperty("id", e.getId());
				json.addProperty("name", e.getName());
				for (Map.Entry<String, Object> property : e.getProperties().entrySet()) {
					json.addProperty(property.getKey(), (String) property.getValue());
				}
				for (Map.Entry<String, Entity> entity : e.getEntities().entrySet()) {
					json.addProperty(entity.getKey(), entity.getValue().toString());
				}
				sArray.add(json);
			}
			fileWriter.write(sArray.toString());
			fileWriter.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void enterEntity(String tekst) {
		// TODO Auto-generated method stub

	}

	@Override
	public void brisiNaOsnovuIDa(String id, String dirPath) {
		entities.clear();
		File directory = new File(dirPath);
		
		File[] contents = directory.listFiles();
		
		for (File f : contents) {
			List<Entity> entities1 = new ArrayList<Entity>();
			if (f.length() > 0) {
				JsonReader jr;
				try {
					jr = new JsonReader(new FileReader(f));
					jr.beginArray();
					while (jr.hasNext()) {
						Entity e = new Entity();

						jr.beginObject();
						while (jr.hasNext()) {

							String name = jr.nextName();

							if (jr.peek() == JsonToken.BEGIN_OBJECT) {
								Entity nested = new Entity();
								jr.beginObject();
								while (jr.hasNext()) {
									String name1 = jr.nextName();
									if (name1.equals("name")) {
										nested.setName(jr.nextString());
									} else if (name1.equals("id")) {
										nested.setId(jr.nextString());
									} else {
										nested.getProperties().put(name1, jr.nextString());
									}
								}
								if (!nested.getId().equals(id))
									e.getEntities().put(name, nested);
								jr.endObject();
							} else {
								String value = jr.nextString();
								if (name.equals("name")) {
									e.setName(value);
								} else if (name.equals("id")) {
									e.setId(value);
								} else {
									e.getProperties().put(name, value);
								}

							}
						}
						if (!e.getId().equals(id)) {
							entities1.add(e);
						}
						jr.endObject();

					}
					jr.endArray();
					jr.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				try {
					FileWriter fileWriter = new FileWriter(new File(f.getAbsolutePath()));
					JsonArray sArray = new JsonArray();
					for (Entity e : entities1) {
						JsonObject json = new JsonObject();
						json.addProperty("id", e.getId());
						json.addProperty("name", e.getName());
						for (Map.Entry<String, Object> property : e.getProperties().entrySet()) {
							json.addProperty(property.getKey(), (String) property.getValue());
						}
						for (Map.Entry<String, Entity> entity : e.getEntities().entrySet()) {
							json.addProperty(entity.getKey(), entity.getValue().toString());
						}
						sArray.add(json);
					}
					fileWriter.write(sArray.toString());
					fileWriter.close();
					entities.addAll(entities1);
				} catch (Exception e) {

					e.printStackTrace();
				}
				
			}
		}	
	
	}

	@Override
	public void brisiKljucVrednost(String name_, String key, String value_, String dirPath) {
		entities.clear();
		File directory = new File(dirPath);
		File[] contents = directory.listFiles();
		
		for (File f : contents) {
			List<Entity> entities1 = new ArrayList<Entity>();
			if (f.length() > 0) {
				JsonReader jr;
				try {
					jr = new JsonReader(new FileReader(f));
					jr.beginArray();
					while (jr.hasNext()) {
						Entity e = new Entity();

						jr.beginObject();
						while (jr.hasNext()) {

							String name = jr.nextName();

							if (jr.peek() == JsonToken.BEGIN_OBJECT) {
								Entity nested = new Entity();
								jr.beginObject();
								while (jr.hasNext()) {
									String name1 = jr.nextName();
									if (name1.equals("name")) {
										nested.setName(jr.nextString());
									} else if (name1.equals("id")) {
										nested.setId(jr.nextString());
									} else {
										String vrednost = jr.nextString();
										if ((!name1.equals(name_) && !vrednost.equals(value_)) || (name_.isEmpty() || vrednost.isEmpty()))
											nested.getProperties().put(name1, vrednost);
									}
								}
								if (!name.equals(name_) || name_.isEmpty()) {
									e.getEntities().put(name, nested);
								}
								
								jr.endObject();
							} else {
								String value = jr.nextString();
								if (name.equals("name")) {
									e.setName(value);
								} else if (name.equals("id")) {
									e.setId(value);
								} else {
									e.getProperties().put(name, value);
								}

							}
						}
						if (!e.getName().equals(name_) || name_.isEmpty()) {
							/// iterirati kroz propertije i proveravati da li se za kljuc nalazi ta vrednost i ako se ne nalazi dodati je
//							if (!key.isEmpty() && !value_.isEmpty()) {
//								for (Map.Entry<String,Object> entry : e.getProperties().entrySet()) {  
//
//									if (!entry.getKey().equals(key) && !entry.getValue().equals(value_)) {
//						            	entities1.add(e); /// MISLIM DA CE OVDE DA DUPLIRA
//						            }
//									
//								}
//							}
							//else {
								entities1.add(e);
						//	}
						}
						jr.endObject();

					}
					jr.endArray();
					jr.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				try {
					FileWriter fileWriter = new FileWriter(new File(f.getAbsolutePath()));
					JsonArray sArray = new JsonArray();
					for (Entity e : entities1) {
						JsonObject json = new JsonObject();
						json.addProperty("id", e.getId());
						json.addProperty("name", e.getName());
						for (Map.Entry<String, Object> property : e.getProperties().entrySet()) {
							json.addProperty(property.getKey(), (String) property.getValue());
						}
						for (Map.Entry<String, Entity> entity : e.getEntities().entrySet()) {
							json.addProperty(entity.getKey(), entity.getValue().toString());
						}
						sArray.add(json);
					}
					fileWriter.write(sArray.toString());
					fileWriter.close();
					entities.addAll(entities1);
				} catch (Exception e) {

					e.printStackTrace();
				}
				
			}
		}
	}

}
