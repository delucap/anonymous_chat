package distributed_systems;


import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;



public class Main {
	@Option(name="-m", aliases="--master_ip", usage="the ip address master peer", required=true)
	private static String master;
	
	@Option(name="-id", aliases="--identifier_peer", usage="the identifier for the peer, must be distinct", required=true)
	private static int id;
	
	
	public static void main(String[] args) throws Exception {
			
		BinderExtended be;
		TextIO textIO = TextIoFactory.getTextIO();
		TextTerminal terminal = textIO.getTextTerminal();
		
		
		Main ma = new Main();
		final CmdLineParser commandline = new CmdLineParser(ma);

		
		
		try
		{
			commandline.parseArgument(args);
			AnonymousChatExtended peer = new AnonymousChatExtended(id, master, new BinderExtended(id));
			String room;
			
			terminal.printf("\n\t*** ANONYMOUS CHAT *** \n");
			
			while(true)
			{
				printMenu(terminal);
				int choice = textIO.newIntInputReader().withMaxVal(9).withMinVal(0).read("Option");
				
				if(choice == 0)
				{
					System.exit(choice);
				}
			}
			
			
		}catch (CmdLineException clEx) {
			System.out.println("Command error: " + clEx);
			clEx.printStackTrace();
		}
		
	}
	
public static void printMenu(TextTerminal terminal) {
		
		terminal.printf("\n1 - CREATE CHAT ROOM\n");
		terminal.printf("\n2 - CREATE SECRET CHAT ROOM\n");
		terminal.printf("\n3 - JOIN TO CHAT ROOM\n");
		terminal.printf("\n4 - JOIN TO SECRET CHAT ROOM\n");
		terminal.printf("\n5 - EXIT FROM CHAT ROOM\n");
		terminal.printf("\n6 - SEND MESSAGE ON ROOM\n");
		terminal.printf("\n7 - VIEW LIST OF YUOURS CHATS\n");
		terminal.printf("\n8 - SHOW BACKUP OF A CHAT ROOM\n");
		terminal.printf("\n9 - SHOW HOW MANY PEERS ARE ACTIVE IN A ROOM\n");
		
		terminal.printf("\n0 - EXIT\n");
	}
}
