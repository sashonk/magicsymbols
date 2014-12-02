package com.me.magic2;

import java.io.File;


public interface Native {
	public void openWebPage(String address);
	

	public void setForEmail(String to, File attachment, String subject);

	public void hideView(int id);
	public void showView(int id);
}
