package cn.harmonycloud.tools;

import cn.harmonycloud.beans.ForecastIndex;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author wangyuzhong
 * @date 19-1-21 下午3:57
 * @Despriction
 */
public class XMLUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(XMLUtil.class);

    private static String fileName = PropertyFileUtil.getValue("CONFIG_FIME_NAME");

    public static Element getRootNode() {
        SAXReader reader = new SAXReader();
        Element bookStore = null;
        try {
            Document document = reader.read(new File(fileName));
            bookStore = document.getRootElement();
        }catch (DocumentException e) {
            e.printStackTrace();
        }
        return bookStore;
    }


    public static Element getNodeElement() {
        Element rootElement = getRootNode();
        Iterator it = rootElement.elementIterator();
        while (it.hasNext()) {
            Element element = (Element)it.next();
            if ("node".equals(element.getName())) {
                return element;
            }
        }

        return null;
    }

    public static Element getServiceElement() {
        Element rootElement = getRootNode();
        Iterator it = rootElement.elementIterator();
        while (it.hasNext()) {
            Element element = (Element)it.next();
            if ("service".equals(element.getName())) {
                return element;
            }
        }

        return null;
    }

    public static Element getClusterElement() {
        Element rootElement = getRootNode();
        Iterator it = rootElement.elementIterator();
        while (it.hasNext()) {
            Element element = (Element)it.next();
            if ("cluster".equals(element.getName())) {
                return element;
            }
        }

        return null;
    }


    public static List<ForecastIndex> getNodeForecastIndex() {
        Element nodeRoot = getNodeElement();
        if (null == nodeRoot) {
            LOGGER.error("the config file not exist node index!");
            return null;
        }
        return getForecastIndexFromXml(nodeRoot);
    }


    public static List<ForecastIndex> getServiceForecastIndex() {
        Element serviceRoot = getServiceElement();
        if (null == serviceRoot) {
            LOGGER.error("the config file not exist service index!");
            return null;
        }
        return getForecastIndexFromXml(serviceRoot);
    }

    private static List<ForecastIndex> getForecastIndexFromXml(Element root) {
        List<ForecastIndex> list = new ArrayList<>();

        if (null == root) {
            throw new RuntimeException("The element is null!");
        }
        Iterator it = root.elementIterator();

        while(it.hasNext()) {

            Element element = (Element)it.next();

            String indexName = "";
            boolean status = false;
            String description = "";

            Iterator strategyIterator = element.elementIterator();
            while (strategyIterator.hasNext()) {
                Element nodeElement = (Element) strategyIterator.next();
                if ("indexName".equals(nodeElement.getName())) {
                    indexName = nodeElement.getStringValue();
                }else if ("status".equals(nodeElement.getName())) {
                    status = Boolean.valueOf(nodeElement.getStringValue());
                }else if ("description".equals(nodeElement.getName())) {
                    description = nodeElement.getStringValue();
                }else {
                    throw new RuntimeException("The config file have unknown element!");
                }
            }

            ForecastIndex evaluateStrategy = null;

            evaluateStrategy.setIndexName(indexName);
            evaluateStrategy.setStatus(status);
            evaluateStrategy.setDescription(description);

            if (evaluateStrategy.isStatus()) {
                list.add(evaluateStrategy);
            }
        }

        return list;
    }
}
