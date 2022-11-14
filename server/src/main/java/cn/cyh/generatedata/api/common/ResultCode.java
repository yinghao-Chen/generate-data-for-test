package cn.cyh.generatedata.api.common;

/**
 * @author cyh
 * @date 2022/11/14
 */
public enum ResultCode implements IResultCode {
    /**
     * 定义返回码
     */
    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常"),
    INTERNAL_SERVER_ERROR(500, "服务器异常");

    final int code;
    final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
