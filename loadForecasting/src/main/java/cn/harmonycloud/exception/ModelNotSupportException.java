package cn.harmonycloud.exception;

/**
 * @author wangyuzhong
 * @date 18-12-26 下午5:14
 * @Despriction
 */
public class ModelNotSupportException extends RuntimeException{
    private static final long serialVersionUID = 500L;

    public ModelNotSupportException()
    {
    }

    public ModelNotSupportException(String msg )
    {
        super( msg );
    }
}
