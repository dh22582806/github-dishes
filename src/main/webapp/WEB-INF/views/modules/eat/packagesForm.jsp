<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>套餐管理</title>
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
		<li><a href="${ctx}/eat/packages/">套餐列表</a></li>
		<li class="active"><a
			href="${ctx}/eat/packages/form?id=${packages.id}">套餐<shiro:hasPermission
					name="eat:packages:edit">${not empty packages.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="eat:packages:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="packages"
		action="${ctx}/eat/packages/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
				<form:input path="" id="companyId" htmlEscape="false"
					maxlength="200" class="required" disabled="true"
					value="${packages.store.company.name}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属门店:</label>
			<div class="controls">
				<tags:treeselect id="store" name="store.id"
					value="${packages.store.id}" labelName="store.name"
					labelValue="${packages.store.name}" allowClear="true" title="门店"
					lastDataValueLabel="门店"
					url="/eat/store/treeDataForEat?type=1&level=1" cssClass="required"
					expandAll="true" notAllowSelectParent="true"
					secondLabelIds="companyId" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">套餐名称:</label>
			<div class="controls">
				<form:input path="name" id="nameId" htmlEscape="false"
					maxlength="200" class="required"
					onchange="queryFirstPy('nameId','nameCodeId')" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">套餐编码:</label>
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
			<label class="control-label">可折扣:</label>
			<div class="controls">
				<form:radiobuttons path="isDiscount"
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
			<shiro:hasPermission name="eat:packages:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
