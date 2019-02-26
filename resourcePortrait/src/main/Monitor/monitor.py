import json

from flask import Flask,request
from flask import jsonify
from config import error_config
from config.config import Config
from DataUtils.alterServiceData import updateService

#app = Flask(__name__)
from DAO.ServiceDAO import app
#获取相关配置信息
config = Config()

#错误处理
@app.errorhandler(error_config.CustomFlaskErr)
def handle_flask_error(error):

    # response 的 json 内容为自定义错误代码和错误信息
    response = jsonify(error.to_dict())

    # response 返回 error 发生时定义的标准错误代码
    response.status_code = error.status_code

    return response
#以下为模拟内部数据接口测试
@app.route('/inside/service/PodInstances', methods=['GET','POST'])
def getPodPeriod():
    dictTemp = {
        "podInstancesNum" : "2"
    }
    return jsonify(dictTemp)
@app.route('/inside/service/PodRequest', methods=['GET','POST'])
def getPodRequest():
    dictTemp = {
        "cpuCosume" : "2",
        "memConsume":"500"
    }
    return jsonify(dictTemp)

@app.route('/inside/service/NetVolumn', methods=['GET','POST'])
def getNetVolumn():
    dictTemp = {
        "DownNetIOCosume" : "2000",
        "UPNetIOCosume": "10000"
    }
    return jsonify(dictTemp)
@app.route('/inside/service/ResourceCosume', methods=['GET','POST'])
def getResourceCosume():
    dictTemp = {
        "timestamp" : "2000",
        "cpuCosume":"0.7",
        "memConsume":"100",
        "DownNetIOcosume":"100",
        "UPNetIOCosume":"200"
    }
    dictTemp2 = {
        "timestamp" : "2001",
        "cpuCosume":"0.7",
        "memConsume":"100",
        "DownNetIOcosume":"100",
        "UPNetIOCosume":"200"
    }
    temp=[dictTemp,dictTemp2]
    return jsonify(temp)
@app.route('/inside/service/LoadMappingInstances', methods=['GET','POST'])
def getLoadMappingInstances():
    dictTemp = {
        "timestamp" : "2000",
        "numInstances":"10",
        "numNetRequest":"2000"
    }
    dictTemp2 = {
        "timestamp" : "2000",
        "numInstances":"10",
        "numNetRequest":"2000"
    }
    temp=[dictTemp,dictTemp2]
    return jsonify(temp)

@app.route('/inside/service/ResponseTime', methods=['GET','POST'])
def getResponseTime():
    dictTemp = {
        "timestamp" : "2000",
        "respomseTime":"10"
    }
    return jsonify(dictTemp)


#获得服务周期
@app.route('/inside/service/SeriviceInfo', methods=['GET','POST'])
def getservicePeriod():
    data = request.get_data()
    dicttest = json.loads(data)
    if request.method =='POST':
        if config.namespace and config.serviceName and config.clusterIP in dicttest:#包含有必须参数
            if dicttest[config.namespace] =="" or dicttest[config.serviceName] =="" or dicttest[config.clusterIP] =="":
                raise error_config.CustomFlaskErr(error_config.PARAMETERS_INVALID,status_code=400)
            else:
                print(dicttest[config.namespace],dicttest[config.serviceName],dicttest[config.clusterIP])
                try:
                    tempCpuRequest = dicttest[config.cpuRequest]
                except KeyError:
                    tempCpuRequest = None
                try:
                    tempCpuLimit = dicttest[config.cpuLimit]
                except KeyError:
                    tempCpuLimit = None
                try:
                    tempMemRequest = dicttest[config.memRequest]
                except KeyError:
                    tempMemRequest = None
                try:
                    tempMemLimit = dicttest[config.memLimit]
                except KeyError:
                    tempMemLimit = None
                try:
                    tempType = dicttest[config.Type]
                except KeyError:
                    tempType = None
                try:
                    tempPeriod = dicttest[config.period]
                except KeyError:
                    tempPeriod = None
                try:
                    tempResponseTime = dicttest[config.period]
                except KeyError:
                    tempResponseTime = None
                #捕捉数据库异常
                try:

                    ret = updateService(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP],
                              cpuRequest=tempCpuRequest,cpuLimit=tempCpuLimit,memRequest=tempMemRequest,memLimit=tempMemLimit,period=tempPeriod,Type=tempType,responseTime=tempResponseTime)
                    if ret != 1:
                        value =  0
                        dicTepm ={
                            "isSucceed":value
                        }
                        return jsonify(dicTepm)
                    else:
                        value = 1
                        dicTepm ={
                            "isSucceed":value
                        }
                        #TODO  查看db.session.commit()提交如果不成功返回值
                        return jsonify(dicTepm)
                except Exception:
                    #print(Exception)
                    raise error_config.CustomFlaskErr(error_config.INTER_MACHINE_ERROR,status_code=501)
        else:
            raise error_config.CustomFlaskErr(error_config.PARAMETERS_NOTENOUGH,status_code=400)
    else:
        raise error_config.CustomFlaskErr(error_config.REQUEST_METHOD_ERROR,status_code=400)

if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0', port=8088)