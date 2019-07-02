package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractGreekLetterSequenceToken;

public class ParentSpecimenGreekLetterSequence extends AbstractGreekLetterSequenceToken<Specimen> {

	public ParentSpecimenGreekLetterSequence() {
		this.name = "PSPEC_GREEK_SEQ";
	}

	@Override
	protected Integer getSequence(Specimen spmn) {
		return getUniqueId(spmn.getParentSpecimen() != null ? spmn.getParentSpecimen().getId().toString() : null);
	}
}