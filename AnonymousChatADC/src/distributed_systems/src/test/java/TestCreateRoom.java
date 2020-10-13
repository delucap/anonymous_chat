package test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import distributed_systems.AnonymousChatExtended;
import distributed_systems.BinderExtended;

public class TestCreateRoom {

	private static AnonymousChatExtended mp1;
	private static AnonymousChatExtended pp1;
	private static AnonymousChatExtended pp2;
	
	@BeforeClass
	public static void init() throws Exception {
		
		mp1 = new AnonymousChatExtended(0, "127.0.0.1", new BinderExtended(0));
		pp1 = new AnonymousChatExtended(1, "127.0.0.1", new BinderExtended(1));
		pp2 = new AnonymousChatExtended(2, "127.0.0.1", new BinderExtended(2));

	}
	@Test
	public void test() {
		assertTrue(mp1.createRoom("Master_Room"));
		pp1.createRoom("Room_1");
		pp2.createRoom("Room2");
		
		assertFalse(mp1.createRoom("MasterRoom"));
		assertFalse(pp1.createRoom("Room1"));
		assertFalse(pp2.createRoom("Room2"));
	}
	

}
