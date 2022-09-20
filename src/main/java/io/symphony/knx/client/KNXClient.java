package io.symphony.knx.client;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator;

public interface KNXClient {

    public void read(DPT dpt, GroupAddress ga);

    public void write(GroupAddress ga, DPTXlator xlator);

    
    public void addGroupAddressListener(GroupAddressListener listener);
    
    public void removeGroupAddressListener(GroupAddressListener listener);
    
}
