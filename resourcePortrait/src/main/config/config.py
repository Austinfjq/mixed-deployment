class Config:
    def __init__(self):
        #请求字段
        self.ServiceId = 'serviceId'
        self.namespace = 'namespace'
        self.serviceName = 'serviceName'
        self.clusterIP = 'clusterIP'
        self.cpuRequest = 'cpuRequest'
        self.cpuLimit = 'cpuLimit'
        self.memRequest = 'memReuqest'
        self.memLimit = 'memLimit'
        self.ownerName = 'ownerName'
        self.ownerType = 'ownerType'
        self.period = 'period'
        self.Type = 'Type'
        self.serviceLoad = 'serviceLoad'
        self.PodIntances = 'podInstancesNum'
        self.timeFrom = 'timeFrom'
        self.timeTo = 'timeTo'
        self.podIntances = 'podIntances'
        self.numNetRequest = 'numNetRequest'
        self.responseTime = 'responseTime'

        self.hostPortURL="http://localhost:8088"
        #针对接口文档1部分
        self.url_service_getPodIntances = '/inside/service/PodInstances'
        self.url_service_getNetVolumn = '/inside/service/NetVolumn'

        #針對接口文檔內部功能點
        self.url_service_getLoadMappingIntances = '/inside/service/LoadMappingInstances'
        self.url_service_getResponseTime = '/inside/service/ResponseTime'
        self.url_service_getResourceCosume  = '/inside/service/ResourceCosume'