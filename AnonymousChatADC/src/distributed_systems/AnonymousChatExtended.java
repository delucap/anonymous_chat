package distributed_systems;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FutureRemove;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class AnonymousChatExtended implements AnonymousChat{
	
	final private Peer peer;
	final private PeerDHT dht;
	final private int MASTERPORT = 4092;
	
	FutureBootstrap ft;
	
	// Here are  contained the rooms
	final private ArrayList<String> rooms = new ArrayList<String>();

	// here are stored the usernames of registrated rooms' users.
	final private HashMap<String, String> usernames = new HashMap<String, String>();
	
	//Constructor
	public AnonymousChatExtended (int _id, String _master_peer, final Binder _bind) throws Exception {
		
		peer = new PeerBuilder(Number160.createHash(_id)).ports(MASTERPORT+_id).start();
		dht = new PeerBuilderDHT(peer).start();
		
		ft = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(MASTERPORT).start();
		ft.awaitUninterruptibly();
		
		if (ft.isSuccess()) {
			peer.discover().peerAddress(ft.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}else {
			throw new Exception("Error in bootstrap phase for master peer");
		}
		
		peer.objectDataReply(new ObjectDataReply() {
			
			@Override
			public Object reply(PeerAddress sender, Object request) throws Exception {
				
				return _bind.parseMessage(request);
			}
		});
	}
	@Override
	public boolean createRoom(String _room_name) {
		// TODO Auto-generated method stub
		
		//check if the room already exists
		if(this.checkRooms(_room_name) != null)
			return false;
		else
		try {
			FutureGet fg = dht.get(Number160.createHash(_room_name)).start();
			fg.awaitListenersUninterruptibly(); //start listening
			
			if(fg.isSuccess() && fg.isEmpty())
			{
				//Building new room
				HashSet<PeerAddress> peers_presents = new HashSet<PeerAddress>();
				peers_presents.add(dht.peer().peerAddress());
				dht.put(Number160.createHash(_room_name)).data(new Data(peers_presents)).start().awaitUninterruptibly();
				
				rooms.add(_room_name);
				
				
				//add a random name
				usernames.put(_room_name, this.generateNickname()); //insert a random function
				this.sendMessage(_room_name, "I am the creator of ["+ usernames.get(_room_name) + "] room!");
				
				return true;			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
		
	}

	@Override
	public boolean joinRoom(String _room_name) {
		// TODO Auto-generated method stub
		if(this.checkRooms(_room_name) != null)
			return false;
		else
			try {
			
			//Controlliamo se il room name è già nella nostra chat list
	
			FutureGet fg = dht.get(Number160.createHash(_room_name)).start();
			fg.awaitUninterruptibly();
				
			if(fg.isSuccess()) {
					
					//se isEmpty() ritorna true significa che la room a cui vogliamo accedere non esiste
					if(fg.isEmpty())
						return false;
					
					//Aggiungiamo questo peer a quelli già presenti nella room
					HashSet<PeerAddress> peers_presents;
					peers_presents = (HashSet<PeerAddress>) fg.dataMap().values().iterator().next().object();
					peers_presents.add(dht.peer().peerAddress());
					
					dht.put(Number160.createHash(_room_name)).data(new Data(peers_presents)).start().awaitUninterruptibly();
					
					/* insert name room into rooms strucutre */
					rooms.add(_room_name);
					usernames.put(_room_name, this.generateNickname()); //insert random generation nickname
					
					/* update welecome message */
					this.sendMessage(_room_name, "[room: " + _room_name + "] Hello There [" + usernames.get(_room_name) + "]!");
					return true;
					}			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;	
	}

	@Override
	public boolean leaveRoom(String _room_name) {
		// TODO Auto-generated method stub
		try {		
			//Viene fatto un parsing del name in input in caso la room in questione sia segreta.
			
			//Se parseName ritorna null non siamo iscritti alla room in questione
			if(this.checkRooms(_room_name) == null) {				
				FutureGet fg = dht.get(Number160.createHash(_room_name)).start();
				fg.awaitUninterruptibly();
				if(fg.isSuccess()) {
					FutureRemove fR = dht.remove(Number160.createHash(_room_name)).start();
					fR.awaitUninterruptibly();
					
					if (fR.isSuccess())
					{
						rooms.remove(_room_name);
						usernames.remove(_room_name);
						return true;
					}
					/**
					 * Se questo peer è l'ultimo all'interno della room, la room deve essere distrutta, o meglio 
					 * rimossa dalla dht al fine di poter ricreare una room con lo stesso nome in futuro.
					 * Insieme ad essa devono essere rimosse dalla dht anche le entità che conservano il backup della chat
					 * e l'eventuale password in caso si tratti di una room segreta.
					 */
				
	
					this.sendMessage(_room_name, "[" + _room_name + "] -- leaving this room -- [" + usernames.get(_room_name) + "]!");
					

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean sendMessage(String _room_name, String _text_message) {
		// TODO Auto-generated method stub
		boolean flag = false;
		boolean flag2 = false;
		
		try {
			
			//se parseName ritorna null la room a cui si vuole mandare un messaggio non esiste o non vi abbiamo accesso
			if(this.checkRooms(_room_name) != null) {
				
				//invio del messaggio a tutti i peer che fanno parte della room
				FutureGet fg = dht.get(Number160.createHash(_room_name)).start();
				fg.awaitUninterruptibly();
				if(fg.isSuccess()) {
					HashSet<PeerAddress> peers_presents;
					peers_presents = (HashSet<PeerAddress>) fg.dataMap().values().iterator().next().object();
					for(PeerAddress peer: peers_presents) {
						FutureDirect fd = dht.peer().sendDirect(peer).object("<<Guest:"+usernames.get(_room_name)+">> "+_text_message).start();
						fd.awaitUninterruptibly();
					}
					flag = true;
				}
				
				/**
				 * Viene creata un'entità a parte nella dht che servirà a conservare il backup della chat 
				 */
				FutureGet fgb = dht.get(Number160.createHash(_room_name+"_backup")).start();
				fgb.awaitUninterruptibly();
				ArrayList<String> allChat = null;
				if(fgb.isSuccess()) {
					
					//se non è stato ancora inviato nessun messaggio nella room, l'entità backup non esiste ancora 
					if(fgb.isEmpty())
						allChat = new ArrayList<String>();
					else
						allChat = (ArrayList<String>) fgb.dataMap().values().iterator().next().object();						
				}
				allChat.add("<<Guest:"+usernames.get(_room_name)+">> "+_text_message);
				dht.put(Number160.createHash(_room_name+"_backup")).data(new Data(allChat)).start().awaitUninterruptibly();
				flag2 = true;
				if(flag && flag2){
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String checkRooms(String _room_name) {
		if(rooms.contains(_room_name))
			return _room_name;
		else 
			return null;
	}

	private String generateNickname() {
		String nickname;
		Random rnd = new Random();
		nickname = ((char)(rnd.nextInt(57)+65)) + "" + ((char)(rnd.nextInt(57)+65)) + "-" + rnd.nextInt(10000);
		
		return nickname;
	}
}
