package jenkins.plugins.maveninfo.extractor.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSet;

/**
 * Ruleset for rules collection.
 * 
 * @author emenaceb
 * 
 */
public class ExtractorRuleSet implements RuleSet {

	private class RuleSetEntry {

		private String pattern;

		private Rule rule;

		public RuleSetEntry(String pattern, Rule rule) {
			super();
			this.pattern = pattern;
			this.rule = rule;
		}

		public String getPattern() {
			return pattern;
		}

		public Rule getRule() {
			return rule;
		}

	}

	private List<RuleSetEntry> entries = new ArrayList<ExtractorRuleSet.RuleSetEntry>();

	public void addRule(String pattern, Rule rule) {
		entries.add(new RuleSetEntry(pattern, rule));
	}

	public void addRuleInstances(Digester digester) {

		for (RuleSetEntry entry : entries) {
			digester.addRule(entry.getPattern(), entry.getRule());
		}

	}

	public String getNamespaceURI() {
		return null;
	}

}
