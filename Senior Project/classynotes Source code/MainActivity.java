package com.example.classynotes;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener
{
	private static final int CAMERA_PIC_REQUEST = 0;
	private static final int LIBRARY_PIC_REQUEST = 1;
	private String title;
	private DrawingView drawView;
	private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, textBtn, camBtn, imgBtn, highLBtn; //currTextColor;
	private float smallBrush, mediumBrush, largeBrush;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawView = (DrawingView)findViewById(R.id.drawing);
		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
		//Get the first button and store it as the instance variable 
		currPaint = (ImageButton)paintLayout.getChildAt(0);
		//Show that button is selected
		currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		
		//Draw/brush size button
		drawBtn = (ImageButton)findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);
		
		//Erase Button 
		eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);
		
		//New Page button
		newBtn = (ImageButton)findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);
		
		//Save Button 
		saveBtn = (ImageButton)findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		
		//Text Button
		textBtn = (ImageButton)findViewById(R.id.text_btn);
		textBtn.setOnClickListener(this);
		
		//Camera Button
		camBtn = (ImageButton)findViewById(R.id.camera_btn);
		camBtn.setOnClickListener(this);
		
		//Image Button
		imgBtn = (ImageButton)findViewById(R.id.img_btn);
		imgBtn.setOnClickListener(this);
		
		//Highlighter Button
		highLBtn = (ImageButton)findViewById(R.id.high_btn);
		highLBtn.setOnClickListener(this);
		
		//Set initial brush size
		drawView.setBrushSize(mediumBrush);
		
//		//Set the Title of Document
//		docTitle = (EditText)findViewById(R.id.doc_title);
//		docTitle.setVisibility(View.INVISIBLE);
//		docTitle.setOnClickListener(this);
		
		
		
		drawView.setCurrentAct(DrawingView.DRAW);
		
		setName();
		//final EditText folderName = (EditText)findViewById(R.id.editText2);
		//menu = new FileSystem();
		
