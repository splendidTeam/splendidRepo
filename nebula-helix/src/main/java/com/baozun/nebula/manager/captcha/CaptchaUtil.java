/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.manager.captcha;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.w3c.dom.css.Counter;

import com.baozun.nebula.manager.captcha.counter.CounterCompareType;
import com.baozun.nebula.manager.captcha.counter.ServerCounter;
import com.baozun.nebula.manager.captcha.counter.SessionCounter;
import com.baozun.nebula.manager.captcha.entity.CaptchaContainer;
import com.baozun.nebula.manager.captcha.entity.CaptchaContainerAndValidateConfig;
import com.baozun.nebula.manager.captcha.entity.CaptchaValidateConfig;
import com.baozun.nebula.manager.captcha.entity.CaptchaValidateEntity;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class CaptchaUtil.
 * 
 * <h3>spring xml配置示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre>
{@code

    <util:properties id="p_ruleConfig" local-override="true" location="classpath:config/ruleConfig.properties"></util:properties>

    <!-- Validate验证器 -->
    <bean id="botdetectCaptchaValidate" class="com.baozun.nebula.manager.captcha.validate.BotdetectCaptchaValidate"></bean>

    <!--********************* Counter********************* -->
    <!-- 目前是基于redis的实现,如需更换,方便将来替换 -->
    <bean id="serverCounter" class="com.baozun.nebula.manager.captcha.counter.CacheServerCounterImpl"></bean>
    <bean id="sessionCounter" class="com.baozun.nebula.manager.captcha.counter.SessionCounterImpl"></bean>

    <bean id="ruleResolver" class="com.baozun.nebula.manager.captcha.rule.DefaultRuleResolver">
        <property name="ruleConfig">
            <bean class="com.baozun.nebula.manager.captcha.rule.RuleConfig">
                <property name="whiteIplist" value="#}{p_ruleConfig['Captcha.whiteIplist'].split(',')} {@code " />
                <property name="blackIpList" value="#}{p_ruleConfig['Captcha.blackIpList'].split(',')} {@code" />
            </bean>
        </property>
    </bean>

    <!-- 登陆使用 -->
    <bean id="loginCaptchaContainerAndValidateConfig" class="com.baozun.nebula.manager.captcha.entity.CaptchaContainerAndValidateConfig">
        <property name="captchaContainer">
            <bean class="com.baozun.nebula.manager.captcha.entity.CaptchaContainer">
                <!-- 每个验证码唯一的id功能标识 -->
                <property name="id" value="loginCaptchaContainer"></property>

                <!-- 容错次数,定义为 <=0 表示无论何时,都需要显示验证码; 1标识表单提交,出错一次(比如用户名密码不匹配) 需要显示 -->
                <property name="countThreshold" value="3"></property>
            </bean>
        </property>
        <property name="captchaValidateConfig">
            <bean class="com.baozun.nebula.manager.captcha.entity.CaptchaValidateConfig">
                <!--Captcha id,通常用来标识 captcha的功能. -->
                <property name="captchaId" value="captcha_pc_login"></property>
                <!-- 请求里面,用户输入的验证码参数名字 -->
                <property name="userInputParamName" value="captchaValue"></property>
                <!--Instance id name.(optional,通常ajax请求需要设置该属性) -->
                <property name="instanceIdName" value="instanceId"></property>
                <!-- 校验器 -->
                <property name="captchaValidate" ref="botdetectCaptchaValidate"></property>
            </bean>
        </property>
    </bean>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>controller使用示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre>
{@code

    &#64;Qualifier("loginCaptchaContainerAndValidateConfig")
    private CaptchaContainerAndValidateConfig loginCaptchaContainerAndValidateConfig;

    &#64;RequestMapping(value = &#123; "/member/login" &#125;,method = RequestMethod.POST)
    public String login(
                    &#64;RequestParam("userName") String userName,
                    &#64;RequestParam("password") String password,
                    HttpServletRequest request,
                    HttpServletResponse response)&#123;

        //校验
        boolean result = CaptchaUtil.validate(loginCaptchaContainerAndValidateConfig, request);

        if (!result)&#123;
            //do some logic
            return null;
        &#125;

        boolean success = userName.equals("张三") && password.equals("123456");

        //业务处理 成功还是失败,成功那么计数器会清零;失败 计数器会+1
        CaptchaUtil.clearOrIncrTryNumber(loginCaptchaContainerAndValidateConfig.getCaptchaContainer().getId(), userName, success, request);

        if (success)&#123;//表示成功
            //do some logic
        &#125;
        return getSpringRedirectPath("/jsp/captcha/botdetect");
    &#125;
}
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * @author feilong
 * @version 1.5.3 2016年3月28日 下午4:34:27
 * @since 1.5.3
 */
public class CaptchaUtil{

    /** The Constant log. */
    private static final Logger                                   LOGGER         = LoggerFactory.getLogger(CaptchaUtil.class);

    /** key是 CaptchaContainer id. */
    private static Map<String, CaptchaContainerAndValidateConfig> containerIdMap = new ConcurrentHashMap<String, CaptchaContainerAndValidateConfig>();

    /** Don't let anyone instantiate this class. */
    private CaptchaUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 通过containerId 找到 {@link CaptchaContainerAndValidateConfig}.
     *
     * @param containerId
     *            the container id
     * @param request
     *            the request
     * @return the captcha container and validate config by container id
     */
    public static CaptchaContainerAndValidateConfig getCaptchaContainerAndValidateConfigByContainerId(
                    String containerId,
                    HttpServletRequest request){
        Validate.notEmpty(containerId, "containerId can't be null/empty!");

        //如果 是空, 那么表示第一次访问 还没有init
        if (Validator.isNullOrEmpty(containerIdMap)){
            initContainerIdMap(request);
        }

        Validate.notNull(
                        containerIdMap,
                        "containerIdMap can't be null!,you may be not config CaptchaContainerAndValidateConfig bean in spring");
        CaptchaContainerAndValidateConfig captchaContainerAndValidateConfig = containerIdMap.get(containerId);

        Validate.notNull(
                        captchaContainerAndValidateConfig,
                        "when containerId is :%s,can not find captchaContainerAndValidateConfig!",
                        containerId);
        return captchaContainerAndValidateConfig;

    }

    /**
     * Inits the container id map.
     *
     * @param request
     *            the request
     */
    private static void initContainerIdMap(HttpServletRequest request){
        WebApplicationContext webApplicationContext = RequestContextUtils.getWebApplicationContext(request, request.getServletContext());
        Map<String, CaptchaContainerAndValidateConfig> captchaContainerAndValidateConfigMap = webApplicationContext
                        .getBeansOfType(CaptchaContainerAndValidateConfig.class);

        Validate.notNull(captchaContainerAndValidateConfigMap, "captchaContainerAndValidateConfigMap can't be null!");

        Collection<CaptchaContainerAndValidateConfig> collection = captchaContainerAndValidateConfigMap.values();

        //第一次赋值,以后就不管了
        containerIdMap = CollectionsUtil.groupOne(collection, "captchaContainer.id");
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("init containerIdMap end,info:{}", JsonUtil.format(containerIdMap));
        }
    }

    /**
     * Compare try number.
     *
     * @param id
     *            the id
     * @param humanKeyValue
     *            if nullorempty,将不判断server里面的计数器
     * @param countThreshold
     *            the count threshold
     * @param request
     *            the request
     * @return the counter compare type
     */
    private static CounterCompareType compareTryNumber(String id,String humanKeyValue,int countThreshold,HttpServletRequest request){

        SessionCounter sessionCounter = RequestContextUtils.getWebApplicationContext(request, request.getServletContext())
                        .getBean(SessionCounter.class);
        int sessionCount = sessionCounter.getCount(id, request);

        ServerCounter serverCounter = RequestContextUtils.getWebApplicationContext(request, request.getServletContext())
                        .getBean(ServerCounter.class);
        int serverCount = serverCounter.getCount(id, humanKeyValue, request);

        //都小
        if (sessionCount < countThreshold && serverCount < countThreshold){
            return CounterCompareType.LESS;
        }

        //都大
        if (sessionCount >= countThreshold && serverCount >= countThreshold){
            return CounterCompareType.SESSION_AND_SERVER_MORE;
        }

        //session 大
        if (sessionCount >= countThreshold){
            return CounterCompareType.SESSION_MORE;
        }

        return CounterCompareType.SERVER_MORE;
    }

    /**
     * 清除 or incr try number.
     *
     * @param captchaContainerId
     *            the id
     * @param humanKeyValue
     *            the human key value
     * @param flag
     *            the flag
     * @param request
     *            the request
     */
    public static void clearOrIncrTryNumber(String captchaContainerId,String humanKeyValue,boolean flag,HttpServletRequest request){
        SessionCounter sessionCounter = RequestContextUtils.getWebApplicationContext(request, request.getServletContext())
                        .getBean(SessionCounter.class);
        if (flag){//表示成功
            sessionCounter.clear(captchaContainerId, request);
        }else{
            sessionCounter.incr(captchaContainerId, request);
        }
        //**************************************************************
        ServerCounter serverCounter = RequestContextUtils.getWebApplicationContext(request, request.getServletContext())
                        .getBean(ServerCounter.class);
        if (flag){//表示成功
            serverCounter.clear(captchaContainerId, humanKeyValue, request);
        }else{
            serverCounter.incr(captchaContainerId, humanKeyValue, request);
        }
    }

    /**
     * 是否需要显示Captcha.
     * 
     * <h3>目前的判断逻辑:</h3>
     * <blockquote>
     * <ol>
     * <li>如果ip在白名单之内,那么不显示</li>
     * <li>如果ip在黑名单之内,那么显示</li>
     * <li>使用Counter计算逻辑 {@link #counterFlag(CaptchaContainerAndValidateConfig, HttpServletRequest)}</li>
     * </ol>
     * </blockquote>
     *
     * @param captchaContainerAndValidateConfig
     *            the captcha container and validate config
     * @param request
     *            the request
     * @return if Validator.isNullOrEmpty(captchaContainerAndValidateConfig),表示不需要校验
     */
    public static boolean needValidate(CaptchaContainerAndValidateConfig captchaContainerAndValidateConfig,HttpServletRequest request){
        if (Validator.isNullOrEmpty(captchaContainerAndValidateConfig)){
            LOGGER.debug("captchaContainerAndValidateConfig isNullOrEmpty,return false,don't need Validate");
            return false;
        }

        RuleResolver ruleResolver = RequestContextUtils.getWebApplicationContext(request, request.getServletContext())
                        .getBean(RuleResolver.class);
        Boolean needShowCaptcha = ruleResolver.needShowCaptcha(request);

        LOGGER.debug("ruleResolver:[{}],ruleResolver.needShowCaptcha result is:[{}]", ruleResolver.getClass().getName(), needShowCaptcha);

        if (null != needShowCaptcha){
            return needShowCaptcha;
        }

        //是否是bot
        boolean needValidate = counterFlag(captchaContainerAndValidateConfig, request);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "needValidate result is:[{}],captchaContainerAndValidateConfig info is:[{}]",
                            needValidate,
                            JsonUtil.format(captchaContainerAndValidateConfig));
        }
        return needValidate;
    }

    /**
     * 定义是否是bot.
     * 
     * <h3>逻辑:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 {@link #countThreshold}<=0,那么表示所有的请求都不是bot</li>
     * <li>否则获得 {@link Counter} 获得当前id 的计数器,如果当前计数值 >={@link #countThreshold},那么标识是个bot</li>
     * </ol>
     * </blockquote>
     *
     * @param captchaContainerAndValidateConfig
     *            the captcha container and validate config
     * @param request
     *            the request
     * @return true, if bot flag
     */
    private static boolean counterFlag(CaptchaContainerAndValidateConfig captchaContainerAndValidateConfig,HttpServletRequest request){
        CaptchaContainer captchaContainer = captchaContainerAndValidateConfig.getCaptchaContainer();
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("captchaContainer info is :{}", JsonUtil.format(captchaContainer));
        }
        int countThreshold = captchaContainer.getCountThreshold();
        if (countThreshold <= 0){//容错次数,定义为 <=0 表示无论何时,都需要显示验证码; 1标识表单提交,出错一次(比如用户名密码不匹配) 需要显示
            LOGGER.debug("countThreshold value is :{},<=0 return true,don't need call counter", countThreshold);
            return true;
        }
        //TODO 提取 model返回的数据 ?
        //String humanKeyValue = "";
        //humanKeyParamName

        String humanKeyValue = null;
        CounterCompareType counterCompareType = CaptchaUtil
                        .compareTryNumber(captchaContainer.getId(), humanKeyValue, countThreshold, request);

        boolean countFlag = counterCompareType != CounterCompareType.LESS;//目前双小  那么不显示,否则都显示

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "counterCompareType:[{}],countFlag is:[{}],captchaContainer info is :{}",
                            counterCompareType,
                            countFlag,
                            JsonUtil.format(captchaContainer));
        }
        return countFlag;
    }

    /**
     * 校验用户输入的验证码值.
     *
     * @param captchaValidateableConfig
     *            the captcha validateable config
     * @param request
     *            the request
     * @return 如果验证成功返回true,否则返回false
     */
    public static boolean validate(CaptchaValidateConfig captchaValidateableConfig,HttpServletRequest request){
        CaptchaValidateEntity captchaValidateEntity = resolverCaptchaValidateEntity(request, captchaValidateableConfig);

        CaptchaValidate captchaValidate = captchaValidateableConfig.getCaptchaValidate();

        boolean validateStatus = validate(captchaValidateEntity, captchaValidate, request);
        LOGGER.debug("validate captcha status:[{}]", validateStatus);
        return validateStatus;
    }

    /**
     * 校验用户输入的验证码值.
     *
     * @param captchaContainerAndValidateConfig
     *            the captcha container and validate config
     * @param request
     *            the request
     * @return 先判断 {@link #needValidate(CaptchaContainerAndValidateConfig, HttpServletRequest)},如果不需要校验返回true;否则调用
     *         {@link #validate(CaptchaValidateConfig, HttpServletRequest)} 返回结果
     */
    public static boolean validate(CaptchaContainerAndValidateConfig captchaContainerAndValidateConfig,HttpServletRequest request){
        if (needValidate(captchaContainerAndValidateConfig, request)){
            CaptchaValidateConfig captchaValidateConfig = captchaContainerAndValidateConfig.getCaptchaValidateConfig();
            return validate(captchaValidateConfig, request);
        }
        return true;
    }

    /**
     * Validate.
     *
     * @param captchaValidateEntity
     *            the captcha validate entity
     * @param captchaValidate
     *            the captcha validate
     * @param request
     *            the request
     * @return 如果验证成功返回true,否则返回false
     */
    private static boolean validate(CaptchaValidateEntity captchaValidateEntity,CaptchaValidate captchaValidate,HttpServletRequest request){
        String userInputValue = captchaValidateEntity.getUserInputValue();

        //验证码输入 是null or empty,那肯定表示验证失败
        if (Validator.isNullOrEmpty(userInputValue)){
            if (LOGGER.isDebugEnabled()){
                LOGGER.warn("userInputValue isNullOrEmpty,captchaValidateEntity:{}", JsonUtil.format(captchaValidateEntity));
            }
            return false;
        }

        String captchaId = captchaValidateEntity.getCaptchaId();

        //ajax 校验
        if (RequestUtil.isAjaxRequest(request)){
            return captchaValidate.validateAjax(captchaId, userInputValue, captchaValidateEntity.getInstanceIdValue(), request);
        }
        //普通校验
        return captchaValidate.validate(captchaId, userInputValue, request);
    }

    /**
     * Resolver captcha validate entity.
     *
     * @param request
     *            the request
     * @param captchaValidateableConfig
     *            the captcha validateable config
     * @return the captcha validate entity
     */
    private static CaptchaValidateEntity resolverCaptchaValidateEntity(
                    HttpServletRequest request,
                    CaptchaValidateConfig captchaValidateableConfig){
        String instanceIdName = captchaValidateableConfig.getInstanceIdName();
        String instanceIdValue = null;

        if (RequestUtil.isAjaxRequest(request)){
            //ajax请求 需要设置instanceIdName属性
            Validate.notEmpty(instanceIdName, "instanceIdName can't be null/empty!");
            instanceIdValue = request.getParameter(instanceIdName);
        }
        //***************************************************************************
        String userInputParamName = captchaValidateableConfig.getUserInputParamName();
        String userInputValue = request.getParameter(userInputParamName);

        CaptchaValidateEntity captchaValidateEntity = new CaptchaValidateEntity();
        captchaValidateEntity.setCaptchaId(captchaValidateableConfig.getCaptchaId());
        captchaValidateEntity.setInstanceIdValue(instanceIdValue);
        captchaValidateEntity.setUserInputValue(userInputValue);

        return captchaValidateEntity;
    }
}