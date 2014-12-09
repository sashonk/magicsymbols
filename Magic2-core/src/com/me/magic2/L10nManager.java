package com.me.magic2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class L10nManager {
	
	public L10nManager(FileHandle l10nDirectory) {
		FileHandle root = l10nDirectory;

		try{
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		for(FileHandle langSource : root.list()){
			Document document =  db.parse(langSource.read());
			Element l10n =  document.getDocumentElement();
			String lang = l10n.getAttribute("lang");
			
			Map<String, String> data = new HashMap<String, String>();
			localizations.put(lang, data);
			
			NodeList entries =  l10n.getElementsByTagName("entry");
			for(int i = 0 ; i<entries.getLength(); i++){
				Element entry = (Element) entries.item(i);
				String name =  entry.getElementsByTagName("name").item(0).getTextContent().trim();
				Element value = (Element) entry.getElementsByTagName("value").item(0);
				NodeList lines = value.getElementsByTagName("line");
				StringBuilder sb = new StringBuilder();
				if(lines.getLength()>0){
					int c = 0;
					for(int j = 0; j <lines.getLength(); j++){
						if(c>0){
							sb.append('\n');
						}
						Element line = (Element) lines.item(j);
						sb.append(line.getTextContent().trim());
						c++;
					}
				}
				else{
					sb.append(value.getTextContent().trim());
				}
				
				data.put(name, sb.toString());
			}
		
			
			
			
		}
	
		
		}
		catch(Throwable t){
			throw new RuntimeException(t);
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
		Map<String, String> l10n = localizations.get(lang);
		if(l10n==null){
			l10n = localizations.get("en");
			if(l10n==null){
				l10n = localizations.values().iterator().next();
			}
		}
		
		String result =  l10n.get(name);
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
	
	static class Localization{
		
		Localization(String lang){
			_entries = new HashMap<String, L10nManager.Localization.Entry>();
			_lang = lang;
		}
		
		static class Entry{
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public List<String> getLines() {
				return lines;
			}
			public void setLines(List<String> lines) {
				this.lines = lines;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			private String name;
			private List<String> lines;
			private String value;
		}
		
		private Map<String, Entry> _entries;
		
		public Map<String, Entry> get_entries() {
			return _entries;
		}

		public void set_entries(Map<String, Entry> _entries) {
			this._entries = _entries;
		}

		public String get_lang() {
			return _lang;
		}

		public void set_lang(String _lang) {
			this._lang = _lang;
		}

		private String _lang;
	}
}
