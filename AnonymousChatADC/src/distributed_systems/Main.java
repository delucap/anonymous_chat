package distributed_systems;


import java.awt.Color;
import java.io.IOException;

import org.beryx.textio.TerminalProperties;
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
				int choice = textIO.newIntInputReader().withMinVal(0).withMaxVal(5).read("\n\t:::");
				
				if(choice == 0)
				{
					printf("\nExiting...");
					System.exit(choice);
				}
				else 
					switch(choice){
					case 1:
					room = textIO.newStringInputReader().withDefaultValue("input").read("Room name: ");

					if(peer.createRoom(room)) {
						terminal.getProperties().setPromptColor(Color.WHITE);
						terminal.printf("\t %s room built! \n", room);
					}else
						terminal.executeWithPropertiesConfigurator(props -> ((TerminalProperties) props).setPromptColor("red"),t -> ((TextTerminal) t).println("\n\t*** WARNING ****\n\t This room already is present!! \n"));
					break;
					
					case 2:
						terminal.printf("\nENTER ROOM NAME\n");
						room = textIO.newStringInputReader().withDefaultValue("input").read("Room name: ");
						
						if(peer.joinRoom(room)) {
							terminal.getProperties().setPromptColor(Color.WHITE);
							terminal.printf("\n Access ok into %s room\n", room);
						}else 
							terminal.executeWithPropertiesConfigurator(props -> ((TerminalProperties) props).setPromptColor("red"),t -> ((TextTerminal) t).println("\n\t*** WARNING ***\n The requested room can not be present or you are already joint it!! \n"));
						break;
						
					case 3:
						
						break;
						
					case 4:
						
						break;
						
					case 5:
						clearScreen();
						break;
						
					default:
						break;
						
					}
				
			}
			
			
		}catch (CmdLineException clEx) {
			System.out.println("Command error: " + clEx);
			clEx.printStackTrace();
		}
		
	}
	
public static void printMenu(TextTerminal t) throws IOException {

		t.printf("\n\tSelect your choice:\n");
		t.printf("\t1) Create a room\n");
		t.printf("\t2) Join into a room\n");
		t.printf("\t3) Send message\n");
		t.printf("\t4) Leave room\n");
		t.printf("\t5) Clear screen\n");
		
		t.printf("\t0) Exit");
		
		
	}

private static void printf(String str)
			{
				System.out.println(str);
			}

public static void clearScreen() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
}  
}
