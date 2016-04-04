package sd.tp1.clt;

import java.io.IOException;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import sd.tp1.gui.GalleryContentProvider;

import sd.tp1.gui.Gui;

/*
 * This class provides the album/picture content to the gui/main application.
 * 
 * Project 1 implementation should complete this class. 
 */
public class SharedGalleryContentProvider implements GalleryContentProvider {

	Gui gui;
	ServerDiscovery discovery;

	SharedGalleryContentProvider() {
		detectChanges();
	}

	private void detectChanges() {
		Thread t = new Thread(new Runnable() {
			public void run()
			{
				gui.updateAlbums();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public void findServers(Gui gui) throws IOException {
		if(discovery == null) {
			discovery = new ServerDiscovery(gui);
			new Thread(discovery).start();
		}
	}

	/**
	 *  Downcall from the GUI to register itself, so that it can be updated via upcalls.
	 */
	@Override
	public void register(Gui gui) {
		if(this.gui == null) {
			this.gui = gui;
			try {
				findServers(this.gui);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the list of albums in the system.
	 * On error this method should return null.
	 */
	@Override
	public List<Album> getListOfAlbums() {
		List<Album> lst = new ArrayList<>();
		for(Client e : discovery.getServers().values()) {
			lst.addAll(e.getListOfAlbums());
		}
		return lst;
	}

	/**
	 * Returns the list of pictures for the given album. 
	 * On error this method should return null.
	 */
	@Override
	public List<Picture> getListOfPictures(Album album) {
		List<Picture> lst = new ArrayList<>();
		for(Client e : discovery.getServers().values()) {
			lst.addAll(e.getListOfPictures(album));
		}
		return lst;
	}

	/**
	 * Returns the contents of picture in album.
	 * On error this method should return null.
	 */
	@Override
	public byte[] getPictureData(Album album, Picture picture) {
		byte [] data;
		for(Client e : discovery.getServers().values()) {
			data = e.getPictureData(album, picture);
			if(data != null)
				return data;
		}
		return null;
	}

	/*
	 * Create a new album.
	 * On error this method should return null.
	 */

	@Override
	public Album createAlbum(String name) {
		int server = 0 + (int)(Math.random() * discovery.getServers().size());
		int cnt = 0;
		for (Client e : discovery.getServers().values()) {
			if(cnt == server)
				return e.createAlbum(name);
			cnt++;
		}
		return null;
	}

	/**
	 * Delete an existing album.
	*/
	@Override
	public void deleteAlbum(Album album) {
		for(Client e : discovery.getServers().values()) {
			e.deleteAlbum(album);
		}
		gui.updateAlbums();
	}
	
	/**
	 * Add a new picture to an album.
	 * On error this method should return null.
	*/
	@Override
	public Picture uploadPicture(Album album, String name, byte [] data) {
		int server = 0 + (int)(Math.random() * discovery.getServers().size());
		int cnt = 0;
		for (Client e : discovery.getServers().values()) {
			if(cnt == server)
				return e.uploadPicture(album, name, data);
			cnt++;
		}
		return null;
	}

	/**
	 * Delete a picture from an album.
	 * On error this method should return false.
	 */
	@Override
	public boolean deletePicture(Album album, Picture picture) {
		for(Client e : discovery.getServers().values()) {
			return e.deletePicture(album, picture);
		}
		return false;
	}

}
