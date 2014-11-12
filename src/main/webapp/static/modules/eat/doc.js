$(function() {
	$(".ui-block-a li>a").attr("href", "#menu").click(function(event) {
		var index = $(this).parent().index("li");
		var continer = $(this).parentsUntil("div").parent().next();
		continer.children().not(":eq(" + index + ")").hide();
		continer.children(":eq(" + index + ")").show();
		continer.find("form").next().empty();
		event.preventDefault();
	}).first().trigger("click");
	$(".ui-block-b").children().not(":first").each(function() {
		var ps = $(this).children("p");
		if (ps.length >= 2) {
			var url = ps[0].innerText;
			var fieldStr = ps[1].innerText;
			var fields = [];
			var isupload = false;
			if (fieldStr != "" && fieldStr != "无") {
				var fieldArr = fieldStr.split(",");
				for ( var index in fieldArr) {
					var fstr = fieldArr[index];
					var indexl = fstr.indexOf("(");
					var indexr = fstr.indexOf(")");
					if (indexl > 0 && indexr > indexl) {
						var obj = {
							name : fstr.substring(0, indexl),
						};
						var params = fstr.substring(indexl, indexr);
						var indexOfFile = params.indexOf("file");
						if (indexOfFile != -1) {
							isupload = true;
							obj.file = "file";
						}
						var indexOfRequire = params.indexOf("require");
						if (indexOfRequire != -1) {
							obj.require = true;
						}
						fields.push(obj);
					} else {
						fields.push({
							name : fstr
						});
					}
				}
			}
			var data = {
				fileds : fields,
				url : url,
				isupload : isupload
			};
			var text = $("#demoTemplate").render(data);
			$(this).append(text);
			$(this).children("div[data-role='collapsible']").collapsible();
			$(this).find("input:not([type='submit'])").textinput();
			$(this).find("input[type='submit']").button();
		}
	});
	$(document).bind('mobileinit', function() {
		$.mobile.selectmenu.prototype.options.nativeMenu = false;
	});
	$('form').submit(function() {
		var next = $(this).next();
		if (next.length === 0) {
			next = $("<div style='word-break: break-all;'></div>");
			$(this).after(next);
		}
		next.empty();
		$.mobile.loading('show');
		$(this).ajaxSubmit({
			success : function(responseText) {
				$.mobile.loading('hide');
				if (typeof (responseText) != "string") {
					responseText = JSON.stringify(responseText);
				}
				next.html(responseText);
			},
			error : function(response) {
				$.mobile.loading('hide');
				var responseText = response.responseText;
				if (typeof (responseText) != "string") {
					responseText = JSON.stringify(responseText);
				}
				next.html("<font color='red'>" + responseText + "</font>");
			}
		});
		return false;
	});
});