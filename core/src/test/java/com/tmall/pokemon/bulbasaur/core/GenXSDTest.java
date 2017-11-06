package com.tmall.pokemon.bulbasaur.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

/**
 * @author yunche.ch@taobao.com
 * @since 2013-1-23 下午12:10:16
 */
public class GenXSDTest {
	@Test
	public void gen() {

		// 创建一个扫描处理器， 扫描xsd
		ClassPathScanHandler handler = new ClassPathScanHandler();

		Set<File> fileList = handler.getPackageAllFiles("", true);// 得到所有xsd(state,invoke,process)
		// 准备好三个xsd框架
		SAXReader saxReader = new SAXReader();
		// process
		Document root_process = DocumentHelper.createDocument();
		Element category_process = root_process.addElement("xs:schema");
		category_process.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		category_process.addAttribute("elementFormDefault", "qualified");
		category_process.addAttribute("targetNamespace", "http://www.tmall.com/pokemon/bulbasaur/schema/process");
		// node
		Document root_node = DocumentHelper.createDocument();
		Element category_node = root_node.addElement("xs:schema");
		category_node.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		category_node.addAttribute("elementFormDefault", "qualified");
		category_node.addAttribute("targetNamespace", "http://www.tmall.com/pokemon/bulbasaur/schema/node");
		// invoke
		Document root_invoke = DocumentHelper.createDocument();
		Element category_invoke = root_invoke.addElement("xs:schema");
		category_invoke.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		category_invoke.addAttribute("elementFormDefault", "qualified");
		category_invoke.addAttribute("targetNamespace", "http://www.tmall.com/pokemon/bulbasaur/schema/invoke");

		for (File f : fileList) {
			// 遍历xsd文件，归类
			Document nodeInFile = null;
			try {
				nodeInFile = saxReader.read(f);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Element rootInFile = nodeInFile.getRootElement();// schema
			if ("http://www.tmall.com/pokemon/bulbasaur/schema/process".equals(rootInFile.attributeValue("targetNamespace"))) {
				List<Element> list = rootInFile.elements();
				for (Element level2 : list) {// 遍历二级节点
					category_process.add((Element) level2.clone());
				}
			}
			if ("http://www.tmall.com/pokemon/bulbasaur/schema/node".equals(rootInFile.attributeValue("targetNamespace"))) {
				List<Element> list = rootInFile.elements();
				for (Element level2 : list) {// 遍历二级节点
					category_node.add((Element) level2.clone());
				}
			}
			if ("http://www.tmall.com/pokemon/bulbasaur/schema/invoke".equals(rootInFile.attributeValue("targetNamespace"))) {
				List<Element> list = rootInFile.elements();
				for (Element level2 : list) {// 遍历二级节点
					category_invoke.add((Element) level2.clone());
				}
			}
		}

		String originPath = this.getClass().getResource("/").getPath() + "xsd/";
		File fp = new File(originPath);
		// 创建目录
		if (!fp.exists()) {
			fp.mkdirs();// 目录不存在的情况下，创建目录。
			System.out.println("路径不存在,但是已经成功创建了" + originPath);
		} else {
			System.out.println("文件路径存在" + originPath);
		}
		write(originPath + "process.xsd", category_process.asXML());
		write(originPath + "node.xsd", category_node.asXML());
		write(originPath + "invoke.xsd", category_invoke.asXML());
	}

	public void write(String path, String content) {
		try {
			File f = new File(path);
			if (f.exists()) {
				System.out.println("文件存在");
			} else {
				System.out.println("文件不存在，正在创建...");
				if (f.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
				}
			}

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(content);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
