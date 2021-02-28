package de.howaner.bukkitmaintenance.packet;

import de.howaner.bukkitmaintenance.util.Varint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class Packet {

    public abstract void read(DataInputStream stream) throws IOException;

    public abstract void write(DataOutputStream stream) throws IOException;

    public abstract int getPacketID();

    public void writeString(DataOutputStream stream, String s) throws IOException {
        if (s.length() > 32767) return;
        stream.writeShort(s.length());
        stream.writeChars(s);
    }

    public String readString(DataInputStream stream, int maxLength) throws IOException {
        short length = stream.readShort();
        if (length > maxLength || length < 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(stream.readChar());
        }

        return builder.toString();
    }

    public void writeVarIntString(DataOutputStream stream, String s) throws IOException {
        byte[] in = s.getBytes(StandardCharsets.UTF_8);
        if (in.length > 32767) {
            throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
        }

        Varint.writeVarInt(stream, in.length);
        stream.write(in, 0, in.length);
    }

    public String readVarIntString(DataInputStream stream, int maxLength) throws IOException {
        int length = Varint.readVarInt(stream);
        if (length > maxLength * 4) {
            throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
        }

        if (length < 0) {
            throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
        }

        byte[] in = new byte[length];
        stream.readFully(in);
        String s = new String(in, StandardCharsets.UTF_8);
        if (s.length() > maxLength) {
            throw new IOException("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
        }

        return s;
    }
}
