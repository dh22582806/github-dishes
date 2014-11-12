<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>菜品管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${ctxStatic}/modules/eat/js/chineseFirstPy.js"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});

</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/eat/dishes/">菜品列表</a></li>
		<li class="active"><a
			href="${ctx}/eat/dishes/form?id=${dishes.id}">菜品<shiro:hasPermission
					name="eat:dishes:edit">${not empty dishes.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="eat:dishes:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="dishes"
		action="${ctx}/eat/dishes/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
				<form:input path="" id="companyId" htmlEscape="false"
					maxlength="200" class="required" disabled="true"
					value="${dishes.dcategory.store.company.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属门店:</label>
			<div class="controls">
				<form:input path="" id="storeId" htmlEscape="false" maxlength="200"
					class="required" disabled="true"
					value="${dishes.dcategory.store.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">菜品类别:</label>
			<div class="controls">
				<tags:treeselect id="dcategory" name="dcategory.id"
					value="${dishes.dcategory.id}" labelName="dcategory.name"
					labelValue="${dishes.dcategory.name}" allowClear="true"
					title="菜品类别" lastDataValueLabel="菜品类别"
					url="/eat/store/treeDataForEat?type=1&level=2&treeType=1" expandAll="true"
					cssClass="required" notAllowSelectParent="true"
					secondLabelIds="companyId,storeId" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">菜品名称:</label>
			<div class="controls">
				<form:input path="name" id="nameId" htmlEscape="false"
					maxlength="200" class="required"
					onchange="queryFirstPy('nameId','nameCodeId')" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">菜品编码:</label>
			<div class="controls">
				<form:input path="nameCode" id="nameCodeId" htmlEscape="false"
					maxlength="20" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="appendedPrependedInput">单价:</label>
			<div class="controls">
				<div class="input-prepend input-append">
					<span class="add-on">¥</span>
					<form:input path="price" htmlEscape="false" maxlength="16"
						class="required" />
					<span class="add-on">元</span>
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时价:</label>
			<div class="controls">
				<form:radiobuttons path="isCurPrice"
					items="${fns:getDictList('yes_no')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
			</div>

		</div>
		<div class="control-group">
			<label class="control-label">折扣:</label>
			<div class="controls">
				<form:radiobuttons path="isDiscount"
					items="${fns:getDictList('yes_no')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">计入低消:</label>
			<div class="controls">
				<form:radiobuttons path="isMinChange"
					items="${fns:getDictList('yes_no')}" itemLabel="label"
					itemValue="value" htmlEscape="false" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="200" class="input-xxlarge" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="eat:dishes:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
