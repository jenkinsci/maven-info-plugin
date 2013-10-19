/* Support Js */
var MavenInfoPlugin = (function(window, undefined) {

	$$('body')[0].insert({
		top : '<div id="MavenVersionInfoPanel"></div>'
	});
	var overlay = new YAHOO.widget.Overlay("MavenVersionInfoPanel");
	overlay.cfg.setProperty("visible", false);

	getJobId = function(element) {
		var td = $(element).up("tr");
		return td.id.substring(4);
	};

	createInfoPanel = function(info) {
		var panel = "";
		var mainVersion = info.mainModule.version;
		var mainGroupId = info.mainModule.groupId;
		var mainArtifactId = info.mainModule.artifactId;
		panel += '<div class="mavenInfoPanel">\n';
		panel += '<div class="mavenMainModule">\n';
		panel += '<div><strong>GroupId : </strong> ' + mainGroupId + '</div>\n';
		panel += '<div><strong>ArtifactId : </strong> ' + mainArtifactId
				+ '</div>\n';
		panel += '<div><strong>Version : </strong> ' + mainVersion + '</div>\n';
		if (info.modules.length > 1) {
			panel += '<div class="mavenModules">\n';
			var currentGroup = "";
			info.modules.each(function(module) {
				if (module.groupId != currentGroup) {
					if (currentGroup != "") {
						panel += '</div>\n';
					}
					currentGroup = module.groupId;
					panel += '<div class="groupId">\n';
					panel += '<div class="groupIdHeader">' + module.groupId
							+ '</div>\n';
				}

				var isMain = false;
				if (module.groupId == mainGroupId
						&& module.artifactId == mainArtifactId) {
					isMain = true;
					panel += '<strong class="mainModule">';
				}
				panel += module.artifactId;
				if (module.version != mainVersion) {
					panel += " [" + module.version + "]";
				}
				if (isMain) {
					panel += "</strong>"
				}
				panel += "<br>\n";
			});
			panel += '</div>\n';
			panel += '</div>\n';
		}
		panel += '</div>\n';
		return panel;
	}

	showInfo = function(element) {
		if (mavenColumnStub) {
			var jobId = getJobId(element);
			mavenColumnStub.getAjaxModuleList(jobId, function(response) {
				var info = response.responseObject();
				var txt = createInfoPanel(info)
				var old = $("MavenVersionInfoPanel");
				if (old) {
					old.innerHTML = txt;
					overlay.cfg.setProperty("context", [ element, "tl", "bl",
							[ "beforeShow", "windowResize" ], [ -2, -2 ] ]);
					overlay.cfg.setProperty("visible", true);
				}

			});
		}
	};

	hideInfo = function(element) {
		overlay.cfg.setProperty("visible", false);
	};

	Behaviour.specify('td.mavenVersionColumn', 'showInfo', 10,
			function(element) {

				element.onmouseover = function(ev) {
					showInfo(element);
				};

				element.onmouseout = function(ev) {
					hideInfo(element);
				};

			});

})(window);
