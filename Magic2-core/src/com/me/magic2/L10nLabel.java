package com.me.magic2;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class L10nLabel extends Label{
	L10nManager _l10n;
	String _key;
	Object[] _params;
	
	public Object[] getParams(){
		return _params;
	}
	
	public L10nLabel(L10nManager mgr, String l10nKey, Skin skin, String styleName) {
		super(mgr.get(l10nKey), skin, styleName);
		// TODO Auto-generated constructor stub
		_l10n = mgr;
		_key = l10nKey;
		
		_l10n.registerLabel(this);
	}
	
	public L10nLabel(L10nManager mgr, String l10nKey, Skin skin, String styleName, Object...params) {
		super(Util.replace(mgr.get(l10nKey), params), skin, styleName);
		// TODO Auto-generated constructor stub
		_l10n = mgr;
		_key = l10nKey;
		_params = params;
		
		_l10n.registerLabel(this);
	}
	
	public void update( Object...params){
		_params = params;
		setText(Util.replace(_l10n.get(_key), _params));
		pack();
	}
	
	public void update(String key, Object...params){
		_key = key;
		_params = params;
		setText(Util.replace(_l10n.get(_key), _params));
		pack();
	}


	public L10nLabel(L10nManager mgr,String l10nKey, Skin skin) {		
		super(mgr.get(l10nKey), skin);
		// TODO Auto-generated constructor stub
		_l10n = mgr;
		_key = l10nKey;
		
		_l10n.registerLabel(this);
	}
	
	public String getKey(){
		return _key;
	}
	
}
