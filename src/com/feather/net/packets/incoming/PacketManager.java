package com.feather.net.packets.incoming;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.utils.ReflectionUtils;

public final class PacketManager {
	
	private static HashMap<ClientPacket, Integer> packet_map = new HashMap<ClientPacket, Integer>();
	private static AbstractPacket[] packet_handlers;
	
	
	public static void main(String[] args) {
		System.out.println("Reflection Test.");
	}
	
	static {
		try {
			ArrayList<Class<?>> classes = ReflectionUtils.getClassesForPackage(PacketManager.class.getPackageName() + ".impl");
			
			packet_handlers = new AbstractPacket[classes.size()-1];
			//System.out.println("Found "+ classes.size() + " Packet Classes.");
			int handler_index = -1;
			
			for(Class<?> c : classes) {
				
				if(c != null && c.getSuperclass() != null) {
					//System.out.println("Class: "+ c.getSimpleName());
					
					
					if(c.getSuperclass().getSuperclass() != null) {
						// Check to see if AbstractPacket.class is the Superclass
						if(c.getSuperclass().getSuperclass().getSimpleName().equalsIgnoreCase(AbstractPacket.class.getSimpleName())) {
							AbstractPacket ap = (AbstractPacket) c.getDeclaredConstructor(null).newInstance(null);
							
							if(ap == null) {
								continue;
							}
							
							handler_index++;
							
							packet_handlers[handler_index] = ap;
							
							if(ap.getHandledPackets().length == 1) {
							    packet_map.put(ap.getHandledPackets()[0], handler_index);
							    System.out.println("Added Handler for Packet: " + ap.getHandledPackets()[0].toString() + ", Index: " + handler_index);
							} else {
								for(ClientPacket ip_cp : ap.getHandledPackets()) {
									packet_map.put(ip_cp, handler_index);
									 System.out.println("Added Handler for Packet: " + ip_cp.toString() + ", Index: " + handler_index);
								}
							}
							
							ap = null;
						}
						
					}
					   
					/*
					// Logic Packet
					if(c.getSuperclass().getSimpleName().equalsIgnoreCase(AbstractLogicPacket.class.getSimpleName())) {
						AbstractLogicPacket ip = (AbstractLogicPacket) c.getDeclaredConstructor(null).newInstance(null);
							
						if(ip.getHandledPackets() == null) {
							System.err.println("Incoming Packet handler '"+ c.getSimpleName() + "' does not handle any packets?");
							continue;
						}
							
						if(ip.getHandledPackets().length == 1) {
						    packet_map.put(ip.getHandledPackets()[0], ip);
						    System.out.println("Added Handler for Packet: " + ip.getHandledPackets()[0].toString());
						} else {
							for(ClientPacket ip_cp : ip.getHandledPackets()) {
								packet_map.put(ip_cp, ip);
								 System.out.println("Added Handler for Packet: " + ip_cp.toString());
							}
						}
					}
					*/
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Total Packet Handlers: "+ packet_map.size());
	}
	
	public static boolean hasPacketHandler(ClientPacket packet_id) {
		for (Map.Entry<ClientPacket, Integer> entry : packet_map.entrySet()) {
			if(entry.getKey() == packet_id) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void handleLogicPacket(Player player, LogicPacket packet) {
		for (Map.Entry<ClientPacket, Integer> entry : packet_map.entrySet()) {
		    if(entry.getKey().getID() == packet.getId().getID()) {
		    	AbstractPacket ap = packet_handlers[entry.getValue()];
		    	
		    	if(ap == null) {
		    		//TODO
		    		return;
		    	}
		    	
		    	if(ap instanceof AbstractLogicPacket) {
		    		((AbstractLogicPacket)ap).onPacketReceived(player, packet, new InputStream(packet.getData()));
		    	} else {
		    		//TODO
		    		return;
		    	}
		    }
		}
	}
	
	/*
	public static void handleLogicPacket(Player player, ClientPacket packet_id, InputStream stream) {
		for (Map.Entry<ClientPacket, Integer> entry : packet_map.entrySet()) {
		    if(entry.getKey().getValue() == packet_id.getValue()) {
		    	AbstractPacket ap = packet_handlers[entry.getValue()];
		    	
		    	if(ap == null) {
		    		//TODO
		    		return;
		    	}
		    	
		    	if(ap instanceof AbstractLogicPacket) {
		    		((AbstractLogicPacket)ap).onPacketReceived(player, null, stream);
		    	} else {
		    		//TODO
		    		return;
		    	}
		    }
		}
		
	}
	*/
}
