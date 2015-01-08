package com.example.classynotes;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class FolderListViewAdapter extends BaseExpandableListAdapter{

	private Context context;
	private ArrayList<FolderInfo> folderList;
	
	public FolderListViewAdapter(Context context, ArrayList<FolderInfo> folderList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.folderList = folderList;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		ArrayList<String> fileList = folderList.get(arg0).getFolderContents();
		return fileList.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View view,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		String fileInfo = (String) getChild(arg0, arg1);
		
		if (view == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.files, null);
		}
		
		TextView folder = (TextView) view.findViewById(R.id.file);
		folder.setText(fileInfo);
		return view;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		ArrayList<String> fileList = folderList.get(arg0).getFolderContents();
		return fileList.size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return folderList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return folderList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View view, ViewGroup arg3) {
		// TODO Auto-generated method stub
		FolderInfo folderInfo = (FolderInfo) getGroup(arg0);
		if (view == null)
		{
			LayoutInflater folder = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = folder.inflate(R.layout.folders, null);
		}
		TextView folder = (TextView) view.findViewById(R.id.folder);
		folder.setText(folderInfo.getFile().getName());
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
