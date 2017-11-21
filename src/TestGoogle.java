
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;




public class TestGoogle {
	
	

    private static  String encodeFileToBase64Binary(File file){
         String encodedfile = null;
         try {
             FileInputStream fileInputStreamReader = new FileInputStream(file);
             byte[] bytes = new byte[(int)file.length()];
             fileInputStreamReader.read(bytes);
             encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
         } catch (FileNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }

         return encodedfile;
     }
	//AIzaSyCTtg3tN8WwwVBvFf85PswsQN749bAl8ks
  public static void main(String[] args) throws Exception {
    // Instantiates a client4
	try{  String re = "";
	    String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyCTtg3tN8WwwVBvFf85PswsQN749bAl8ks";
	    URL obj = new URL(url);
	    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	   // BufferedImage img = ImageIO.read(new File("c://users//uma//desktop//1.jpg"));
	    String imgstr = encodeFileToBase64Binary(new File("F:\\UMA\\3.jpg"
	    		
	    		));

	//    imgstr = encodeFileToBase64Binary(img, "png");

	    //add reuqest header
	    con.setRequestMethod("POST");

	    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

	    String urlParameters = "{\n"
	            + "  \"requests\":[\n"
	            + "    {\n"
	            + "      \"image\":{\n"
	            + "        \"content\":\"" + imgstr + "\"\n"
	            + "      },\n"
	            + "      \"features\":[\n"
	            + "        {\n"
	            + "          \"type\":\"TEXT_DETECTION\",\n"
	            + "          \"maxResults\":1\n"
	            + "        }\n"
	            + "      ]\n"
	            + "    }\n"
	            + "  ]\n"
	            + "}";

	    // Send post request
	    con.setDoOutput(true);
	    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	    wr.writeBytes(urlParameters);
	    wr.flush();
	    wr.close();
	    System.out.println("1");
		  
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    System.out.println("12");
	    while ((inputLine = in.readLine()) != null) {
	    	System.out.println(inputLine);
	    	response.append(inputLine);
	    }
	    in.close();

	    re = response.toString();
	 
	    System.out.println(re);
	    try(  PrintWriter out = new PrintWriter( "F:\\UMA\\result.txt" )  ){
	        out.println( re );
	    }
	    Thread.sleep(10000);
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	  
	  
	//  detectText("c:\\users\\uma\\desktop\\1.jpg","c:\\users\\uma\\desktop\\1re.txt");
    /**try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

      // The path to the image file to annotate
      String fileName = "./resources/wakeupcat.jpg";

      // Reads the image file into memory
      Path path = Paths.get(fileName);
      byte[] data = Files.readAllBytes(path);
      ByteString imgBytes = ByteString.copyFrom(data);

      // Builds the image annotation request
      List<AnnotateImageRequest> requests = new ArrayList<>();
      Image img = Image.newBuilder().setContent(imgBytes).build();
      Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
      AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
          .addFeatures(feat)
          .setImage(img)
          .build();
      requests.add(request);

      // Performs label detection on the image file
      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      for (AnnotateImageResponse res : responses) {
        if (res.hasError()) {
          System.out.printf("Error: %s\n", res.getError().getMessage());
          return;
        }

        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
          Object k;
		//Object v;
		//annotation.getAllFields().forEach((k, v)->
       //       System.out.printf("%s : %s\n", k, v.toString()));
        }
      }
    }**/
  }

public static void detectText(String filePath, PrintStream out) throws Exception, IOException {
	  List<AnnotateImageRequest> requests = new ArrayList<>();

	  ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

	  Image img = Image.newBuilder().setContent(imgBytes).build();
	  Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
	  AnnotateImageRequest request =
	      AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
	  requests.add(request);

	  try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
	    BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
	    List<AnnotateImageResponse> responses = response.getResponsesList();

	    for (AnnotateImageResponse res : responses) {
	      if (res.hasError()) {
	        out.printf("Error: %s\n", res.getError().getMessage());
	        return;
	      }

	      // For full list of available annotations, see http://g.co/cloud/vision/docs
	      for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
	        out.printf("Text: %s\n", annotation.getDescription());
	        out.printf("Position : %s\n", annotation.getBoundingPoly());
	      }
	    }
	  }
	}

}
