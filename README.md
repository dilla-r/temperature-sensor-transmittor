# temperature-sensor-transmittor
### This project is a JAX-WS that uses SOAP over HTTP to allows sensor clients to send temperature information to a server. The messages are encrypted in RSA, and generates and verifies a digital signature.
##### Project3Task1Client.java calls the various methods. SignatureWriter.java handles encryption and signature generation. Project3Task1Server.java is the server that handles the requests, decrypts the message, and verifies the signature, and stores the values. 