//		final Dialog files = new Dialog(this);
//		files.setTitle("Enter a name for a folder:");
//		files.setContentView(R.layout.name);
//		Button create = (Button)files.findViewById(R.id.create);
//		create.setOnClickListener(new OnClickListener()
//		{
//		    @Override
//		    public void onClick(View v) 
//		    {
//		    	if (menu != null)
//		    		//menu.createFolder(folderName.getText().toString());
//		        files.dismiss();
//		    }
//		});
//		files.show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View view){
	//respond to clicks  
		if(view.getId()==R.id.draw_btn){
		    //draw button clicked
			drawView.setCurrentAct(DrawingView.DRAW);
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener()
			{
			    @Override
			    public void onClick(View v) 
			    {
			        drawView.setBrushSize(smallBrush);
			        drawView.setLastBrushSize(smallBrush);
			        drawView.setErase(false);
			        drawView.setHighlighter(false);
			        brushDialog.dismiss();
			    }
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener()
			{
			    @Override
			    public void onClick(View v) 
			    {
			        drawView.setBrushSize(mediumBrush);
			        drawView.setLastBrushSize(mediumBrush);
			        drawView.setErase(false);
			        drawView.setHighlighter(false);
			        brushDialog.dismiss();
			    }
			});
			 
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener()
			{
			    @Override
			    public void onClick(View v) 
			    {
			        drawView.setBrushSize(largeBrush);
			        drawView.setLastBrushSize(largeBrush);
			        drawView.setErase(false);
			        drawView.setHighlighter(false);
			        brushDialog.dismiss();
			    }
			});
			brushDialog.show();
			
		}//end of brush size draw button click
		else if (view.getId()==R.id.erase_btn)
		{
			//Erase 
			drawView.setCurrentAct(DrawingView.DRAW);
			setBrushSize(view, true, false);
		}//end if erase
		else if(view.getId() == R.id.high_btn)
		{
			setBrushSize(view, false, true);
			drawView.setCurrentAct(DrawingView.HIGH);
		}
		else if(view.getId()==R.id.new_btn)
		{
			//New Page button
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
			    public void onClick(DialogInterface dialog, int which)
			    {
			        drawView.startNew();
			        dialog.dismiss();
			        setName();
			    }

			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
			    public void onClick(DialogInterface dialog, int which)
			    {
			        dialog.cancel();
			    }
			});
			newDialog.show();
			
			
		}//end new page 
		else if (view.getId()==R.id.save_btn)
		{
			
			drawView.setDrawingCacheEnabled(true);
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("to Device Gallery or External Application folder?");
			saveDialog.setPositiveButton("External Folders", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					
			    	try {
			    	dialog.dismiss();
					//b = new DrawingInfo(drawView.getDrawingCache());
					Bitmap b = drawView.getDrawingCache();
					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					b.compress(Bitmap.CompressFormat.PNG, 100, bs);
					Intent save = new Intent(MainActivity.this, FolderSystem.class);
					save.putExtra("option", FolderSystem.SAVE);
					save.putExtra("byteArray", bs.toByteArray());
					save.putExtra("name", title);
					bs.close();
					startActivity(save);
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			saveDialog.setNeutralButton("Gallery", new DialogInterface.OnClickListener()
			{
			    public void onClick(DialogInterface dialog, int which)
			    {
			        //save drawing
			    	
					
			    	String imgSaved = MediaStore.Images.Media.insertImage(
			    		    getContentResolver(), drawView.getDrawingCache(),
			    		    UUID.randomUUID().toString()+".png", "drawing");
			    	if(imgSaved!=null)
			    	{
			    		//Save successful
			    	    Toast savedToast = Toast.makeText(getApplicationContext(), 
			    	        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
			    	    savedToast.show();
			    	}
			    	else
			    	{
			    		//Save unsuccessful
			    	    Toast unsavedToast = Toast.makeText(getApplicationContext(), 
			    	        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
			    	    unsavedToast.show();
			    	}
			    	drawView.destroyDrawingCache();
			    }
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
			    public void onClick(DialogInterface dialog, int which)
			    {
			        dialog.cancel();
			    }
			});
			saveDialog.show();
		}
//		else if(view.getId() == R.id.doc_title)
//		{
//			docTitle.setVisibility(View.VISIBLE);
//			String tit = docTitle.getText().toString();
//			setTitle(tit);
//		}
		else if(view.getId() == R.id.text_btn)
		{
			final Dialog editText = new Dialog(this);
			editText.setTitle("Enter Text:");
			editText.setContentView(R.layout.textbox);
			
			
			final EditText text = (EditText)editText.findViewById(R.id.editText1);
			//LinearLayout textLayout = (LinearLayout)findViewById(R.id.text_colors);
			//Get the first button and store it as the instance variable 
			//ImageButton currTextColor = (ImageButton)textLayout.getChildAt(0);
			//Show that button is selected
			//currTextColor.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			Button done = (Button)editText.findViewById(R.id.done);
			done.setOnClickListener(new OnClickListener()
			{
			    @Override
			    public void onClick(View v) 
			    {
			        drawView.setText(text.getText().toString());
			        editText.dismiss();
			        drawView.setCurrentAct(DrawingView.REC);
			        Toast.makeText(getApplicationContext(), 
			    	        "Draw box for text.", Toast.LENGTH_SHORT).show();
			    }
			    
			});
			editText.show();
			
		}
		else if(view.getId() == R.id.camera_btn)
		{
			openCamera(view);
		}
		else if(view.getId() == R.id.img_btn)
		{
			openLibrary(view);
		}
	}
	
	protected void setName() {
		final Dialog editText = new Dialog(this);
		editText.setTitle("Enter a name for this document:");
		editText.setContentView(R.layout.name);
		
		final EditText name = (EditText)editText.findViewById(R.id.editText2);
		Button done = (Button)editText.findViewById(R.id.new_name);
		done.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v) 
		    {	
		    	title = name.getText().toString();
				setTitle(title);
				editText.dismiss();
				
		    }
		});
		editText.show();
		
	}

	public void openCamera(View view)
	{
	    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
	    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);  
	}

	public void openLibrary(View view)
	{
	    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
	            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//(Intent.ACTION_GET_CONTENT);
	    photoPickerIntent.setType("image/*");
	    startActivityForResult(photoPickerIntent, LIBRARY_PIC_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK)
		{
			Uri image = data.getData();
			if(requestCode == CAMERA_PIC_REQUEST || requestCode == LIBRARY_PIC_REQUEST)
			{
				//Draw Rectangle first
				drawView.setCurrentAct(DrawingView.IMG);
				//Send Image over to place in Rec
				try {
					drawView.setImg(BitmapFactory.decodeStream(getContentResolver().openInputStream(image)));
					Toast.makeText(getApplicationContext(), 
			    	        "Draw area for image.", Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					Toast.makeText(getApplicationContext(), 
			    	        "Error with image.", Toast.LENGTH_SHORT).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}
	
	public void paintClicked(View view)
	{
	    //use chosen color
		if(view!=currPaint)
		{
			//update color
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			//Update UI and set previous color back to normal
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
			
			//make sure erasing is turned off
			drawView.setErase(false);
			drawView.setHighlighter(false);
			drawView.setBrushSize(drawView.getLastBrushSize());
			
		}
	}
//	public void textColor(View view)
//	{
//		if(view != currTextColor)
//		{
//			//update color
//			ImageButton imgView = (ImageButton)view;
//			String color = view.getTag().toString();
//			drawView.setTextColor(color);
//			//Update UI and set previous color back to normal
//			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
//			currTextColor.setImageDrawable(getResources().getDrawable(R.drawable.paint));
//			currTextColor=(ImageButton)view;
//		}
//	}

	/*public String getTitle() {
		if (title != null)
			return title;
	}*/

	
	private void setBrushSize(View view, final boolean erase, final boolean high)
	{
		final Dialog brushDialog = new Dialog(this);
		brushDialog.setTitle("Brush size:");
		brushDialog.setContentView(R.layout.brush_chooser);
		ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
		smallBtn.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v) 
		    {
		        drawView.setBrushSize(smallBrush);
		        drawView.setErase(erase);
		        drawView.setHighlighter(high);
		        brushDialog.dismiss();
		    }
		});
		ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
		mediumBtn.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v) 
		    {
		        drawView.setBrushSize(mediumBrush);
		        drawView.setErase(erase);
		        drawView.setHighlighter(high);
		        brushDialog.dismiss();
		    }
		});
		 
		ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
		largeBtn.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v) 
		    {
		        drawView.setBrushSize(largeBrush);
		        drawView.setErase(erase);
		        drawView.setHighlighter(high);
		        brushDialog.dismiss();
		    }
		});
		brushDialog.show();
	}

}
