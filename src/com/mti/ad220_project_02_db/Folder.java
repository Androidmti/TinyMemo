package com.mti.ad220_project_02_db;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.widget.Toast;

public class Folder {

	Context context;
	File myDir;

	public Folder(Context context, File path) {
		// Constructor
		this.context = context;
		myDir = path;
	} // Folder(Context context, File path)

	public boolean createFolder(String folderName) {

		File folderToCreate = new File(myDir, folderName);

		boolean success = false;
		if (!folderToCreate.exists()) {

			success = folderToCreate.mkdir();
		} 

		return success;
	} // createFolder(String folderName)

	public void delete(String file) {

		File item = new File(myDir, file);
		DeleteRecursive(item);

	} // delete(String file)

	private void DeleteRecursive(File fileOrDirectory) {

		// recursively delete all files and folders inside passed in folder/file
		if (fileOrDirectory.exists()) {
			if (fileOrDirectory.isDirectory())
				for (File child : fileOrDirectory.listFiles())
					DeleteRecursive(child);

			fileOrDirectory.delete();
		}
		else {

			toast("Error: \"" + fileOrDirectory + "\" does not exist");
		}
	} // DeleteRecursive(File fileOrDirectory)

	public void rename(String oldName, String newName) {

		File oldFile = new File(myDir, oldName);
		File newFile = new File(myDir, newName);

		if (oldFile.exists()) {
			if (!newFile.exists()) {
				oldFile.renameTo(newFile);
			}
			else {
				toast("Can't rename, \"" + newName + "\" already exists");
			}
		}
		else {
			toast("Error: \"" + oldName + "\" does not exist");
		}
	} // rename(String oldName, String newName)

	@SuppressWarnings("unchecked")
	public File[] getFilesAndFolders() {

		@SuppressWarnings("rawtypes")
		Comparator comp = new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				File f1 = (File) o1;
				File f2 = (File) o2;
				if (f1.isDirectory() && !f2.isDirectory())
				{
					// Directory before non-directory
					return -1;
				}
				else if (!f1.isDirectory() && f2.isDirectory())
				{
					// Non-directory after directory
					return 1;
				}
				else
				{
					// Alphabetic order otherwise
					return f1.compareTo(f2);
				}
			} // compare(Object o1, Object o2)
		};

		// get list of all files and folders
		File[] fList = myDir.listFiles();
		// sort files and folders: folders first, then files
		Arrays.sort(fList, comp);
		return fList;
	} // getFilesAndFolders()

	public File getCurrentPath() {

		return myDir;
	} // getCurrentPath()

	private void toast(String msg) {

		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	} // toast(String msg)
} // class Folder
