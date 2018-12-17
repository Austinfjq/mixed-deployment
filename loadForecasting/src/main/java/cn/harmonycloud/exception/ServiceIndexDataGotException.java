package cn.harmonycloud.exception;

/**
 * @author wangyuzhong
 * @date 18-12-6 下午7:13
 * @Despriction
 */
public class ServiceIndexDataGotException extends RuntimeException
{
    private static final long serialVersionUID = 500L;

    public ServiceIndexDataGotException()
    {
    }

    public ServiceIndexDataGotException(String msg )
    {
        super( msg );
    }
}
