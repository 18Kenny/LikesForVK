package Utils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class VkApiClientLog extends VkApiClient {
    private static final Logger logger = LogManager.getLogger(VkApiClientLog.class);

    public VkApiClientLog(TransportClient transportClient) {
        super(transportClient);
    }

    @Override
    public LikesLog likes() {
        logger.info("we initiated new Likes object for client");
        return new LikesLog(this);
    }
}
