
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import mx.org.azure.BlobStorage;


/**
 *
 * @author elopez
 */
public class Test {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

        String filePath = "D:\\Users\\elopez\\Pictures\\photo-of-siberian-husky-puppy-2853130-1024x683.jpg";

        String url = null;

        {

            BlobStorage.SetConnection("DefaultEndpointsProtocol=https;AccountName=ruvcomun;AccountKey=7i1Clr5xSmWrv5SUamS7xq15Agu/U24g5PVKGT53oRYcLrU4kb9dLoTUGMZMfnGyoVyMl3xmMBze3Gayf3T0CQ==", "zcudonacion");

            //String storageConnectionString = BlobStorage.storageConnectionString;
            //BlobStorage.SetConnection(storageConnectionString, "zcudonacion");
            {
                url = BlobStorage.uploadFile(filePath, "photo-of-siberian-husky-puppy-2853130-1024x6834.jpg", true);
            }

            {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));

                url = BlobStorage.uploadFile(bytes, "photo-of-siberian-husky-puppy-2853130-1024x6835.jpg", true);
            }

            {

                {

                    byte[] initialArray = BlobStorage.getDocumentArrayByte(url);

                    InputStream stream = new ByteArrayInputStream(initialArray);

                    Files.copy(stream, Paths.get("D:\\Users\\elopez\\Pictures\\photo-of-siberian-husky-puppy-2853130-1024x683_COPY_1.jpg"));
                }

                {
                    InputStream data = BlobStorage.getDocumentInputStream(url);

                    Files.copy(data, Paths.get("D:\\Users\\elopez\\Pictures\\photo-of-siberian-husky-puppy-2853130-1024x683_COPY_2.jpg"));
                }

            }
        }

    }

}
