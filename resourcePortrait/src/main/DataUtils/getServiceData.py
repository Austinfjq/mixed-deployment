from DataUtils.utils import Utils
from config.config import Config
from Service.Service import findByThreeKeysInfo,findRecordsByThreeKeysInfo
import json
import requests
#定义一个全局配置类
config = Config()

#获取Pod实例数
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值:json
#1.podInstancesNum
def getPodIntances(namespace,serviceName,clusterIP,notEnoughURL=config.hostPortURL):
    newUrl = notEnoughURL+config.url_service_getPodIntances
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    #data = {"namespace" : "foo", "serviceName" : "22","clusterIP" : "22"}
    in_json = json.dumps(paramater)
    return temp.getResponse(requestParam=in_json)


#获取指定service的cpu_request,mem_request
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#json
def getPodRequest(namespace,serviceName,clusterIP,notEnoughURL=config.hostPortURL):
    #newUrl = notEnoughURL + config.url_service_getPodRequest
    #将请求参数封装成parameter
    ret = getServiceTypeImpl(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP)
    #temp = Utils(newUrl)
    print("***********TEST*******************")
    print(ret)
    dictTemp = {
        "cpuCosume" : ret['cpuRequest'],
        "memCosume": ret['memRequest']
    }
    return json.dumps(dictTemp)
    '''
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    #in_json = json.dumps(paramater)
    '''
    #in_json = json.dumps(paramater)
    #return temp.getResponse(requestParam=in_json)

# 获取指定service的实时网络IO流量:
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#1.
def getNetVolumn(namespace,serviceName,clusterIP,notEnoughURL=config.hostPortURL):
    newUrl = notEnoughURL + config.url_service_getNetVolumn
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    in_json = json.dumps(paramater)
    return temp.getResponse(requestParam = in_json)

#获得服务所属类型
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#1.
def getServiceTypeImpl(namespace,serviceName,clusterIP):
    #newUrl = notEnoughURL + config.url_service_getServiceType
    #temp = Utils(newUrl)
    #将请求参数封装成parameter
    '''
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    return temp.getResponse(paramaters = paramater)
    '''
    retServiceType=findByThreeKeysInfo(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP)
    print(len(retServiceType))
    error={"error":"NOT FOUND"}
    if retServiceType is None:#发生了异常
        return None
    elif len(retServiceType):
        return retServiceType[0]
    else:
        return error;

#getServiceTypeImpl(namespace='default',serviceName='hadoop',clusterIP='192.168.122.12')
#获得指定服务的所有负载实例数映射记录
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#1.
def getMappingRecords(namespace,serviceName,clusterIP,numNetRequest):
    retMapping=findRecordsByThreeKeysInfo(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP)
    print(len(retMapping))
    error={"error":"NOT FOUND"}
    min=1000000
    if retMapping is None:#发生了异常
        return None
    elif len(retMapping):
        #完成排序
        for index,item in enumerate(retMapping):
            if numNetRequest<item['serviceLoad'] and item['serviceLoad'] <min:#找比传入请求数大的最小的请求记录
                min=item['serviceLoad']
                subScript=index
        return retMapping[subScript]
    else:
        return error;


#获得指定服务的相应时间段的CPU,mem,网络上行速率，网络下载速率值
#時間傳入時間戳格式
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#5.timeFrom：記錄時間上界（時間戳格式）
#6.timeTo：記錄時間下界（時間戳格式）
#返回值
#1.
def getResourceCosume(namespace,serviceName,clusterIP,timeFrom,timeTo,notEnoughURL=config.hostPortURL):
    newUrl = notEnoughURL + config.url_service_getResourceCosume
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP,
                 config.timeFrom:timeFrom,
                 config.timeTo:timeTo}
    in_json = json.dumps(paramater)
    return temp.getResponse(requestParam=in_json)



#获得指定服务的相应时间段的服务单位时间请求数及实例数
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#5.timeFrom：記錄時間上界（時間戳格式）
#6.timeTo：記錄時間下界（時間戳格式）
#返回值
#1.timestamp:记录时间戳
#2.numInstances：相应服务的Pod实例数
#3.numNetRequest：服务单位时间请求数
#TODO
def getLoadMappingIntances(namespace,serviceName,clusterIP,timeFrom,timeTo,notEnoughURL=config.hostPortURL):
    newUrl = notEnoughURL + config.url_service_getLoadMappingIntances
    temp = Utils(newUrl)
    #将请求参数封装成parameter

    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP,
                 config.timeFrom:timeFrom,
                 config.timeTo:timeTo}
    in_json = json.dumps(paramater)

    return temp.getResponse(requestParam=in_json)


#获得指定服务的单位时间请求数响应时间平均值
#傳遞值：
#1.notEnoughURL ：http://主機:端口
#2.namespace: 服務所在namespace
#3.serviceName:服務名
#4.clusterIP：服务内部IP
#返回值
#1.timestamp:记录时间戳
#2.respomseTime，平均响应时间
def getResponseTime(namespace,serviceName,clusterIP,notEnoughURL=config.hostPortURL):
    newUrl = notEnoughURL + config.url_service_getResponseTime
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    in_json = json.dumps(paramater)
    return temp.getResponse(requestParam=in_json)



#获得服务的周期
#print(getResponseTime(namespace="default",serviceName="wordpress",clusterIP="192.168.122.133"))
