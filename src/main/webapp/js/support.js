/* Support Js */
var MavenInfoPlugin = (function(window, undefined) {

	var getJobId, hideInfo, createLastVersionPanel,
		createDependenciesVersionPanel, showPanel, types, showInfo;

	getJobId = function(element) {
		var td = element.closest("tr");
		return td.id.substring(4);
	};

	createLastVersionPanel = function(info) {
		var panel, mainVersion, mainGroupId, mainArtifactId, currentGroup;
		panel = "";
		mainVersion = info.mainModule.version;
		mainGroupId = info.mainModule.groupId;
		mainArtifactId = info.mainModule.artifactId;
		panel += '<div class="mavenInfoPanel">';
		panel += '<div class="mavenMainModule">';
		panel += '<div><strong>GroupId : </strong> ' + mainGroupId + '</div>';
		panel += '<div><strong>ArtifactId : </strong> ' + mainArtifactId
				+ '</div>';
		panel += '<div><strong>Version : </strong> ' + mainVersion + '</div>';
		if (info.modules.length > 1) {
			panel += '<div class="mavenModules">';
			currentGroup = "";
			info.modules.forEach(function(module) {
				if (module.groupId != currentGroup) {
					if (currentGroup != "") {
						panel += '</div>';
					}
					currentGroup = module.groupId;
					panel += '<div class="groupId">';
					panel += '<div class="groupIdHeader">' + module.groupId
							+ '</div>';
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
				panel += "<br>";
			});
			panel += '</div>';
			panel += '</div>';
		}
		panel += '</div>';
		return panel;
	}
	
	createDependenciesVersionPanel = function(info) {
		var panel, mainVersion, currentGroup;
		panel = "";
		panel += '<div class="mavenInfoPanel">';
		mainVersion = info.version;
		if (info.dependencies.length > 0) {
			panel += '<div class="mavenModules">';
			currentGroup = "";
			info.dependencies.forEach(function(module) {
				if (module.groupId != currentGroup) {
					if (currentGroup != "") {
						panel += '</div>';
					}
					currentGroup = module.groupId;
					panel += '<div class="groupId">';
					panel += '<div class="groupIdHeader">' + module.groupId
							+ '</div>';
				}

				panel += module.artifactId;
				if(module.versions.length == 1) {
					if(module.versions[0] != mainVersion) {
						panel += " [" + module.versions[0] + "]";
					}
				} else if (module.versions.length > 1) {
					var hasMore = false ;
					panel += " [" ;
					module.versions.forEach(function (version) {
						if(hasMore) { panel += ", "; } 
						panel += version ;
						hasMore = true;
					});
					panel += "]";
				}
				panel += "<br>";
			});
			panel += '</div>';
			panel += '</div>';
		}
		panel += '</div>';
		return panel;
	}


	showPanel = function(element, txt) {
    const s = element.querySelector(".mipVersion");
     s.setAttribute("data-html-tooltip", txt);
     Behaviour.applySubtree(s, true);
     s.dispatchEvent(new Event('mouseenter'));
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
					txt = type.create(info);
					showPanel(element, txt);
				});
			}
		}
	};
	
	showInfo = function(element, typeName) {
		var jobId, name;
		jobId = getJobId(element);
		name = typeName + ":" + jobId ;
		showInfoReal(element, typeName);
	}
	
	Behaviour.specify('td.mavenLastVersionColumn', 'lastVersion', 10,
		function(element) {
      const s = element.querySelector(".mip-tooltip");
			s.onmouseenter = function(ev) { showInfo(element, "lastVersion"); };
		});
	
	Behaviour.specify('td.mavenDependenciesColumn', 'dependenciesVersion', 10,
		function(element) {
      const s = element.querySelector(".mip-tooltip");
			s.onmouseenter = function(ev) { showInfo(element, "dependenciesVersion"); };
		});

	return {
		stub : {
			lastVersion : null,
			dependenciesVersion : null,
			
		},
		delay: 800
	}
})(window);
