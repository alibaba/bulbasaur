package com.tmall.pokemon.bulbasaur.core;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.junit.Test;

/**
 * @author yunche.ch@taobao.com
 * @since 2013-1-15 下午01:48:13
 */
public class ValidataXMLTest {

	/**
	 * 通过XSD（XML Schema）校验XML
	 */
	@Test
	public void validateXMLByXSD() {
		String path = this.getClass().getResource("/").getPath();
		int index = path.lastIndexOf("/");
		path = path.substring(0, index);

		String xmlFileName = path + "/processCore.xml";
		// String xsdFileName =
		// "/Users/user/workspace/bulbasaur/core/src/test/resources/process_bak.xsd";
		String xsdFileName = path + "/test.xsd";
		try {
			// 创建默认的XML错误处理器
			XMLErrorHandler errorHandler = new XMLErrorHandler();
			// 获取基于 SAX 的解析器的实例
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 解析器在解析时验证 XML 内容。
			factory.setValidating(true);
			// 指定由此代码生成的解析器将提供对 XML 名称空间的支持。
			factory.setNamespaceAware(true);
			// 使用当前配置的工厂参数创建 SAXParser 的一个新实例。
			SAXParser parser = factory.newSAXParser();
			// 创建一个读取工具
			SAXReader xmlReader = new SAXReader();
			// 获取要校验xml文档实例
			Document xmlDocument = xmlReader.read(new File(xmlFileName));
			// 设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在
			// [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", "file:" + xsdFileName);
			// 创建一个SAXValidator校验工具，并设置校验工具的属性
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			// 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
			validator.setErrorHandler(errorHandler);
			// 校验
			validator.validate(xmlDocument);

			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
			// 如果错误信息不为空，说明校验失败，打印错误信息
			if (errorHandler.getErrors().hasContent()) {
				System.out.println("XML文件通过XSD文件校验失败！");
				writer.write(errorHandler.getErrors());
			} else {
				System.out.println("Good! XML文件通过XSD文件校验成功！");
			}
		} catch (Exception ex) {
			System.out.println("XML文件: " + xmlFileName + " 通过XSD文件:" + xsdFileName + "检验失败。\n原因： " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * 通过DTD校验XML
	 */
	public static void validateXMLByDTD() {
		// todo：暂时不用，以后再说吧
	}

}
