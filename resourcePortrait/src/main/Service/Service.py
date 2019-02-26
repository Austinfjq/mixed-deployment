from DAO.ServiceDAO import db,Service,ServiceLoad_mapping_podInstances
from flask import jsonify
#from flask_sqlalchemy import SQLAlchemy
from config import error_config

#增加
def add(newValue):
    try:
        db.session.add(newValue)
        db.session.commit()
    except(Exception) as e:
        print(e)
        return 0
    return 1
#TODO
#删除可以复用，先查找到（各自有自己的查找函数），删除

def delete(newValue):
    try:
        db.session.delete(newValue)
        db.session.commit()
    except(Exception) as e:
        print(e)
        return 0
    return 1
#TODO
#更新值

#其中newValue为Service()对象
def updateServiceInfo(newValue):

    Result = findServiceInfo(namespace=newValue.namespace,serviceName=newValue.serviceName,clusterIP=newValue.clusterIP)
    if Result is None:
        return None
    if newValue.cpuRequest is not None:
        Result.cpuRequest = newValue.cpuRequest
    if newValue.cpuLimit is not None:
        Result.cpuLimit = newValue.cpuLimit
    if newValue.memRequest is not None:
        Result.memRequest = newValue.memRequest
    if newValue.memLimit is not None:
        Result.memLimit = newValue.memLimit
    if newValue.period is not None:
        Result.period = newValue.period
    if newValue.ServiceType is not None:
        Result.ServiceType = newValue.ServiceType
    if newValue.responseTime is not None:
        Result.responseTime = newValue.responseTime
    db.session.commit()
    return 1
#查找
def findServiceInfo(namespace,serviceName,clusterIP):
    try:
        rets = Service.query.filter(Service.namespace == namespace,Service.serviceName == serviceName,Service.clusterIP == clusterIP).first()
        if rets is not None:
            return rets
        else:
            raise error_config.CustomFlaskErr(error_config.NOT_FOUND_ERROR,status_code=404)
    except(Exception) as e:
        print(e)
        return None


#查找服务按照输入参数：namespace，serviceName，clusterIP
def findByThreeKeysInfo(namespace,serviceName,clusterIP):
    try:
        rets = Service.query.filter(Service.namespace == namespace,Service.serviceName == serviceName,Service.clusterIP == clusterIP).all()
        result = []
        for ret in rets:
            result.append(ret.to_json())
        return result
    except(Exception) as e:
        print(e)
        return None

#查找映射记录实例数按照输入参数：namespace，serviceName，clusterIP
def findRecordsByThreeKeysInfo(namespace,serviceName,clusterIP):
    try:
        serviceID=findServiceInfo(namespace=namespace,serviceName=serviceName,clusterIP=clusterIP).serviceID
        rets = ServiceLoad_mapping_podInstances.query.filter(ServiceLoad_mapping_podInstances.serviceID == serviceID).all()
        result = []
        for ret in rets:
            result.append(ret.to_json())
        return result
    except(Exception) as e:
        print(e)
        return None

#print(findServiceInfo(namespace="default",serviceName="hadoop",clusterIP="192.168.122.12"))
#service = Service(namespace='default',serviceName='hadoop',clusterIP='192.168.122.12',cpuRequest=30,cpuLimit=120)
#updateServiceInfo(service)
#tests = Service()
#以下使用该框架利用投影操作减少数据库读入
'''
tests = User.query.with_entities(User.id).filter(User.email == '396@qq.com').all()

print(str(tests))

b=dict(zip("ID",tests))
print(b)
'''
'''
result = []
for test in tests:
    print(test)
    print(type(test))
    result.append(test.to_json())

print(type(tests))
print(result)

print(type(result))
'''
'''
serviceLoad_mapping_podIntances = ServiceLoad_mapping_podInstances(serviceID='60536412-0bfa-11e9-a672-a08869a6afc6',
                                                                   serviceLoad=1000, podInstances=10)
测试实例映射关系成功
add(serviceLoad_mapping_podIntances)
'''