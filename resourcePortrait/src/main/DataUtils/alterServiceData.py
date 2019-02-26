from DataUtils.utils import Utils
from config.config import Config
from Service.Service import add
from DAO.ServiceDAO import Service,Service_Storge_Info
from Service.Service import updateServiceInfo
from Service.Service import ServiceLoad_mapping_podInstances
from Service.Service import findServiceInfo,findRecordsByThreeKeysInfo
from Service.Service import delete
#定义一个全局配置类
config = Config()

#删除指定服务的所有负载实例数映射记录
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#1.isSucceed
def deleteMappingRecords(namespace,serviceName,clusterIP):
    #newUrl = notEnoughURL+config.url_service_deleteMappingRecords
    #temp = Utils(newUrl)
    #将请求参数封装成parameter
    #TODO
    '''

    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}


    return temp.deleteResponse(paramaters = paramater)
    '''
    rets = findRecordsByThreeKeysInfo(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP)
    if rets is None :
        return 0
    elif len(rets):
        for index,ret in enumerate(rets):
            print(index)
            delete(ret)
        return 1
    else:
        return 0

#添加指定服务的所有负载实例数映射记录
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#1.isSucceed

def insertMappingRecords(namespace,serviceName,clusterIP,serviceLoad,podIntances):


    service=findServiceInfo(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP)
    serviceID=service.serviceID
    serviceLoad_mapping_podIntances = ServiceLoad_mapping_podInstances(serviceID=serviceID,
                                                                       serviceLoad=serviceLoad, podInstances=podIntances)

    return add(serviceLoad_mapping_podIntances)


def insertServiceStorge(namespace,serviceName,clusterIP,storgeType,storgeValue):
    service=findServiceInfo(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP)
    serviceID=service.serviceID
    service_storge_info = Service_Storge_Info(serviceID=serviceID,
                                              storgeType=storgeType, storgeValue=storgeValue)
    return add(service_storge_info)



#更新服務信息
#傳遞值
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#5.cpuRequest:推荐其yaml配置文件cpuRequest值
#6.cpuLimit:推荐其yaml配置文件cpuLimit值
#7.memRequest:推荐其yaml配置文件memRequest值
#8.memLimit:推荐其yaml配置文件memLimit值
#9.period:服务内部IP
#10.Type:1.CPU密集型/2.mem密集型/3.网络上传密集型/4.网络下载密集型
#返回值
#1.isSucceed

def updateService(namespace,serviceName,clusterIP,cpuRequest=None,responseTime=None,cpuLimit=None,memRequest=None,memLimit=None,period=None,Type=None):
    service = Service(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP,cpuRequest=cpuRequest,
                      cpuLimit=cpuLimit,memRequest=memRequest,
                      memLimit=memLimit,period=period,ServiceType=Type,responseTime=responseTime)
    return updateServiceInfo(service)

#插入服務信息
#傳遞值
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#5.cpuRequest:推荐其yaml配置文件cpuRequest值
#6.cpuLimit:推荐其yaml配置文件cpuLimit值
#7.memRequest:推荐其yaml配置文件memRequest值
#8.memLimit:推荐其yaml配置文件memLimit值
#9.period:服务内部IP
#10.Type:1.CPU密集型/2.mem密集型/3.网络上传密集型/4.网络下载密集型
#返回值
#1.isSucceed
def insertServiceInfo(namespace,serviceName,clusterIP,cpuRequest=None,cpuLimit=None,memRequest=None,memLimit=None,period=None,Type=None):

    service = Service(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP,cpuRequest=cpuRequest,
                      cpuLimit=cpuLimit,memRequest=memRequest,
                      memLimit=memLimit,period=period,ServiceType=Type)
    return add(service)


#以下为测试代码
#insertServiceInfo(namespace="default",serviceName="wordpress",clusterIP="192.168.122.12",Type=1)
#updateService(namespace="default",serviceName="hadoop",clusterIP="192.168.122.12",period=600)
#addMappingRecords(namespace="default",serviceName="wordpress",clusterIP="192.168.122.133",serviceLoad=1000,podIntances=10)
#service=findServiceInfo(namespace="default",serviceName="hadoop",clusterIP="192.168.122.12")

#print(service.serviceID)
#addMappingRecords(namespace="default",serviceName="wordess",clusterIP="192.168.122.133",serviceLoad=3000,podIntances=25)
#insertServiceStorge(namespace='default',serviceName='wordpress',clusterIP='192.168.122.133',storgeType="emptyDIR",storgeValue=200)
