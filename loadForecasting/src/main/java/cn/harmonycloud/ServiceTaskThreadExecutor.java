package cn.harmonycloud;

import cn.harmonycloud.beans.*;
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
 * @date 18-12-5 下午3:03
 * @Despriction
 */
public class ServiceTaskThreadExecutor implements Runnable{


    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceTaskThreadExecutor.class);

    @Override
    public void run() {
        IData iData = new DataImp();
        IForecast iForecast = new ForecastImp();
        List<Service> services = iData.getAllOnlineService(PropertyFileUtil.getValue("ClusterIp"));

        if (null == services) {
            LOGGER.error("get service list failed!");
        }

        LOGGER.info("service list:" + services.toString());

        List<ForecastIndex> serviceForecastIndexs = AchieveForecastIndex.getServiceForecastIndexs();

        for (Service service:services) {
            for (ForecastIndex forecastIndex:serviceForecastIndexs) {
                String ID = service.getMasterIp()+"&"+service.getNamespace()+"&"+service.getServiceName();
                ForecastCell forecastCell = iData.getForecastCell(ID,0,forecastIndex.getIndexName());

                if (null == forecastCell) {
                    LOGGER.error("the forecastCell is null!");
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
