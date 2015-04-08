package com.example.bushramohamed.foodexpenditure;

import android.content.res.Resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by bushramohamed on 2015-03-28.
 */
public class DomParser {


    public String[] parseProvince(InputStream input){
        String[] provinceList = null;

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();

            Document document = dBuilder.parse(input);

            NodeList rootNodes = document.getElementsByTagName("Structure");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element)rootNode;
            NodeList structureList = rootElement.getElementsByTagName("CodeLists");

            Node theStructure = structureList.item(0);
            Element structureElement = (Element)theStructure;

            Node structureCodeList = structureElement.getElementsByTagName("structure:CodeList").item(0);
            Element  structureCodeListElement = (Element)structureCodeList;

            NodeList provinces = structureCodeListElement.getElementsByTagName("structure:Code");
            provinceList = new String[provinces.getLength()];

            for(int y = 0; y < provinces.getLength(); y++){
                Node nNode = provinces.item(y);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    provinceList[y] = eElement.getElementsByTagName("structure:Description").item(0).getTextContent();
                }
            }


        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return provinceList;


    }


    public List<Product> parseFoodCategory(InputStream input){
        List<Product> product = null;

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();

            Document document = dBuilder.parse(input);


            NodeList rootNodes = document.getElementsByTagName("Structure");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element)rootNode;
            NodeList structureList = rootElement.getElementsByTagName("CodeLists");

            Node theStructure = structureList.item(0);
            Element structureElement = (Element)theStructure;

            Node structureCodeList = structureElement.getElementsByTagName("structure:CodeList").item(2);
            Element  structureCodeListElement = (Element)structureCodeList;

            NodeList products = structureCodeListElement.getElementsByTagName("structure:Code");
            product = new ArrayList<Product>();

            for(int y = 0; y < products.getLength(); y++){
                Node nNode = products.item(y);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int value = Integer.parseInt(eElement.getAttribute("value"));
                    String productType = eElement.getElementsByTagName("structure:Description").item(0)
                                                 .getTextContent();

                    Product currentProduct = new Product();
                    currentProduct.setValue(value);
                    currentProduct.setProductType(productType);

                    product.add(currentProduct);

                }
            }


        }catch(Exception e){
            e.printStackTrace();
            return null;
        }


        return product;
    }

    public Product parseFoodDetail(Product product, String Geo_Location, InputStream input){

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();

            Document document = dBuilder.parse(input);

            NodeList rootNodes = document.getElementsByTagName("CompactData");
            Node rootNode = rootNodes.item(0);
            Element rootElement = (Element)rootNode;


            Node cansimDataSet = rootElement.getElementsByTagName("cansim:DataSet").item(0);

            Element cansimDataSetElement = (Element)cansimDataSet;

            NodeList data = cansimDataSetElement.getElementsByTagName("cansim:Series");


            for(int y = 0; y < data.getLength(); y++){
                Node nNode = data.item(y);

                int GEO = Integer.parseInt(Geo_Location);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    //Gets the specific region selected
                    if( Integer.parseInt(eElement.getAttribute("GEO")) == GEO && Integer.parseInt(eElement.getAttribute("SUMMARY"))
                            == product.getValue() ){

                        //Year 2012
                        Node n1 = eElement.getElementsByTagName("cansim:Obs")
                                .item(2);

                        try {
                            Element cansimObs1 = (Element) n1;
                            Double year12 = Double.parseDouble(cansimObs1.getAttribute("OBS_VALUE"));
                            product.setSpending_12(year12);

                            //Year 2013
                            Node n2 = eElement.getElementsByTagName("cansim:Obs")
                                    .item(3);

                            Element cansimObs2 = (Element) n2;
                            Double year13 = Double.parseDouble(cansimObs2.getAttribute("OBS_VALUE"));
                            product.setSpending_13(year13);

                            return product;

                        }catch (NumberFormatException e){
                            return null;
                        }
                    }


                }
            }


        }catch(Exception e){
            e.printStackTrace();
            return null;
        }



        return null;
    }

}