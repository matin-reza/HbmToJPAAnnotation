package hbmToJPAAnnotation.impl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FormulaTag extends Attribute {

	private String value;

	public FormulaTag() {
	}

	public FormulaTag(String name, String type, String setterGetter, String value) {
		super(name, type, setterGetter, null, null, null, null, null);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<FormulaTag> getFormulaPropertyTag(Document document) throws Exception {
		NodeList nodeList = document.getElementsByTagName("property");
		List<FormulaTag> pList = new ArrayList<FormulaTag>();
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {
			NamedNodeMap attrList = nodeList.item(x).getAttributes();
			Node formulaNode = attrList.getNamedItem("formula");
			if (formulaNode != null) {
				pList.add(new FormulaTag(

						attrList.getNamedItem("name").getNodeValue(),

						attrList.getNamedItem("type") == null ? "Date??"
								: attrList.getNamedItem("type").getNodeValue().replace(attrList.getNamedItem("type").getNodeValue().substring(0, 1),
										attrList.getNamedItem("type").getNodeValue().substring(0, 1).toUpperCase()),
						attrList.getNamedItem("name").getNodeValue(),

						attrList.getNamedItem("formula").getNodeValue()));
			}

		}

		return pList;

	}
}
