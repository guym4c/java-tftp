# java-tftp

A client and server that implements the [TFTP](https://www.ietf.org/rfc/rfc1350.txt) protocol over UDP. There is no support for error handling other than file-not-found errors, and no support handling datagram duplication in the network.

Due to the nature of TFTP, the client and server are extremely similar and therefore share almost all code. The server will start a new thread for each incoming connection.
