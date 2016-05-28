function FormValidator(name, sortNo, validate){
	this.name = name;
	this.sortNo = sortNo;
	this.validate = validate;
};

var formValidateList = [];

//表单验证
function itemFormValidate(form){
	debugger;
	formValidateList.sort(function(a, b){
		return a.sortNo - b.sortNo;
	});
	for(var i = 0; i < formValidateList.length; i++) {
		if(formValidateList[i] instanceof FormValidator) {
			var result = formValidateList[i].validate(form);
			if(result != loxia.SUCCESS) {
				return result;
			}
		} else {
			
		}
	}
	
	return loxia.SUCCESS;
}