/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.utilities.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DOM4J对XML的操作
 * 
 * @author chenguang.zhou
 * @date 2015年1月14日 上午9:37:55
 */
public class XMLUtils{

    private static final Logger log = LoggerFactory.getLogger(XMLUtils.class);

    /**
     * 创建一个新的XML文档
     */
    public static Document createDocument(){
        return DocumentHelper.createDocument();
    }

    /**
     * 获取XML文档
     * 
     * @param inputXml
     */
    public static Document getDocument(InputStream inputXml){
        Document document = null;
        SAXReader saxReader = null;
        try{
            saxReader = new SAXReader();
            document = saxReader.read(inputXml);
        }catch (DocumentException e){
            log.error(e.getMessage(), e);
        }
        return document;
    }

    /**
     * 添加元素
     * 
     * @param name
     */
    public static Element addElement(Document document,String name){
        return document.addElement(name);
    }

    /**
     * 给元素中添加注释
     * 
     * @param comment
     */
    public static void addComment(Object obj,String comment){
        if (obj instanceof Document){
            ((Document) obj).addComment(comment);
        }else if (obj instanceof Element){
            ((Element) obj).addComment(comment);
        }
    }

    /**
     * 给元素添加属性和属性值
     * 
     * @param name
     * @param value
     */
    public static void setAttribute(Element element,String name,String value){
        element.addAttribute(name, value);
    }

    /**
     * 写xml文件
     * 
     * @param xmlStream
     * @param document
     */
    public static void writerXML(OutputStream outXml,Document document,String encoding){
        OutputFormat format = OutputFormat.createPrettyPrint();
        if (StringUtils.isBlank(encoding)){
            encoding = "UTF-8";
        }
        format.setEncoding(encoding);
        XMLWriter out = null;
        try{
            out = new XMLWriter(outXml, format);
            out.write(document);
            out.flush();
        }catch (UnsupportedEncodingException e){
            log.error(e.getMessage(), e);
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }finally{
            if (out != null){
                try{
                    out.close();
                }catch (IOException e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static Map<String, String> parserXml(String xml){
        Map<String, String> responseMap = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        try{
            Document document = saxReader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            Element employees = document.getRootElement();
            for (Iterator i = employees.elementIterator(); i.hasNext();){
                Element employee = (Element) i.next();
                Iterator j = employee.elementIterator();
                if (j.hasNext()){
                    for (; j.hasNext();){
                        Element node = (Element) j.next();
                        responseMap.put(employee.getName(), employee.getText());
                    }
                }else{
                    responseMap.put(employee.getName(), employee.getText());
                }
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return responseMap;
    }

    public static void main(String[] args){
        String xml = "<xml><appid>wx2421b1c4370ec43b</appid><attach>支付测试</attach>" + "<body>JSAPI支付测试</body>" + "<mch_id>10000100</mch_id>" + "<nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>"
                        + "<notify_url>http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url>" + "<openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>" + "<out_trade_no>1415659990</out_trade_no>" + "<spbill_create_ip>14.23.150.211</spbill_create_ip>"
                        + "<total_fee>1</total_fee>" + "<trade_type>JSAPI</trade_type>" + "<sign>0CB01533B8C1EF103065174F50BCA001</sign>" + "</xml>";
        Map<String, String> map = parserXml(xml);
        System.out.print(map);
    }
}
