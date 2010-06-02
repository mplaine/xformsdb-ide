package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import fi.tkk.media.xide.client.utils.ConnectionToServer;

/**
 * Contains a set of checkers used for local check of the field value
 * @author evgeniasamochadina
 *
 */
public class LocalFieldCheckers {
	
	static interface LocalChecker {
		public String doCheck();
	}
	
	public static class TemplateIDChecker implements LocalChecker{
		TextBoxBase tb;
		
		public TemplateIDChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public String doCheck() {
			
			String text = tb.getText();
			// Check that the id starts with tpl_
			if (!text.startsWith("tpl_")) {
				return "ID should start with 'tpl_'";
			} 
			
			// Check the id doesn't have special symbols  or spaces
			String noSpaceAndSSymb = new NoSpaceAndSSymbChecker(tb).doCheck();
			if (!noSpaceAndSSymb.equals("")) {
				return noSpaceAndSSymb;
			}

			return "";
		}
	}
	
	public static class NoSpaceAndSSymbChecker implements LocalChecker{
		TextBoxBase tb;
		
		public NoSpaceAndSSymbChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public String doCheck() {
			
			// Check the id doesn't have special symbols 
			String specialSymbolResult = new NoSpecialSymbolsChecker(tb).doCheck();
			if (!specialSymbolResult.equals("")) {
				return specialSymbolResult;
			}
			
			// Check the id doesn't have spaces
			String noSpaceResult = new NoSpaceChecker(tb).doCheck();
			if (!noSpaceResult.equals("")) {
				return noSpaceResult;
			}
			return "";
		}
	}
	
	public static class NoSpaceChecker implements LocalChecker{
		TextBoxBase tb;
		
		public NoSpaceChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public String doCheck() {
			
			String text = tb.getText();
			
			if (text.contains(" ")){
				return "Field cannot contain spaces";
			}
			return "";
		}
	}
	
	public static class NoSpecialSymbolsChecker implements LocalChecker{
		TextBoxBase tb;
		
		public NoSpecialSymbolsChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public String doCheck() {
			
			String text = tb.getText();
			
			if (text.contains("'") || text.contains("/") || text.contains("<") || text.contains(">") || text.contains("&")){
				return "Field cannot contain special symbols like ', /, <, >, &";
			}
			
			return "";
		}
	}

}
