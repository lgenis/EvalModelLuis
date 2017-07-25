package test;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBAdmin;
import model.Admin;
import model.Comment;
import model.Gallery;
import model.Item;

public class TestRemoveItem {
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
	public void testRemoveItem(){
		Item item2 = getMockItem("La Republique Guidant le Peuple");
		Item item1 = getMockItem("La Gioconda");
		Gallery gallery11 = getMockGallery("Louvre");
		Admin admin1 = getMockAdmin("Lucille");
		
		dbAdmin.connect();
		
		EntityManager entityManager=dbAdmin.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(admin1);
		
		admin1.getGalleries().add(gallery11);
		gallery11.setAdmin(admin1);
		
		
		gallery11.getItems().add(item1);
		gallery11.getItems().add(item2);
		item1.setGallery(gallery11);
		item2.setGallery(gallery11);
		

		entityManager.getTransaction().commit();
		
		dbAdmin.close();
		
		dbAdmin.connect();	
			//Admin resultsAdm1 = (Admin) dbAdmin.find(Admin.class, admin1.getId());
			//Gallery resultGall1 = (Gallery) dbAdmin.find(Gallery.class, gallery11.getId());
			Item resultItem1 = (Item) dbAdmin.find(Item.class, item1.getId());
			Item resultItem2 = (Item) dbAdmin.find(Item.class, item2.getId());
			
		dbAdmin.close();
		
		Assert.assertEquals(item1.getName(), resultItem1.getName());
		Assert.assertEquals(item2.getName(), resultItem2.getName());
		Assert.assertEquals(1, admin1.getGalleries().size());
		Assert.assertEquals(2, gallery11.getItems().size());
		
		Assert.assertEquals(gallery11.getId(), resultItem2.getGallery().getId());
		Assert.assertEquals(gallery11.getId(), resultItem1.getGallery().getId());
		
		//Remove and check
		dbAdmin.removeItem(resultItem1);
			
		dbAdmin.connect();	
		 	resultItem2 = (Item) dbAdmin.find(Item.class, item2.getId());
			Assert.assertEquals(1, dbAdmin.selectAll(Admin.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Gallery.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Item.class).size());
			Assert.assertEquals("La Republique Guidant le Peuple", resultItem2.getName());
			
		dbAdmin.close();
		
		
		
		
	}
	
	@Test
	public void testRemoveItemCascade(){
		Item item2 = getMockItem("La Republique Guidant le Peuple");
		Item item1 = getMockItem("La Gioconda");
		Gallery gallery11 = getMockGallery("Louvre");
		Admin admin1 = getMockAdmin("Lucille");
		
		dbAdmin.connect();
		
		EntityManager entityManager=dbAdmin.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(admin1);
		
		admin1.getGalleries().add(gallery11);
		gallery11.setAdmin(admin1);
		
		
		gallery11.getItems().add(item1);
		gallery11.getItems().add(item2);
		item1.setGallery(gallery11);
		item2.setGallery(gallery11);
		
		Comment comment1 = getMockComment("picture");
		
		item1.getComments().add(comment1);
		comment1.setItem(item1);

		entityManager.getTransaction().commit();
		
		dbAdmin.close();
		
		dbAdmin.connect();	
			//Admin resultsAdm1 = (Admin) dbAdmin.find(Admin.class, admin1.getId());
			//Gallery resultGall1 = (Gallery) dbAdmin.find(Gallery.class, gallery11.getId());
			Item resultItem1 = (Item) dbAdmin.find(Item.class, item1.getId());
			Item resultItem2 = (Item) dbAdmin.find(Item.class, item2.getId());
			
		
		
		
			Assert.assertEquals(1, dbAdmin.selectAll(Admin.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Gallery.class).size());
			Assert.assertEquals(2, dbAdmin.selectAll(Item.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Comment.class).size());
		
		dbAdmin.close();
		
		//Remove and check
		dbAdmin.removeItem(resultItem1);
			
		dbAdmin.connect();	
		 	
			Assert.assertEquals(1, dbAdmin.selectAll(Admin.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Gallery.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Item.class).size());
			Assert.assertEquals("La Republique Guidant le Peuple", resultItem2.getName());
			Assert.assertEquals(0, dbAdmin.selectAll(Comment.class).size());
			
		dbAdmin.close();
		
		
		
		
	}
	
	
	//@Test(expected = RuntimeException.class)
	public void testNullItem() {

		dbAdmin.removeItem(null);
		
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
	
	private Comment getMockComment(String name){
		Comment com = new Comment();
		com.setRate(name.length()%10);
		com.setMessage("I like " + name);
		
		return com;
		
	}
}
