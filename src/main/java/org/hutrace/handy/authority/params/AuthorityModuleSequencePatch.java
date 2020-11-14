package org.hutrace.handy.authority.params;

import java.util.List;

import org.hutrace.handy.annotation.verify.NotNull;
import org.hutrace.handy.annotation.verify.Size;

public class AuthorityModuleSequencePatch {
	
	@NotNull
	@Size(min = 1)
	private List<AuthorityModuleSequence> sequences;

	public List<AuthorityModuleSequence> getSequences() {
		return sequences;
	}

	public void setSequences(List<AuthorityModuleSequence> sequences) {
		this.sequences = sequences;
	}
	
}
