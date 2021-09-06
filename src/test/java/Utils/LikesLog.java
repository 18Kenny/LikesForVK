package Utils;

import com.vk.api.sdk.actions.Likes;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.queries.likes.LikesAddQuery;
import com.vk.api.sdk.queries.likes.LikesDeleteQuery;
import com.vk.api.sdk.queries.likes.LikesGetListQuery;
import com.vk.api.sdk.queries.likes.LikesIsLikedQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LikesLog extends Likes {
    private static final Logger logger = LogManager.getLogger(LikesLog.class);

    public LikesLog(VkApiClient client) {
        super(client);
    }

    @Override
    public LikesAddQuery add(UserActor actor, Type type, int itemId) {
        logger.info("we add new like for item with itemId = " + itemId);
        return new LikesAddQuery(getClient(), actor, type, itemId);
    }

    @Override
    public LikesDeleteQuery delete(UserActor actor, Type type, int itemId) {
        logger.info("we delete like for item with itemId = " + itemId);
        return new LikesDeleteQuery(getClient(), actor, type, itemId);
    }

    @Override
    public LikesGetListQuery getList(UserActor actor, Type type) {
        logger.info("we get list likes for user");
        return new LikesGetListQuery(getClient(), actor, type);
    }

    @Override
    public LikesIsLikedQuery isLiked(UserActor actor, Type type, int itemId) {
        logger.info("we will check item with id = " + itemId);
        return new LikesIsLikedQuery(getClient(), actor, type, itemId);
    }
}
