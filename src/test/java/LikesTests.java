import Utils.ConfigReader;
import Utils.DataProviderSource;
import Utils.VkApiClientLog;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.likes.Type;
import models.Liker;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class LikesTests {
    private final int user_id = Integer.parseInt(ConfigReader.getConfigPropertyMap().get("db.user_id"));
    private final int defaultPostId = 848;
    public String messageNegative = "One of the parameters specified was missing or invalid (100): One of the parameters specified was missing or invalid: item_id should be greater or equal to 0";
    public String gotWrongCode = "we got wrong code for negative scenario";
    public String gotWrongMessage = "we got wrong message for negative scenario";
    public String infoMessageNegative = "we initiated negative scenario";
    private VkApiClientLog vk;
    private UserActor actor;
    private static final Logger logger = LogManager.getLogger(LikesTests.class);

    @AfterMethod(onlyForGroups = {"parallel", "needWait"})
    private static void waiter() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(3000); //need more users for normal parallel work
    }

    private void setStatus(int postId, String status) throws ClientException, ApiException {
        logger.info("we change status post on the necessary for tests");
        String currentStatus = vk.likes().isLiked(actor, Type.POST, postId).execute().getLiked().getValue();
        if (status.equals("1") && !currentStatus.equals(status)) {
            vk.likes().add(actor, Type.POST, postId).execute();
        } else if (status.equals("0") && !currentStatus.equals(status)) {
            vk.likes().delete(actor, Type.POST, postId).execute();
        } else {
            assertTrue(true, "Wrong input for method - " + status);
        }
    }

    @BeforeClass
    private void preparation() {
        String log4jConfPath = "./src/test/resources/logging.properties";
        PropertyConfigurator.configure(log4jConfPath);
        HttpTransportClient httpClient = HttpTransportClient.getInstance();
        logger.setLevel(Level.INFO);
        vk = new VkApiClientLog(httpClient);
        actor = new UserActor(
                user_id,
                ConfigReader.getConfigPropertyMap().get("db.access_token"));
    }

    @Test(groups = {"post", "needWait"}, dataProviderClass = DataProviderSource.class, dataProvider = "jsonDataProvider")
    void userIsLikesPost(Liker liker) throws ClientException, ApiException {
        int postId = liker.getPostId();
        setStatus(postId, "1");
        logger.info("we validate status post is liked or not");
        assertNotEquals(postId, vk.likes().isLiked(actor, Type.POST, postId).execute().getLiked().getValue(), "post have status liked");
    }

    @Test(groups = {"post"}, priority = 2)
    void userIsLikesNegativePost() throws ClientException {
        try {
            vk.likes().isLiked(
                    actor,
                    Type.POST,
                    -defaultPostId
            ).execute();
        } catch (ApiException e) {
            logger.info(infoMessageNegative);
            logger.info("we validate error message and code response");
            assertEquals((int) e.getCode(), 100, gotWrongCode);
            assertEquals(messageNegative, e.getMessage(), gotWrongMessage);
        }
    }

//    for normal logging , we should create separate listener for logging each tread separately
    @Test(groups = {"post", "parallel"}, dataProviderClass = DataProviderSource.class, dataProvider = "jsonDataProviderParallel", priority = 1)
    void userLikePost(Liker liker) throws ClientException, ApiException {
        int postId = liker.getPostId();
        setStatus(postId, "0");
        vk.likes().add(
                actor,
                Type.POST,
                postId
        ).execute();
        logger.info("we validate status post is liked or not");
        assertNotEquals(postId, vk.likes().isLiked(actor, Type.POST, postId).execute().getLiked().getValue(), "post have status not liked");
    }

    @Test(groups = {"post", "negative"}, priority = 2)
    void userLikePostNegative() throws ClientException {
        try {
            vk.likes().add(
                    actor,
                    Type.POST,
                    -defaultPostId
            ).execute();
        } catch (ApiException e) {
            logger.info(infoMessageNegative);
            logger.info("we validate error message and code response");
            assertEquals((int) e.getCode(), 100, gotWrongCode);
            assertEquals(messageNegative, e.getMessage(), gotWrongMessage);
        }
    }

    @Test(groups = {"post"}, dataProviderClass = DataProviderSource.class, dataProvider = "jsonDataProvider", priority = 2)
    void userDislikePost(Liker liker) throws ClientException, ApiException {
        int postId = liker.getPostId();
        setStatus(postId, "1");
        vk.likes().delete(
                actor,
                Type.POST,
                postId
        ).execute();
        logger.info("we check the status of the post, it should be disliked");
        assertNotEquals(postId, vk.likes().isLiked(actor, Type.POST, postId).execute().getLiked().getValue(), "post have status liked");
    }

    @Test(groups = {"post", "negative"}, priority = 2)
    void userDislikeNegative() throws ClientException {
        try {
            vk.likes().delete(
                    actor,
                    Type.POST,
                    -defaultPostId
            ).execute();
        } catch (ApiException e) {
            logger.info(infoMessageNegative);
            logger.info("we validate error message and code response");
            assertEquals((int) e.getCode(), 100, gotWrongCode);
            assertEquals(messageNegative, e.getMessage(), gotWrongMessage);
        }
    }

    @Test(groups = {"post", "needWait"}, priority = 2)
    void userLikeGetListPost() throws ClientException, ApiException {
        setStatus(defaultPostId, "1");
        logger.info("we validate list likes post, user should be in the list");
        assertTrue(vk
                .likes()
                .getList(actor, Type.POST)
                .ownerId(user_id)
                .itemId(defaultPostId)
                .execute()
                .toString()
                .contains("94579179"), "list likers don't contain all users");
    }

    @Test(groups = {"post", "negative"}, priority = 2)
    void userLikeGetListNegative() throws ClientException {
        try {
            vk
                    .likes()
                    .getList(actor, Type.POST)
                    .ownerId(-user_id)
                    .itemId(+defaultPostId)
                    .execute();
        } catch (ApiException e) {
            logger.info(infoMessageNegative);
            logger.info("we validate error message and code response");
            assertEquals((int) e.getCode(), 100, gotWrongCode);
            assertEquals(messageNegative, e.getMessage(), gotWrongMessage);
        }
    }
}

