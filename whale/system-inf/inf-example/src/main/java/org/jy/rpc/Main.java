package org.jy.rpc;

import org.jy.rpc.op.Echo;
import org.jy.rpc.op.RemoteEcho;
import org.jy.rpc.support.Server;



public class Main {
	public static void main(String[] args) {
		Server server = new RPC.RPCServer();
		server.register(Echo.class, RemoteEcho.class);
		server.start();
	}

}
