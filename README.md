# Distributed Systems course 2019/2020 - Anonymous Chat project

Following project's aim is to design and develop an anonymous chat API based on P2P Network. Each peer can send messages on a public chat room in an anonymous way. The system allows the users to create a new room, join in a room, leave a room, and send messages. As described in the [AnonymousChat Java API](https://github.com/spagnuolocarmine/distributedsystems/blob/master/challenges/AnonymousChat.java).

### Basic operations
Following the basic operations executable are described:
- *createRoom*: create a public room;
- *joinRoom*: join in public existing room;
- *sendMessage*: send a string message to all members of joint a specific public room.
- *leaveRoom*: leave a specific public room.

### Technologies

The proposed project exploits the following techonlogies:

- Eclipse, as IDE;
- Java 8, a main programming language;
- Apache Maven, as project management and comprehension tool;
- Tom p2p for providing a decentralized key-value infrastructure for distributed applications.
- JUnit, as a testing framework;
- Docker, for virtualization

## Programming Project Structure

The main program is structured in five Java classes : 

- *AnonymousChat*: the API that define all the operations of the project.
- _AnonymousChatExtended_ : the implementation of the main class which contains the API.	
- _Binder_: the API that define the message listener\binder.
- _BinderExtended_: a simple implpementation of a method for parsing the messages.
- _Main_ is the main class that provides to execute the main operations.

In the current project an ad-hoc folder related to test case is present. More precisely, the folder contain _n_ test case files where _n_ is equal number to class files.

## Protocol Description

The proposed project relies on building an anonymous chat. A userfriendly interface is presented when you start a peer. The interface suggest several operations to do. Two, or more peers can coomunicate in anonymous way by providing the existing three peers in the network.
A most recent created peer can join in a specific room by typing the related name. Always the latter can send a message first by typing the room name after by inserting the string message that would send. If a peer sends a message in a room where more peers have been joint, the latters receive in anonymous way, the sent message. A peer can leave a select room by typing the room name. 


### How to build in a Docker container

1) First operation provide to download the Dockerfile on your PC. 
2) After you can prime the building phase with following operation:

```docker build -f "Dockerfile" -t anonymouschatADC .```

_anonymouschatADC_ is the name chosen with project.

If the Dockerfile is present a different path with respect to the local path, you must change the dot with Dockerfile path.

#### How to start the master peer
One time that you have built the project, you can prime the master peer.
In order to start an intercative mode with a specific environment variables you type:

```docker run -i --name MASTER_PEER -e MASTERIP="127.0.0.1" -e ID=0 anonymouschatADC```

So:
- (-i) option is for interactive mode
- (-e) option is for setting environment variables


The MASTERIP environment variable is related to the master peer ip address typed into Java class. Therefore, the ID environment variable is the unique id of your peer. 

#### How to start a generic peer

When the master is initialized, you must check the IP of Master Peer in order to set its for next Peers.
For checking, you can type the following commands:
- ```docker ps``` for listing the active containers;
- copy the chosen container ID;
- ```docker inspect <container ID>``` for inspect the chosen container.

As last operation you can prime a generic peer in a similar way when you start a MasterPEER by changing the MASTERIP environment variable:  
```docker run -i --name PEER1 -e MASTERIP="172.17.0.2" -e ID=1 anonymouschatADC```

**In order to obtain a fair execution you must run at least three peers.**

### Developer:

Pasquale De Luca: mat.05225000685 -- p.deluca16@studenti.unisa.it
