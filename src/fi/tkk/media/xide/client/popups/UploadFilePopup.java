package fi.tkk.media.xide.client.popups;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.utils.PopupBase;

public class UploadFilePopup extends PopupBase {
	String fileName;

	public UploadFilePopup(final XIDEFolder parentFolder) {
//		super("Upload file", "You are going to upload a file under " + parentFolder.getRelatedPath()
//				+ ". Please select a file and press Upload button");
		super("Upload file", "You are going to upload a file under <b><i>" + parentFolder.getAbsolutePath()
				+ "</i></b>. Please select a file and press Upload button", true);
		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "UploadFile");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		HorizontalPanel hp = new HorizontalPanel();
		// Create a TextBox, giving it a name so that it will be submitted.
		final TextBox tb = new TextBox();
		tb.setText(parentFolder.getAbsolutePath());
		// tb.setEnabled(false);
		TextBox.setVisible(tb.getElement(), false);
		tb.setName("folderNameFormElement");
		hp.add(tb);

		// Create a FileUpload widget.
		final FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");

		hp.add(upload);

		// Add a 'submit' button.
		popup.addButton("Upload", new ClickHandler() {

			public void onClick(ClickEvent event) {
				fileName = upload.getFilename();
				form.submit();
			}});
		

		panel.add(hp);
		// Add an event handler to the form.
		form.addSubmitHandler(new SubmitHandler() {

			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can
				// take
				// this opportunity to perform validation.
				if (tb.getText().length() == 0) {
					Window.alert("The text box must not be empty");
					event.cancel();
				}// TODO Auto-generated method stub

			}

		});

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this event is
				// fired. Assuming the service returned a response of type text/html,
				// we can get the result text here (see the FormPanel documentation for
				// further explanation).

				String result = event.getResults();
				

				popup.hide();

				if (result.contains("UPLOADOK")) {
					boolean hasThisFile = false;

					for (Iterator<XIDEFile> iterator = parentFolder.getFileChild().iterator(); iterator.hasNext();) {
						XIDEFile file = iterator.next();
						if (file.getName().equals(fileName)) {
							hasThisFile = true;
							break;
						}
					}

					if (!hasThisFile) {
						// Uploading finished successfully
						XIDEFile newFile = new XIDEFile(fileName, parentFolder);

						// Need to parse file type to add in under proper list
						// of the currently selected element
						HasDisplayableProperties elementProperties = Main.getInstance().getSelectedElement().getProperties();
						
						HashMap list = XIDEFile.getListByFileType(result, elementProperties);
						if (list != null) {
							list.put(newFile.getName(), newFile);
						}

						new Popup(new HTML("File uploaded successfully!"));
					} else {
						new Popup(new HTML("File uploaded successfully! Previous version of this file is overwritten!"));
					}
					
					
					Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
					Main.getInstance().UpdateUI(Main.RIGHT_TAB);
					
					// Update page/application
					Main.getInstance().updateDateOfSelectedElement();
				} else {
					// Uploading failed
					new Popup(new HTML(event.getResults()));
				}
			}
		});

		popup.addContent(form);
		// addCloseButton("Upload", new Action() {
		//
		// public void doAction() {
		// // TODO Auto-generated method stub
		// form.submit();
		// }});
		popup.addCloseButton("Cancel");
		// RootPanel.get().add(form);

		popup.showPopup();
	}

}
