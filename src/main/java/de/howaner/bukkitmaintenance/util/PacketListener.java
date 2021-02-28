package de.howaner.bukkitmaintenance.util;

import de.howaner.bukkitmaintenance.MainServer;
import de.howaner.bukkitmaintenance.config.Config;
import de.howaner.bukkitmaintenance.json.DisconnectJSON;
import de.howaner.bukkitmaintenance.json.StatusResponseJSON;
import de.howaner.bukkitmaintenance.packet.*;
import lombok.Cleanup;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketListener extends Thread {

    protected static final Class[] packets = new Class[256];

    static {
        packets[0x00] = Packet0Handshake.class; //For 1.7
        packets[0x02] = Packet2Handshake.class;
        packets[0xFA] = Packet250PluginMessage.class;
        packets[0xFE] = Packet254ServerPing.class;
        packets[0xFF] = Packet255Disconnect.class;
    }

    private final MainServer mainServer;

    public PacketListener(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    public static Packet getNewPacket(int packetID) {
        if (packetID < 0 || packetID >= packets.length || packets[packetID] == null)
            return null;
        try {
            return (Packet) packets[packetID].newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void run() {
        try {
            try (ServerSocket server = new ServerSocket(Config.getBindPort(), 50, InetAddress.getByName(Config.getBindAddress()))) {
                Socket socket;
                while ((socket = server.accept()) != null) {
                    @Cleanup DataInputStream reader = new DataInputStream(socket.getInputStream());

                    int packetID = Varint.readVarInt(reader);
                    Packet packet = getNewPacket(packetID);
                    if (packet == null) {
                        packet = getNewPacket(Varint.readVarInt(reader));
                    }

                    if (packet == null) {
                        Logger.getGlobal().log(Level.INFO, () -> "Unkown Packet ID received: " + packetID);
                        reader.close();
                        socket.close();
                        continue;
                    }

                    packet.read(reader);
                    @Cleanup DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

                    if (packet instanceof Packet254ServerPing) {
                        this.a((Packet254ServerPing) packet, reader, writer);
                    } else if (packet instanceof Packet2Handshake) {
                        this.a((Packet2Handshake) packet, writer);
                    } else if (packet instanceof Packet0Handshake) {
                        this.a((Packet0Handshake) packet, reader, writer);
                    }

                    writer.flush();
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(Packet0Handshake packet, DataInputStream reader, DataOutputStream writer) throws Exception {
        if (packet.getD() == 2) {
            DisconnectJSON json = new DisconnectJSON();
            json.setText(Config.getKickMessage());

            Packet0Disconnect dPacket = new Packet0Disconnect();
            dPacket.setA(MainServer.instance.getGson().toJson(json));

            Packet0LoginStart loginPacket = new Packet0LoginStart();
            loginPacket.read(reader);

            Logger.getGlobal().log(Level.INFO, () -> "Received Login Packet from " + loginPacket.getA() + "!");
            this.send17Packet(writer, dPacket);
        } else if (packet.getD() == 1) {
            Logger.getGlobal().log(Level.INFO, "Received Status Packet!");
            Varint.readVarInt(reader); //Packet Length
            if (Varint.readVarInt(reader) != 0x00) {
                throw new IOException("The Client don't send a Status Request Packet!");
            }

            StatusResponseJSON.Version version = new StatusResponseJSON.Version();
            version.setName(Config.getVersionName());
            version.setProtocol(0);

            StatusResponseJSON.Players players = new StatusResponseJSON.Players();
            players.setOnline(0);
            players.setMax(0);
            players.setSample(new ArrayList<>());

            StatusResponseJSON.Description description = new StatusResponseJSON.Description();
            description.setText(Config.getMultilineMotd());

            StatusResponseJSON json = new StatusResponseJSON();
            json.setVersion(version);
            json.setPlayers(players);
            json.setDescription(description);
            json.setFavicon(mainServer.getIcon());

            Packet0StatusResponse statusPacket = new Packet0StatusResponse();
            statusPacket.setA(MainServer.instance.getGson().toJson(json));
            this.send17Packet(writer, statusPacket);

            //Ping Time Packets
            Varint.readVarInt(reader); //Packet Size
            if (Varint.readVarInt(reader) != 0x01) return;
            Packet1Ping pingPacket = new Packet1Ping();
            pingPacket.read(reader);
            this.send17Packet(writer, pingPacket);
        }
    }

    public void a(Packet2Handshake packet, DataOutputStream writer) throws Exception {
        Logger.getGlobal().log(Level.INFO, "Received Login Packet!");
        Packet255Disconnect disconnectPacket = (Packet255Disconnect) getNewPacket(0xFF);
        assert disconnectPacket != null;
        disconnectPacket.setA(Config.getKickMessage());
        this.sendPacket(writer, disconnectPacket);
    }

    public void a(Packet254ServerPing packet, DataInputStream reader, DataOutputStream writer) throws Exception {
        Logger.getGlobal().log(Level.INFO, "Received Status Packet!");
        //Magical Byte Check
        if (packet.getA() != (byte) 1) {
            throw new IOException("Magic Byte isn't 1!");
        }

        //Is the next Packet a Pluginmessage?
        if (reader.readUnsignedByte() != 0xFA) {
            throw new IOException("The received Packet isn't a Plugin Message.");
        }

        Packet250PluginMessage pluginPacket = (Packet250PluginMessage) getNewPacket(0xFA);
        assert pluginPacket != null;
        pluginPacket.read(reader);

        if (!pluginPacket.getA().equals("MC|PingHost")) {
            throw new IOException("Bad channel: " + pluginPacket.getA());
        }

        Packet255Disconnect responsePacket = new Packet255Disconnect();
        responsePacket.setA(PingUtil.createPingString(0, Config.getVersionName(), Config.getMotd(), 0, 0));
        this.sendPacket(writer, responsePacket);
    }

    public void send17Packet(DataOutputStream stream, Packet packet) throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream packetStream = new DataOutputStream(b);
        Varint.writeVarInt(packetStream, packet.getPacketID());
        packet.write(packetStream);

        byte[] out = b.toByteArray();
        Varint.writeVarInt(stream, out.length);
        stream.write(out);
    }

    public void sendPacket(DataOutputStream stream, Packet packet) throws Exception {
        stream.writeByte(packet.getPacketID());
        packet.write(stream);
    }
}
