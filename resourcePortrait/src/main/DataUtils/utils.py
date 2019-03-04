import json
import requests
import http.client
from config.config import Config
#定义一个全局配置类
config = Config()
'''
data = {"namespace" : "foo", "serviceName" : "22","clusterIP" : "22"}
in_json = json.dumps(data)
'''

class Utils:
    def __init__(self,url="",):
        self.url=url
    #解析返回一个URL json的数据
    def getResponse(self,requestParam,header={}):
        r = requests.get(url=self.url,params = requestParam,headers=header)
        print(requestParam)
        print(r.url)
        return r.json()
    def putResponse(self,requestParam,header={}):
        r = requests.put(url=self.url,data=requestParam,headers=header)
        return r.json()
    def postResponse(self,requestParam,header={}):
        r = requests.post(url=self.url,data=requestParam,headers=header)
        return r.json()
    def deleteResponse(self,requestParam,header={}):
        r = requests.delete(url=self.url,data=requestParam,headers=header)
        return r.json()



#test数据
"""
url="http://127.0.0.1:8088/inside/service/PodInstance"
test = Utils(url)
data = test.getResponse(requestParam=in_json)
print (data)
print (type(data))
"""

#getPodRequest(config.hostPortURL,namespace="default",serviceName="wordpress",clusterIP="192.168.122.133")
'''url="http://127.0.0.1:8089/inside/service/PodInstance"
test = Utils(url)
data = test.getResponse(requestParam=in_json)
'''
def adjustValidParameter(dictTemp):
    parameterPool = []
    for _,value in vars(config).items():
        parameterPool.append(value)

    for key in dictTemp:
        if key not in parameterPool:
            return 0
    return 1


#以下为判断参数是否存在在config配置中的函数模块测试
'''
#True
dictTest = {"serviceId":"111","serviceName":"Test"}
print("The result is "+str(adjustValidParameter(dictTest)))
#false
dictTest = {"serviceId":"111","wrong":"Test"}
print("The result is "+str(adjustValidParameter(dictTest)))
'''
