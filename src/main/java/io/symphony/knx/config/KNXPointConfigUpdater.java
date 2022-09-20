package io.symphony.knx.config;

import io.symphony.extension.config.data.PointConfig;
import io.symphony.extension.config.rest.PointConfigUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KNXPointConfigUpdater implements PointConfigUpdater {

    @Override
    public void updateNonNull(PointConfig from, PointConfig to) {
        if (from instanceof KNXPointConfig && to instanceof KNXPointConfig)
            updateNonNull((KNXPointConfig) from, (KNXPointConfig) to);
    }

    private void updateNonNull(KNXPointConfig from, KNXPointConfig to) {
		if (from.getDpt() != null)
			to.setDpt(from.getDpt());
		if (from.getRead() != null)
			to.setRead(from.getRead());
		if (from.getWrite() != null)
			to.setWrite(from.getWrite());
    }

    @Override
    public void updateAll(PointConfig from, PointConfig to) {
        if (from instanceof KNXPointConfig && to instanceof KNXPointConfig)
            updateAll((KNXPointConfig) from, (KNXPointConfig) to);
    }

    private void updateAll(KNXPointConfig from, KNXPointConfig to) {
        to.setDpt(from.getDpt());
        to.setRead(from.getRead());
        to.setWrite(from.getWrite());
    }

}
