package com.mti.ad220_project_02_db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private static final int EDITOR_ID = 100;

	private MemoArrayAdapter memoAdapter;

	private Folder currentFolder;
	private File rootFolder;
	private static List<File> encryptedFiles = new ArrayList<File>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// save apps root directory
		rootFolder = getFilesDir();

		// open root folder
		currentFolder = new Folder(this, rootFolder);
		refreshView();

		// register list view for context menu
		registerForContextMenu(getListView());

	} // onCreate(Bundle savedInstanceState)

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// save current folder's path
		savedInstanceState.putString("path", currentFolder.getCurrentPath().getPath());
	} // onSaveInstanceState(Bundle savedInstanceState)

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// open folder using saved path
		File newPath = new File(savedInstanceState.getString("path"));
		currentFolder = new Folder(this, newPath);

		if (!newPath.getPath().equals(rootFolder.getPath())) {
			// if opened folder is not root folder, enable back button
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		refreshView();
	} // onRestoreInstanceState(Bundle savedInstanceState)

	private void openFile(String fileName) {

		Intent editor = new Intent(this, EditorActivity.class);
		editor.putExtra("path", currentFolder.getCurrentPath().toString());
		editor.putExtra("filename", fileName);
		startActivity(editor);
	} // openFile(String fileName)

	private void openFolder(String folderName) {

		File newPath = new File(currentFolder.getCurrentPath() + File.separator + folderName);
		currentFolder = new Folder(this, newPath);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		refreshView();
	} // openFolder(String folderName)


	private void openPreviousFolder() {

		File newPath = currentFolder.getCurrentPath().getParentFile();
		currentFolder = new Folder(this, newPath);

		refreshView();

		if (newPath.getPath().equals(rootFolder.getPath())) {

			//disable home button if current folder is root folder
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);
		}
	} // openPreviousFolder()

	private void refreshView() {

		File [] filesAndFolders;
		// get list of files and folders of current folder
		filesAndFolders = currentFolder.getFilesAndFolders();
		// set adapter with updated list of files and folders
		memoAdapter = new MemoArrayAdapter(this, filesAndFolders);
		setListAdapter(memoAdapter);
	} // refreshView()

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// open clicked item
		File item = (File) getListAdapter().getItem(position);

		if (item.isDirectory()) {
			// if item is a folder, open folder
			openFolder(item.getName());
		}
		else {
			// if item is a file, open editor and load file
			openFile(item.getName());
		}
	} // onListItemClick(ListView l, View v, int position, long id)

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		// display context menu on long click
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		int position = info.position;

		File file = (File) getListAdapter().getItem(position);

		// set title of context menu
		menu.setHeaderTitle(file.getName());

		if (file.isDirectory()) {
			// if long clicked folder, get context menu for folders
			String[] menuItems = getResources().getStringArray(R.array.folder_menu_items);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
		else {
			// if long clicked file, get context menu for files
			String[] menuItems = getResources().getStringArray(R.array.file_menu_items);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	} // onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();

		File file = (File) getListAdapter().getItem(info.position);

		int menuItemIndex = item.getItemId();

		if (file.isDirectory()) {
			// if selected item is folder, use folder menu
			switch (menuItemIndex) {
			// first option is "Open"
			case 0: openFolder(file.getName());
			break;
			// second option is "Rename"
			case 1: rename(file);
			break;
			// third option is "Delete"
			case 2: delete(file);
			break;
			}
		}
		else {
			// if selected item is a file, use file menu
			switch (menuItemIndex) {
			// first option is "Open"
			case 0: openFile(file.getName());
			break;
			// second option is "Rename"
			case 1: rename(file);;
			break;
			// third option is "Delete"
			case 2: delete(file);
			break;
			// forth option is "Encrypt"
			// encryption is not implemented
			// case 3: toast("Encrypting file");
			// break;
			}
		}
		return true;
	} // onContextItemSelected(MenuItem item)

	private void delete(final File file) {

		String title;
		String prompt;

		if (file.isDirectory()) {
			title = "Delete folder";
			prompt = "Are you sure you want to delete this folder and all of its contents?\n\n"
					+ file.getName();
		}
		else {
			title = "Delete memo";
			prompt = "Are you sure you want to delete this memo?\n\n" + file.getName();
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		alert.setMessage(prompt)
		.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				currentFolder.delete(file.getName());
				refreshView();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		alert.show();
	} // delete(final File file)

	private void rename(final File file) {

		String prompt;
		String title;

		if (file.isDirectory()) {
			title = "Rename folder";
			prompt = "Type new name of the folder:";
		}
		else {
			title = "Rename memo";
			prompt = "Type new name of the memo:";
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(title);
		alert.setMessage(prompt);

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		alert.setView(input);

		alert.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();

				currentFolder.rename(file.getName(), value);

				// refresh view to see new folder
				refreshView();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	} // rename(final File file)

	public void encryptFile(File file) {
		// currently encryption is not implemented
		if (!encryptedFiles.contains(file)) {

			encryptedFiles.add(file);
		}
	} // encryptFile(File file)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	} // onCreateOptionsMenu(Menu menu)

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar button selections.
		switch (item.getItemId()) {

		case R.id.action_about:
			// About button
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			return true;

		case R.id.create_memo:
			// create memo button
			Intent editor = new Intent(this, EditorActivity.class);
			editor.putExtra("path", currentFolder.getCurrentPath().toString());
			startActivityForResult(editor, EDITOR_ID);
			return true;

		case android.R.id.home:
			// home button
			openPreviousFolder();
			return true;

		case R.id.create_folder:
			// create folder button
			createFolder();
			return true;
		}

		return super.onOptionsItemSelected(item);
	} // onOptionsItemSelected(MenuItem item)

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == EDITOR_ID) {
			if (resultCode == RESULT_OK) {
				// get path String from EditorActivity 
				String path = data.getStringExtra("path");
				// set current path to the one that was passed in
				File oldPath = new File(path);
				currentFolder = new Folder(this, oldPath);

				refreshView();
			}
		}
	} // onActivityResult(int requestCode, int resultCode, Intent data)

	private void createFolder() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Create folder");
		alert.setMessage("Type the name of your folder:");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();

				// create new folder
				if (currentFolder.createFolder(value)) {
					// successfully created folder
				}
				else {
					toast("Folder \"" + value + "\" already exists");
				}
				// refresh view to see new folder
				refreshView();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();		
	} // createFolder()

	public void toast(String msg) {

		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	} // toast(String msg)

} // class MainActivity extends ListActivity


