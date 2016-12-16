package jvm.mohawk.chatter.clientapi.upload;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This class contains methods that relate to uploading information to the chatter web server.
 * Currently only used to upload profile pictures
 */
public final class ChatterUploader {

    private ChatterUploader(){}

    public static UploadProfilePictureResponse uploadProfilePicture(final ChatterClient.Config config,
                                                                    final String user,
                                                                    final File pic,
                                                                    final boolean checkSize) throws IOException {
        if(!pic.exists())
            return new UploadProfilePictureResponse(config, user, pic, UploadProfilePictureResponse.Code.IMAGE_NOT_FOUND);
        BufferedImage image = ImageIO.read(pic);
        if(image == null)
            return new UploadProfilePictureResponse(config, user, pic, UploadProfilePictureResponse.Code.IMAGE_WRONG_FORMAT);
        if(checkSize && (image.getWidth() > 512 || image.getHeight() > 512))
            return new UploadProfilePictureResponse(config, user, pic, UploadProfilePictureResponse.Code.IMAGE_TOO_BIG);
        final String extension = pic.getName().substring(pic.getName().lastIndexOf('.')+1);
        final File tmpFile = File.createTempFile(Long.toString(System.currentTimeMillis()), '.'+extension);
        if(checkSize){
            final Image scaledImg = image.getScaledInstance(
                    Math.min(image.getWidth(), 512),
                    Math.min(image.getHeight(), 512),
                    Image.SCALE_SMOOTH
            );
            final BufferedImage scaled = new BufferedImage(scaledImg.getWidth(null), scaledImg.getHeight(null), image.getType());
            Graphics2D g2d = scaled.createGraphics();
            g2d.drawImage(scaledImg, 0, 0, null);
            g2d.dispose();
            image = scaled;
        }
        if(!ImageIO.write(image, extension, tmpFile))
            return new UploadProfilePictureResponse(config, user, pic, UploadProfilePictureResponse.Code.ERROR);
        try(final CloseableHttpClient client = HttpClients.createDefault()){
            final HttpPost post = new HttpPost(config.uploadProfilePicScript());
            final HttpEntity entity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("pic", new FileBody(tmpFile))
                    .addPart("user", new StringBody(user, ContentType.MULTIPART_FORM_DATA))
                    .build();
            post.setEntity(entity);
            try(final CloseableHttpResponse response = client.execute(post)){
                final String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                final String url = result.startsWith("!") ? result.substring(1) : null;
                final UploadProfilePictureResponse.Code code = Optional.of(result)
                        .filter(s -> s.matches("\\d+"))
                        .map(Integer::parseInt)
                        .map(UploadProfilePictureResponse.Code::forId)
                        .orElse(UploadProfilePictureResponse.Code.ERROR);
                return new UploadProfilePictureResponse(
                        config,
                        user,
                        pic,
                        url != null ? UploadProfilePictureResponse.Code.SUCCESS : code,
                        url
                );
            }
        }
    }

    public static UploadProfilePictureResponse tryUploadProfilePicture(final ChatterClient.Config config,
                                                                       final String user,
                                                                       final File pic,
                                                                       final boolean checkSize){
        try{
            return uploadProfilePicture(config, user, pic, checkSize);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
