/**
 * \file
 * @see XmlParser .
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author hcw
 *
 * @version 1.0
 *
 */

package my.frmwk.util;


import org.w3c.dom.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * xml解析器
 * 提供一些通用方法返回XML中节点，供不同调用者使用。
 *
 */
public class XmlParser {
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		XmlParser parser = new XmlParser();
		System.out.println(parser.getNodeId("/abc"));
		System.out.println(Integer.parseInt(new String("0")));
	}
	
	public XmlParser()
	{

	}
	
	/**
	 * 加载xml stream
	 * @param strXsdPath
	 */
	public Document loadXml(InputStream XmlStream)
	{
		Document doc = null;

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		
	        factory.setIgnoringElementContentWhitespace(true);
	        DocumentBuilder db = factory.newDocumentBuilder();
	        doc = db.parse(XmlStream);    
		}
		catch (ParserConfigurationException e) 
		{
	        e.printStackTrace();
	    } 
		catch (SAXException e) 
	    {
	        e.printStackTrace();
	    } 
		catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
		return doc;
	}
	
	public String printNod(Node nod)
	{
		if(null == nod)
		{
			return "";
		}
		String strResult = "";
		strResult += "<" + nod.getNodeName();
		strResult += printAttrs(nod);
		strResult += ">";
		if(getValidChildLens(nod) > 0)
		{
			strResult += printChildNods(nod);
			strResult += "</" + nod.getNodeName() + ">";
		}
		else
		{
			strResult += nod.getTextContent();
			strResult += "</" + nod.getNodeName() + ">";
		}
		
		return strResult;
	}
	
	private int getValidChildLens(Node nod)
	{
		int iLen = 0;
		for(int i = 0, j = (null == nod ? 0 : nod.getChildNodes().getLength()); i < j; ++i)
		{
			Node tmp = nod.getChildNodes().item(i);
			if(tmp.getNodeType() == Node.ELEMENT_NODE)
			{
				++iLen;
			}
		}
		
		return iLen;
	}
	
	private String printChildNods(Node nod)
	{
		String strResult = "";
		for(int i = 0, j = (null == nod ? 0 : nod.getChildNodes().getLength()); i < j; ++i)
		{
			Node tmp = nod.getChildNodes().item(i);
			if(tmp.getNodeType() == Node.ELEMENT_NODE)
			{
				strResult += printNod(tmp);
			}
		}
		
		return strResult;
	}
	
	private String printAttrs(Node nod)
	{
		String strResult = "";
		NamedNodeMap attrs = nod.getAttributes();
		for(int i = 0, j = (null == attrs ? 0 : attrs.getLength()); i < j; ++i)
		{
			Node tmp = attrs.item(i);
			if(tmp.getNodeType() == Node.ATTRIBUTE_NODE)
			{
				strResult += " " + tmp.getNodeName() + "='" + tmp.getNodeValue() + "'";
			}
		}
		return strResult;
	}
		
	/**
	 * 仅适合节点名称唯一的xml 
	 * @param strObjPath 节点路径
	 * @return
	 */
	public Node getNodeByObjPath(Node nod, String strObjPath)
	{
		if(null == nod || null == strObjPath)
		{
			return null;
		}
		
		String strArray[] = strObjPath.split("/");;
		Node nodResult = nod;
		
		for(int i = 0; i < strArray.length; i++)
		{
			if(strArray[i].isEmpty())
			{
				continue;
			}
			
			nodResult = findNode(nodResult.getChildNodes(), strArray[i]);
			if(null == nodResult)
			{				
				break;
			}
		}
		
		return nodResult;
	}
	
	/**
	 * 根据对象路径返回该元素的内容
	 * @param strObjPath
	 * @return
	 */
	public String getContentByObjPath(Node nod, String strObjPath)
	{
		String strContent = new String("");
		Node tmp = getNodeByObjPath(nod, strObjPath);
		if(null == tmp)
		{
			System.out.println("Can't find the node by the obj path:" + strObjPath);
			return strContent;
		}
		
		strContent = tmp.getTextContent();
				
		return strContent;
	}
	
	/**
	 * 根据对象路径返回该元素的内容
	 * @param strObjPath
	 * @return
	 */
	public String getContentByNodName(Node nod, String strNodeName)
	{
		String strContent = new String("");
		for(int i = 0, j = (null == nod ? 0 : nod.getChildNodes().getLength()); i < j; ++i)
		{
			Node tmp = nod.getChildNodes().item(i);
			if(tmp.getNodeType() == Node.ELEMENT_NODE && tmp.getNodeName().equalsIgnoreCase(strNodeName))
			{
				strContent = tmp.getTextContent();
			}
		}
				
		return strContent;
	}
	
	/**
	 * 获取多个同名节点的内容
	 * @param doc
	 * @param strObjPath
	 * @param strNodeName
	 * @return
	 */
	public LinkedList<String> getContsByNodName(Node nod, String strObjPath, String strNodeName)
	{
		LinkedList<Node> nods = getNodsByObjpath(nod, strObjPath, strNodeName);
		LinkedList<String> results = new LinkedList<String>();
		for(int i = 0, j = (null == nods ? 0 : nods.size()); i < j; ++i)
		{
			results.add(nods.get(i).getTextContent());
		}
		
		return results;
	}
	
	/**
	 * 获取多个同名节点的内容
	 * @param doc
	 * @param strObjPath
	 * @param strNodeName
	 * @return
	 */
	public LinkedList<String> getContsByNodName(Node nod, String strNodeName)
	{
		LinkedList<String> results = new LinkedList<String>();
		for(int i = 0, j = (null == nod ? 0 : nod.getChildNodes().getLength()); i < j; ++i)
		{
			Node tmp = nod.getChildNodes().item(i);
			if(tmp.getNodeType() == Node.ELEMENT_NODE && tmp.getNodeName().equalsIgnoreCase(strNodeName))
			{
				results.add(tmp.getTextContent());
			}
		}
		
		return results;
	}
	
	public int getIdAttrValue(Node nod, String strAttrName)
	{
		NamedNodeMap attrs = nod.getAttributes();
		int id = -1;
		for(int i = 0, j = (null == attrs ? 0 : attrs.getLength()); i < j; ++i)
		{
			Node tmp = attrs.item(i);
			if(tmp.getNodeName().equalsIgnoreCase(strAttrName))
			{
				id = Integer.parseInt(tmp.getNodeValue());
			}
		}
		
		return id;
	}
	
	public String getAttrValue(Node nod, String strAttrName)
	{
		String str = "";
		if(null == nod){
			return str;
		}
		
		NamedNodeMap attrs = nod.getAttributes();
		for(int i = 0, j = (null == attrs ? 0 : attrs.getLength()); i < j; ++i)
		{
			Node tmp = attrs.item(i);
			if(tmp.getNodeName().equalsIgnoreCase(strAttrName))
			{
				str =tmp.getNodeValue();
			}
		}
		
		return str;
	}
	
	public boolean getEnableAttr(Node nod, String strAttrName)
	{
		NamedNodeMap attrs = nod.getAttributes();
		boolean bResult = true;
		for(int i = 0, j = (null == attrs ? 0 : attrs.getLength()); i < j; ++i)
		{
			Node tmp = attrs.item(i);
			if(tmp.getNodeName().equalsIgnoreCase(strAttrName) && 
					"false".equalsIgnoreCase(tmp.getTextContent()))
			{
				bResult = false;
			}
		}
		
		return bResult;
	}
	
	private int getNodeId(String strNodePath)
	{
		int id = -1;
		int id_begin = strNodePath.indexOf("[");
		int id_end = strNodePath.indexOf("]");
		if(id_begin > 0 && id_end > id_begin)
		{
			id = Integer.parseInt(strNodePath.substring(id_begin + 1, id_end));
		}
		
		return id;
	}
	
	
	/**
	 * 获取nod下所有名称为strNodeName的子节点
	 * @param nod
	 * @param strNodeName
	 * @return
	 */
	public LinkedList<Node> getNodsByName(Node nod, String strNodeName)
	{
		LinkedList<Node> nods = new LinkedList<Node>();
		for(int i = 0, j = (null == nod ? 0 : nod.getChildNodes().getLength()); i < j; ++i)
		{
			Node tmp = nod.getChildNodes().item(i);
			if(tmp.getNodeType() == Node.ELEMENT_NODE && tmp.getNodeName().equalsIgnoreCase(strNodeName))
			{
				nods.add(tmp);
			}
		}
		
		return nods;
	}
	
	/**
	 * 找出strObjPath下 所有名称为strNodeName 的节点。
	 * @param strObjPath
	 * @param strNodeName
	 * @return
	 */
	public LinkedList<Node> getNodsByObjpath(Node nodRoot, String strObjPath, String strNodeName)
	{
		LinkedList<Node> listNod = new LinkedList<Node>();
		Node nod = getNodeByObjPath(nodRoot, strObjPath);
		for(int i = 0, j = (null == nod ? 0 :nod.getChildNodes().getLength()); i < j; i++)
		{
			Node tmp = nod.getChildNodes().item(i);
			if(tmp.getNodeType() == Node.ELEMENT_NODE && tmp.getNodeName().equalsIgnoreCase(strNodeName))
			{
				listNod.add(tmp);
			}
		}
		
		
		return listNod;
	}

	/**
	 * 查找nods下节点名称为strNodeName的子节点
	 * @param nods
	 * @param strNodeName
	 * @return
	 */
	private Node findNode(NodeList nods, String strNodeName)
	{
		int id = getNodeId(strNodeName);
		strNodeName = (-1 == id ? strNodeName : strNodeName.substring(0, strNodeName.indexOf("[")));
		Node nod = null;
		for(int i = 0, j = (null == nods ? 0 : nods.getLength()); i < j; i++)
		{
			Node tmp = nods.item(i);
			if(tmp.getNodeName().equalsIgnoreCase(strNodeName) && id == getIdAttrValue(tmp, "id"))
			{
				nod = tmp;
				break;
			}
		}
		
		return nod;
	}	
}
