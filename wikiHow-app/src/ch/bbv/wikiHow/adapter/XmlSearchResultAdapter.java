package ch.bbv.wikiHow.adapter;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TwoLineListItem;
import ch.bbv.wikiHow.model.ArticleResult;

/**
 * Special adapter which parses the given XML from wikiHow.
 * 
 * @author ruthziegler
 * 
 */
public class XmlSearchResultAdapter extends BaseAdapter {

	private List<ArticleResult> results;
	private final LayoutInflater inflater;

	public XmlSearchResultAdapter(String xml, Context context) {
		Log.d(TAG, "XMLSearchResultAdapter created.");

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		try {
			results = parseXml(xml);
		} catch (Exception e) {
			Log.e(TAG, "Error parsing the XML.", e);
			results = Collections.EMPTY_LIST;
		}
	}

	public int getCount() {
		return results.size();
	}

	public Object getItem(int pos) {
		return results.get(pos);
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView : createView(parent);
		bindView(view, results.get(position));
		return view;
	}

	private TwoLineListItem createView(ViewGroup parent) {
		TwoLineListItem item = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		item.getText2().setSingleLine();
		item.getText2().setEllipsize(TruncateAt.END);
		return item;
	}

	private void bindView(TwoLineListItem view, ArticleResult article) {
		view.getText1().setText(article.getTitle());
		view.getText2().setText(article.getIdentifier());
	}

	private List<ArticleResult> parseXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml));

		Document doc = builder.parse(is);

		NodeList nodes = doc.getElementsByTagName("R");
		Log.d(TAG, "Number of nodes: " + nodes.getLength());

		results = new ArrayList<ArticleResult>(nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			Element item = (Element) nodes.item(i);

			NodeList idNode = item.getElementsByTagName("UE");
			String id = idNode.item(0).getTextContent();
			NodeList titleNode = item.getElementsByTagName("T");
			String title = titleNode.item(0).getTextContent();

			results.add(new ArticleResult(id, title));
		}

		return results;
	}
}
