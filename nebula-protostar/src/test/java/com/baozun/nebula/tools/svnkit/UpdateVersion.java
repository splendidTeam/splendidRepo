package com.baozun.nebula.tools.svnkit;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateVersion {
	
	public static  String orivsersion = null;
	
	public static boolean doc2XmlFile(Document document, String filename) {
		boolean flag = true;
		try {
			/** 将document中的内容写入文件中 */
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filename));
			 transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");  
			transformer.transform(source, result);
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}

	public static Document load(String filename) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File(filename));
			document.normalize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	
	public static void updateVersion(String version,String pomPath) {
		xmlUpdate(version, pomPath);
	}

	/**
	 * 修改文件的具体某个节点的值
	 */
	public static void xmlUpdate(String version,String pomPath) {
		Document document = load(pomPath);
		Element root = document.getDocumentElement();
		//修改各个项目的parent version
		NodeList parent =root.getElementsByTagName("parent");
		for (int i = 0; i < parent.getLength(); i++) {
			NodeList list  = parent.item(i).getChildNodes();
			for (int j = 0; j < list.getLength(); j++) {
				Node subnode =list.item(j);
				if (subnode.getNodeType() == Node.ELEMENT_NODE
						&& subnode.getNodeName() == "version") {
					if(orivsersion==null){
						//orivsersion = subnode.getTextContent();
					}
					subnode.getFirstChild().setNodeValue(version);
				}
			}
		}
		//修改parent中version版本号
		NodeList versions =root.getElementsByTagName("version");
		for (int i = 0; i < versions.getLength(); i++) {
			Node subnode  = versions.item(i);
			if (subnode.getNodeType() == Node.ELEMENT_NODE
					&& subnode.getParentNode().getNodeName().equals("project")
					//&& subnode.getTextContent().indexOf("version.nebula")==-1
					) {
				subnode.getFirstChild().setNodeValue(version);
			}
		}
		//修改parent中version.nebula版本号
		NodeList properties =root.getElementsByTagName("properties");
		for (int i = 0; i < properties.getLength(); i++) {
			NodeList list  = properties.item(i).getChildNodes();
			for (int j = 0; j < list.getLength(); j++) {
				Node subnode =list.item(j);
				if (subnode.getNodeType() == Node.ELEMENT_NODE
						&& subnode.getNodeName() == "version.nebula") {
					subnode.getFirstChild().setNodeValue(version);
				}
			}
		}

		doc2XmlFile(document, pomPath);
	}

	public static void main(String args[]) throws Exception {
		String version="1.0.8";
		String pomPath="D:/baocun-ws/nebula-repo-5.1.1b/pom.xml";
		UpdateVersion.updateVersion(version,pomPath);
	}
}
