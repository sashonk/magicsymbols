package com.me.magic2;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LangDialog extends Dialog{

	
	public LangDialog(final IGame igame) {
		super("", igame.getManager().getSkin(), "default");
		Skin s = igame.getManager().getSkin();
		
		final Dialog langDialog = this;//new Dialog("",s, "default");
		langDialog.padTop(15);
		langDialog.setBackground("dialog");
		
		Table lang_en = new Table();				
		Button en = new ImageButton(s, "flag_en");
		lang_en.add(en).row();
		Label text_en = new Label("english", s, "tiny");
		lang_en.add(text_en);
		lang_en.pack();
		
		Table lang_ru = new Table();				
		Button ru = new ImageButton(s, "flag_ru");
		lang_ru.add(ru).row();
		Label text_ru = new Label("русский", s, "tiny");
		lang_ru.add(text_ru);
		lang_ru.pack();
		
		langDialog.getContentTable().add(lang_ru);
		langDialog.getContentTable().add(lang_en);
		//langDialog.show(getStage());
		
		
		lang_ru.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				String lang = igame.getL10n().getLanguage();
				if(!"ru".equals(lang)){
					igame.getL10n().setLanguage("ru");
				}
				langDialog.hide();
			
				Settings s = igame.getSettings();
				s.setLanguage("ru");
				s.write();
			}
		});
		
		
		lang_en.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {

				String lang =  igame.getL10n().getLanguage();
				if(!"en".equals(lang)){
					igame.getL10n().setLanguage("en");
				}
				langDialog.hide();
			
				Settings s = igame.getSettings();
				s.setLanguage("en");
				s.write();
			}
		});
	
	}

	

}
