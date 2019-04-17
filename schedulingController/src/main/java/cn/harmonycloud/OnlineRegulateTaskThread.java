package cn.harmonycloud;

import cn.harmonycloud.beans.Service;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.dao.imp.ServiceDaoImp;
import cn.harmonycloud.service.serviceImp.OnlineRegulateControlServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @classname：OnlineRegulateTaskThread
 * @author：WANGYUZHONG
 * @date：2019/4/10 21:18
 * @description: 处理在线应用调控分析的线程
 * @version:1.0
 **/
public class OnlineRegulateTaskThread implements Runnable{

    private final static Logger LOGGER = LoggerFactory.getLogger(OnlineRegulateTaskThread.class);

    @Autowired
    OnlineRegulateControlServiceImp iOnlineRegulateControl;

    @Value("${ClusterIp}")
    private String clusterIp;

    @Override
    public void run() {
        ServiceDAO serviceDAO = new ServiceDaoImp();
        List<Service> services = serviceDAO.getAllOnlineService(clusterIp);

        if (services == null) {
            LOGGER.error("get all online service failed!");
            return;
        }

        if (services.size() == 0) {
            LOGGER.error("there is no any online service!");
            return;
        }

        for (Service service :services) {
            iOnlineRegulateControl.dealService(service);
        }
    }
}
