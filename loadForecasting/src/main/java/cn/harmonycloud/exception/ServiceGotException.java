package cn.harmonycloud.exception;

/**
 * @author wangyuzhong
 * @date 18-12-6 下午5:34
 * @Despriction
 */
public class ServiceGotException extends RuntimeException
{
    private static final long serialVersionUID = 500L;

    public ServiceGotException()
    {
    }

    public ServiceGotException(String msg )
    {
        super( msg );
    }
}