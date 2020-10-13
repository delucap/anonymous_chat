package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import org.junit.BeforeClass;
import org.junit.Test;

import distributed_systems.AnonymousChatExtended;
import distributed_systems.BinderExtended;

public class TestJoinRoom {

	private static AnonymousChatExtended mp;
	private static AnonymousChatExtended pp1;
	private static AnonymousChatExtended pp2;
	
	@BeforeClass 
	public static void init() throws Exception {		
		
		mp = new AnonymousChatExtended(0, "127.0.0.1", new BinderExtended(0));
		pp1 = new AnonymousChatExtended(1, "127.0.0.1", new BinderExtended(1));
		pp2 = new AnonymousChatExtended(2, "127.0.0.1", new BinderExtended(2));
	}
	
	@Test
	public void test() {
		/* Building master room */
		mp.createRoom("MasterRoom");
		
		/*
		 * Gli altri peer entrano nella room creata
		 */
		pp1.joinRoom("MasterRoom");
		pp2.joinRoom("MasterRoom");
		
		/* check join not existing room */
		assertFalse(mp.joinRoom("notExist"));
		assertFalse(pp1.joinRoom("notExist"));
		assertFalse(pp2.joinRoom("notExist"));
		
		/* check join into existing room */
		assertFalse(mp.joinRoom("MasterRoom"));
		assertFalse(pp1.joinRoom("MasterRoom"));
		assertFalse(pp2.joinRoom("MasterRoom"));
		

	}

}
