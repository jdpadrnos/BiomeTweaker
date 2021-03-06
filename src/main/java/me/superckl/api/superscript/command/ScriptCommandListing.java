package me.superckl.api.superscript.command;

import me.superckl.api.superscript.util.ConstructorListing;

/**
 * A subclass of ConstructorListing that allows tracking of multiple possible parameter lists for the same command name.
 */
public class ScriptCommandListing extends ConstructorListing<IScriptCommand>{

	private boolean performInst;

	public boolean isPerformInst() {
		return this.performInst;
	}

	public void setPerformInst(final boolean performInst) {
		this.performInst = performInst;
	}

}
