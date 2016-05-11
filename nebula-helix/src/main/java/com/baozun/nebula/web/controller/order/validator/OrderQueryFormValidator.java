package com.baozun.nebula.web.controller.order.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.order.form.OrderQueryForm;

public class OrderQueryFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderQueryForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderQueryForm orderqueryform=(OrderQueryForm) target;
        //校验是否有非空数据
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"orderTimeType","orderTimeType.notavailable");
    }

}
