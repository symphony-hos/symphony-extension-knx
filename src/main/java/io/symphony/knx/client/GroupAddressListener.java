package io.symphony.knx.client;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.IndividualAddress;

public interface GroupAddressListener {

    public default void groupWrite(IndividualAddress source, GroupAddress destination, byte[] asdu) {}

    public default void groupRead(IndividualAddress source, GroupAddress destination, byte[] asdu) {}

    public default void groupReadResponse(IndividualAddress source, GroupAddress destination, byte[] asdu) {}
    
    public boolean subscribesTo(GroupAddress address);
	
}
