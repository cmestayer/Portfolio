package com.example.classynotes;

import android.view.View;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

public class DrawingView extends View 
{
	public static final int REC = 1;
	public static final int DRAW = 2;
	public static final int TEXT = 3;
	public static final int IMG = 4;
	public static final int HIGH = 5;
	
	protected int currentAction;
	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint, textPaint, dashPaint, highPaint;
	//initial color
	private int paintColor = 0xFF660000;
	//initial text color
	private int textColor = 0xFF000000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	//Eraser
	private boolean erase=false;
	private boolean highlighter=false;
	
	private Rect r;
	
	private String text;
	Bitmap b;
	private TextView tv;
	private EditText et;
	
	Bitmap imgBitmap;
	
	private float brushSize, lastBrushSize;
	
	protected float mStartX;
    protected float mStartY;

    protected float touchX;
    protected float touchY;

	public DrawingView(Context context, AttributeSet attrs)
	{
	    super(context, attrs);
	    setupDrawing();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		//view given size
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	private void setupDrawing()
	{
		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		//get drawing area setup for interaction 
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		//For the dash rectangle
		dashPaint = new Paint();
		dashPaint.setARGB(255, 0, 0, 0);
		dashPaint.setStyle(Paint.Style.STROKE);
		dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
		
		//For the Highlighter
		highPaint = new Paint();
		highPaint.setColor(0xFFFFFF00);
		highPaint.setAlpha(100);
		highPaint.setStrokeWidth(brushSize);
		highPaint.setStyle(Paint.Style.STROKE);
		highPaint.setStrokeJoin(Paint.Join.ROUND);
		highPaint.setStrokeCap(Paint.Cap.ROUND);
		
		
		//Instantiating the canvas Paint object
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	private void onTouchEventDraw(MotionEvent event, Paint paint)
	{
		//Actions/Touch events
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				    drawPath.moveTo(touchX, touchY);
				    invalidate();
				    break;
				case MotionEvent.ACTION_MOVE:
				    drawPath.lineTo(touchX, touchY);
				    invalidate();
				    break;
				case MotionEvent.ACTION_UP:
				    drawCanvas.drawPath(drawPath, paint);
				    drawPath.reset();
				    invalidate();
				    break;
				default:
				    break;
				}
	}
	
	/*private void setupText()
	{
		textPaint = new Paint();
		drawCanvas.drawPaint(textPaint);
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(16);
	}*/
	
	/*public void addText(String text)
	{
		Rect r = null; 
		drawRectText(text, drawCanvas, r);
	}*/
	
	/***********************DRAW RECTANGLE FOR TEXTBOX*************************/
	private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas,dashPaint);
    }

    private void onTouchEventRectangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //isDrawing = true;
                mStartX = touchX;
                mStartY = touchY;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            	currentAction = TEXT;
                invalidate();
                break;
        }
        
    }

    private void drawRectangle(Canvas canvas,Paint paint){
        float right = mStartX > touchX ? mStartX : touchX;
        float left = mStartX > touchX ? touchX : mStartX;
        float bottom = mStartY > touchY ? mStartY : touchY;
        float top = mStartY > touchY ? touchY : mStartY;
        canvas.drawRect(left, top , right, bottom, paint);

        r = new Rect((int)left, (int)top, (int)right, (int)bottom);
    }
	
	public void drawRectText(Canvas canvas) {
		
		textPaint = new Paint();
	    textPaint.setTextSize(20);
	    textPaint.setTextAlign(Align.CENTER);
	    int width = r.width();

	    int numOfChars = textPaint.breakText(text,true,width,null);
	    int start = (text.length()-numOfChars)/2;
	    drawCanvas.drawText(text,start,start+numOfChars,r.exactCenterX(),r.exactCenterY(),textPaint);
	}
	
	private void drawTextOnCanvas(Canvas canvas, String text) {
        // maybe color the background..
        //canvas.drawPaint(canvasPaint);
		textPaint = new Paint();
	    textPaint.setTextSize(20);

        // Setup a textview like you normally would with your activity context
        //tv = new TextView(getContext());
	    tv = new EditText(getContext());
        tv.setText(text);

        // maybe set textcolor
        tv.setTextColor(textColor);

        // you have to enable setDrawingCacheEnabled, or the getDrawingCache will return null
        tv.setDrawingCacheEnabled(true);

        // we need to setup how big the view should be..which is exactly as big as the canvas
        tv.measure(MeasureSpec.makeMeasureSpec(r.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(r.height(), MeasureSpec.EXACTLY));

        // assign the layout values to the textview
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        //canvas.drawText(et.getText().toString(), r.left, r.top, textPaint);
        // draw the bitmap from the drawingcache to the canvas
        drawCanvas.drawBitmap(tv.getDrawingCache(), r.left, r.top, textPaint);

        // disable drawing cache
        tv.setDrawingCacheEnabled(false);
        
        currentAction = DRAW;
        
	}
	
	/***********************DRAW Image*************************/
	private void onTouchIMG(MotionEvent event) {
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mStartX = touchX;
            mStartY = touchY;
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            drawImgOnCanvas(drawCanvas);
            currentAction = DRAW;
            break;
    }
	  }
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		//draw view
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		
		switch(currentAction){
		case REC:
			//create rec
			onDrawRectangle(canvas);
			//currentAction = DRAW;
			break;
		case DRAW:
			canvas.drawPath(drawPath, drawPaint);
			break;
		case TEXT:
			drawTextOnCanvas(canvas, text);
			//drawRectText(canvas);
			break;
		case IMG:
			drawImgOnCanvas(canvas);
			break;
		case HIGH:
			canvas.drawPath(drawPath, highPaint);
		
		}
	}
	
	private void drawImgOnCanvas(Canvas canvas) {
		// TODO Auto-generated method stub
		float right = mStartX > touchX ? mStartX : touchX;
        float left = mStartX > touchX ? touchX : mStartX;
        float bottom = mStartY > touchY ? mStartY : touchY;
        float top = mStartY > touchY ? touchY : mStartY;
        r = new Rect((int)left, (int)top, (int)right, (int)bottom);
        canvas.drawBitmap(imgBitmap, null, r, canvasPaint);//(imgBitmap, top, left, drawPaint);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//detect user touch 
		
		//Retrieve the X and Y positions of the user touch
		touchX = event.getX();
		touchY = event.getY();
		
		switch(currentAction)
		{
		case REC:
			onTouchEventRectangle(event);
			break;
		case DRAW:
			onTouchEventDraw(event, drawPaint);
			break;
		case TEXT:
			break;
		case IMG:
			onTouchIMG(event);
			break;
		case HIGH:
			onTouchEventDraw(event, highPaint);
		
		}
		
		//invalidate();
		return true;
		
		
	}
	
	public void setColor(String newColor)
	{
		//set color     
		invalidate();
		//Parse and set the color
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public void setBrushSize(float newSize)
	{
		//update size
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}
	
	public void setLastBrushSize(float lastSize)
	{
	    lastBrushSize=lastSize;
	}
	
	public float getLastBrushSize()
	{
	    return lastBrushSize;
	}

	public void setErase(boolean isErase) {
		// TODO Auto-generated method stub
		erase=isErase;
		if(erase) 
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else 
			drawPaint.setXfermode(null);
		
	}
	
	public void startNew()
	{
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}
	
	public void setCurrentAct(int action)
	{
		currentAction = action; 
	}

	public int getCurrentAct() {
		// TODO Auto-generated method stub
		return currentAction;
	}

	public void setText(String string) {
		// TODO Auto-generated method stub
		text = string;
	}
	
	public void setImg(Bitmap b)
	{
		imgBitmap = b;
	}

	public void setHighlighter(boolean high) {
		// TODO Auto-generated method stub
		highlighter = high;
		//currentAction = HIGH;
	}

	public void setTextColor(String color) {
		// TODO Auto-generated method stub
		textColor = Color.parseColor(color);
	}
	
	
}
