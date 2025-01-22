package com.scm.scm.services.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.scm.helper.AppConstants;
import com.scm.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

    private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String filename){

        // code jo image ko upload kr rha ho server pr

        try {
            byte[] data = contactImage.getBytes();
            // contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", filename
            ));
            // and return the url
            return this.getUrlFromPublicId(filename);


        } catch (IOException e) {
            logger.error("Error occurred during image upload", e);
            return null;
           
        }

        
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        
        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMAGE_WIDTH)
            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
            .crop(AppConstants.CONTACT_IMAGE_CROP)
        )
        .generate(publicId);
    }

   
}
