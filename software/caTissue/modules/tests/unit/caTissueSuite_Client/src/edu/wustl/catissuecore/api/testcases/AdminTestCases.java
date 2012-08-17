package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.SearchExampleQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Ion C. Olaru
 * */
public class AdminTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public AdminTestCases() {
		super();
	}

    public void testAddInstitution() throws ApplicationException {
        Institution i = new Institution();
        i.setName("Some Inst. Name - 01" + UniqueKeyGeneratorUtil.getUniqueKey());
        assertNull(i.getId());
        i = insert(i);
        assertNotNull(i);
        assertTrue(i.getId() > 0);
    }

    public void testAddParticipantAlone() throws ApplicationException {
        Participant p = new Participant();
        p.setLastName("Malkovich");
        p.setFirstName("John");
        p.setActivityStatus("Active");
        p = insert(p);
        assertNotNull(p.getId());
        assertTrue(p.getId() > 0);
    }

	public void testAddParticipantWithCPR() {
		try {
			Participant participant = BaseTestCaseUtility.initParticipant();
			CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPShortTitleForAddParticipantWithCPR());
			User user = getUserByLoginName(PropertiesLoader.getAdminUsername());

			CollectionProtocolRegistration cpr = BaseTestCaseUtility.initCollectionProtocolRegistration(participant, cp, user);
			Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = new HashSet<CollectionProtocolRegistration>();
			collectionProtocolRegistrationCollection.add(cpr);
			participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);

			participant = insert(participant);
			assertTrue("Participant inserted successfully." + participant.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to insert Participant.", true);
		}
	}

	public void testAddParticipantWithMedicalIdentifiers() {
		try {
			Participant p = BaseTestCaseUtility.initParticipant();
            Collection<ParticipantMedicalIdentifier> pmis = new ArrayList<ParticipantMedicalIdentifier>();
            ParticipantMedicalIdentifier pmi  = new ParticipantMedicalIdentifier();
            pmi.setMedicalRecordNumber("MRN-01-ABC-" + UniqueKeyGeneratorUtil.getUniqueKey());
            pmi.setParticipant(p);
            pmi.setSite(new Site());
            pmi.getSite().setId((long) 1);
            pmi.getSite().setName("In Transit");
            pmis.add(pmi);
            p.setParticipantMedicalIdentifierCollection(pmis);
			p = insert(p);
			assertTrue("Participant inserted successfully." + p.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to insert Participant.", true);
		}
	}

	public void testAddParticipantWithRecordEntries() {
		try {
			Participant p = BaseTestCaseUtility.initParticipant();
            Collection<ParticipantRecordEntry> pres = new HashSet<ParticipantRecordEntry>();
            ParticipantRecordEntry pre = new ParticipantRecordEntry();
            pre.setParticipant(p);
            pre.setActivityStatus("Active");
            pres.add(pre);
            p.setParticipantRecordEntryCollection(pres);
			p = insert(p);
			assertTrue("Participant inserted successfully." + p.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to insert Participant.", true);
		}
	}

	public void testUpdateParticipant() {
		Participant participant = BaseTestCaseUtility.initParticipant();
		try {
			CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPTitleForUpdateParticipant());
			User user = getUserByLoginName(PropertiesLoader.getAdminUsername());

			CollectionProtocolRegistration cpr = BaseTestCaseUtility.initCollectionProtocolRegistration(participant, cp, user);
			Collection<CollectionProtocolRegistration> cprCollection = new HashSet<CollectionProtocolRegistration>();
			cprCollection.add(cpr);
			participant.setCollectionProtocolRegistrationCollection(cprCollection);
		} catch (ParseException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		}

		try {
			participant = insert(participant);
			BaseTestCaseUtility.updateParticipant(participant);

			Participant updatedParticipant = update(participant);

			Collection<CollectionProtocolRegistration> cprs = updatedParticipant.getCollectionProtocolRegistrationCollection();
			if (updatedParticipant != null
					&& updatedParticipant.getRaceCollection().contains("Unknown")
					&& updatedParticipant.getRaceCollection().contains("Black or African American") && cprs != null
					&& cprs.size() > 0) {
				assertTrue("Participant updated successfully", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to update participant: " + participant.getId(), true);
		}
	}

	public void testUpdateCPRWithBarcode() {
		Participant participant = BaseTestCaseUtility.initParticipant();

		try {
			CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPTitleForUpdateParticipantWithBarcodeinCPR());
			User user = getUserByLoginName(PropertiesLoader.getAdminUsername());
			CollectionProtocolRegistration cpr = BaseTestCaseUtility.initCollectionProtocolRegistration(participant, cp, user);
			Collection<CollectionProtocolRegistration> cprs = new HashSet<CollectionProtocolRegistration>();
			cprs.add(cpr);
			participant.setCollectionProtocolRegistrationCollection(cprs);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		} catch (ParseException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		}

		try {
			participant = insert(participant);

			BaseTestCaseUtility.updateParticipant(participant);

			CollectionProtocolRegistration cpr1 = participant.getCollectionProtocolRegistrationCollection().iterator().next();
			String barcode = "PATICIPANT" + UniqueKeyGeneratorUtil.getUniqueKey();
			cpr1.setBarcode(barcode);

			Participant updatedParticipant = update(participant);

			CollectionProtocolRegistration cpr2 = updatedParticipant.getCollectionProtocolRegistrationCollection().iterator().next();
			if (!barcode.equals(cpr2.getBarcode())) {
				assertFalse("Failed to update participant for setting CPR having barcode value as " + barcode, true);
			}
			assertTrue("Domain object successfully updated ---->" + updatedParticipant, true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to update participant for setting CPR having barcode value", true);
		}
	}

public void testMatchingParticipant() throws ApplicationException {
		
		Participant p1 = new Participant();
		p1.setLastName("Micheal");
		p1.setFirstName("Johnson");
		p1.setActivityStatus("Active");
		p1.setGender("Male Gender");
		p1.setVitalStatus("Alive");
		p1 = insert(p1);
		
		
		Participant participant = new Participant();
		//participant.setFirstName(PropertiesLoader.getFirstNameForMatchingParticipant());
		participant.setLastName("Micheal");
		participant.setFirstName("Johnson");
		participant.setActivityStatus("Active");
		participant.setGender("Male Gender");
		participant.setVitalStatus("Alive");

		try {
			List<Object> resultList = getApplicationService().getParticipantMatchingObects(participant);
			log.info("list size :: "+resultList.size());
			if(resultList!=null && resultList.isEmpty())
			{
				log.info("list is Empty");
				throw new ApplicationException();
			}

			for (Object object : resultList) {
				if (object instanceof Participant || object instanceof DefaultLookupResult) {
					Participant result = null;
					if (object instanceof DefaultLookupResult) {
						DefaultLookupResult d = (DefaultLookupResult) object;
						result = (Participant) d.getObject();
						log.info("Matching Participant Info :: ");
						log.info("First Name   :: "+result.getFirstName());
						log.info("Last Name    :: "+result.getLastName());
						log.info("Vital Status :: "+result.getVitalStatus());
						log.info("Gender       :: "+result.getGender());
						
					} else {
						result = (Participant) object;
					}

					if (!StringUtils.contains(result.getFirstName(), PropertiesLoader.getFirstNameForMatchingParticipant())) {
						assertFalse("Failed to retrieve matching participants having first name value as " + PropertiesLoader.getFirstNameForMatchingParticipant(), true);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve matching participants having first name value as " + PropertiesLoader.getFirstNameForMatchingParticipant(), true);
		}
	}

	private CollectionProtocol getCollectionProtocolByShortTitle(String shortTitle) throws ApplicationException {
		CollectionProtocol cp = new CollectionProtocol();
		cp.setShortTitle(shortTitle);

		List<CollectionProtocol> collectionProtocols = searchByExample(CollectionProtocol.class, cp);

		CollectionProtocol result = null;
		if (collectionProtocols != null && !collectionProtocols.isEmpty()) {
			result = collectionProtocols.get(0);
		}

		return result;
	}

	private User getUserByLoginName(String loginName) throws ApplicationException {
		User user = new User();
		user.setLoginName(loginName);
		List<User> users = searchByExample(User.class, user);
		User result = null;
		if (users != null && !users.isEmpty()) {
			result = users.get(0);
		}
		return result;
	}

    public void testInsertParticipant() throws Exception {
        Participant p = new Participant();
        assertNull(p.getId());
        p.setFirstName("Jane");
        p.setLastName("Doe");
        p.setActivityStatus("Active");
        SDKQuery query = new InsertExampleQuery(p);
        SDKQueryResult result = applicationService.executeQuery(query);
        p = (Participant) result.getObjectResult();
        assertNotNull(p.getId());
    }

    public void testSearchByExampleUsingInterfaceSearch() throws Exception {
        Participant p = new Participant();
        p.setFirstName("Alice");
        List<Participant> result = searchByExample(Participant.class, p);
        assertEquals(2, result.size());
    }

    public void testSearchByExampleUsingSDKQuery() throws Exception {
        Participant p = new Participant();
        p.setFirstName("Alice");
        SDKQuery sQuery = new SearchExampleQuery(p);
        SDKQueryResult result = applicationService.executeQuery(sQuery);
        Collection c = result.getCollectionResult();
        assertEquals(2, c.size());
    }

    public void testInsertCollectionProtocol() throws ApplicationException {
        String key = UniqueKeyGeneratorUtil.getUniqueKey();

        CollectionProtocol cp = new CollectionProtocol();
        cp.setActivityStatus("Active");
        cp.setTitle("CP Title - " + key);
        cp.setShortTitle("CP Short Title - " + key);
        cp.setStartDate(new Date());

        User user = getUserByLoginName(PropertiesLoader.getAdminUsername());

        cp.setPrincipalInvestigator(user);
        cp.setCoordinatorCollection(new HashSet());

        cp = insert(cp);

        System.out.println(">>> CP Inserted: " + cp.getId());
    }
    
    public void testSearchParticipantByExample() throws ApplicationException {
    	String key = UniqueKeyGeneratorUtil.getUniqueKey();
		Participant pat = new Participant();
		pat.setLastName("Malkovich"+key);
		pat.setFirstName("John"+key);
		pat.setActivityStatus("Active");
		pat.setGender("Male Gender");
		pat = insert(pat);

		String gender = PropertiesLoader.getGenderForSearchParticipantByExample();
		Participant participant = new Participant();
		participant.setGender(gender);

		List<Participant> result = null;
		try {
			result = searchByExample(Participant.class, participant);
			if(result!=null && result.size()<1)
			{
				throw new ApplicationException();
			}
			for (Participant p : result) {
				if (!gender.equals(p.getGender())) {
					assertFalse("Failed to retrieve matching participants having gender value as " + PropertiesLoader.getGenderForSearchParticipantByExample(), true);
					break;
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve matching participants having gender value as " + PropertiesLoader.getGenderForSearchParticipantByExample(), true);
		}
	}
    
    public void testSearchStorageType() throws ApplicationException
	{
		StorageType storagetype = new StorageType();
		storagetype.setDefaultTemperatureInCentigrade(-80.0);
		storagetype.setName("Type"+UniqueKeyGeneratorUtil.getUniqueKey());
		Capacity capacity=new Capacity();
		capacity.setOneDimensionCapacity(10);
		capacity.setTwoDimensionCapacity(10);
		storagetype.setCapacity(capacity);
		storagetype.setOneDimensionLabel("Rows");
		storagetype.setTwoDimensionLabel("Columns");
		storagetype=insert(storagetype);

    	log.info(" searching domain object");
    	StorageType type=new StorageType();
    	type.setId(storagetype.getId());

         try {
        	 List resultList = searchByExample(StorageType.class,type);
        		 StorageType storageType= (StorageType) resultList.get(0);
        		 log.info(" Domain Object is successfully Found ---->  :: " + storageType.getName());
          } 
          catch (Exception e) {
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);

          }
	}
    
    public void testSearchStorageContainer() throws ApplicationException
	{
    	log.info(" searching domain object");
    	StorageContainer container=new StorageContainer();
    	container.setId(new Long(1));

         try {
        	 List resultList = searchByExample(StorageContainer.class,container);
        	 StorageContainer containerFromResult= (StorageContainer) resultList.get(0);
        		 log.info(" Domain Object is successfully Found ---->  :: " + containerFromResult.getName());
          } 
          catch (Exception e) {
           	log.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);

          }
	}
    
    public void testSearchStorageContainerWithCaseSensitiveBarcode() throws ApplicationException
	{
		StorageContainer st=new StorageContainer();

		Capacity capacity=new Capacity();
		capacity.setOneDimensionCapacity(10);
		capacity.setTwoDimensionCapacity(10);
		st.setCapacity(capacity);

		StorageType sType=new StorageType();
		sType.setId(new Long(3));
		st.setStorageType(sType);

		Site site=new Site();
		site.setId(new Long(1));
		st.setSite(site);

		st.setActivityStatus("Active");
		st.setFull(false);

		st.setBarcode(PropertiesLoader.getCaseSensitiveBarcodeForContainer());
		st=insert(st);


		boolean found=false;
    	log.info(" searching domain object");
    	StorageContainer container=new StorageContainer();
    	container.setBarcode(PropertiesLoader.getCaseSensitiveBarcodeForContainer());

         try {
        	 List resultList = searchByExample(StorageContainer.class,container);
        	 for (int i = 0; i < resultList.size(); i++)
			{
        		 StorageContainer containerFromResult= (StorageContainer) resultList.get(i);
        		 if(container.getBarcode().equals(containerFromResult.getBarcode()))
        		 {
        			 log.info(" Domain Object is successfully Found with given barcode ---->  :: " + containerFromResult.getBarcode());
        			 found=true;
        			 break;
        		 }
			}
        	 if(!found)
        	 {
        		 throw new Exception("Storage Container not available with given barcode.");
        	 }
          } 
          catch (Exception e) {
           	log.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);

          }
	}
    
    public void testSearchSpecimenArrayType() throws ApplicationException
	{
		SpecimenArrayType specimenArrayType=new SpecimenArrayType();
		specimenArrayType=BaseTestCaseUtility.initSpecimenSpecimenArrayType();
		specimenArrayType=insert(specimenArrayType);
		log.info("Domain object successfully inserted "+specimenArrayType.getName());
		
		log.info(" searching domain object");
		SpecimenArrayType specArrayType=new SpecimenArrayType();
		specArrayType.setName(specimenArrayType.getName());

		try {
			List resultList = searchByExample(SpecimenArrayType.class,specArrayType);
			SpecimenArrayType specArrayTypeFromResult= (SpecimenArrayType) resultList.get(0);
			log.info(" Domain Object is successfully Found with given barcode ---->  :: " + specArrayTypeFromResult.getName());
		} 
		catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Does not find Domain Object", true);

		}
	}
    
    /**
	 * Search Container which is located at given position of parent container
	 *
	 */
	public void testSearchStorageContainerLocatedAtPosition()
	{
			try
			{
				StorageContainer parentContainer=new StorageContainer();
				parentContainer.setName(PropertiesLoader.getParentContainerName());
				StorageContainer containerToFind = new StorageContainer();
				containerToFind.setId(parentContainer.getId());

				ContainerPosition conPosition = new ContainerPosition();
				conPosition.setPositionDimensionOne(PropertiesLoader.getPositionDimensionOneInParentContainer());
				conPosition.setPositionDimensionTwo(PropertiesLoader.getPositionDimensionTwoInParentContainer());
				conPosition.setParentContainer(parentContainer);

				List result = searchByExample(ContainerPosition.class, conPosition);
				log.info(result);
				if(result.size()>1||result.size()<1)
				{
					assertFalse("Could not find Storage Container Object", true);
				}
				assertTrue("Storage Container successfully found. Size:" +result.size(), true);
			}
			catch(Exception e)
			{
				Logger.out.error(e.getMessage(),e);
				System.out
						.println("StorageContainerTestCases.testSearchStorageContainerLocatedAtPosition()");
				System.out.println(e.getMessage());
				e.printStackTrace();
				assertFalse("Could not find Storage Container Object", true);
			}
	}
	
	public void testSearchSpecimenArray()
	{
		SpecimenArray specArray=new SpecimenArray();
		specArray.setId(new Long(1));

		try {
			List resultList = searchByExample(SpecimenArray.class,specArray);
			SpecimenArray specArrayFromResult= (SpecimenArray) resultList.get(0);
			log.info(" Domain Object is successfully Found ---->  :: " + specArrayFromResult.getName());
		} 
		catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Does not find Domain Object", true);

		}
	}
}
