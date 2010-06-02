package fi.tkk.media.xide.client.fs;

/**
 * SourceCodeFile class extends normal XIDEFile. The only difference is that 
 * source code file always has its content loaded when created.
 * Thus method getContent is overwritten
 * @author evgeniasamochadina
 *
 */
public class SourceCodeFile extends XIDEFile {
	
	public SourceCodeFile() {}
	
	public SourceCodeFile(String name, XIDEFolder parentFolder, String fileContent) {
		super(name, parentFolder);
		this.fileContent = fileContent;
		
		// Source code file is always a private file. It should be deleted when the owner is deleted. 
		this.fileOwner = XIDEFile.FILE_PRIVATE;
	}
	
	public String getContent() {
		return fileContent;
	}
}
