package distributed_systems;

import org.beryx.textio.TerminalProperties;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class BinderExtended implements Binder{
	// https://github.com/beryx/text-io
	
	int pId;
	
	public BinderExtended(int peerId) {
		this.pId = peerId;
	}

	@Override
	public Object Message(Object obj) {
		TextIO textIO = TextIoFactory.getTextIO();
		
		TextTerminal terminal = textIO.getTextTerminal();
		
		terminal.printf("\n\tMessage received: " + obj + "\n");
		
		return "success";
	}
}

