package fi.tkk.media.xide.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import fi.tkk.media.xide.client.fs.SourceCodeFile;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;

/**
 * Parses application file structure to get files and folders which belong to a specified page.
 * 
 * Is used when a page is deleted and all private page files and folders should be deleted. 
 * 
 * @author Evgenia Samochadina
 * @date Mar 5, 2010
 *
 */
public class ServerFileStructureParserForDelete {

	// Array lists for all file which are detected to be related to the page
	ArrayList<File> fileList;


	/**
	 * Iterates through folder structure of the application and lists all the files and folders which are related to the given page
	 *  
	 * @param possibleFolderNames
	 *            names of second level folders which should be shown (page and component names should be here) Can be
	 *            null (in this case all folders will be included)
	 * @param rootPath
	 *            absolute path to the root folder of the element (template or application)
	 * @param pagePath
	 *            name of the object for which the folder structure is created. By this parameter it is identified which
	 *            source code file need to be included in hierarchy. Can be null (in this case all source code files
	 *            will be included)
	 * @return root folder
	 */
	public ServerFileStructureParserForDelete(String rootPath, String pagePath) {

		File rootFolderFile = new File(rootPath);

		if (rootFolderFile.exists() == false) {
			// Root folder does not exist. Error in configuration
			// System.out.println("ServerFileStructureParser: Error in configuration! The folder does not exist!");
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
			fileList = new ArrayList<File>();
			
			if (rootFolderFile.exists() && rootFolderFile.isDirectory()) {
				File[] files = rootFolderFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					String fileTypeFolderName = files[i].getName();
					if (files[i].isDirectory()) {
						// one of resource folders
						
						// Current file is 1st level resource folder
						readFirstLevelFolder(files[i].getAbsolutePath(), pagePath);
					} else {
						// Current file is a source code file
						// Check if it is a source of this page
						if (files[i].getName().equals(pagePath + ".xml")) {
							//Add it to the list
							fileList.add(files[i]);
						}
						
					}
				}
			}
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
	private void readFirstLevelFolder( String folderPath, String pageName) {

		// Create a file to check the content of the directory
		File firstLevelFolderFile = new File(folderPath);

		// If everything is correct
		if (firstLevelFolderFile.exists() && firstLevelFolderFile.isDirectory()) {
			// Create a list of files and folders under 1st level folder
			File[] secondLevelfiles = firstLevelFolderFile.listFiles();

			for (int i = 0; i < secondLevelfiles.length; i++) {

				String secondLevelFileName = secondLevelfiles[i].getName();
				if (secondLevelfiles[i].isDirectory()) {
					// Current child is a directory

					// Check directory name
					if (secondLevelFileName.equals(pageName)) {
						// This is a folder containing private resources of the given page
						// Add folder to the folder list
						fileList.add(secondLevelfiles[i]);
					}
				} else {
					// Current child is a file
					// It is common by definition
				}
			}
		}
	}


	public ArrayList<File> getFilesToDelete() {
		return fileList;
	}

}
