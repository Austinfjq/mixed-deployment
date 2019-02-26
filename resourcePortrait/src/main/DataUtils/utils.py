import json
import requests
import http.client


data = {"namespace" : "foo", "serviceName" : "22","clusterIP" : "22"}
in_json = json.dumps(data)

class Utils:
    def __init__(self,url="",):
        self.url=url
    #解析返回一个URL json的数据
    def getResponse(self,requestParam,header={}):
        r = requests.get(url=self.url,data=requestParam,headers=header)
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