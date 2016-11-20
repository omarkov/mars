package web.java.databaseAccess;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import server.domain.Group;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import server.domain.User;

public class ManageData {

	static Document users;	
	static Document groups;
	
//	public User getUser(String userLoginID) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File usersXML = new File("c:/users.xml");
//		User foundUser = new User();
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			users = builder.parse("c:/users.xml");
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList loginIDs = users.getElementsByTagName("userLoginID");
//		for (int i = 0; i < loginIDs.getLength(); i++) {
//			if (loginIDs.item(i).getTextContent().equalsIgnoreCase(userLoginID)) {
//				Node parentNode = loginIDs.item(i).getParentNode();
//				NodeList rightUserNode = parentNode.getChildNodes();
//				for (int j = 0; j < rightUserNode.getLength(); j++) {
//					if (rightUserNode.item(j).getNodeName().equalsIgnoreCase("comment")) {
//						foundUser.setComment(rightUserNode.item(j).getTextContent());
//					}
//					if (rightUserNode.item(j).getNodeName().equalsIgnoreCase("firstName")) {
//						foundUser.setFirstName(rightUserNode.item(j).getTextContent());
//					}
//					if (rightUserNode.item(j).getNodeName().equalsIgnoreCase("lastName")) {
//						foundUser.setLastName(rightUserNode.item(j).getTextContent());
//					}
//					if (rightUserNode.item(j).getNodeName().equalsIgnoreCase("OpsTagID")) {
//						foundUser.setOpsTagID(rightUserNode.item(j).getTextContent());
//					}
//					if (rightUserNode.item(j).getNodeName().equalsIgnoreCase("password")) {
//						foundUser.setPassword(rightUserNode.item(j).getTextContent());
//					}
//					if (rightUserNode.item(j).getNodeName().equalsIgnoreCase("UserLoginID")) {
//						foundUser.setUserLoginID(rightUserNode.item(j).getTextContent());
//					}
//				}
//			}
//		}
//    return foundUser;
//	}
//	
//	/**
//	 * There must be a xml file in c:/ called users.xml. This file must
//	 * have only one root element. See xml-spezification.
//	 * @param user
//	 */
//	public void addUser(User user){
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File usersXML = new File("c:/users.xml");
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			users = builder.parse("c:/users.xml");
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList nodeList = users.getChildNodes();
//		Node userList = nodeList.item(0);
//		Element userElement = users.createElement("user");
//		
//		Element comment = users.createElement("comment");
//		comment.setTextContent(user.getComment());
//		
//		Element firstName = users.createElement("firstName");
//		firstName.setTextContent(user.getFirstName());
//		
//		Element lastName = users.createElement("lastName");
//		lastName.setTextContent(user.getLastName());
//		
//		Element userName = users.createElement("userLoginID");
//		userName.setTextContent(user.getUserLoginID());
//		
//		Element tagID = users.createElement("OpsTagID");
//		tagID.setTextContent(user.getOpsTagID());
//		
//		Element password = users.createElement("password");
//		password.setTextContent(user.getPassword());
//		
//		//TODO handle groups
////		Object[] groups = user.getGroups().toArray();
////		Group currentGroup = null;
////		for (int i = 0; i <= user.getGroups().size(); i++) {
////			currentGroup = (Group) groups[i];
////			Element groupElement = users.createElement("group");
////			groupElement.setTextContent(currentGroup.getId());
////		}
//
//		userElement.appendChild(firstName);
//		userElement.appendChild(lastName);
//		userElement.appendChild(userName);
//		userElement.appendChild(comment);
//		userElement.appendChild(tagID);
//		userElement.appendChild(password);
//		userList.appendChild(userElement);
//		
//	    TransformerFactory tFactory =
//	        TransformerFactory.newInstance();
//	    try {
//			Transformer transformer = tFactory.newTransformer();
//			DOMSource source = new DOMSource(users);
//			StreamResult result = new StreamResult(usersXML);
//			transformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//		
//	}
//	
//	/**
//	 * This method assumes that the userName of each user is unique
//	 * @param user The user to delete
//	 */
//	public void deleteUser(String userLoginID) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File usersXML = new File("c:/users.xml");
//
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			users = builder.parse("c:/users.xml");
//			//users = builder.newDocument();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList loginIDs = users.getElementsByTagName("userLoginID");
//		for (int i = 0; i < loginIDs.getLength(); i++) {
//			if (loginIDs.item(i).getTextContent().equalsIgnoreCase(userLoginID)) {
//				Node parentNode = loginIDs.item(i).getParentNode().getParentNode();
//				parentNode.removeChild(loginIDs.item(i).getParentNode());
//			}
//		}
//		
//	    TransformerFactory tFactory =
//	        TransformerFactory.newInstance();
//	    try {
//			Transformer transformer = tFactory.newTransformer();
//			DOMSource source = new DOMSource(users);
//			StreamResult result = new StreamResult(usersXML);
//			transformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//	}
//	/**
//	 * This method assumes that the userName of each user is unique
//	 * @param user The user to update
//	 */
//
//	public void updateUser(User user) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File usersXML = new File("c:/users.xml");
//
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			users = builder.parse("c:/users.xml");
//			//users = builder.newDocument();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList loginIDs = users.getElementsByTagName("userLoginID");
//		for (int i = 0; i < loginIDs.getLength(); i++) {
//			if (loginIDs.item(i).getTextContent().equalsIgnoreCase(user.getUserLoginID())) {
//				NodeList userToUpdate = loginIDs.item(i).getParentNode().getChildNodes();
//				for (int j = 0; j < userToUpdate.getLength(); j++) {
//					if (userToUpdate.item(j).getNodeName().equalsIgnoreCase("comment")) {
//						userToUpdate.item(j).setTextContent(user.getComment());
//					}
//					if (userToUpdate.item(j).getNodeName().equalsIgnoreCase("firstName")) {
//						userToUpdate.item(j).setTextContent(user.getFirstName());
//					}
//					if (userToUpdate.item(j).getNodeName().equalsIgnoreCase("lastName")) {
//						userToUpdate.item(j).setTextContent(user.getLastName());
//					}
//					if (userToUpdate.item(j).getNodeName().equalsIgnoreCase("OpsTagID")) {
//						userToUpdate.item(j).setTextContent(user.getOpsTagID());
//					}
//					if (userToUpdate.item(j).getNodeName().equalsIgnoreCase("password")) {
//						userToUpdate.item(j).setTextContent(user.getPassword());
//					}
//					if (userToUpdate.item(j).getNodeName().equalsIgnoreCase("UserLoginID")) {
//						userToUpdate.item(j).setTextContent(user.getUserLoginID());
//					}
//				}
//			}
//		}
//	    TransformerFactory tFactory =
//	        TransformerFactory.newInstance();
//	    try {
//			Transformer transformer = tFactory.newTransformer();
//			DOMSource source = new DOMSource(users);
//			StreamResult result = new StreamResult(usersXML);
//			transformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public Group getGroup(String groupID) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File groupsXML = new File("c:/groups.xml");
//		Group foundGroup = new Group();
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			groups = builder.parse("c:/groups.xml");
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList groupIDs = groups.getElementsByTagName("id");
//		for (int i = 0; i < groupIDs.getLength(); i++) {
//			if (groupIDs.item(i).getTextContent().equalsIgnoreCase(groupID)) {
//				Node parentNode = groupIDs.item(i).getParentNode();
//				NodeList rightGroupNode = parentNode.getChildNodes();
//				for (int j = 0; j < rightGroupNode.getLength(); j++) {
//					if (rightGroupNode.item(j).getNodeName().equalsIgnoreCase("comment")) {
//						foundGroup.setComment(rightGroupNode.item(j).getTextContent());
//					}
//					if (rightGroupNode.item(j).getNodeName().equalsIgnoreCase("name")) {
//						foundGroup.setName(rightGroupNode.item(j).getTextContent());
//					}
//					if (rightGroupNode.item(j).getNodeName().equalsIgnoreCase("id")) {
//						foundGroup.setId(rightGroupNode.item(j).getTextContent());
//					}
//					if (rightGroupNode.item(j).getNodeName().equalsIgnoreCase("expirationDate")) {
//						DateFormat expirationDate = null;
//						try {
//							foundGroup.setExpirationDate(expirationDate.parse(rightGroupNode.item(j).getTextContent()));
//						} catch (DOMException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//		return foundGroup;
//	}
//	
//	public void addGroup(Group newGroup){
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File groupsXML = new File("c:/groups.xml");
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			groups = builder.parse("c:/groups.xml");
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList nodeList = groups.getChildNodes();
//		Node groupList = nodeList.item(0);
//		Element groupElement = groups.createElement("group");
//		
//		Element comment = groups.createElement("comment");
//		comment.setTextContent(newGroup.getComment());
//		
//		Element name = groups.createElement("name");
//		name.setTextContent(newGroup.getName());
//		
//		Element expirationDate = groups.createElement("expirationDate");
//		expirationDate.setTextContent(newGroup.getExpirationDate().toString());
//		
//		Element id = groups.createElement("id");
//		id.setTextContent(newGroup.getId());
//
//		groupElement.appendChild(name);
//		groupElement.appendChild(comment);
//		groupElement.appendChild(id);
//		groupElement.appendChild(expirationDate);
//		groupList.appendChild(groupElement);
//		
//	    TransformerFactory tFactory =
//	        TransformerFactory.newInstance();
//	    try {
//			Transformer transformer = tFactory.newTransformer();
//			DOMSource source = new DOMSource(groups);
//			StreamResult result = new StreamResult(groupsXML);
//			transformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	
//	public void updateGroup(Group group) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File groupsXML = new File("c:/groups.xml");
//
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			groups = builder.parse("c:/groups.xml");
//			//users = builder.newDocument();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList ids = groups.getElementsByTagName("id");
//		for (int i = 0; i < ids.getLength(); i++) {
//			if (ids.item(i).getTextContent().equalsIgnoreCase(group.getId())) {
//				NodeList groupToUpdate = ids.item(i).getParentNode().getChildNodes();
//				for (int j = 0; j < groupToUpdate.getLength(); j++) {
//					if (groupToUpdate.item(j).getNodeName().equalsIgnoreCase("comment")) {
//						groupToUpdate.item(j).setTextContent(group.getComment());
//					}
//					if (groupToUpdate.item(j).getNodeName().equalsIgnoreCase("name")) {
//						groupToUpdate.item(j).setTextContent(group.getName());
//					}
//					if (groupToUpdate.item(j).getNodeName().equalsIgnoreCase("id")) {
//						groupToUpdate.item(j).setTextContent(group.getId());
//					}
//					if (groupToUpdate.item(j).getNodeName().equalsIgnoreCase("expirationDate")) {
//						groupToUpdate.item(j).setTextContent(group.getExpirationDate().toString());
//					}
//				}
//			}
//		}
//	    TransformerFactory tFactory =
//	        TransformerFactory.newInstance();
//	    try {
//			Transformer transformer = tFactory.newTransformer();
//			DOMSource source = new DOMSource(groups);
//			StreamResult result = new StreamResult(groupsXML);
//			transformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//
//	
//	public void deleteGroup(String id) {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		File groupsXML = new File("c:/groups.xml");
//
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			groups = builder.parse("c:/groups.xml");
//			//users = builder.newDocument();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NodeList ids = groups.getElementsByTagName("id");
//		for (int i = 0; i < ids.getLength(); i++) {
//			if (ids.item(i).getTextContent().equalsIgnoreCase(id)) {
//				Node parentNode = ids.item(i).getParentNode().getParentNode();
//				parentNode.removeChild(ids.item(i).getParentNode());
//			}
//		}
//		
//	    TransformerFactory tFactory =
//	        TransformerFactory.newInstance();
//	    try {
//			Transformer transformer = tFactory.newTransformer();
//			DOMSource source = new DOMSource(groups);
//			StreamResult result = new StreamResult(groupsXML);
//			transformer.transform(source, result);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//	}
//
//	/**
//	 * Only for testing puposes
//	 * @param args
//	 */
//	public static void main(String args[]) {
//		ManageData manageData = new ManageData();
//		User user = new User();
//		user.setComment("Androide");
//		user.setFirstName("Commander");
//		user.setLastName("Data");
//		user.setId("CommandDa");
//		user.setOpsTagID("1701E");
//		user.setPassword("OmegaAlpaBetaZeta");
//		user.setUserLoginID("1100110");
//		//manageData.addUser(user);
//		//manageData.deleteUser(user);
//		//manageData.updateUser(user);
//		//user = manageData.getUser("yeah");
//		//System.out.println(user.getFirstName());
//		Group newGroup = new Group();
//		newGroup.setComment("Golfgruppe");
//		Date expirationDate = new Date();
//		//expirationDate.setDate(32423423);
//		newGroup.setExpirationDate(expirationDate);
//		newGroup.setId("gruppe3");
//		newGroup.setName("G");
//		manageData.addGroup(newGroup);
//		//manageData.updateGroup(newGroup);
//		//manageData.deleteGroup("yeah");
//	}
}
