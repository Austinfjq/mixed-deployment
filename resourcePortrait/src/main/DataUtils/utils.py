import json
import requests
import json
import http.client
data = {"spam" : "foo", "parrot" : 42}
in_json = json.dumps(data)
class Utils:
    def __init__(self,url=""):
        self.url=url

    def setURL(self,url):
        self.url=url
    def getURL(self):
        return self.url
    #解析返回一个URL json的数据
    def getdata(self,requestParam,header={}):
        r = requests.post(url=self.url,data=requestParam,headers=header)
        return r.json()
#test数据



url="http://127.0.0.1:5000/index"
test = Utils(url)
data = test.getdata(requestParam="",header={})
print (data)
print (type(data))