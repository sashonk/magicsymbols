package com.me.magic2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class L10nManager {
	
	public L10nManager(String...langs){
		FileHandle root = Gdx.files.internal("data/l10n/");
		
		for(String lang : langs){
			FileHandle file = root.child(lang+".txt");
			if(!file.exists()){
				throw new RuntimeException("l10n file not found: "+lang);
			}
			
			Map<String, String> localization =new HashMap<String, String>();
			localizations.put(lang, localization);
			
			BufferedReader br =null;
			try{ 
				br = new BufferedReader(file.reader());
				
				while(true){
					String line = br.readLine();
					if(line==null){
						break;
					}
					
					String[] parts = line.split("=");
					String name = parts[0].trim();
					
					String rawValue = parts[1].trim();
					//StringBuilder value =  new StringBuilder(parts[1].trim()) ;
					StringBuilder value = new StringBuilder();
					
					if(rawValue.indexOf('[')>-1){
						while(true){
							if(rawValue==null){
								break;
							}
							
							int begin = 0;
							int end = rawValue.length();
							
							boolean exit = false;
							if(rawValue.indexOf("[")>-1){
								begin = rawValue.indexOf("[")+1;
							}
							
							if(rawValue.indexOf("]")>-1){
								end = rawValue.indexOf("]");
								exit = true;
							}
							
							value.append(rawValue.substring(begin, end));
							
							if(exit){
								break;
							}
							else{
								value.append('\n');
								rawValue = br.readLine();
							}
							
							
						}						
					}
					else{
						value.append(parts[1].trim());
					}
					
					if(name.length()==0 || value.length()==0){
						continue;
					}
					
					
					localization.put(name, value.toString());
				}
			}
			catch(Exception ex){
				Gdx.app.error(L10nManager.this.getClass().getName(), "error reading file" ,ex);
			}
			finally{
				if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Gdx.app.error(L10nManager.this.getClass().getName(), "error closing stream" ,e);
						
					}
				}
			}
			
			
			
		}
	
	}
	
	
	public String getLanguage(){
		return language;
	}
	
	private String language;
	public void setLanguage(String lang){
		language = lang;

		for(L10nLabel label : labels){
			Object[] params = label.getParams();
			String key = label.getKey();
			String text = params!=null ? Util.replace(get(key), params) : get(key);
			label.setText(text);
			label.pack();
		}
		
		for(L10nButton button : buttons){
			String key = button.getKey();
			String text = get(key);
			button.setText(text);
			button.pack();
		}
	}
	
	public void registerLabel(L10nLabel label){
		labels.add(label);
	}
	
	public void registerButton(L10nButton label){
		buttons.add(label);
	}
	
	private List<L10nLabel> labels= new LinkedList<L10nLabel>();
	
	private List<L10nButton> buttons= new LinkedList<L10nButton>();
	
	public String get(String name, String lang){

		String result =  localizations.get(lang).get(name);
		if(result==null){
			result = String.format("[unlocalized:%s]", name);
		}
		
		return result;
	
	}

	public String get(String name){
		String result =  get(name, language);
		
		return result;
		}
	

	
	public Map<String, String> getLocalizedLanguages(){
		Map<String, String> result = new TreeMap<String, String>();
		for(String lang : localizations.keySet()){
			String localizedLang = get(lang);

			
			result.put(lang, localizedLang);
		}
		
		return Collections.unmodifiableMap(result);
	}
	
	public Set<String> getLanguages(){
		return Collections.unmodifiableSet(localizations.keySet());
	}

	Map<String, Map<String, String>> localizations = new HashMap<String, Map<String, String>>();
	
}
