package cn.harmonycloud.tools;

import cn.harmonycloud.strategy.AbstractClusterStrategy;
import cn.harmonycloud.strategy.AbstractNodeStrategy;
import cn.harmonycloud.strategy.AbstractServiceStrategy;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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

    private static String fileName = Constant.STRATEGY_CONFIG_FIME_NAME;

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
            if (Constant.NODE.equals(element.getName())) {
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
            if (Constant.SERVICE.equals(element.getName())) {
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
            if (Constant.CLUSTER.equals(element.getName())) {
                return element;
            }
        }

        return null;
    }


    public static List<AbstractNodeStrategy> getNodeStrategys() {
        Element root = getNodeElement();
        List<AbstractNodeStrategy> list = new ArrayList<>();

        if (null == root) {
            throw new RuntimeException("The strategy config not have node elememt!");
        }

        Iterator it = root.elementIterator();

        while(it.hasNext()) {

            Element element = (Element)it.next();

            String name = "";
            String className = "";
            double maxValue = 0;
            boolean status = false;
            String description = "";

            Iterator strategyIterator = element.elementIterator();
            while (strategyIterator.hasNext()) {
                Element nodeElement = (Element) strategyIterator.next();
                if ("name".equals(nodeElement.getName())) {
                    name = nodeElement.getStringValue();
                }else if("class".equals(nodeElement.getName())) {
                    className = nodeElement.getStringValue();
                }else if ("maxValue".equals(nodeElement.getName())) {
                    maxValue = Double.valueOf(nodeElement.getStringValue());
                }else if ("status".equals(nodeElement.getName())) {
                    status = Boolean.valueOf(nodeElement.getStringValue());
                }else if ("description".equals(nodeElement.getName())) {
                    description = nodeElement.getStringValue();
                }else {
                    throw new RuntimeException("The strategy config file have unknown element!");
                }
            }

            if ("".equals(className)) {
                throw new RuntimeException("Class element value is null!");
            }

            AbstractNodeStrategy abstractNodeStrategy = null;
            try {
                Class c=Class.forName(className);
                Object obj=c.newInstance();
                abstractNodeStrategy = (AbstractNodeStrategy)obj;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            abstractNodeStrategy.setName(name);
            abstractNodeStrategy.setClassName(className);
            abstractNodeStrategy.setMaxValue(maxValue);
            abstractNodeStrategy.setStatus(status);
            abstractNodeStrategy.setDescription(description);

            if (abstractNodeStrategy.isStatus()) {
                list.add(abstractNodeStrategy);
            }
        }

        return list;
    }


    public static List<AbstractServiceStrategy> getServiceStrategys() {
        Element root = getServiceElement();
        List<AbstractServiceStrategy> list = new ArrayList<>();

        if (null == root) {
            throw new RuntimeException("The strategy config not have service elememt!");
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

            if ("".equals(className)) {
                throw new RuntimeException("Class element value is null!");
            }

            AbstractServiceStrategy abstractServiceStrategy = null;
            try {
                Class c=Class.forName(className);
                Object obj=c.newInstance();
                abstractServiceStrategy = (AbstractServiceStrategy)obj;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            abstractServiceStrategy.setName(name);
            abstractServiceStrategy.setClassName(className);
            abstractServiceStrategy.setStatus(status);
            abstractServiceStrategy.setDescription(description);

            if (abstractServiceStrategy.isStatus()) {
                list.add(abstractServiceStrategy);
            }
        }

        return list;
    }

    public static List<AbstractClusterStrategy> getClusterStrategys() {
        Element root = getClusterElement();

        //TODO 后期扩展
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<AbstractNodeStrategy> list = getNodeStrategys();
        for (AbstractNodeStrategy abstractNodeStrategy:list) {
            System.out.println(abstractNodeStrategy.toString());
        }

        List<AbstractServiceStrategy> list1 = getServiceStrategys();
        for (AbstractServiceStrategy abstractServiceStrategy:list1) {
            System.out.println(abstractServiceStrategy.toString());
        }
    }
}
