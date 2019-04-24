package cn.harmonycloud.tools;

import cn.harmonycloud.beans.EvaluateStrategy;
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

    private static String fileName = PropertyFileUtil.getValue("STRATEGY_CONFIG_FIME_NAME");

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


    public static List<EvaluateStrategy> getNodeStrategys() {
        Element nodeRoot = getNodeElement();
        if (null == nodeRoot) {
            LOGGER.error("the strategy config file not exist node strategy!");
            return null;
        }
        return getEvaluateStrategys(nodeRoot);
    }


    public static List<EvaluateStrategy> getServiceStrategys() {
        Element serviceRoot = getServiceElement();
        if (null == serviceRoot) {
            LOGGER.error("the strategy config file not exist service strategy!");
            return null;
        }
        return getEvaluateStrategys(serviceRoot);
    }

    public static List<EvaluateStrategy> getClusterStrategys() {
        Element clusterRoot = getClusterElement();
        if (null == clusterRoot) {
            LOGGER.error("the strategy config file not exist cluster strategy!");
            return null;
        }
        return getEvaluateStrategys(clusterRoot);
    }

    private static List<EvaluateStrategy> getEvaluateStrategys(Element root) {
        List<EvaluateStrategy> list = new ArrayList<>();

        if (null == root) {
            throw new RuntimeException("The element is null!");
        }
        Iterator it = root.elementIterator();

        while(it.hasNext()) {

            Element element = (Element)it.next();

            String name = "";
            String className = "";
            boolean status = false;
            String description = "";

            Iterator strategyIterator = element.elementIterator();
            while (strategyIterator.hasNext()) {
                Element nodeElement = (Element) strategyIterator.next();
                if ("name".equals(nodeElement.getName())) {
                    name = nodeElement.getStringValue();
                }else if("class".equals(nodeElement.getName())) {
                    className = nodeElement.getStringValue();
                }else if ("status".equals(nodeElement.getName())) {
                    status = Boolean.valueOf(nodeElement.getStringValue());
                }else if ("description".equals(nodeElement.getName())) {
                    description = nodeElement.getStringValue();
                }else {
                    throw new RuntimeException("The strategy config file have unknown element!");
                }
            }

            EvaluateStrategy evaluateStrategy = null;

            evaluateStrategy.setName(name);
            evaluateStrategy.setClassName(className);
            evaluateStrategy.setStatus(status);
            evaluateStrategy.setDescription(description);

            if (evaluateStrategy.isStatus()) {
                list.add(evaluateStrategy);
            }
        }

        return list;
    }
}
