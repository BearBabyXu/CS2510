For IDE user (eclipse, Netbeans...):
1. import project
2. put data.txt outside /src
3. run file initiate/initiateServers.java
4. run file RunWriteClient1.java
5. run file RunWriteClient2-6.java
6. run file RunReadClient7-10.java

For other user:
1. please compile files in project
2. put data.txt outside /src
3. run file initiate/initiateServers.java
4. run file RunWriteClient1.java
5. run file RunWriteClient2-6.java
6. run file RunReadClient7-10.java

*please know before using*
(a) If you want to implement n Writing clients, please go to RunWriteClient1.java and change the code at line 40 into "fileDivide(n)".
(b) sharedFile.txt can either be created or not outside of /src.
(c) sharedFile.txt can either be empty or the following process will base on the data already in it.
(d) When run RunWriteClient1.java, data.txt will automatically divided into n different data.txt which are read by different clients.


group: Meng Ju Wu, Yi Xu