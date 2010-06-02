package fi.tkk.media.xide.client.Data;
import java.io.Serializable;

public class Tag implements Serializable{
	
	String title;
	String description;

	int id;
	public Tag() {
	}
	
	public Tag(String title, String descr) {
		this.title = title;
		this.description = descr;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
}
