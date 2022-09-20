package io.symphony.knx.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import io.symphony.knx.properties.IPConnectionProperties;
import io.symphony.knx.properties.KNXExtensionProperties;
import lombok.RequiredArgsConstructor;
import tuwien.auto.calimero.IndividualAddress;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KNXIPClient extends KNXClientSupport {
	
	private final KNXExtensionProperties properties;
		
	@PostConstruct
	private void setUp() {
		this.connect();
	}
	
	@PreDestroy
	private void tearDown() {
		this.disconnect();
	}

	@Override
	public KNXNetworkLink createConnection() throws Exception {
		IPConnectionProperties config = properties.getConnection().getIp();
		
		// Local Socket
        String localAddress = config.getLocalAddress();
  		Integer localPort = config.getLocalPort() != null ? config.getLocalPort() : 0;
		InetSocketAddress local = new InetSocketAddress(localPort);
		if (localAddress != null)
			local = new InetSocketAddress(localAddress, localPort);
	
		// Remote Socket
		String remoteAddr = config.getRemoteAddress();
		InetAddress remoteInetAddr = InetAddress.getByName(remoteAddr);
		int remotePort = config.getRemotePort();
		InetSocketAddress remote = new InetSocketAddress(remoteInetAddr, remotePort);
		
		// Settings
		String localSource = config.getLocalSource();
		KNXMediumSettings settings = KNXMediumSettings.create(KNXMediumSettings.MEDIUM_TP1, new IndividualAddress(localSource));
		
		// Network Link
		boolean nat = config.getNat();
		return KNXNetworkLinkIP.newTunnelingLink(local, remote, nat, settings);
	}


}
