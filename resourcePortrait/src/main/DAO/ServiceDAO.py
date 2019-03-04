from flask_sqlalchemy import SQLAlchemy
from flask import Flask
from config.config import Config
import uuid

config = Config()
app = Flask(__name__)
# url的格式为：mysql+pymysql://username:password@server/db
app.config["SQLALCHEMY_DATABASE_URI"] = "mysql+pymysql://root:123456@localhost/hybriddeployment"
# 动态追踪数据库的修改. 性能不好. 且未来版本中会移除. 目前只是为了解决控制台的提示才写的
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
# 创建数据库的操作对象
db = SQLAlchemy(app)


class Service(db.Model):
    __tablename__ = "service"
    serviceID = db.Column(db.String(50),primary_key=True)
    namespace = db.Column(db.String(50),nullable=False)
    serviceName = db.Column(db.String(50),nullable=False)
    clusterIP = db.Column(db.String(50),nullable=False)
    responseTime = db.Column(db.String(10),nullable=True)
    ownerName = db.Column(db.String(30),nullable=True)
    ownerType = db.Column(db.String(30),nullable=True)
    cpuRequest = db.Column(db.SMALLINT)
    cpuLimit = db.Column(db.SMALLINT)
    memRequest = db.Column(db.SMALLINT)
    memLimit = db.Column(db.SMALLINT)
    period =  db.Column(db.Integer)# 以分钟为单位
    ServiceType = db.Column(db.SMALLINT)#1.CPU密集型2.mem密集型3.网络上传密集型4.网络下载密集型
    def __init__(self,namespace=None,serviceName=None,clusterIP=None,cpuRequest=None,
                 cpuLimit=None,memRequest=None,memLimit=None,period=None,
                 ServiceType=None,responseTime=None,ownerName=None,ownerType=None):
        self.serviceID = str(uuid.uuid1())

        self.namespace = namespace
        self.serviceName = serviceName
        self.clusterIP = clusterIP
        self.cpuRequest = cpuRequest
        self.cpuLimit = cpuLimit
        self.memRequest = memRequest
        self.memLimit = memLimit
        self.period = period
        self.ServiceType = ServiceType
        self.responseTime = responseTime
        self.ownerType = ownerType
        self.ownerName = ownerName

    def to_json(self):
        dict = self.__dict__
        if "_sa_instance_state" in dict:
            del dict["_sa_instance_state"]
        return dict

    def __repr__(self):
        return '<Service %r>' % self.serviceName




class ServiceLoad_mapping_podInstances(db.Model):
    __tablename__ = "serviceLoad_mapping_podInstances"
    mappingID = db.Column(db.String(50),primary_key=True)
    serviceID = db.Column(db.String(50),db.ForeignKey(Service.serviceID))
    serviceLoad = db.Column(db.Integer,nullable=False)
    podInstances = db.Column(db.Integer,nullable=False)

    def __init__(self,serviceID=None,serviceLoad=None,podInstances=None):
        self.mappingID = str(uuid.uuid1())

        self.serviceID = serviceID
        self.serviceLoad = serviceLoad
        self.podInstances = podInstances

    def to_json(self):
        dict = self.__dict__
        if "_sa_instance_state" in dict:
            del dict["_sa_instance_state"]
        return dict

    def __repr__(self):
        return '<Mapping %r>' % self.mappingID

class Service_Storge_Info(db.Model):
    __tablename__ = "service_storge_info"
    storgeID = db.Column(db.String(50),primary_key=True)
    serviceID = db.Column(db.String(50),db.ForeignKey(Service.serviceID))
    storgeType = db.Column(db.Integer)
    storgeValue = db.Column(db.Integer)

    def __init__(self,serviceID=None,storgeType=None,storgeValue=None):
        self.storgeID = str(uuid.uuid1())

        self.serviceID = serviceID
        self.storgeType = storgeType
        self.storgeValue = storgeValue

    def to_json(self):
        dict = self.__dict__
        if "_sa_instance_state" in dict:
            del dict["_sa_instance_state"]
        return dict
    def __repr__(self):
        return '<storge %r>' % self.storgeID

#下面都是测试写的代码，与项目无关
class User(db.Model):
    __tablename__ = "user"
    id = db.Column(db.String(50),autoincrement=False,primary_key=True)
    username = db.Column(db.String(80),nullable=True)
    email = db.Column(db.String(120),nullable=False)

    def __init__(self,username=None, email=None):
        self.id = str(uuid.uuid1())
        self.username = username
        self.email = email

    def to_json(self):
        dict = self.__dict__
        if "_sa_instance_state" in dict:
            del dict["_sa_instance_state"]
        return dict

    def __repr__(self):
        return '<User %r>' % self.username

class Test(db.Model):
    __tablename__ = "Test"
    id = db.Column(db.Integer,autoincrement=True,primary_key=True)
    username = db.Column(db.String(80),nullable=False)
    email = db.Column(db.String(120),nullable=False)

    def __init__(self, username, email):
        self.username = username
        self.email = email

    def __repr__(self):
        return '<User %r>' % self.username
