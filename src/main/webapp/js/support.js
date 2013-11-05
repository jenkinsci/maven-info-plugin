/* Support Js */
var MavenInfoPlugin = (function(window, undefined) {

	var overlay, getJobId, hideInfo, createLastVersionPanel, 
		createDependenciesVersionPanel, showPanel, types, showInfo
		delayTimer = null, delayId = "";
	
	$$('body')[0].insert({
		top : '<div id="MavenVersionInfoPanel"></div>'
	});
	
	overlay = new YAHOO.widget.Overlay("MavenVersionInfoPanel");
	overlay.cfg.setProperty("visible", false);

	getJobId = function(element) {
		var td = $(element).up("tr");
		return td.id.substring(4);
	};
	
	
	
	
	createLastVersionPanel = function(info) {
		var panel, mainVersion, mainGroupId, mainArtifactId, currentGroup;
		panel = "";
		mainVersion = info.mainModule.version;
		mainGroupId = info.mainModule.groupId;
		mainArtifactId = info.mainModule.artifactId;
		panel += '<div class="mavenInfoPanel">\n';
		panel += '<div class="mavenMainModule">\n';
		panel += '<div><strong>GroupId : </strong> ' + mainGroupId + '</div>\n';
		panel += '<div><strong>ArtifactId : </strong> ' + mainArtifactId
				+ '</div>\n';
		panel += '<div><strong>Version : </strong> ' + mainVersion + '</div>\n';
		if (info.modules.length > 1) {
			panel += '<div class="mavenModules">\n';
			currentGroup = "";
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
	
	createDependenciesVersionPanel = function(info) {
		var panel, mainVersion, currentGroup;
		panel = "";
		panel += '<div class="mavenInfoPanel">\n';
		mainVersion = info.version;
		if (info.dependencies.length > 0) {
			panel += '<div class="mavenModules">\n';
			currentGroup = "";
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

	showPanel = function(element, txt) {
		var old = $("MavenVersionInfoPanel");
		if (old) {
			old.innerHTML = txt;
			overlay.cfg.setProperty("context", [ element, "tl", "bl",
					[ "beforeShow", "windowResize" ], [ -2, -2 ] ]);
			overlay.cfg.setProperty("visible", true);
		}
	}
	
	types =  {
		lastVersion : {
			method: "getAjaxModuleList",
			create: createLastVersionPanel
		},
		dependenciesVersion : {
			method: "getAjaxDependenciesList",
			create: createDependenciesVersionPanel
		}
	}
	

	
	showInfoReal = function(element, typeName) {
		var type, stub, jobId;
		type = types[typeName] ;
		stub = MavenInfoPlugin.stub[typeName] ;
		
		if (stub) {
			jobId = getJobId(element);
			if(type.method) {
				stub[type.method](jobId, function(response) {
					var info, txt;
					info = response.responseObject();
					txt = type.create(info)
					showPanel(element, txt);
				});
			}
		}
	};
	
	showInfo = function(element, typeName) {
		var jobId, name;
		jobId = getJobId(element);
		name = typeName + ":" + jobId ;
		
		if(delayTimer != null) {
			if(delayId == name) {
				return ;
			}
			clearTimeout(delayTimer) ;
			delayTimer = null ;
		} 
		
		delayId = name ;
		delayTimer = setTimeout(function() {showInfoReal(element, typeName)}, MavenInfoPlugin.delay) ;
	}
	
	hideInfo = function(element, typeName) {
		var jobId, name;
		jobId = getJobId(element);
		name = typeName + ":" + jobId ;
		
		if(delayTimer != null && delayId == name) {
			clearTimeout(delayTimer) ;
			delayTimer = null ;
		}
		overlay.cfg.setProperty("visible", false);
	};


	Behaviour.specify('td.mavenLastVersionColumn', 'lastVersion', 10,
		function(element) {
			element.onmouseover = function(ev) { showInfo(element, "lastVersion"); };
			element.onmouseout =  function(ev) { hideInfo(element, "lastVersion"); };
		});
	
	Behaviour.specify('td.mavenDependenciesColumn', 'dependenciesVersion', 10,
		function(element) {
			element.onmouseover = function(ev) { showInfo(element, "dependenciesVersion"); };
			element.onmouseout =  function(ev) { hideInfo(element, "dependenciesVersion"); };
		});

	return {
		stub : {
			lastVersion : null,
			dependenciesVersion : null,
			
		},
		delay: 800
	}
})(window);
