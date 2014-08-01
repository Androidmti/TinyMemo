package com.mti.ad220_project_02_db;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemoArrayAdapter extends ArrayAdapter<File> {
	
	private Context context;
	private File[] name;

	public MemoArrayAdapter(Context context, File[] name) {
		// Constructor
		super(context, R.layout.row_listview, name);
		this.context = context;
		this.name = name;
	} // MemoArrayAdapter(Context context, File[] name)

	class ViewHolder {
		
		TextView tvName;
		TextView tvDateUpdated;
		ImageView icon;
		
		ViewHolder(View v) {
			// ViewHolder constructor
			// initialize controls
			tvName = (TextView) v.findViewById(R.id.tvName);
			tvDateUpdated = (TextView) v.findViewById(R.id.tvDateUpdated);
			icon = (ImageView) v.findViewById(R.id.icon);
		} // ViewHolder(View v)
	} // class ViewHolder
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// View holder pattern to assign data to views in a ListView cell
		View rowView = convertView;
		ViewHolder holder = null;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.row_listview, parent, false);	
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		}
		else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		// set file or folder name
		holder.tvName.setText(name[position].getName());
		
		// set last date modified in "Jul 26, 2014 1:58:00 PM" format
		DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		holder.tvDateUpdated.setText(dateFormat.format(name[position].lastModified()));
		
		// set file or folder icon
		if (name[position].isDirectory()) {
			holder.icon.setImageResource(R.drawable.folder_icon);
		}
		else {
			holder.icon.setImageResource(R.drawable.document_empty_icon);
		}

		return rowView;
	} // getView(int position, View convertView, ViewGroup parent)
	
	@Override
	public File getItem(int position) {
		
		return name[position];
	} // getItem(int position)
} // class MemoArrayAdapter extends ArrayAdapter<File>
