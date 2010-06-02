package fi.tkk.media.xide.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.media.xide.server.transformation.filehandlers.WidgetMaker;


/**
 * Convenience class for handling files.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on November 27, 2007
 */
public class FileUtils {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static  Logger loggerInstance = Logger.getLogger(FileUtils.class);
	private static Logger getLogger(){
		if (loggerInstance == null) {
			loggerInstance = Logger.getLogger(FileUtils.class);
		}
		return loggerInstance;
	}
	
	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private FileUtils() {
		getLogger().log( Level.DEBUG, "Constructor has been called." );
	}
	

	// PUBLIC STATIC METHODS
	/**
	 * Get the file.
	 * 
	 * 
	 * @param path				The path of the file.
	 * @return file				The file.
	 * @throws FileException	File exception.
	 */
	public static File getFile( String path ) throws FileException {
		getLogger().log( Level.DEBUG, "Method has been called." );
		File f			= null;
		

		try {
			// Create the file
			f			= new File( path );
			getLogger().log( Level.DEBUG, "The file (" + path + ") has been loaded." );
		} catch ( Exception ex ) {
			throw new FileException( "Failed to load the file (" + path  + ").", ex );
		}

		try {
			// Test the existence of the file
			if ( ( f.exists() == true ) && ( f.isFile() == true ) && ( f.canRead() == true ) && ( f.canWrite() == true ) ) {
				getLogger().log( Level.DEBUG, "The file (" + path + ") exists, can be read, and written." );
			}
			else {
				throw new FileException( "Failed to retrieve file '" + path + "'." );
			}
		} catch ( FileException fex ) {
			throw fex;
		} catch ( Exception ex ) {
			throw new FileException( "Failed to get the file (" + path + ").", ex );
		}
		
		
		return f;
	}
	
	
	/**
	 * Get the file as an input stream.
	 * 
	 * 
	 * @param path				The path of the file.
	 * @return fileInputStream	The file as an input stream.
	 * @throws FileException	File exception.
	 */
	public static InputStream getFileAsInputStream( String path ) throws FileException {
		getLogger().log( Level.DEBUG, "Method has been called." );
		InputStream is	= null;
		File f			= null;
		
		
		try {
			// Create the file
			f			= new File( path );
			getLogger().log( Level.DEBUG, "The file (" + path + ") has been loaded." );
		} catch ( Exception ex ) {
			throw new FileException( "Failed to load the file (" + path  + ").", ex );
		}
			
		try {
			// Test the existence of the file
			if ( ( f.exists() == true ) && ( f.isFile() == true ) && ( f.canRead() == true ) ) {
				getLogger().log( Level.DEBUG, "The file (" + path + ") exists and can be read." );

				// Create the file input stream
				is		= new FileInputStream( f );
				getLogger().log( Level.DEBUG, "The file (" + path + ") has been read." );
			}
			else {
				throw new FileException( "Failed to retrieve file '" + path + "'." );
			}
		} catch ( FileException fex ) {
			throw fex;
		} catch ( Exception ex ) {
			throw new FileException( "Failed to get the file (" + path + ") as an input stream.", ex );
		} finally {
			// Do not close the InputStream
		}

		
		return is;
	}
	
	
	/**
	 * Write the content of an input stream to a file.
	 * 
	 * 
	 * @param content			The content as an input stream.
	 * @param inputCharsetName	The name of a supported input charset or
	 * 							null which then uses the default charset.
	 * @param outputCharsetName	The name of a supported output charset or
	 * 							null which then uses the default charset.
	 * @param path				The path of a file.
	 * @param append			If <code>true</true>, then bytes will be written to the end of the file
	 * 							rather than the beginning. 
	 * @return success			<code>true</true> if reading the content & writing a file
	 * 							was successful, otherwise <code>false</code>. 
	 * @throws FileException	File exception.
	 */
	public static boolean writeFile( InputStream content, String inputCharsetName, String outputCharsetName, String path, boolean append ) throws FileException {
		// TODO: check if necessary/useful/fix me
		getLogger().log( Level.DEBUG, "Method has been called." );
		boolean success		= false;
		Reader r			= null;
		BufferedReader br	= null;
		boolean lineRead	= false;
		String line			= null;
		File f				= null;
		OutputStream os		= null;
		Writer w			= null;
		BufferedWriter bw	= null;
		PrintWriter pw		= null;
		boolean dirsOK		= true;
		boolean fileOK		= true;

		
		try {
			// Create the file instance
			f				= new File( path );
		} catch ( Exception ex ) {
			throw new FileException( "Failed to load the file.", ex );
		}

		try {
			// Test the existence of the file
			if ( f.exists() == false ) {
				// Check parents
				if ( f.getParent() != null ) {
					// Test the existence of the directories
					if ( (f.getParentFile().exists() == false ) ) {
						dirsOK	= f.getParentFile().mkdirs();						
						getLogger().log( Level.DEBUG, "Directories have been created." );
					}
				}

				if ( dirsOK == true ) {
					// Create the file
					fileOK	= f.createNewFile();
					getLogger().log( Level.DEBUG, "File has been created." );
				}
			}
			
			// OK to use the file
			if ( ( f.isFile() == true ) && ( f.canRead() == true ) && ( f.canWrite() == true ) && ( dirsOK == true ) && ( fileOK == true ) ) {
				getLogger().log( Level.DEBUG, "File is okay." );
				// Read the InputStream
				if ( inputCharsetName == null ) {
					r	= new InputStreamReader( content );					
				}
				else {
					r	= new InputStreamReader( content, inputCharsetName );
				}
				br	= new BufferedReader( r );				
				// Write the file
				os	= new FileOutputStream( f, append );
				if ( outputCharsetName == null ) {
					w	= new OutputStreamWriter( os );
				}
				else {
					w	= new OutputStreamWriter( os, outputCharsetName );
				}
				bw	= new BufferedWriter( w );
				pw	= new PrintWriter( bw );

				// Read the InputStream and write the file
				while ( ( line = br.readLine() ) != null ) {
					// Check line
					if ( lineRead == false ) {
						lineRead	= true;
					}
					
					// Write the line
					pw.println( line );
				}
				pw.flush();

				// Check errors
				if ( pw.checkError() == true ) {
					throw new IOException( "ERROR: Failed to print stream." );
				}

				success	= true;
				getLogger().log( Level.DEBUG, "File has been written." );
			}
			else {
				throw new FileException( "File is not okay." );
			}
		} catch ( FileException fex ) {
			throw fex;
		} catch ( Exception ex ) {
			throw new FileException( "Failed to write the content of an input stream to a file.", ex );
		} finally {
			try {
				if ( content != null ) {
					content.close();
				}
				if ( r != null ) {
					r.close();
				}
				if ( br != null ) {
					br.close();
				}
				if ( os != null ) {
					os.close();
				}
				if ( w != null ) {
					w.close();
				}
				if ( bw != null ) {
					bw.close();
				}
				if ( pw != null ) {
					pw.close();
				}
			} catch ( IOException ioex ) {
				getLogger().log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}

		
		return success;
	}	
	
	
	/**
	 * Write the content of a string to a file.
	 * 
	 * 
	 * @param content			The content as a string.
	 * @param outputCharsetName	The name of a supported output charset or
	 * 							null which then uses the default charset.
	 * @param path				The path of a file.
	 * @param append			If <code>true</true>, then bytes will be written to the end of the file
	 * 							rather than the beginning.
	 * @return success			<code>true</true> if reading the content & writing a file
	 * 							was successful, otherwise <code>false</code>.
	 * @throws FileException	File exception.
	 */
	public static boolean writeFile( String content, String outputCharsetName, String path, boolean append ) throws FileException {
		// TODO: check if necessary/useful/fix me
		getLogger().log( Level.DEBUG, "Method has been called." );
		boolean success		= false;
		File f				= null;
		OutputStream os		= null;
		Writer w			= null;
		BufferedWriter bw	= null;
		PrintWriter pw		= null;
		boolean dirsOK		= true;
		boolean fileOK		= true;

		
		try {
			// Create the file instance
			f				= new File( path );
		} catch ( Exception ex ) {
			throw new FileException( "Failed to load the file.", ex );
		}

		try {
			// Test the existence of the file
			if ( f.exists() == false ) {
				// Check parents
				if ( f.getParent() != null ) {
					// Test the existence of the directories
					if ( (f.getParentFile().exists() == false ) ) {
						dirsOK	= f.getParentFile().mkdirs();						
					}
				}

				if ( dirsOK == true ) {
					// Create the file
					fileOK	= f.createNewFile();
					getLogger().log( Level.DEBUG, "File has been created." );
				}
			}
			
			// OK to use the file
			if ( ( f.isFile() == true ) && ( f.canRead() == true ) && ( f.canWrite() == true ) && ( dirsOK == true ) && ( fileOK == true ) ) {
				getLogger().log( Level.DEBUG, "File is okay." );
				// Write the file
				os	= new FileOutputStream( f, append );
				if ( outputCharsetName == null ) {
					w	= new OutputStreamWriter( os );
				}
				else {
					w	= new OutputStreamWriter( os, outputCharsetName );
				}
				bw	= new BufferedWriter( w );
				pw	= new PrintWriter( bw );
				pw.println( content );
				pw.flush();
				
				// Check errors
				if ( pw.checkError() == true ) {
					throw new IOException( "ERROR: Failed to print stream." );
				}
				
				success	= true;
				getLogger().log( Level.DEBUG, "File has been written." );
			}
			else {
				throw new FileException( "File is not okay." );
			}
		} catch ( FileException fex ) {
			throw fex;
		} catch ( Exception ex ) {
			throw new FileException( "Failed to write the content of a string to a file.", ex );
		} finally {
			try {
				if ( os != null ) {
					os.close();
				}
				if ( w != null ) {
					w.close();
				}
				if ( bw != null ) {
					bw.close();
				}
				if ( pw != null ) {
					pw.close();
				}
			} catch ( IOException ioex ) {
				getLogger().log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}

		
		return success;
	}
}