package com.baozun.nebula.event;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.EmailAttachmentCommand;

public class EmailEvent extends AbstractNebulaEvent{

    private static final long serialVersionUID = 1970111120185633266L;

    /**
     * 这code只是定义的通用的模板
     * 如果有实施商城的code常量，请自建一个常量类
     * 如在自己项目中建一个constants包，并创建一个EmailConstants常量类
     */

    /** 激活邮件 */
    public static final String VALIDATE_ACTIVE = "EMAIL_VALIDATE_ACTIVE";

    /** 注册成功 */
    public static final String REG_SUCCESS = "EMAIL_REG_SUCCESS";

    /**
     * 收件人email
     */
    private String receiverEmail;

    private String code;

    private Map<String, Object> dataMap;

    /**
     * 附件列表
     */
    private List<EmailAttachmentCommand> attachmentList = null;

    /**
     * 邮件内容
     */
    @SuppressWarnings("unused")
    private String message;

    public EmailEvent(Object source, String receiverEmail, String code, Map<String, Object> dataMap){
        super(source);
        this.receiverEmail = receiverEmail;
        this.code = code;
        this.dataMap = dataMap;
    }

    public EmailEvent(Object source, String receiverEmail, String code, Map<String, Object> dataMap, List<EmailAttachmentCommand> attachmentList){
        super(source);
        this.receiverEmail = receiverEmail;
        this.code = code;
        this.dataMap = dataMap;
        this.attachmentList = attachmentList;
    }

    public String getReceiverEmail(){
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail){
        this.receiverEmail = receiverEmail;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public Map<String, Object> getDataMap(){
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap){
        this.dataMap = dataMap;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public List<EmailAttachmentCommand> getAttachmentList(){
        return attachmentList;
    }

    public void setAttachmentList(List<EmailAttachmentCommand> attachmentList){
        this.attachmentList = attachmentList;
    }

}
