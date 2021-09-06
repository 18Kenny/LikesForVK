package Utils;

import com.paypal.selion.platform.dataprovider.DataProviderFactory;
import com.paypal.selion.platform.dataprovider.DataResource;
import com.paypal.selion.platform.dataprovider.SeLionDataProvider;
import com.paypal.selion.platform.dataprovider.impl.InputStreamResource;
import models.Liker;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProviderSource {


    private static Object[][] ClientDataReturner(String path) throws IOException {
        DataResource resource =
                new InputStreamResource(new FileInputStream(path),
                        Liker.class, "json");
        SeLionDataProvider dataProvider =
                DataProviderFactory.getDataProvider(resource);
        Object[][] data = dataProvider.getAllData();
        return data;
    }

    @DataProvider(name = "jsonDataProviderParallel", parallel = true)
    public static Object[][] getJsonDataProvider() throws IOException {
        return ClientDataReturner("./src/test/resources/dataForLiker.json");
    }

    @DataProvider(name = "jsonDataProvider")
    public static Object[][] getJsonDataProviderUsualClient() throws IOException {
        return ClientDataReturner("./src/test/resources/dataForRemoveLik.json");
    }
}