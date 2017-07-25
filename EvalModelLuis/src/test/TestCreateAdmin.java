package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBAdmin;


import model.Admin;

public class TestCreateAdmin {
	DBAdmin dbAdmin; 

	@Before
	public void init(){
		dbAdmin =   new DBAdmin();
		dbAdmin.connect();
		dbAdmin.deleteAll(Admin.class);
		dbAdmin.close();
	}
	
	
	
	@Test
	public void testCreateAdmin(){
		
		Admin admin1 = getMockAdmin("Federico");
		dbAdmin.createAdmin(admin1);
		
		dbAdmin.connect();		
			Admin results1 = (Admin) dbAdmin.find(Admin.class, admin1.getId());
		dbAdmin.close();
		
		Assert.assertEquals(admin1.getName(), results1.getName());
		Assert.assertEquals(admin1.getGalleries(), results1.getGalleries());
		
	}
	
	
	
	@Test(expected = RuntimeException.class)
	public void testNullAdmin() {
		dbAdmin.createAdmin(null);
		
	}
	
	
	
	
	private Admin getMockAdmin(String name){
		Admin adm = new Admin();
		adm.setName(name);
		
		return adm;
		
	}
	
	

}
