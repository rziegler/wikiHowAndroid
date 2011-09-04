package ch.bbv.wikiHow.model;

public class Article {

	private String identifier;
	private String title;
	private String html;
	private String category;
	private boolean isCached;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isCached() {
		return isCached;
	}

	public void setCached(boolean isCached) {
		this.isCached = isCached;
	}
}
