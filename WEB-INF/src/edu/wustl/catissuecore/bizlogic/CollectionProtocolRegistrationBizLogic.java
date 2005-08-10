/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class CollectionProtocolRegistrationBizLogic extends DefaultBizLogic
{

    /**
     * Saves the user object in the database.
     * @param session The session in which the object is saved.
     * @param obj The user object to be saved.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException
    {
        CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;

		System.out.println("PID "+collectionProtocolRegistration.getParticipant().getSystemIdentifier());
        Participant participant = null;
        List list = dao.retrieve(Participant.class.getName(), "systemIdentifier", 
        	collectionProtocolRegistration.getParticipant().getSystemIdentifier());
        if (list.size() != 0)
        {
			participant = (Participant) list.get(0);
        }
        System.out.println("participant "+participant.getFirstName());

		CollectionProtocol collectionProtocol = null;
		list = dao.retrieve(CollectionProtocol.class.getName(), "systemIdentifier", 
			collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
		if (list.size() != 0)
		{
			collectionProtocol = (CollectionProtocol)list.get(0);
		}
		System.out.println("participant "+collectionProtocol.getTitle());

		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		collectionProtocolRegistration.setParticipant(participant);

        dao.insert(collectionProtocolRegistration,true);
    }

    /**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws DAOException 
     */
    protected void update(DAO dao,Object obj) throws DAOException
    {
    }
}