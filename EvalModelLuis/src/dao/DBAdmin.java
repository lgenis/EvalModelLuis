package dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import model.Admin;
import model.Comment;
import model.Gallery;
import model.Item;


public class DBAdmin extends DBManager implements AdminServices {

	@Override
	public void createAdmin(Admin admin) {
		
		if(admin==null)
			throw new RuntimeException("Admin cannot be null");

		connect();
			EntityManager entityManager = getEntityManager();  
	        entityManager.getTransaction().begin();
	        entityManager.persist(admin);
	        entityManager.getTransaction().commit();
		close();
		
	}

	@Override
	public void removeAdmin(Admin admin) {
		
		if(admin==null)
			throw new RuntimeException("Admin cannot be null");
		connect();
			EntityManager entiManager = getEntityManager();
			entiManager.getTransaction().begin();
			Admin  recovered = entiManager.find(Admin.class, admin.getId());
			entiManager.remove(recovered);
			entiManager.getTransaction().commit();
		close();
	}

	@Override
	public void updateAdmin(Admin admin) {
		
		//Runtime exception illegal parameters if not ID
		if (admin.getId()<=0 || admin.equals(null)){
			throw new RuntimeException("Wrong id or null");
		}
		connect();
			EntityManager entiManager = getEntityManager();
		try{	
		
			entiManager.getTransaction().begin();
			
			Admin  recovered = entiManager.find(Admin.class, admin.getId());
			recovered.setName(admin.getName());
			//recovered.setGalleries(admin.getGalleries());
			entiManager.getTransaction().commit();
			
		}catch (IllegalArgumentException e){
			throw new RuntimeException("Bad argument in find. Wrong class or Id for " + admin.getName());
		}finally{
			close();
		}
	}

	@Override
	public Set<Gallery> getGalleries(int adminId) {
		Set<Gallery> out = new HashSet<Gallery>();
		connect();
		HashSet<Gallery> gal = new HashSet<Gallery>(this.selectAll(Gallery.class));
		for (Gallery gl: gal){
			if (gl.getAdmin().getId()==adminId){
				out.add(gl);
			}
		}
		close();
		return out;
	}

	@Override
	public void createGallery(Admin admin, Gallery gallery) {
		
		if(gallery==null)
			throw new RuntimeException("Gallery cannot be null");
		
		if(admin.getId()==0 || admin==null)
			throw new RuntimeException("La admin no  existe o es null");
		
		connect();
		   EntityManager entityManager = getEntityManager();  
		   
		        entityManager.getTransaction().begin();
		        
		    	Admin  recovered = entityManager.find(Admin.class, admin.getId());
		        //entityManager.persist(gallery);
		        //if (admin!=null){
		    	    recovered.getGalleries().add(gallery);
		        	gallery.setAdmin(recovered );

		        //}
		        entityManager.getTransaction().commit();

		close();

	}

	@Override
	public void removeGallery(Gallery gallery) {

		if(gallery==null)
			throw new RuntimeException("Gallery cannot be null");
		
		connect();
			EntityManager entiManager = getEntityManager();
			entiManager.getTransaction().begin();
			Admin  recoveredAmin = entiManager.find(Admin.class, gallery.getAdmin().getId());			
			Gallery  recoveredGallery = entiManager.find(Gallery.class, gallery.getId());
			
			recoveredAmin.getGalleries().remove(recoveredGallery); 
			
			//entiManager.remove(recoveredGallery);
			entiManager.getTransaction().commit();
		close();

	}

	@Override
	public void update(Gallery gallery) {
		
		//Runtime exception illegal parameters if not ID
		if (gallery.getId()<=0 || gallery.equals(null)){
			throw new RuntimeException("Wrong id or null");
			
		}
		connect();
		EntityManager entiManager = getEntityManager();
			
		try{	
			
			entiManager.getTransaction().begin();
			Gallery  recovered = entiManager.find(Gallery.class, gallery.getId());
			recovered.setName(gallery.getName());
			recovered.setDescription(gallery.getDescription());
			//recovered.setAdmin(gallery.getAdmin());
			//recovered.setItems(gallery.getItems());
			entiManager.getTransaction().commit();
			
		}catch (IllegalArgumentException e){
			throw new RuntimeException("Bad argument in find. Wrong class or Id for " + gallery.getName());
			
		}finally{
			close();
			
		}
		
	}

	@Override
	public Set<Item> getItems(int galleryId) {
		Set<Item> out = new HashSet<Item>();
		connect();
		HashSet<Item> gal = new HashSet<Item>(this.selectAll(Item.class));
		for (Item gl: gal){
			if (gl.getGallery().getId()==galleryId){
				out.add(gl);
			}
		}
		close();
		return out;
	}

	@Override
	public void createItem(Gallery gallery, Item item) {
		if(item==null)
			throw new RuntimeException("Gallery cannot be null");
		if(gallery.getId()==0 || gallery==null)
			throw new RuntimeException("La gallery no  existe o es null");
		
		connect();
		   EntityManager entityManager = getEntityManager();  
		   
		        entityManager.getTransaction().begin();
		        
		        Gallery  recovered = entityManager.find(Gallery.class, gallery.getId());
		        
		        recovered.getItems().add(item); 	        
		        item.setGallery(gallery);
		        
		        entityManager.getTransaction().commit();

		close();

	}

	@Override
	public void removeItem(Item item) {
		if(item==null)
			throw new RuntimeException("Item cannot be null");
		
		connect();
			EntityManager entiManager = getEntityManager();
			entiManager.getTransaction().begin();
		    Gallery  recoveredGallery = entiManager.find(Gallery.class, item.getGallery().getId());
			Item  recovered = entiManager.find(Item.class, item.getId());
			recoveredGallery.getItems().remove(recovered); 
			entiManager.getTransaction().commit();
		close();

	}

	@Override
	public void updateItem(Item item) {
		//Runtime exception illegal parameters if not ID
		if (item.getId()<=0 || item.equals(null)){
			throw new RuntimeException("Wrong id or null");
			
		}
		connect();
		EntityManager entiManager = getEntityManager();
			
		try{	
			Item  recovered = entiManager.find(Item.class, item.getId());
			
			entiManager.getTransaction().begin();
			recovered.setName(item.getName());
			recovered.setDescription(item.getDescription());
			recovered.setPrice(item.getPrice());
			recovered.setGallery(item.getGallery());
			recovered.setComments(item.getComments());
			entiManager.getTransaction().commit();
			
		}catch (IllegalArgumentException e){
			throw new RuntimeException("Bad argument in find. Wrong class or Id for " + item.getName());
			
		}finally{
			close();
			
		}

	}

	@Override
	public Set<Comment> getComment(int itemId) {
		Set<Comment> out = new HashSet<Comment>();
		connect();
		HashSet<Comment> gal = new HashSet<Comment>(this.selectAll(Comment.class));
		for (Comment gl: gal){
			if (gl.getItem().getId()==itemId){
				out.add(gl);
			}
		}
		close();
		return out;
	}

}
