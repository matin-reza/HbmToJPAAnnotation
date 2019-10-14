package hbmToJPAAnnotation.impl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FilterTag {
	private String name;
	private String condition;
	private List<Attribute> paramList = new ArrayList<Attribute>();

	public FilterTag() {
	}

	public FilterTag(String name, String condition, List<Attribute> paramList) {
		this.name = name;
		this.condition = condition;
		this.paramList = paramList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<Attribute> getParamList() {
		return paramList;
	}

	public void setParamList(List<Attribute> paramList) {
		this.paramList = paramList;
	}

	public void addToListAttribute(Attribute attribute) {
		this.paramList.add(attribute);
	}

	public List<FilterTag> getFilterTag(Document document) throws Exception {

		List<FilterTag> filterTags = new ArrayList<FilterTag>();

		NodeList nodeList = document.getElementsByTagName("filter");

		for (int x = 0, size = nodeList.getLength(); x < size; x++) {

			FilterTag filterTag = new FilterTag();

			filterTag.setName(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
			filterTag.setCondition(nodeList.item(x).getAttributes().getNamedItem("condition").getNodeValue());

			filterTags.add(filterTag);
		}

		return filterTags;
	}

	public List<FilterTag> getFilterDefTag(Document document) throws Exception {

		List<FilterTag> filterTags = new ArrayList<FilterTag>();
		NodeList nodeList = document.getElementsByTagName("filter-def");
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {

			FilterTag filterTag = new FilterTag();

			filterTag.setName(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());

			Element filterParamElement = (Element) nodeList.item(x);
			NodeList paramTage = filterParamElement.getElementsByTagName("filter-param");
			for (int y = 0, size1 = paramTage.getLength(); y < size1; y++) {
				Attribute attr = new Attribute();
				attr.setName(paramTage.item(y).getAttributes().getNamedItem("name").getNodeValue());
				attr.setType(paramTage.item(y).getAttributes().getNamedItem("type").getNodeValue());
				filterTag.addToListAttribute(attr);
			}

			filterTags.add(filterTag);
		}

		return filterTags;
	}

}
