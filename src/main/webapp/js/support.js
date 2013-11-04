/* Support Js */
var MavenInfoPlugin = (function(window, undefined) {

	$$('body')[0].insert({
		top : '<div id="MavenVersionInfoPanel"></div>'
	});
	var overlay = new YAHOO.widget.Overlay("MavenVersionInfoPanel");
	overlay.cfg.setProperty("visible", false);

	var getJobId = function(element) {
		var td = $(element).up("tr");
		return td.id.substring(4);
	};
	
	var hideInfo = function(element) {
		overlay.cfg.setProperty("visible", false);
	};
	
	
	var createLastVersionPanel = function(info) {
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
	
	var createDependenciesVersionPanel = function(info) {
		var panel = "";
		panel += '<div class="mavenInfoPanel">\n';
		var mainVersion = info.version;
		if (info.dependencies.length > 0) {
			panel += '<div class="mavenModules">\n';
			var currentGroup = "";
			info.dependencies.each(function(module) {
				if (module.groupId != currentGroup) {
					if (currentGroup != "") {
						panel += '</div>\n';
					}
					currentGroup = module.groupId;
					panel += '<div class="groupId">\n';
					panel += '<div class="groupIdHeader">' + module.groupId
							+ '</div>\n';
				}

				panel += module.artifactId;
				if(module.versions.length == 1) {
					if(module.versions[0] != mainVersion) {
						panel += " [" + module.versions[0] + "]";
					}
				} else if (module.versions.length > 1) {
					var hasMore = false ;
					panel += " [" ;
					module.versions.each(function (version) {
						if(hasMore) { panel += ", "; } 
						panel += version ;
						hasMore = true;
					});
					panel += "]";
				}
				panel += "<br>\n";
			});
			panel += '</div>\n';
			panel += '</div>\n';
		}
		panel += '</div>\n';
		return panel;
	}

	var showPanel = function(element, txt) {
		var old = $("MavenVersionInfoPanel");
		if (old) {
			old.innerHTML = txt;
			overlay.cfg.setProperty("context", [ element, "tl", "bl",
					[ "beforeShow", "windowResize" ], [ -2, -2 ] ]);
			overlay.cfg.setProperty("visible", true);
		}
	}
	
	var types =  {
		lastVersion : {
			method: "getAjaxModuleList",
			create: createLastVersionPanel
		},
		dependenciesVersion : {
			method: "getAjaxDependenciesList",
			create: createDependenciesVersionPanel
		}
	}
	

	
	var showInfo = function(element, typeName) {
		var type = types[typeName] ;
		var stub = MavenInfoPlugin.stub[typeName] ;
		
		if (stub) {
			var jobId = getJobId(element);
			if(type.method) {
				stub[type.method](jobId, function(response) {
					var info = response.responseObject();
					var txt = type.create(info)
					showPanel(element, txt);
				});
			}
		}
	};


	Behaviour.specify('td.mavenLastVersionColumn', 'lastVersion', 10,
		function(element) {
			element.onmouseover = function(ev) { showInfo(element, "lastVersion"); };
			element.onmouseout =  function(ev) { hideInfo(element); };
		});
	
	Behaviour.specify('td.mavenDependenciesColumn', 'dependenciesVersion', 10,
		function(element) {
			element.onmouseover = function(ev) { showInfo(element, "dependenciesVersion"); };
			element.onmouseout =  function(ev) { hideInfo(element); };
		});

	return {
		stub : {
			lastVersion : null,
			dependenciesVersion : null
		}
	}
})(window);
