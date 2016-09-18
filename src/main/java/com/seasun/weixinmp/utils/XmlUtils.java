package com.seasun.weixinmp.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtils {
	protected static final Logger log = LoggerFactory.getLogger(XmlUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T parseObject(String xmlString, Class<T> returnClass) {
		try {
			JAXBContext context = JAXBContext.newInstance(returnClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			T returnObject = (T) unmarshaller.unmarshal(new StringReader(xmlString));

			return returnObject;
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
		return null;
	}

	public static String toXmlString(Object object) {
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			StringWriter writer = new StringWriter();
			marshaller.marshal(object, writer);
			String xmlString = writer.toString();
			return xmlString;
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
		return null;
	}
}