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
        self.period = 'period'
        self.Type = 'Type'
        self.serviceLoad = 'serviceLoad'
        self.PodIntances = 'podInstancesNum'
        self.timeFrom = 'startTime'
        self.timeTo = 'endTime'
        self.podIntances = 'podIntances'
        self.numNetRequest = 'numNetRequest'
        self.responseTime = 'responseTime'
        self.ownerType = 'ownerType'
        self.ownerName = 'ownerName'

        self.hostPortURL="http://localhost:8080"
        #针对接口文档1部分
        self.url_service_getPodIntances = '/service/podNums'
        self.url_service_getNetVolumn = '/service/netVolume'

        #針對接口文檔內部功能點
        self.url_service_getLoadMappingIntances = '/service/loadMappingInstances'
        self.url_service_getResponseTime = '/inside/service/ResponseTime'
        self.url_service_getResourceCosume  = '/service/resourceConsume'