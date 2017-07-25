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

public class TestRemoveGallery {
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
	public void testRemoveGallery(){
		Gallery gallery2 = getMockGallery("Riksmuseum");
		Gallery gallery11 = getMockGallery("Reina Sofia");
		Admin admin1 = getMockAdmin("Reinaldo");
		
		dbAdmin.connect();
			EntityManager entityManager=dbAdmin.getEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(admin1);
			
			admin1.getGalleries().add(gallery11);
			admin1.getGalleries().add(gallery2);
			gallery11.setAdmin(admin1);
			gallery2.setAdmin(admin1);
			
			entityManager.getTransaction().commit();
			
		dbAdmin.close();
		
		dbAdmin.connect();		
			//Admin resultsAdm1 = (Admin) dbAdmin.find(Admin.class, admin1.getId());
			Gallery resultGall1 = (Gallery) dbAdmin.find(Gallery.class, gallery11.getId());
			Gallery resultGall2 = (Gallery) dbAdmin.find(Gallery.class, gallery2.getId());
			
		dbAdmin.close();
		
		//Check inserted
		Assert.assertEquals(gallery11.getName(), resultGall1.getName());
		Assert.assertEquals(gallery2.getName(), resultGall2.getName());
		Assert.assertEquals(2, admin1.getGalleries().size());

		Assert.assertEquals(admin1.getId(), resultGall1.getAdmin().getId());
		Assert.assertEquals(admin1.getId(), resultGall2.getAdmin().getId());
		//Remove and check
			dbAdmin.removeGallery(resultGall1);
				
			dbAdmin.connect();	
				resultGall2 = (Gallery) dbAdmin.find(Gallery.class, gallery2.getId());
				Assert.assertEquals(1, dbAdmin.selectAll(Admin.class).size());
				Assert.assertEquals(1, dbAdmin.selectAll(Gallery.class).size());
				Assert.assertEquals("Riksmuseum", resultGall2.getName());
			dbAdmin.close();
		
	}
	
	
	@Test
	public void testRemoveGalleryCascade(){
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
			Gallery resultGall1 = (Gallery) dbAdmin.find(Gallery.class, gallery11.getId());
		
			Assert.assertEquals(1, dbAdmin.selectAll(Admin.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Gallery.class).size());
			Assert.assertEquals(2, dbAdmin.selectAll(Item.class).size());
			Assert.assertEquals(1, dbAdmin.selectAll(Comment.class).size());
		

		dbAdmin.close();
		
		//Remove and check
		dbAdmin.removeGallery(resultGall1);
			
		dbAdmin.connect();	
			Assert.assertEquals(1, dbAdmin.selectAll(Admin.class).size());
			Assert.assertEquals(0, dbAdmin.selectAll(Gallery.class).size());
			Assert.assertEquals(0, dbAdmin.selectAll(Item.class).size());
			Assert.assertEquals(0, dbAdmin.selectAll(Comment.class).size());
			
		dbAdmin.close();
		
		
		
		
	}
	
	
	
	@Test(expected = RuntimeException.class)
	public void testNullGallery() {

		dbAdmin.removeGallery(null);
		
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
