package test;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBAdmin;
import model.Admin;
import model.Comment;
import model.Gallery;
import model.Item;

public class TestGetComments {
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
	public void testGetComments(){
		Admin admin1 = getMockAdmin("adminis");
		
		Gallery gallery1 = getMockGallery("Jose");
		
		
		Item item1 = getMockItem("Rex");
		Item item2 = getMockItem("Firulais");
		
		Comment comment1 = getMockComment("blabla");
		Comment comment2 = getMockComment("bleble");
		
		Comment comment3 = getMockComment("juasjuas");
		
		dbAdmin.connect();
		
			dbAdmin.getEntityManager().getTransaction().begin();
				
				dbAdmin.getEntityManager().persist(admin1);
				

				admin1.getGalleries().add(gallery1);
				gallery1.setAdmin(admin1);
				
				gallery1.getItems().add(item1);
				gallery1.getItems().add(item2);
					item1.setGallery(gallery1);
					item2.setGallery(gallery1);
				
				item1.getComments().add(comment1);
				item1.getComments().add(comment2);
					comment1.setItem(item1);
					comment2.setItem(item1);
					
				item2.getComments().add(comment3);
				comment3.setItem(item2);
			
			dbAdmin.getEntityManager().getTransaction().commit();
			
		dbAdmin.close();		
	
		
		Set<Comment> list = dbAdmin.getComment(item1.getId());
		
		Assert.assertEquals(2, list.size());
		
		ArrayList<Comment> listArray = new ArrayList<>(list);
		
		Assert.assertEquals(2, listArray.size());

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
