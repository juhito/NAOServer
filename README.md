# Server application for NAO Robot
Server code for NAOController App. Uses Java sockets and multi-threading.

# Description
Server application for NAO Robot V5. Server is split into two parts, BroadcastServer and NAOServer, both running on separate threads. BroadcastServer uses the connection-less DatagramSocket and waits for broadcast messages that includes a certain message. After the message is received, we construct a packet which includes this server's ip address and send it back. NAOServer uses connection-oriented TCP ServerSocket. When a client connects, we make a new thread and pass a handler to it.

We can now pass data between client and a server using the socket's input&output streams. On the server side, when data is received, we strip down the received object which contains a command and possible arguments. There is a singleton instance that controls the robot and using Java Reflection, we can call the corresponding method that correlates with the command. Every method on the singleton instance is asynchronous.

After calling the method, we send back data to the client if there is any.

# Installation

## requirements

* [32-bit Java JRE 7](https://www.oracle.com/java/technologies/javase/javase7-archive-downloads.html) - Runs on the robot
* [Naoqi SDK](https://community.aldebaran.com/en/resources/software)
* NAO Robot v5

More in-depth guide how to setup your NAO-Robot can be found here: [link](http://doc.aldebaran.com/2-1/dev/java/index_java.html)

## install
1. Clone this project
```bash
$ git clone https://github.com/juhito/NAOServer
```
2. Change CWD to the cloned project.
```bash
$ cd NAOServer
```
3. Compile the application and upload class files to NAO (using FTP or scp)
4. Run the application on NAO
```bash
$ java -cp /path/to/java-naoqi-sdk-<version>-<platform>.jar:. NAOServer
```

# License
[MIT](https://github.com/juhito/NAOServer/blob/development/LICENSE)

# Project status
This project isn't currently under active development.
