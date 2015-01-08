package com.example.classynotes;

import java.io.File;
import java.util.ArrayList;

public class FolderInfo {

	private File folder;
	private ArrayList<String> contents = new ArrayList<String>();
	
	public FolderInfo(File f)
	{
		folder = f;
	}
	public File getFile()
	{
		return folder;
	}
	
	public ArrayList<String> getFolderContents()
	{
		return contents;
	}
	
	public void addContents(String string)
	{
		contents.add(string);
	}
}
