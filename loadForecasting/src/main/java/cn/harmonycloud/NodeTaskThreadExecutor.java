package cn.harmonycloud;

import cn.harmonycloud.beans.ForecastCell;
import cn.harmonycloud.beans.ForecastIndex;
import cn.harmonycloud.beans.ForecastResultCell;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.service.IData;
import cn.harmonycloud.service.IForecast;
import cn.harmonycloud.service.imp.DataImp;
import cn.harmonycloud.service.imp.ForecastImp;
import cn.harmonycloud.tools.DataUtil;
import cn.harmonycloud.tools.PropertyFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 18-12-10 上午11:32
 * @Despriction
 */
public class NodeTaskThreadExecutor implements Runnable{

    private final static Logger LOGGER = LoggerFactory.getLogger(NodeTaskThreadExecutor.class);

    @Override
    public void run() {

        IData iData = new DataImp();
        IForecast iForecast = new ForecastImp();
        List<Node> nodes = iData.getNodeList(PropertyFileUtil.getValue("ClusterIp"));

        if (null == nodes) {
            LOGGER.error("get node list failed!");
        }

        List<ForecastIndex> nodeForecastIndices = AchieveForecastIndex.getNodeForecastIndexs();

        for (Node node:nodes) {
            for (ForecastIndex forecastIndex:nodeForecastIndices) {
                String ID = node.getMasterIp()+"&"+node.getHostName();
                ForecastCell forecastCell = iData.getForecastCell(ID,"1",forecastIndex.getIndexName());

                if (null == forecastCell) {
                    LOGGER.error("the forecastCell:"+forecastCell.toString()+" is not exit!");
                    break;
                }

                List<ForecastResultCell> forecastResultCells = iForecast.forecast(forecastCell);

                if (null == forecastResultCells) {
                    LOGGER.error("the forecastCell:"+forecastCell.toString()+" forecast failed!");
                    break;
                }
                boolean isForecast = iData.saveForecastData(DataUtil.listToJson(forecastResultCells));

                if (isForecast) {
                    LOGGER.debug("the " + forecastCell.toString() + " forecast succeed!");
                } else {
                    LOGGER.error("the " + forecastCell.toString() + " forecast failed!");
                }
            }
        }

    }


}
