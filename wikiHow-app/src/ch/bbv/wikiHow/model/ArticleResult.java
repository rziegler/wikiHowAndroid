package ch.bbv.wikiHow.model;

/**
 * Represents a result from the search via internet.
 * 
 * @author ruthziegler
 * 
 */
public class ArticleResult {

	private final String identifier;
	private final String title;

	public ArticleResult(String identifier, String title) {
		this.identifier = identifier;
		this.title = title;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getTitle() {
		return title;
	}
}
