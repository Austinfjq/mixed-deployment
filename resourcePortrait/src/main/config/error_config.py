#定义错误状态码和相应Message

USER_ALREADY_EXISTS = 20001  # 用户已经存在
PARAMETERS_INVALID = 20002 #参数不合法
PARAMETERS_NOTENOUGH = 20003#指定不能省略的参数不够
REQUEST_METHOD_ERROR = 20004#请求方法错误
INTER_NOT_IMPLEMENT_ERROR = 20005#=未实现
INTER_MACHINE_ERROR = 20006#内部错误
NOT_FOUND_ERROR =20404#未找到




#message和自定义状态码的映射字典
J_MSG = {USER_ALREADY_EXISTS: 'User already exists',
         PARAMETERS_INVALID:'Error parameters',
         REQUEST_METHOD_ERROR:'Error request method',
         PARAMETERS_NOTENOUGH:'Not enough parameters',
         INTER_NOT_IMPLEMENT_ERROR:'Not Implemention',
         INTER_MACHINE_ERROR:'Inter Error',
         NOT_FOUND_ERROR:'Not Found Error'}

#自定义处理错误类
class CustomFlaskErr(Exception):

    # 默认的返回码
    status_code = 400

    # 自己定义了一个 return_code，作为更细颗粒度的错误代码
    def __init__(self, return_code=None, status_code=None, payload=None):
        Exception.__init__(self)
        self.return_code = return_code
        if status_code is not None:
            self.status_code = status_code
        self.payload = payload

    # 构造要返回的错误代码和错误信息的 dict
    def to_dict(self):
        rv = dict(self.payload or ())

        # 增加 dict key: return code
        rv['return_code'] = self.return_code

        # 增加 dict key: message, 具体内容由常量定义文件中通过 return_code 转化而来
        rv['message'] = J_MSG[self.return_code]

        # 日志打印
        print(J_MSG[self.return_code])

        return rv