package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBAdmin;
import model.Admin;
import model.Comment;
import model.Gallery;
import model.Item;



public class TestGetGalleries {
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
	public void testGetGalleries(){
		
		
		Admin admin1 = getMockAdmin("Jose");
		Admin admin2 = getMockAdmin("Juan");
		
		Gallery gallery1 = getMockGallery("Rex");
		Gallery gallery2 = getMockGallery("Garfield");
		Gallery gallery3 = getMockGallery("Piolin");
		Gallery gallery4 = getMockGallery("Ratata");
		
		dbAdmin.connect();
		
			dbAdmin.getEntityManager().getTransaction().begin();
	
				dbAdmin.getEntityManager().persist(admin1);
				dbAdmin.getEntityManager().persist(admin2);

				
				admin1.getGalleries().add(gallery1);
					gallery1.setAdmin(admin1);
				admin1.getGalleries().add(gallery2);
					gallery2.setAdmin(admin1);
				admin1.getGalleries().add(gallery3);
					gallery3.setAdmin(admin1);
				admin2.getGalleries().add(gallery4);
					gallery4.setAdmin(admin2);
			
			dbAdmin.getEntityManager().getTransaction().commit();
			
		dbAdmin.close();		
	
		
		Set<Gallery> list = dbAdmin.getGalleries(admin1.getId());
		
		Assert.assertEquals(3, list.size());
		
		ArrayList<Gallery> listArray = new ArrayList<>(list);
		
		Assert.assertEquals(3, listArray.size());

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
