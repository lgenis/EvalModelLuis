package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBAdmin;

import model.Admin;
import model.Gallery;

public class TestCreateGallery {
	DBAdmin dbAdmin; 

	@Before
	public void init(){
		dbAdmin =   new DBAdmin();
		dbAdmin.connect();
		dbAdmin.deleteAll(Gallery.class);
		dbAdmin.deleteAll(Admin.class);
		dbAdmin.close();
	}
	
	
	
	@Test
	public void testCreateGallery(){
		Gallery gallery2 = getMockGallery("Ricksmuseum");
		Gallery gallery11 = getMockGallery("Reina Sofia");
		Admin admin1 = getMockAdmin("Reinaldo");
		
		dbAdmin.createAdmin(admin1);

		dbAdmin.createGallery(admin1, gallery11);
		dbAdmin.createGallery(admin1, gallery2);
		
		dbAdmin.connect();		
			Admin resultsAdm1 = (Admin) dbAdmin.find(Admin.class, admin1.getId());
			Gallery resultGall1 = (Gallery) dbAdmin.find(Gallery.class, gallery11.getId());
			Gallery resultGall2 = (Gallery) dbAdmin.find(Gallery.class, gallery2.getId());
			
		dbAdmin.close();
		
		
		Assert.assertEquals(admin1.getName(), resultsAdm1.getName());
		Assert.assertEquals(2, admin1.getGalleries().size());

		Assert.assertEquals(gallery11.getName(), resultGall1.getName());
		Assert.assertEquals(gallery2.getName(), resultGall2.getName());
		
		Assert.assertEquals(gallery11.getDescription(), resultGall1.getDescription());
		Assert.assertEquals(gallery2.getDescription(), resultGall2.getDescription());
		
		Assert.assertEquals(resultGall2.getAdmin().getId(), resultGall1.getAdmin().getId());
		
	}
	
	
	
	@Test(expected = RuntimeException.class)
	public void testNullAdmin() {
		
		Gallery gallery11 = getMockGallery("Firulais");
		dbAdmin.createGallery(null, gallery11);
		
	}
	
	@Test(expected = RuntimeException.class)
	public void testNullGallery(){
		
		Gallery Gallery11 = null;
		Admin admin1 = getMockAdmin("Fernandez");
	
		dbAdmin.createGallery(admin1, Gallery11);
		
	}
	
	
	
	private Admin getMockAdmin(String name){
		Admin adm = new Admin();
		adm.setName(name);
		
		return adm;
		
	}
	
	private Gallery getMockGallery(String name){
		Gallery gall = new Gallery();
		gall.setName(name);
		gall.setDescription("Nice " + gall.getClass() + " " + name);
		
		
		return gall;
		
	}
}
