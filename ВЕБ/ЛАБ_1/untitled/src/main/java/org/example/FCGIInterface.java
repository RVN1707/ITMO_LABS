package org.example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Properties;

public class FCGIInterface {
    private static final String RCSID = "$Id: FCGIInterface.java,v 1.4 2000/03/27 15:37:25 robs Exp $";
    public static FCGIRequest request = null;
    public static boolean acceptCalled = false;
    public static boolean isFCGI = true;
    public static Properties startupProps;
    public static ServerSocket srvSocket;

    public FCGIInterface() {
    }

    public int FCGIaccept() {
        int acceptResult = 0;
        if (!acceptCalled) {
            isFCGI = System.getProperties().containsKey("FCGI_PORT");
            acceptCalled = true;
            if (isFCGI) {
                startupProps = new Properties(System.getProperties());
                String str = new String(System.getProperty("FCGI_PORT"));
                if (str.length() <= 0) {
                    return -1;
                }

                int portNum = Integer.parseInt(str);

                try {
                    srvSocket = new ServerSocket(portNum);
                } catch (IOException var6) {
                    if (request != null) {
                        request.socket = null;
                    }

                    srvSocket = null;
                    request = null;
                    return -1;
                }
            }
        } else if (!isFCGI) {
            return -1;
        }

        if (isFCGI) {
            try {
                acceptResult = this.FCGIAccept();
            } catch (IOException var5) {
                return -1;
            }

            if (acceptResult < 0) {
                return -1;
            }

            System.setIn(new BufferedInputStream(request.inStream, 8192));
            System.setOut(new PrintStream(new BufferedOutputStream(request.outStream, 8192)));
            System.setErr(new PrintStream(new BufferedOutputStream(request.errStream, 512)));
            System.setProperties(request.params);
        }

        return 0;
    }

    int FCGIAccept() throws IOException {
        boolean errCloseEx = false;
        boolean outCloseEx = false;
        if (request != null) {
            System.err.close();
            System.out.close();
            boolean prevRequestfailed = errCloseEx || outCloseEx || request.inStream.getFCGIError() != 0 || request.inStream.getException() != null;
            if (prevRequestfailed || !request.keepConnection) {
                request.socket.close();
                request.socket = null;
            }

            if (prevRequestfailed) {
                request = null;
                return -1;
            }
        } else {
            request = new FCGIRequest();
            request.socket = null;
            request.inStream = null;
        }

        boolean isNewConnection = false;

        do {
            if (request.socket == null) {
                try {
                    request.socket = srvSocket.accept();
                } catch (IOException var5) {
                    request.socket = null;
                    request = null;
                    return -1;
                }

                isNewConnection = true;
            }

            request.isBeginProcessed = false;
            request.inStream = new FCGIInputStream(request.socket.getInputStream(), 8192, 0, request);
            request.inStream.fill();
            if (request.isBeginProcessed) {
                request.params = new Properties(startupProps);
                switch (request.role) {
                    case 1:
                        request.params.put("ROLE", "RESPONDER");
                        break;
                    case 2:
                        request.params.put("ROLE", "AUTHORIZER");
                        break;
                    case 3:
                        request.params.put("ROLE", "FILTER");
                        break;
                    default:
                        return -1;
                }

                request.inStream.setReaderType(4);
                if ((new FCGIMessage(request.inStream)).readParams(request.params) < 0) {
                    return -1;
                }

                request.inStream.setReaderType(5);
                request.outStream = new FCGIOutputStream(request.socket.getOutputStream(), 8192, 6, request);
                request.errStream = new FCGIOutputStream(request.socket.getOutputStream(), 512, 7, request);
                request.numWriters = 2;
                return 0;
            }

            request.socket.close();
            request.socket = null;
        } while(!isNewConnection);

        return -1;
    }
}