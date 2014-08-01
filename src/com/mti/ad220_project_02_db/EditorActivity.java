package com.mti.ad220_project_02_db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends ActionBarActivity {

	private EditText txtEditor;
	private EditText etTitle;

	private String path;
	private String fileName;

	private boolean isNewFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		txtEditor = (EditText) findViewById(R.id.etEditor);
		etTitle = (EditText) findViewById(R.id.etTitle);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			path = extras.getString("path");
			fileName = extras.getString("filename");

			if (fileName != null) {
				// file was opened, so it's not new
				isNewFile = false;
				readFile();
			}
			else {
				// file is brand new
				isNewFile = true;
			}
		}

		// testing encoding and decoding
		//		String testString = "Encode this string\n\nIt has multiple\nlines";
		//		
		//		String password = "password";
		//		
		//		Encryption encr = new Encryption();
		//		
		//		try {
		//			txtEditor.setText("\nOriginal: " + testString);
		//			
		//			String encodedStr = encr.encrypt(password, testString);
		//			txtEditor.append("\nEncoded: " + encodedStr);
		//			
		//			String decodedStr = encr.decrypt(password, encodedStr);
		//			txtEditor.append("\nDecoded: " + decodedStr);
		//		}
		//		catch (Throwable t) {
		//			txtEditor.append("\nException: " + t.toString());
		//		}


	} // onCreate(Bundle savedInstanceState)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return true;
	} // onCreateOptionsMenu(Menu menu)

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar button selections
		int id = item.getItemId();
		if (id == R.id.action_close_no_save) {
			// Close without saving button
			Intent intent = new Intent();
			intent.putExtra("path", path);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		}

		else if (id == R.id.save_memo) {
			// Save button
			saveFile();
			return true;
		}

		else if (id == android.R.id.home) {
			// Home button
			Intent intent = new Intent();
			intent.putExtra("path", path);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	} // onOptionsItemSelected(MenuItem item)

	private void saveFile() {

		String title = etTitle.getText().toString();
		try {
			if (!title.equals("")) {

				String targetFilePath = path + File.separator + title;

				File file = new File(targetFilePath);

				if (isNewFile == true && file.exists()) {
					// if file is brand new and file with same name already exists
					// give error message and exit out of this method

					AlertDialog.Builder alert = new AlertDialog.Builder(this);
					alert.setTitle("Memo already exists");
					alert.setMessage("Memo with the title \"" + file.getName() +
							"\" already exists.\nChoose a different title.");
					alert.setPositiveButton("Ok", null);
					alert.show();
					return;
				}

				FileOutputStream fileout = new FileOutputStream(file);
				fileout.write(txtEditor.getText().toString().getBytes());
				fileout.close();

				toast("Saved");
				// set activity title to display name of the memo
				setTitle(title);
			}
			else {
				toast("Please provide a title");
			}
		}
		catch (Throwable t) {
			toast("Exception: " + t.toString());
		}
	} // saveFile()

	private void readFile() {

		// set title of activity to display name of opened file
		setTitle(fileName);

		// hide EditText with title when opening a file
		etTitle.setVisibility(View.INVISIBLE);

		String targetFilePath = path + File.separator + fileName;

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader
					(new BufferedInputStream(new FileInputStream(new File(targetFilePath)))));

			String line = null;

			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			reader.close();

			etTitle.setText(fileName);
			txtEditor.setText(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // readFile()

	public void toast(String msg) {

		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	} // toast(String msg)
} // class EditorActivity extends ActionBarActivity
