package com.finger.agent.scrap.service;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.finger.agent.config.FcAgentConfig;

public class ServiceCodeXmlDO 
{
    public static final String SERVICE = "service";
    public static final String SERVICES = "services";
    public static final String ID = "id";
    public static final String FIELDS = "field";
    public static final String SEVICE_NAME = "name";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_KONAME = "koName";
    public static final String FIELD_ENNAME = "enName";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_ISNOTNULL = "isNotNull";
    public static final String BIZ_SERVICE = "biz_service";
    public static final String BIZ_NAME = "bizName";
    public static final String BIZ_CODE = "bizCode";
    public static final String BIZ_FIELD = "fieldCode";
    
    public static final String OUT_FIELD = "output";
    public static final String IN_FIELD = "input";

    public static Element loadDocument(File file) throws ServiceCodeXmlDoException {
        Document doc = null;
        try {
            //File file = new File(location);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
            doc = parser.parse(file);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        } catch (SAXParseException err) {
            System.out.println( err.getMessage() + "CodeXmlDO=loadDocument1>>");
            throw new ServiceCodeXmlDoException("CodeXmlDO ** Parsing error" + ", line "
                                        + err.getLineNumber()
                                        + ", uri " + err.getSystemId()
                                        + " CodeXmlDO", err);
        } catch (SAXException se) {
            System.out.println( se.getMessage() + "CodeXmlDO=loadDocument2>>");
            throw new ServiceCodeXmlDoException(se.getMessage(),se);
        } catch (Exception e) {
            System.out.println( e.getMessage() + "CodeXmlDO=loadDocument3>>");
            throw new ServiceCodeXmlDoException(e.getMessage(),e);
        }
    }

    public static HashMap<String, ServiceCodeDataSet> loadCodeDefinitions(File file)
                          throws ServiceCodeXmlDoException {
        Element root = loadDocument(file);
        return getScreens(root);
    }

    public static HashMap<String, ServiceCodeDataSet> getScreens(Element root) 
    {
        HashMap<String, ServiceCodeDataSet> codes = new HashMap<String, ServiceCodeDataSet>();

        NodeList listServices = root.getElementsByTagName(SERVICES);
        for (int i = 0; i < listServices.getLength(); i++) 
        {
            Node nodeServices = listServices.item(i);
            String id = ((Element)nodeServices).getAttribute(ID);
            ServiceCodeDataSet codeSet = new ServiceCodeDataSet(id);
            if (nodeServices != null)
            {
                NodeList listService = ((Element)nodeServices).getElementsByTagName(SERVICE);
                for(int j = 0; j < listService.getLength(); j++)
                {
                    Node nodeService = listService.item(j);
                    /*String name = ((Element)nodeService).getAttribute(SEVICE_NAME);*/
                    //codeSet.setFields( getFields(nodeService) );
                    //input
                    NodeList inputNode = ((Element)nodeService).getElementsByTagName(IN_FIELD);
                    codeSet.setInFields( getFields(inputNode) );
                    //output
                    NodeList outputNode = ((Element)nodeService).getElementsByTagName(OUT_FIELD);
                    codeSet.setOutFields( getFields(outputNode) );
                    codeSet.setBizServices( getBizService(nodeService) );
                }
            }
            codes.put(id, codeSet);
        }
        return codes;
    }
    
    private static HashMap<String, ServiceField> getFields(NodeList nodeList) {
        HashMap<String, ServiceField> fields = new HashMap<String, ServiceField>();
        for (int i = 0; i < nodeList.getLength(); i++) 
        {
            Node node = nodeList.item(i);
            if (node != null) {
                NodeList  children = node.getChildNodes();
                for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                    Node  child = children.item(innerLoop);
                    if ((child != null) && (child.getNodeName() != null) 
                         && child.getNodeName().equals(FIELDS) ) 
                    {
                        if (child instanceof Element) 
                        {
                            Element childElement = ((Element)child);
                            String code = childElement.getAttribute(FIELD_CODE);
                            String koName = childElement.getAttribute(FIELD_KONAME);
                            String enName = childElement.getAttribute(FIELD_ENNAME);
                            String lenString = childElement.getAttribute(FIELD_LENGTH);
                            String isNotNullString = childElement.getAttribute(FIELD_ISNOTNULL);
                            boolean isNotNull = false;
                            if ((isNotNullString != null) 
                                 && isNotNullString.equals("true")) {
                                isNotNull = true;
                            }
                            int length = 0;
                            try {
                                length = Integer.parseInt(lenString);
                            } catch (Exception e) {
                                length = 0;
                            }
                            fields.put(enName, 
                                    new ServiceField(code, koName, enName, length, isNotNull));
                           // System.out.println("toString:"+enName+","+koName);
                        }
                    }
                }
            }
        }
        return fields;
    }

    private static HashMap<String, BizService> getBizService(Node node) {
        HashMap<String, BizService> bizService = new HashMap<String, BizService>();
        if (node != null) {
            NodeList  children = node.getChildNodes();
            for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                Node  child = children.item(innerLoop);
                if ((child != null) && (child.getNodeName() != null) 
                     && child.getNodeName().equals(BIZ_SERVICE) ) {
                    if (child instanceof Element) {
                        Element childElement = ((Element)child);
                        String bizName = childElement.getAttribute(BIZ_NAME);
                        String bizCode = childElement.getAttribute(BIZ_CODE);
                        String fieldCode = childElement.getAttribute(BIZ_FIELD);
                        bizService.put(bizCode, new BizService(bizName,bizCode,fieldCode));
                     }
                }
            }
        }
        return bizService;
    }
    
  
    public static String getSubTagValue(Node node, String subTagName) {
        String returnString = "";
        if (node != null) {
            NodeList  children = node.getChildNodes();
            for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                Node  child = children.item(innerLoop);
                if ((child != null) && (child.getNodeName() != null) 
                     && child.getNodeName().equals(subTagName) ) {
                    Node grandChild = child.getFirstChild();
                    if (grandChild.getNodeValue() != null) return grandChild.getNodeValue();
                }
            }
        }
        return returnString;
    }

    
    public static String getSubTagValue(Element root, String tagName, String subTagName) {
        String returnString = "";
        NodeList list = root.getElementsByTagName(tagName);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                NodeList  children = node.getChildNodes();
                for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) 
                {
                    Node  child = children.item(innerLoop);
                    if ((child != null) && (child.getNodeName() != null) 
                        && child.getNodeName().equals(subTagName) ) 
                    {
                        Node grandChild = child.getFirstChild();
                        if (grandChild.getNodeValue() != null) return grandChild.getNodeValue();
                    }
                }
            }
        }
        return returnString;
    }

    public static String getTagValue(Element root, String tagName) {
        String returnString = "";
        NodeList list = root.getElementsByTagName(tagName);
        for (int loop = 0; loop < list.getLength(); loop++) 
        {
            Node node = list.item(loop);
            if (node != null) 
            {
                Node child = node.getFirstChild();
                if ((child != null) && child.getNodeValue() != null) return child.getNodeValue();
            }
        }
        return returnString;
    }
    
  
    public static void main(String args[]) throws Exception {
        String path = "";
        HashMap<String, ServiceCodeDataSet> hash = ServiceCodeXmlDO.loadCodeDefinitions(new File(path));
        System.out.println("==========================================================\n"+ hash );
    }
    
    
}
