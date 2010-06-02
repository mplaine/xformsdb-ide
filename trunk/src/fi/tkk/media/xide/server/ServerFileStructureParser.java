package fi.tkk.media.xide.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import fi.tkk.media.xide.client.fs.SourceCodeFile;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;

public class ServerFileStructureParser {

	public static final String[] DIRECTORIES = { "css", "data", "db", "query", "resource" };

	// Array lists for all file types
	LinkedHashMap<String, XIDEFile> cssFiles;
	LinkedHashMap<String, XIDEFile> dataFiles;
	LinkedHashMap<String, XIDEFile> dbFiles;
	LinkedHashMap<String, XIDEFile> queryFiles;
	LinkedHashMap<String, XIDEFile> resourceFiles;
	LinkedHashMap<String, XIDEFile> sourceFiles;

	private XIDEFolder rootfolder;

	/**
	 * Creates folder structure and corresponding files starting from rootPath. For template: (null, path, null) or
	 * (null, path, template_id) For page: (page_name + containers_names, path to the application, page_id) For
	 * application: (null, path, null)
	 * 
	 * @param possibleFolderNames
	 *            names of second level folders which should be shown (page and component names should be here) Can be
	 *            null (in this case all folders will be included)
	 * @param rootPath
	 *            absolute path to the root folder of the element (template or application)
	 * @param objectName
	 *            name of the object for which the folder structure is created. By this parameter it is identified which
	 *            source code file need to be included in hierarchy. Can be null (in this case all source code files
	 *            will be included)
	 * @return root folder
	 */
	public ServerFileStructureParser(ArrayList<String> possibleFolderNames, String rootPath, String objectName) {

		File rootFolderFile = new File(rootPath);
		// System.out.println("ServerFileStructureParser: going to parse " + rootPath + ", exist = " +
		// rootFolderFile.exists());
		if (rootFolderFile.exists() == false) {
			// Root folder does not exist. Error in configuration
			// System.out.println("ServerFileStructureParser: Error in configuration! The folder does not exist!");
			this.rootfolder = null;
		} else {
			// Root folder exists

			XIDEFolder rootFolder = null;
			// if (objectName != null) {
			// rootFolder = new XIDEFolder(rootPath, objectName, null);
			// }
			// else {
			rootFolder = new XIDEFolder(rootPath, rootFolderFile.getName(), null);
			// }

			// Array lists for all file types
			cssFiles = new LinkedHashMap<String, XIDEFile>();
			dataFiles = new LinkedHashMap<String, XIDEFile>();
			dbFiles = new LinkedHashMap<String, XIDEFile>();
			queryFiles = new LinkedHashMap<String, XIDEFile>();
			resourceFiles = new LinkedHashMap<String, XIDEFile>();
			sourceFiles = new LinkedHashMap<String, XIDEFile>();

			if (rootFolderFile.exists() && rootFolderFile.isDirectory()) {
				File[] files = rootFolderFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					String fileTypeFolderName = files[i].getName();
					if (files[i].isDirectory()) {
						// Current file is 1st level resource folder

						ArrayList<String> extensions = null;
						// Check folder name and initiate it's reading
						if (fileTypeFolderName.equals(DIRECTORIES[0])) {
							// CSS folder
							extensions = new ArrayList<String>();
							extensions.add(".css");
							readFirstLevelFolder(extensions, possibleFolderNames, fileTypeFolderName, rootFolder, objectName, cssFiles);
						} else if (fileTypeFolderName.equals(DIRECTORIES[1])) {
							// Data folder (data instances)
							extensions = new ArrayList<String>();
							extensions.add(".xml");
							readFirstLevelFolder(extensions, possibleFolderNames, fileTypeFolderName, rootFolder, objectName, dataFiles);
						} else if (fileTypeFolderName.equals(DIRECTORIES[2])) {
							// DB initial data (xml files)
							extensions = new ArrayList<String>();
							extensions.add(".xml");
							readFirstLevelFolder(extensions, possibleFolderNames, fileTypeFolderName, rootFolder, objectName, dbFiles);
						} else if (fileTypeFolderName.equals(DIRECTORIES[3])) {
							// Query folder (.xq and .xpath)
							extensions = new ArrayList<String>();
							extensions.add(".xq");
							extensions.add(".xpath");
							readFirstLevelFolder(extensions, possibleFolderNames, fileTypeFolderName, rootFolder, objectName, queryFiles);
						} else if (fileTypeFolderName.equals(DIRECTORIES[4])) {
							// Resource folder (images, text (?)...)
							readFirstLevelFolder(null, possibleFolderNames, fileTypeFolderName, rootFolder, objectName, resourceFiles);
						} else {
							// System.out.println("Unknown 1st level directory name: " + fileTypeFolderName);
						}
					} else {
						// Current file is a source code file
						// if (possibleFolderNames == null) {
						// // All filed and folders should be added
						// // TODO: add this file to the source codes list
						// }
						// else {
						if (objectName != null && !fileTypeFolderName.equals(objectName + ".xml")) {
							// do nothing, this file does not math
						} else {
							// File should be added
							// TODO: may be change later
							// Add ONLY xml files
							if (fileTypeFolderName.endsWith(".xml")) {
								try {
									sourceFiles.put(fileTypeFolderName, new SourceCodeFile(fileTypeFolderName, rootFolder, SearchServiceImpl
											.readFileAsString(rootFolder.getAbsolutePath() + fileTypeFolderName)));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						// }
					}
				}
			}
			this.rootfolder = rootFolder;
		}
	}

	/**
	 * Gets the type of the file (css, di, data, source, query)
	 * 
	 * DIRECTORIES = {"css", "data"(di), "db", "query", "resource" };
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileType(String filePath) {
		if (filePath.contains("/" + DIRECTORIES[0] + "/")) {
			return XIDEFile.CSS_FILE;
		} else if (filePath.contains("/" + DIRECTORIES[1] + "/")) {
			return XIDEFile.DI_FILE;
		} else if (filePath.contains("/" + DIRECTORIES[2] + "/")) {
			return XIDEFile.DATA_FILE;
		} else if (filePath.contains("/" + DIRECTORIES[3] + "/")) {
			return XIDEFile.QUERY_FILE;
		} else if (filePath.contains("/" + DIRECTORIES[4] + "/")) {
			return XIDEFile.RESOURCE_FILE;
		} else {
			return XIDEFile.SOURCE_FILE;
		}
	}

	/**
	 * Creates file structure starting from first level folder (css/data/db/resources/queries)
	 * 
	 * @param possibleFileExtensions
	 *            List of file extensions which are allowed under this folder
	 * @param possibleFolderNames
	 *            List of folder names from which files should be added to the list (for the Page it is it's own name,
	 *            its containers names). Files which are directly under main directory are always included. If it is
	 *            null then all files from the main folder is added.
	 * @param objectName
	 *            Name of the object (Template or Page)
	 * @param fileList
	 *            Array list which contains files of current main directory
	 */
	private void readFirstLevelFolder(ArrayList<String> possibleFileExtensions, ArrayList<String> possibleFolderNames, String firstLevelFolderName,
			XIDEFolder rootFolder, String objectName, LinkedHashMap<String, XIDEFile> fileList) {

		// Create XIDE folder
		XIDEFolder firstLevelFolder = new XIDEFolder(firstLevelFolderName, null, rootFolder);

		// Create a file to check the content of the directory
		File firstLevelFolderFile = new File(firstLevelFolder.getAbsolutePath());

		// If everything is correct
		if (firstLevelFolderFile.exists() && firstLevelFolderFile.isDirectory()) {
			// Create a list of files and folders under 1st level folder
			File[] secondLevelfiles = firstLevelFolderFile.listFiles();

			for (int i = 0; i < secondLevelfiles.length; i++) {

				String secondLevelFileName = secondLevelfiles[i].getName();
				if (secondLevelfiles[i].isDirectory()) {
					// Current child is a directory

					// Check directory name
					if (secondLevelFileName.equals(".svn") || possibleFolderNames != null && !possibleFolderNames.contains(secondLevelFileName)) {
						// Do nothing. Files from this directory should be included
					} else {
						// Folder should be included
						XIDEFolder secondLevelFolder = new XIDEFolder(secondLevelFileName, null, firstLevelFolder);

						// Files should be included

						File[] filesUnderSecondLevel = secondLevelfiles[i].listFiles();
						for (int j = 0; j < filesUnderSecondLevel.length; j++) {
							File currentFile = filesUnderSecondLevel[j];
							// TODO: add real file
							if (!currentFile.isDirectory()) {
								String fileName = currentFile.getName();
								String extension = fileName.substring(fileName.indexOf("."));
								if (possibleFileExtensions == null || (possibleFileExtensions != null && possibleFileExtensions.contains(extension))) {
									// This file is under page folder e.g. css/index/file.css
									XIDEFile file = new XIDEFile(fileName, secondLevelFolder, XIDEFile.FILE_PRIVATE);
									fileList.put(fileName, file);

								}
							}
							// File is a directory. This is possible only under data/.../ folder!
							// Now all directories are parsed, except .svn folders
							else if (!currentFile.getName().equals(".svn")) {
								// if (firstLevelFolderName.equals("data")) {
								// The files and folders should be included!
								// readFolderHierarchiclly(possibleFileExtensions, possibleFolderNames,
								// currentFile.getName(), rootFolder, fileList);
								readFirstLevelFolder(possibleFileExtensions, possibleFolderNames, currentFile.getName(), secondLevelFolder,
										objectName, fileList);
								// }
							}
						}
					}
				} else {
					// Current child is a file
					// Add file
					File currentFile = secondLevelfiles[i];
					// TODO: add real file
					String extension = currentFile.getName().substring(currentFile.getName().indexOf("."));
					if (possibleFileExtensions == null || (possibleFileExtensions != null && possibleFileExtensions.contains(extension))) {
						// This file is a common file since it it under 1st level folder
						XIDEFile file = new XIDEFile(currentFile.getName(), firstLevelFolder, XIDEFile.FILE_COMMON);
						fileList.put(currentFile.getName(), file);
					}
				}
			}
		}
	}

	// private void readFolderHierarchiclly(ArrayList<String> possibleFileExtensions, ArrayList<String>
	// possibleFolderNames,
	// String firstLevelFolderName, XIDEFolder rootFolder,
	// LinkedHashMap<String, XIDEFile> fileList) {
	//
	// // Create root folder
	// XIDEFolder firstLevelFolder = new XIDEFolder(firstLevelFolderName, null, rootFolder);
	//		
	// // Create a file to check the content of the directory
	// File firstLevelFolderFile = new File(firstLevelFolder.getAbsolutePath());
	//
	// // If everything is correct
	// if( firstLevelFolderFile.exists() && firstLevelFolderFile.isDirectory()){
	// // Create a list of files and folders under 1st level folder
	// File[] secondLevelfiles = firstLevelFolderFile.listFiles();
	//			
	// for(int i=0; i < secondLevelfiles.length; i++){
	//				
	// String secondLevelFileName = secondLevelfiles[i].getName();
	// if (secondLevelfiles[i].isDirectory()) {
	// // Current child is a directory
	//					
	// // Check directory name
	// if (secondLevelFileName.equals(".svn") || possibleFolderNames != null &&
	// !possibleFolderNames.contains(secondLevelFileName)) {
	// // Do nothing. Files from this directory should be included
	// }
	// else {
	// // Folder should be included
	// XIDEFolder secondLevelFolder = new XIDEFolder(secondLevelFileName, null, firstLevelFolder);
	//
	// // Files should be included
	//						
	// File[] filesUnderSecondLevel = secondLevelfiles[i].listFiles();
	// for(int j=0; j < filesUnderSecondLevel.length; j++){
	// File currentFile = filesUnderSecondLevel[j];
	// // TODO: add real file
	// if (!currentFile.isDirectory()) {
	// String fileName = currentFile.getName();
	// String extension = fileName.substring(fileName.indexOf("."));
	// if (possibleFileExtensions == null ||
	// (possibleFileExtensions != null && possibleFileExtensions.contains(extension))) {
	// XIDEFile file = new XIDEFile(fileName, secondLevelFolder);
	// fileList.put(fileName, file);
	//									
	// }
	// }
	// // File is a directory. This is possible only under data/.../ folder!
	// {
	// if (firstLevelFolderName.equals("data")) {
	// // The files and folders should be included!
	// readFolderHierarchiclly(possibleFileExtensions, possibleFolderNames,
	// currentFile.getName(), rootFolder, fileList);
	// readFirstLevelFolder(possibleFileExtensions, possibleFolderNames,
	// currentFile.getName(), secondLevelFolder, objectName, fileList);
	// }
	// }
	// }
	// }
	// }
	// else {
	// // Current child is a file
	// // Add file
	// File currentFile = secondLevelfiles[i];
	// // TODO: add real file
	// String extension = currentFile.getName().substring(currentFile.getName().indexOf("."));
	// if (possibleFileExtensions == null ||
	// (possibleFileExtensions != null && possibleFileExtensions.contains(extension))) {
	// XIDEFile file = new XIDEFile(currentFile.getName(), firstLevelFolder);
	// fileList.put(currentFile.getName(), file);
	// }
	// }
	// }
	// }
	//
	// }
	public XIDEFolder getRootFolder() {
		return rootfolder;
	}

}
