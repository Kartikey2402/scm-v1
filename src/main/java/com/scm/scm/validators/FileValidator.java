package com.scm.scm.validators;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private Logger logger = LoggerFactory.getLogger(FileValidator.class);

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2; // 2MB

    // type

    // width

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        
        if(file == null || file.isEmpty()){
            // logger.error("File is empty");
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();

            return true;
        }
        logger.info("File size: " + file.getSize());

        System.out.println("File size: " + file.getSize());
        if(file.getSize() > MAX_FILE_SIZE){
            logger.error("File exceeded size");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should be less than 5MB").addConstraintViolation();

            return false;
        }
        else{
            return true;
        }

        // resolution check
        // try {
        //     BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            
        //     if (bufferedImage.getWidth() > 500) {
        //         return false;
        //     }
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

}
