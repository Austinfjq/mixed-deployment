import json

from flask import Flask,request
from flask import jsonify
from config import error_config
from config.config import Config
from DataUtils.getServiceData import getServiceTypeImpl,getMappingRecords,getPodRequest,getPodIntances,getNetVolumn
from DAO.ServiceDAO import app#!!!!!!!!!!!!!!!!!!!!!!!!!!

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

#获得需要创建Pod的当前需要占用的CPU,MEM,网络IO流量
@app.route('/')
@app.route('/service/PodCosume', methods=['GET','POST'])
def getPodCosume():
    data = request.get_data()
    dicttest = json.loads(data)
    if request.method =='POST':
        if config.namespace and config.serviceName and config.clusterIP in dicttest:#包含有必须参数
            if dicttest[config.namespace] =="" or dicttest[config.serviceName] =="" or dicttest[config.clusterIP] =="":
                raise error_config.CustomFlaskErr(error_config.PARAMETERS_INVALID,status_code=400)
            else:
                print(dicttest[config.namespace],dicttest[config.serviceName],dicttest[config.clusterIP])
                #获取当前PodCpuRequest和MemRequest   返回json
                ret = getPodRequest(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP])
                retDict = json.loads(ret)
                cpuRequest = retDict['cpuCosume']
                memRequest = retDict['memCosume']
                #获取网络返回一个字典
                retJsonNet = getNetVolumn(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP])
                retDictNet = retJsonNet
                downNetIOCosume = float(retDictNet["DownNetIOCosume"])
                upNetIOCosume = float(retDictNet["UPNetIOCosume"])

                #获取当前Pod实例数   返回json
                retJsonInstances = getPodIntances(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP])
                retDictInstances = retJsonInstances
                currentPodInstances = float(retDictInstances[config.PodIntances])
                podNeedDownNet = downNetIOCosume/currentPodInstances
                podNeedUpNet = upNetIOCosume/currentPodInstances
                toRetdict = {
                    "cpuCosume":str(cpuRequest),
                    "memRequest":str(memRequest),
                    "DownNetIOCosume":str(int(podNeedDownNet)),
                    "UPNetIOCosume":str(int(podNeedUpNet))
                }
                return jsonify(toRetdict)
        else:
            raise error_config.CustomFlaskErr(error_config.PARAMETERS_NOTENOUGH,status_code=400)
    else:
        raise error_config.CustomFlaskErr(error_config.REQUEST_METHOD_ERROR,status_code=400)


    #if request.method =='POST':



#获得服务所属类型
@app.route('/service/ServiceType', methods=['GET', 'POST'])
def getServiceType():
    data = request.get_data()
    dicttest = json.loads(data)
    if request.method =='POST':
        if config.namespace and config.serviceName and config.clusterIP in dicttest:#包含有必须参数
            if dicttest[config.namespace] =="" or dicttest[config.serviceName] =="" or dicttest[config.clusterIP] =="":
                raise error_config.CustomFlaskErr(error_config.PARAMETERS_INVALID,status_code=400)
            else:
                print(dicttest[config.namespace],dicttest[config.serviceName],dicttest[config.clusterIP])

                ret = getServiceTypeImpl(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP])
                print(ret)
                if ret is None:
                    raise error_config.CustomFlaskErr(error_config.INTER_MACHINE_ERROR,status_code=501)
                elif "error" in ret:
                    raise error_config.CustomFlaskErr(error_config.NOT_FOUND_ERROR,status_code=404)
                else:
                    return jsonify({}.fromkeys(['ServiceType'], ret['ServiceType']))
        else:
            raise error_config.CustomFlaskErr(error_config.PARAMETERS_NOTENOUGH,status_code=400)
    else:
        raise error_config.CustomFlaskErr(error_config.REQUEST_METHOD_ERROR,status_code=400)


#获得服务在保证Qos并指定单位时间请求数下的应至少有的Pod实例数
@app.route('/service/ServiceInstances', methods=['GET', 'POST'])
def getServiceInstances():
    #if request.method =='POST':
    data = request.get_data()
    dicttest = json.loads(data)
    if request.method =='POST':
        if config.namespace and config.serviceName and config.clusterIP and config.numNetRequest in dicttest:#包含有必须参数
            if dicttest[config.namespace] =="" or dicttest[config.serviceName] =="" or dicttest[config.clusterIP] ==""or dicttest[config.numNetRequest]==0:
                raise error_config.CustomFlaskErr(error_config.PARAMETERS_INVALID,status_code=400)
            else:
                print(dicttest[config.namespace],dicttest[config.serviceName],dicttest[config.clusterIP],dicttest[config.numNetRequest])
                ret = getMappingRecords(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP],numNetRequest=dicttest[config.numNetRequest])
                print(ret)
                if ret is None:
                    raise error_config.CustomFlaskErr(error_config.INTER_MACHINE_ERROR,status_code=501)
                elif "error" in ret:
                    raise error_config.CustomFlaskErr(error_config.NOT_FOUND_ERROR,status_code=404)
                else:
                    return jsonify({}.fromkeys(['podInstances'], ret['podInstances']))
        else:
            raise error_config.CustomFlaskErr(error_config.PARAMETERS_NOTENOUGH,status_code=400)
    else:
        raise error_config.CustomFlaskErr(error_config.REQUEST_METHOD_ERROR,status_code=400)


#获得服务周期
@app.route('/service/Period', methods=['GET','POST'])
def getservicePeriod():
    data = request.get_data()
    dicttest = json.loads(data)
    if request.method =='POST':
        if config.namespace and config.serviceName and config.clusterIP in dicttest:#包含有必须参数
            if dicttest[config.namespace] =="" or dicttest[config.serviceName] =="" or dicttest[config.clusterIP] =="":
                raise error_config.CustomFlaskErr(error_config.PARAMETERS_INVALID,status_code=400)
            else:
                print(dicttest[config.namespace],dicttest[config.serviceName],dicttest[config.clusterIP])

                ret = getServiceTypeImpl(namespace=dicttest[config.namespace],serviceName=dicttest[config.serviceName],clusterIP=dicttest[config.clusterIP])
                print(ret)
                if ret is None:
                    raise error_config.CustomFlaskErr(error_config.INTER_MACHINE_ERROR,status_code=501)
                elif "error" in ret:
                    raise error_config.CustomFlaskErr(error_config.NOT_FOUND_ERROR,status_code=404)
                else:
                    if ret['period'] is None:
                        value=""
                        IsPeriodism="0"
                    else:
                        value= str(ret['period'])
                        IsPeriodism="1"

                    dicTepm ={
                        "Period":value,
                        "IsPeriodism":IsPeriodism
                    }

                    return jsonify(dicTepm)
        else:
            raise error_config.CustomFlaskErr(error_config.PARAMETERS_NOTENOUGH,status_code=400)
    else:
        raise error_config.CustomFlaskErr(error_config.REQUEST_METHOD_ERROR,status_code=400)



if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0', port=8089)