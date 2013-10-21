package jenkins.plugins.maveninfo.util;

import hudson.maven.ModuleName;
import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.junit.Test;

public class ModuleNamePatternTest {

	private static ModuleName MN(String groupId, String artifactId) {
		Dependency d = new Dependency();
		d.setGroupId(groupId);
		d.setArtifactId(artifactId);

		return new ModuleName(d);
	}

	@Test
	public void createError() {
		testPatternError("", null);
		testPatternError("a", null);
		testPatternError("a:b:c", null);
	}

	@Test
	public void filterModules_EmptyArtifact() {
		ModuleNamePattern pattern = new ModuleNamePattern("a:");

		testFilterModules(pattern, MN("a", "artifact"), true);
		testFilterModules(pattern, MN("b", "artifact"), false);
		testFilterModules(pattern, MN("a", "the_artifact"), true);
		testFilterModules(pattern, MN("b", "artifact_1"), false);

	}

	@Test
	public void filterModules_EmptyGroup() {
		ModuleNamePattern pattern = new ModuleNamePattern(":artifact");

		testFilterModules(pattern, MN("a", "artifact"), true);
		testFilterModules(pattern, MN("b", "artifact"), true);
		testFilterModules(pattern, MN("a", "the_artifact"), false);
		testFilterModules(pattern, MN("b", "artifact_1"), false);

	}

	@Test
	public void filterModules_WilcardGroup() {
		ModuleNamePattern pattern = new ModuleNamePattern("*a*:artifact");

		testFilterModules(pattern, MN("a", "artifact"), true);
		testFilterModules(pattern, MN("a", "artifact_2"), false);
		testFilterModules(pattern, MN("bbbabbb", "artifact"), true);
		testFilterModules(pattern, MN("bbbbbbb", "artifact"), false);
		testFilterModules(pattern, MN("b.a.b", "artifact"), true);
		testFilterModules(pattern, MN("b.b.b", "artifact"), false);

		pattern = new ModuleNamePattern("a.b.*:artifact");

		testFilterModules(pattern, MN("a.b", "artifact"), false);
		testFilterModules(pattern, MN("a1b2", "artifact"), false);
		testFilterModules(pattern, MN("a.b.c", "artifact"), true);
		testFilterModules(pattern, MN("a.b.c", "artifact_2"), false);
		testFilterModules(pattern, MN("a.b.c.d.e", "artifact"), true);
		testFilterModules(pattern, MN("a.b.dddddddddddd", "artifact"), true);
		testFilterModules(pattern, MN("a.a.b.c", "artifact"), false);
	}

	@Test
	public void filterModules_WilcardArtifact() {
		ModuleNamePattern pattern = new ModuleNamePattern("group:*a*");

		testFilterModules(pattern, MN("group", "a"), true);
		testFilterModules(pattern, MN("group_2", "a"), false);
		testFilterModules(pattern, MN("group", "bbbabbb"), true);
		testFilterModules(pattern, MN("group", "bbbbbbb"), false);
		testFilterModules(pattern, MN("group", "b.a.b"), true);
		testFilterModules(pattern, MN("group", "b.b.b"), false);

		pattern = new ModuleNamePattern("group:a.b.*");

		testFilterModules(pattern, MN("group", "a.b"), false);
		testFilterModules(pattern, MN("group", "a1b2"), false);
		testFilterModules(pattern, MN("group", "a.b.c"), true);
		testFilterModules(pattern, MN("group_2", "a.b.c"), false);
		testFilterModules(pattern, MN("group", "a.b.c.d.e"), true);
		testFilterModules(pattern, MN("group", "a.b.dddddddddddd"), true);
		testFilterModules(pattern, MN("group", "a.a.b.c"), false);
	}

	@Test
	public void preparePattern() {
		testPreparePattern("", "");
		testPreparePattern("a", "\\Qa\\E");
		testPreparePattern("a.b", "\\Qa.b\\E");
		testPreparePattern("*", ".*");
		testPreparePattern("*a", ".*\\Qa\\E");
		testPreparePattern("a*", "\\Qa\\E.*");
		testPreparePattern("*a*", ".*\\Qa\\E.*");
		testPreparePattern("*a.b*", ".*\\Qa.b\\E.*");

	}

	private void testFilterModules(ModuleNamePattern pattern, ModuleName name,
			boolean expected) {
		Assert.assertEquals(expected, pattern.matches(name));
	}

	private void testPatternError(String pattern, String expected) {
		try {
			new ModuleNamePattern(pattern);
			Assert.fail();
		} catch (InvalidPatternException e) {
			if (StringUtils.isNotBlank(expected)) {
				Assert.assertTrue(e.getMessage().contains(expected));
			}
		}
	}

	private void testPreparePattern(String pattern, String expected) {
		String actual = ModuleNamePattern.preparePattern(pattern);
		Assert.assertEquals(expected, actual);
	}
}
