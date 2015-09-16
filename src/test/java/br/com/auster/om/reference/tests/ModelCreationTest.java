package br.com.auster.om.reference.tests;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.TestCase;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;
import br.com.auster.common.io.IOUtils;
import br.com.auster.common.log.LogFactory;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.reference.ImportRecord;
import br.com.auster.om.reference.model.CarrierAddress;
import br.com.auster.om.reference.model.CarrierCompany;

public class ModelCreationTest extends TestCase {

    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        LogFactory.configureLogSystem(DOMUtils.openDocument(IOUtils.openFileForRead("/log4j.xml")));
    }
	
	
	public void testCarrierModel() {
		LogFactory.getLogger(ModelCreationTest.class);
		try {
			CarrierTests carrier = new CarrierTests();
			carrier.test();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return;
		}
		assertTrue(true);
	}

	
	private class CarrierTests {

		private ImportRecord record;
		private Session session;
		
		public void test() throws Exception {
			modelCreationTest();
			insertCarrierTest();
			addSecondAddressTest();
			searchCarrierTest();
		}
		
		private void modelCreationTest() throws Exception {
			// model creation
			Configuration cfg = new Configuration();
			cfg.configure("/hibernate-configuration.xml");
			SessionFactory sf = cfg.buildSessionFactory();
			session = sf.openSession();
			
			record = new ImportRecord();
			record.setFilename("some-file.xml");
			record.setImportDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			session.saveOrUpdate(record);

		}
		
		private void insertCarrierTest() throws Exception {
			
			CarrierCompany company = new CarrierCompany();
			company.setCountryCode("55");
			company.setMarketingName("Operadora 123");
			company.setName("Operadora 123 LTDA");
			company.setNationalCode("69");
			company.setRegistrationNumber("xxx.xxx.xxx/xxxx-xx");
			company.setImportInfo(record);
			session.save(company);
//			session.flush();
			
			
			CarrierAddress address = new CarrierAddress();
			address.setStreetName("Rua Princesa Isabel");
			address.setAddressNumber("94");
			address.setComplement1("cj. 34");
			address.setCity("São Paulo");
			address.setState("SP");
			address.setCountry("Brasil");
			address.setZipCode("06041-101");			
			address.setImportInfo(record);
			
			company.addCompanyAddress(address);
			
			session.saveOrUpdate(company);
		}
		
		private void searchCarrierTest() throws Exception {
			Criteria c = session.createCriteria(CarrierCompany.class);
			c.add(Expression.eq("uid", Long.valueOf(2L)));
			CarrierCompany cc = (CarrierCompany) c.uniqueResult();
			if (cc == null) {
				throw new IllegalStateException("Could not find Carrier Company");
			}
			assertEquals(2L, cc.getUid());
			assertEquals("55", cc.getCountryCode());
			assertEquals("69", cc.getNationalCode());
			
			assertNotNull(cc.getCompanyAddresses());
			assertEquals(2, cc.getCompanyAddresses().size());
		}
		
		private void addSecondAddressTest() throws Exception {
			
			Criteria c = session.createCriteria(CarrierCompany.class);
			c.add(Expression.eq("uid", Long.valueOf(2L)));
			CarrierCompany cc = (CarrierCompany) c.uniqueResult();
			if (cc == null) {
				throw new IllegalStateException("Could not find Carrier Company");
			}
			
			CarrierAddress address = new CarrierAddress();
			address.setStreetName("Av. Dr. Joao Colin");
			address.setAddressNumber("2008");
			address.setComplement1("1004/C");
			address.setCity("Joinville");
			address.setState("SC");
			address.setCountry("Brasil");
			address.setZipCode("89201-000");			
			address.setImportInfo(record);
			
			assertNotNull(cc.getCompanyAddresses());
			assertEquals(1, cc.getCompanyAddresses().size());
			
			cc.addCompanyAddress(address);
			
			session.saveOrUpdate(cc);
			session.saveOrUpdate(address);
//			session.flush();
//			session.connection().commit();
		}		
	}
	
	
}

