import com.azure.storage.blob.BlobServiceClientBuilder
import com.typesafe.scalalogging.LazyLogging
import java.io.{File, FileWriter}

object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {
    logger.info("Hello Azure Storage")
    val connectStr = System.getenv("AZURE_STORAGE_CONNECTION_STRING")
    // Create a BlobServiceClient object (using BlobServiceClientBuilder) which will be used to create a container client
    val blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient
    //Create a unique name (String) for the container
    val containerName = "blob-container-" + java.util.UUID.randomUUID
    // Create the container (using blobServiceClient) and return a container client object
    val containerClient = blobServiceClient.createBlobContainer(containerName)

    // Create a local file in the ./data/ directory for uploading and downloading
    val localPath = "./data/"
    val fileName = "file-" + java.util.UUID.randomUUID + ".txt"
    val localFile = new File(localPath + fileName)

    // Write text to the file
    val writer = new FileWriter(localPath + fileName, true)
    writer.write("Hello, Scala and Azure!")
    writer.close()

    // Get a reference to a blob (using containerClient)
    val blobClient = containerClient.getBlobClient(fileName)

    logger.info("Uploading to Blob storage as blob: " + blobClient.getBlobUrl)

    // Upload the blob (using blobClient)
    blobClient.uploadFromFile(localPath + fileName)

    logger.info("Listing blobs...")

    // List the blob(s) in the container.
    containerClient.listBlobs.forEach(
      item => logger.info(item.getName)
    )

    // Download the blob to a local file
    // Append the string "DOWNLOAD" before the .txt extension so that you can see both files.
    val downloadFileName = fileName.replace(".txt", "DOWNLOAD.txt")
    val downloadedFile = new File(localPath + downloadFileName)

    logger.info("Downloading blob to " + localPath + downloadFileName)
    blobClient.downloadToFile(localPath + downloadFileName)

    logger.info("Press ENTER to continue with the clean up")
    scala.io.StdIn.readLine()

    // Clean up
    logger.info("Deleting blob container...")
    containerClient.delete()

    logger.info("Deleting the local source and downloaded files...")

    val isLocalDeleted = localFile.delete()
    val isDownloadedDeleted = downloadedFile.delete()

    if (isLocalDeleted && isDownloadedDeleted) {
      logger.info("Deleted both files")
    } else {
      logger.info("Couldn't delete the files!")
    }

    println("Done")
  }
}