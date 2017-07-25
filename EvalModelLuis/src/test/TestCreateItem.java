package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBAdmin;
import model.Admin;
import model.Gallery;
import model.Item;

public class TestCreateItem {
	DBAdmin dbAdmin; 

	@Before
	public void init(){
		dbAdmin =   new DBAdmin();
		dbAdmin.connect();
		dbAdmin.deleteAll(Item.class);
		dbAdmin.deleteAll(Gallery.class);
		dbAdmin.deleteAll(Admin.class);
		dbAdmin.close();
	}
	
	
	
	@Test
	public void testCreateItem(){
		Item item2 = getMockItem("La Republique Guidant le Peuple");
		Item item1 = getMockItem("La Gioconda");
		Gallery gallery11 = getMockGallery("Louvre");
		Admin admin1 = getMockAdmin("Lucille");
		
		dbAdmin.createAdmin(admin1);
		dbAdmin.createGallery(admin1, gallery11);
		dbAdmin.createItem(gallery11, item1);
		dbAdmin.createItem(gallery11, item2);
		
		dbAdmin.connect();	
		
			Item resultItem1 = (Item) dbAdmin.find(Item.class, item1.getId());
			Item resultItem2 = (Item) dbAdmin.find(Item.class, item2.getId());
		dbAdmin.close();
		
		Assert.assertEquals(item1.getGallery().getName(), item2.getGallery().getName());
		Assert.assertEquals(1, admin1.getGalleries().size());
		Assert.assertEquals(2, gallery11.getItems().size());


		Assert.assertEquals(item1.getName(), resultItem1.getName());
		Assert.assertEquals(item2.getName(), resultItem2.getName());
		
		Assert.assertEquals(item1.getDescription(), resultItem1.getDescription());
		Assert.assertEquals(item2.getDescription(), resultItem2.getDescription());
		
		Assert.assertEquals(resultItem2.getGallery().getId(), resultItem1.getGallery().getId());
		
		
		
		
	}
	
	
	
	//@Test(expected = RuntimeException.class)
	public void testNullAdmin() {
		Gallery gallery11 = getMockGallery("Firulais");
		

		
		dbAdmin.createGallery(null, gallery11);
		
	}
	
	//@Test(expected = RuntimeException.class)
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
	
	private Item getMockItem(String name){
		Item ite = new Item();
		ite.setName(name);
		ite.setDescription("Nice " + ite.getClass() + " " + name);
		ite.setPrice(1000f*name.length());
		return ite;
		
	}
}
