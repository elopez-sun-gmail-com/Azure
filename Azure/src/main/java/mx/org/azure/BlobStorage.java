package mx.org.azure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author elopez
 */
public class BlobStorage {

    private static Logger logger = Logger.getLogger("MyLog");

    public static String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=ruvcomun;AccountKey=7i1Clr5xSmWrv5SUamS7xq15Agu/U24g5PVKGT53oRYcLrU4kb9dLoTUGMZMfnGyoVyMl3xmMBze3Gayf3T0CQ==";

    private static String containerName = "zcudonacion";

    /**
     *
     * @param storageConnectionString
     * @param containerName
     */
    public static void SetConnection(String storageConnectionString, String containerName) {

        BlobStorage.storageConnectionString = storageConnectionString;

        BlobStorage.containerName = containerName;
    }

    /**
     *
     * @param filePath
     * @param nombre
     * @param duplicados
     * @return
     */
    public static String uploadFile(String filePath, String nombre, boolean duplicados) {

        String url = null;

        try {

            byte[] bytes = Files.readAllBytes(Paths.get(filePath));

            url = BlobStorage.uploadFile(bytes, nombre, duplicados);

        } catch (Exception e) {
            System.out.print("Exception -->> " + e.getMessage());
        }

        return url;
    }

    /**
     *
     * @param inputStream
     * @param nombre
     * @param duplicados
     * @return
     */
    public static String uploadFile(InputStream inputStream, String nombre, boolean duplicados) {

        String url = null;

        try {

            byte[] bytes = IOUtils.toByteArray(inputStream);

            url = BlobStorage.uploadFile(bytes, nombre, duplicados);

        } catch (Exception e) {
            System.out.print("Exception -->> " + e.getMessage());
        }

        return url;
    }

    /**
     *
     * @param bytes
     * @param nombre
     * @param duplicados
     * @return
     */
    public static String uploadFile(byte[] bytes, String nombre, boolean duplicados) {

        String url = null;

        try {

            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);

            CloudBlobClient serviceClient = account.createCloudBlobClient();

            CloudBlobContainer container = serviceClient.getContainerReference(containerName);

            container.createIfNotExists();

            //Permissions
            {
                BlobContainerPermissions blobContainerPermissions = new BlobContainerPermissions();

                blobContainerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

                container.uploadPermissions(blobContainerPermissions);
            }

            if (duplicados == false) {
                serviceClient.getContainerReference(containerName).getBlockBlobReference(nombre).deleteIfExists();
            }

            CloudBlockBlob blob = container.getBlockBlobReference(nombre);

            File sourceFile = File.createTempFile(nombre, null, null);

            sourceFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(sourceFile);

            fileOutputStream.write(bytes);

            fileOutputStream.close();

            FileInputStream input = new FileInputStream(sourceFile);

            blob.upload(input, sourceFile.length());

            url = blob.getUri().toURL().toString();

            System.out.println("URL -->> " + url);

        } catch (Exception e) {
            System.out.print("Exception -->> " + e.getMessage());
        }

        return url;
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] getDocumentArrayByte(String url) throws IOException {

        InputStream data = BlobStorage.getDocumentInputStream(url);

        byte[] initialArray = IOUtils.toByteArray(data);

        return initialArray;

    }

    /**
     *
     * @param url
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getDocumentInputStream(String url) throws MalformedURLException, IOException {

        URL u = new URL(url);

        InputStream openStream = u.openStream();

        return openStream;

    }

}
