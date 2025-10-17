package org.example;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class FCGIMessage {
    private static final String RCSID = "$Id: FCGIMessage.java,v 1.4 2000/10/02 15:09:07 robs Exp $";
    private int h_version;
    private int h_type;
    private int h_requestID;
    private int h_contentLength;
    private int h_paddingLength;
    private int br_role;
    private int br_flags;
    private FCGIInputStream in;

    public FCGIMessage() {
    }

    public FCGIMessage(FCGIInputStream instream) {
        this.in = instream;
    }

    public int processHeader(byte[] hdr) throws IOException {
        this.processHeaderBytes(hdr);
        if (this.h_version != FCGIGlobalDefs.def_FCGIVersion1) {
            return -2;
        } else {
            this.in.contentLen = this.h_contentLength;
            this.in.paddingLen = this.h_paddingLength;
            if (this.h_type == 1) {
                return this.processBeginRecord(this.h_requestID);
            } else if (this.h_requestID == 0) {
                return this.processManagementRecord(this.h_type);
            } else if (this.h_requestID != this.in.request.requestID) {
                return 1;
            } else {
                return this.h_type != this.in.type ? -3 : 0;
            }
        }
    }

    private void processHeaderBytes(byte[] hdrBuf) {
        this.h_version = hdrBuf[0] & 255;
        this.h_type = hdrBuf[1] & 255;
        this.h_requestID = (hdrBuf[2] & 255) << 8 | hdrBuf[3] & 255;
        this.h_contentLength = (hdrBuf[4] & 255) << 8 | hdrBuf[5] & 255;
        this.h_paddingLength = hdrBuf[6] & 255;
    }

    public int processBeginRecord(int requestID) throws IOException {
        if (requestID != 0 && this.in.contentLen == 8) {
            if (this.in.request.isBeginProcessed) {
                byte[] endReqMsg = new byte[16];
                System.arraycopy(this.makeHeader(3, requestID, 8, 0), 0, endReqMsg, 0, 8);
                System.arraycopy(this.makeEndrequestBody(0, 1), 0, endReqMsg, 8, 8);

                try {
                    this.in.request.outStream.write(endReqMsg, 0, 16);
                } catch (IOException e) {
                    this.in.request.outStream.setException(e);
                    return -1;
                }
            }

            this.in.request.requestID = requestID;
            byte[] beginReqBody = new byte[8];
            if (this.in.read(beginReqBody, 0, 8) != 8) {
                return -3;
            } else {
                this.br_flags = beginReqBody[2] & 255;
                this.in.request.keepConnection = (this.br_flags & FCGIGlobalDefs.def_FCGIKeepConn) != 0;
                this.br_role = (beginReqBody[0] & 255) << 8 | beginReqBody[1] & 255;
                this.in.request.role = this.br_role;
                this.in.request.isBeginProcessed = true;
                return 2;
            }
        } else {
            return -3;
        }
    }

    public int processManagementRecord(int type) throws IOException {
        byte[] response = new byte[64];
        int wrndx = response[8];
        if (type == 9) {
            Properties tmpProps = new Properties();
            this.readParams(tmpProps);
            if (this.in.getFCGIError() != 0 || this.in.contentLen != 0) {
                return -3;
            }

            if (tmpProps.containsKey("FCGI_MAX_CONNS")) {
                this.makeNameVal("FCGI_MAX_CONNS", "1", response, wrndx);
            } else if (tmpProps.containsKey("FCGI_MAX_REQS")) {
                this.makeNameVal("FCGI_MAX_REQS", "1", response, wrndx);
            } else if (tmpProps.containsKey("FCGI_MAX_CONNS")) {
                this.makeNameVal("FCGI_MPXS_CONNS", "0", response, wrndx);
            }

            int plen = 64 - wrndx;
            int len = wrndx - 8;
            System.arraycopy(this.makeHeader(10, 0, len, plen), 0, response, 0, 8);
        } else {
            int len = 8;
            int plen = 8;
            System.arraycopy(this.makeHeader(11, 0, len, 0), 0, response, 0, 8);
            System.arraycopy(this.makeUnknownTypeBodyBody(this.h_type), 0, response, 8, 8);
        }

        try {
            this.in.request.socket.getOutputStream().write(response, 0, 16);
            return 3;
        } catch (IOException var8) {
            return -1;
        }
    }

    void makeNameVal(String name, String value, byte[] dest, int pos) {
        int nameLen = name.length();
        if (nameLen < 128) {
            dest[pos++] = (byte)nameLen;
        } else {
            dest[pos++] = (byte)((nameLen >> 24 | 128) & 255);
            dest[pos++] = (byte)(nameLen >> 16 & 255);
            dest[pos++] = (byte)(nameLen >> 8 & 255);
            dest[pos++] = (byte)nameLen;
        }

        int valLen = value.length();
        if (valLen < 128) {
            dest[pos++] = (byte)valLen;
        } else {
            dest[pos++] = (byte)((valLen >> 24 | 128) & 255);
            dest[pos++] = (byte)(valLen >> 16 & 255);
            dest[pos++] = (byte)(valLen >> 8 & 255);
            dest[pos++] = (byte)valLen;
        }

        try {
            System.arraycopy(name.getBytes("UTF-8"), 0, dest, pos, nameLen);
            pos += nameLen;
            System.arraycopy(value.getBytes("UTF-8"), 0, dest, pos, valLen);
            int var10000 = pos + valLen;
        } catch (UnsupportedEncodingException var8) {
        }

    }

    public int readParams(Properties props) throws IOException {
        byte[] lenBuff = new byte[3];
        int i = 1;

        int nameLen;
        while((nameLen = this.in.read()) != -1) {
            ++i;
            if ((nameLen & 128) != 0) {
                if (this.in.read(lenBuff, 0, 3) != 3) {
                    this.in.setFCGIError(-4);
                    return -1;
                }

                nameLen = (nameLen & 127) << 24 | (lenBuff[0] & 255) << 16 | (lenBuff[1] & 255) << 8 | lenBuff[2] & 255;
            }

            int valueLen;
            if ((valueLen = this.in.read()) == -1) {
                this.in.setFCGIError(-4);
                return -1;
            }

            if ((valueLen & 128) != 0) {
                if (this.in.read(lenBuff, 0, 3) != 3) {
                    this.in.setFCGIError(-4);
                    return -1;
                }

                valueLen = (valueLen & 127) << 24 | (lenBuff[0] & 255) << 16 | (lenBuff[1] & 255) << 8 | lenBuff[2] & 255;
            }

            byte[] name = new byte[nameLen];
            byte[] value = new byte[valueLen];
            if (this.in.read(name, 0, nameLen) != nameLen) {
                this.in.setFCGIError(-4);
                return -1;
            }

            if (this.in.read(value, 0, valueLen) != valueLen) {
                this.in.setFCGIError(-4);
                return -1;
            }

            String strName = new String(name);
            String strValue = new String(value);
            props.put(strName, strValue);
        }

        return 0;
    }

    public byte[] makeHeader(int type, int requestId, int contentLength, int paddingLength) {
        byte[] header = new byte[8];
        header[0] = (byte)FCGIGlobalDefs.def_FCGIVersion1;
        header[1] = (byte)type;
        header[2] = (byte)(requestId >> 8 & 255);
        header[3] = (byte)(requestId & 255);
        header[4] = (byte)(contentLength >> 8 & 255);
        header[5] = (byte)(contentLength & 255);
        header[6] = (byte)paddingLength;
        header[7] = 0;
        return header;
    }

    public byte[] makeEndrequestBody(int appStatus, int protocolStatus) {
        byte[] body = new byte[8];
        body[0] = (byte)(appStatus >> 24 & 255);
        body[1] = (byte)(appStatus >> 16 & 255);
        body[2] = (byte)(appStatus >> 8 & 255);
        body[3] = (byte)(appStatus & 255);
        body[4] = (byte)protocolStatus;

        for(int i = 5; i < 8; ++i) {
            body[i] = 0;
        }

        return body;
    }

    public byte[] makeUnknownTypeBodyBody(int type) {
        byte[] body = new byte[8];
        body[0] = (byte)type;

        for(int i = 1; i < 8; ++i) {
            body[i] = 0;
        }

        return body;
    }
}

