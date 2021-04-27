
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipant;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipantsList;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ParticipantService {
	ResponseEvent<List<MatchedParticipantsList>> getMatchingParticipants(RequestEvent<List<ParticipantDetail>> req);

	ResponseEvent<ParticipantDetail> getParticipant(RequestEvent<Long> req);

	ResponseEvent<ParticipantDetail> createParticipant(RequestEvent<ParticipantDetail> req);

	ResponseEvent<ParticipantDetail> updateParticipant(RequestEvent<ParticipantDetail> req);
	
	ResponseEvent<ParticipantDetail> patchParticipant(RequestEvent<ParticipantDetail> req);
	
	ResponseEvent<ParticipantDetail>  delete(RequestEvent<Long> req);

	//
	// Internal APIs
	//
	Participant createParticipant(Participant participant);
	
	void updateParticipant(Participant existing, Participant newParticipant);
	
	ParticipantDetail saveOrUpdateParticipant(ParticipantDetail participant);
}
