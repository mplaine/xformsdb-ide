package fi.tkk.media.xide.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFileServlet extends HttpServlet {
	
	
		private static final long serialVersionUID = 8305367618713715640L;
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			response.setContentType("text/plain");
			try
			{
				String path = getFileItem(request);
				if (path != null) {
					response.getWriter().write(new String("UPLOADOK" + ServerFileStructureParser.getFileType(path)));;
				}
				else {	
					throw new Exception("File saving failed");
				}
			}
			catch(Exception e)
			{
				response.getWriter().write(new String("Uploading failed : " + e.getMessage()));
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}

		/**
		 * 
		 * @param request
		 * @return path of the file
		 */
		private String getFileItem(final HttpServletRequest request)
		{
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Create a progress listener, if want to use
			ProgressListener progressListener = new ProgressListener()
			{
				private long megaBytes = -1;
				int i = -1;

				public void update(long pBytesRead, long pContentLength, int pItems)
				{
					if(i != pItems)
					{
						i = pItems;
					}
					long mBytes = pBytesRead / 10000;
					if(megaBytes == mBytes)
					{
						return;
					}
					megaBytes = mBytes;
					if(pContentLength == -1)
					{
//						System.out.println("So far, " + pBytesRead + " bytes have been read.");
					}
					else
					{
//						System.out.println("Read, " + pBytesRead + " of " + pContentLength + " bytes.");
					}
				}
			};
			upload.setProgressListener(progressListener);

			try
			{
				List items = upload.parseRequest(request);
//				System.out.println("List Items, size --->" + items.size());
				Iterator it = items.iterator();

				// Search for folder name
				while(it.hasNext())
				{
					FileItem item = (FileItem) it.next();
					if(item.isFormField())
					{
						// Folder name name
						if (item.getFieldName().equals("folderNameFormElement")) {
							folderName = item.getString();
						}
//						System.out.println("File String->" + item.getString());
					}
				}
				
				// Search for file
				it = items.iterator();
				while(it.hasNext())
				{
					FileItem item = (FileItem) it.next();
					if(!item.isFormField())
					{
//						System.out.println(String.format("File Name-> %s, Size-> %s, Content Type->%s, Isformfield-> %s, inMemory-> %s",
//	                                                   item.getFieldName(), item.getSize(), item.getContentType(), item.isFormField(), item.isInMemory()));
						return this.processUpload(item);
					}
				}
				return null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println(e.toString());
				return null;
			}
			
		}

		String folderName = "";
		private String processUpload(FileItem uploadItem)
		{
			try
			{
				InputStream input = uploadItem.getInputStream();
				byte[] fileContents = uploadItem.get();
				
				String fileName = uploadItem.getName();
				int slash = fileName.lastIndexOf("/");
				
				// 
				if(slash == -1){
					slash = fileName.lastIndexOf("\\");
				}

				if(slash != -1){
					fileName = fileName.substring(slash + 1);
				}

				try{
//					System.out.println("file " + folderName + fileName);
					File uploadedFile = new File(folderName + fileName);
					uploadItem.write(uploadedFile);
					return uploadedFile.getPath();
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				} 
				// TODO: add code to process file contents here.
				// Process a file upload
				/*
				 * if (writeToFile) {
				 * File uploadedFile = new File(...);
				 * item.write(uploadedFile);
				 * }
				 * else
				 * {
				 * InputStream uploadedStream =
				 * item.getInputStream();
				 * uploadedStream.close();
				 * }
				 */
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}
}
