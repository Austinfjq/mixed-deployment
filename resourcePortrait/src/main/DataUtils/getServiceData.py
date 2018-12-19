from getData.utils import Utils
from config.config import Config

#定义一个全局配置类
config = Config()


#获取Pod实例数
#传递服务所在namespace，服务名和服务IP
def getPodIntances(notEnoughURL,namespace,serviceName,clusterIP):
    newUrl = notEnoughURL+config.url_service_getPodIntances
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    #TODO
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    return temp.getdata(paramaters = paramater)


#获取指定service的cpu_request,mem_request
def getPodIntances(notEnoughURL,namespace,serviceName,clusterIP):
    newUrl = notEnoughURL + config.url_service_getPodRequest
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    #TODO
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    return temp.getdata(paramaters = paramater)

# 获取指定service的实时网络IO流量:
def getPodIntances(notEnoughURL,namespace,serviceName,clusterIP):
    newUrl = notEnoughURL + config.url_service_getNetVolumn
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    #TODO
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    return temp.getdata(paramaters = paramater)

#  获得服务所属类型
def getServiceType(notEnoughURL,namespace,serviceName,clusterIP):
    newUrl = notEnoughURL + config.url_service_getServiceType
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    #TODO
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    return temp.getdata(paramaters = paramater)

#获得指定服务的所有负载实例数映射记录
def getMappingRecords(notEnoughURL,namespace,serviceName,clusterIP):
    newUrl = notEnoughURL + config.url_service_getMappingRecords
    temp = Utils(newUrl)
    #将请求参数封装成parameter
    #TODO
    paramater = {config.namespace:namespace,
                 config.serviceName:serviceName,
                 config.clusterIP:clusterIP}
    return temp.getdata(paramaters = paramater)

#