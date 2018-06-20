package kit;

public enum ResultCodeEnum
{
	SUCCESS("30000","操作成功"),
	FAILED("30001","操作失败"),
	UN_LOGIN("30002","未登录"),
	ALREADY_LOGIN("30007","已有用户登录"),
	MISS_PARA("30003","缺少参数"),
	SERVER_EXCEPTION("30004","服务器异常"),
	CODE_ERROR("30005","验证码错误"),
	INCORRECT_PARA("30006","参数形式不符"),
	
	ACCOUNT_PASS_ERROR("10001","账号或密码错误"),
	PROPERTY_ALREADY("10002","已注册"),
	PHONE_ERROR("10003","手机号格式错误"),
	PHONE_EXIST("10004","手机号已存在"),
	ID_EXIST("10005","身份证号已存在"),
	ADRESS_EXIST("10006","地址已存在"),
	ID_ERROR("10007","身份证号格式错误"),
	OLDPHONE_ERROR("10008","旧手机号错误"),
	MAIL_EXIST("10009","邮箱已存在"),
	NO_ACCOUNT("10010","账号不存在"),
	
	IMG_UPLOAD_ERROR("50001","上传出现错误"),
	IMG_WRITE_ERROR("50002","文件写入服务器出现错误"),
	IMG_URL_ERROR("50003","文件不存在"),
	
	RECO_ERROR("50004","人脸识别失败"),
	
	DELETE_THEATER("40001","删除影厅失败"),
  
	SHOWTIME_CONFLICT("60000", "放映时间冲突");
	
	private String code;
    private String desc;

    ResultCodeEnum(String code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public String getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }

}
