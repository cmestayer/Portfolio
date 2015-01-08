package com.example.classynotes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.BmpImage;


import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FolderSystem extends Activity implements OnClickListener{
	
	public static final int NOTHING = 0;
	public static final int SAVE = 1;
	public static final int OPEN = 2;
	public static final int PDF = 3;

	
	protected int currentAction;

	File mainFile;
	private ArrayList<FolderInfo> FolderNames = new ArrayList<FolderInfo>();
	private LinkedHashMap<String, FolderInfo> FileNames;
	
	private FolderListViewAdapter listAdapter;
	private ExpandableListView myList;
	
	EditText newFolder;
	
	String fileName;
	Bitmap bmp;
	
	
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_folder_system);
	      
	       //Create the folder where all the documents get saved
		mainFile = new File(Environment.getExternalStorageDirectory() + File.separator + "Classy Notes");
		if (!mainFile.isDirectory())
			mainFile.mkdirs();
	      getFolders();
	      
	     myList = (ExpandableListView) findViewById(R.id.list);
	     listAdapter = new FolderListViewAdapter(FolderSystem.this, FolderNames);
	     myList.setAdapter(listAdapter);
	     
	     expandAll();
	     
	     newFolder = (EditText)findViewById(R.id.create_file);
	     
	     Button addFolder = (Button)findViewById(R.id.create);
	     addFolder.setOnClickListener(this);
	     
	     myList.setOnGroupClickListener(myListGroupClicked);
	     
	     Bundle extras = getIntent().getExtras();
	     if(extras == null)
	     {
	    	 return;
	     }
	     currentAction = extras.getInt("option");
	     if (currentAction == SAVE)
	     {
	    	 if(getIntent().hasExtra("byteArray")) {
	    		 //ImageView previewThumbnail = new ImageView(this);
	    		 Bitmap b = BitmapFactory.decodeByteArray(
	    		     getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);        
	    		 //previewThumbnail.setImageBitmap(b);
	    		 saveDoc(b, extras.getString("name"));
	    		 }
	    	 
	    	 Toast.makeText(getBaseContext(), "Choose a folder to save to", Toast.LENGTH_LONG).show();
	     }
	     
	   }
	
	private OnGroupClickListener myListGroupClicked = new OnGroupClickListener(){
		
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
		{
			//do stuff
			FolderInfo folderInfo = FolderNames.get(groupPosition);
			if (currentAction == SAVE)
			{
				try {
					
					Document toPDF = new Document();
					PdfWriter.getInstance(toPDF, new FileOutputStream(folderInfo.getFile().getPath() + File.separator + fileName));
					toPDF.open();
					
					
					
					
			          //FileOutputStream out = new FileOutputStream(folderInfo.getFile().getPath() + File.separator + fileName );
			        ByteArrayOutputStream stream = new ByteArrayOutputStream();  
					bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			        Image i = Image.getInstance(stream.toByteArray());
					toPDF.add(i);
					toPDF.close();
					stream.close();
			        //out.flush();
			          //out.close();
			          folderInfo.addContents(fileName);
			          listAdapter.notifyDataSetChanged();
			          currentAction = NOTHING;
			      } catch(Exception e) {}
			}
			
			return false;
		}
	};
	
	private void expandAll() {
		// TODO Auto-generated method stub
		int count = listAdapter.getGroupCount();
		for(int i = 0; i < count; i++)
			myList.expandGroup(i);
	}
	
	private void collapseAll(){
		int count = listAdapter.getGroupCount();
		for(int i = 0; i < count; i++)
			myList.collapseGroup(i);
	}


	public void createFolder(String name)
	{
		//Create a new Folder inside the Main folder
		File newFile = new File(Environment.getExternalStorageDirectory() + File.separator + "Classy Notes" + File.separator + name);
		if(!newFile.isDirectory())
		{
			newFile.mkdirs();
			FolderInfo f = new FolderInfo(newFile);
			FolderNames.add(f);
		}
		
	}
	
	public void saveDoc(Bitmap bitmap, String name)
	{
		fileName = name;
		bmp = bitmap;
	}
	
	public File getMainFolder()
	{
		return mainFile;
	}
	
	public void getFolders()
	{
	    File[] files = mainFile.listFiles();
	    FolderInfo main = new FolderInfo(mainFile);
	    FolderNames.add(main);
	    for(File f : files)
	    {
	        if(f.isDirectory())
	        {
	            try
	            {
	            	FolderInfo folder = new FolderInfo(f);
	                FolderNames.add(folder);
	                File[] file = f.listFiles();
	                for(File k : file)
	                {
	                	if(k.isFile())
	                	{
	                		folder.addContents(k.getName());
	                	}
	                		
	                }
	                	
	            }
	            catch(Exception e){}
	        }
	        else if (f.isFile())
	        {
	        	try
	            {
	                main.addContents(f.getName());
	            }
	            catch(Exception e){}
	        }
	    }

	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId())
		{
		case R.id.create:
			if (newFolder.getText() == null)
			{
				Toast.makeText(getBaseContext(), "No folder name entered", Toast.LENGTH_LONG).show();
				break;
			}
			createFolder(newFolder.getText().toString());
			//do something
			listAdapter.notifyDataSetChanged();
			
		}
	}

	
}

