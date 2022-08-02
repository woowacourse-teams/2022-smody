package com.woowacourse.smody.image;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.util.FilePathBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalImageUploader implements ImageUploader {

    private static final String IMAGE_FOLDER_NAME = "images";
    private static final String ABSOLUTE_LOCAL_PATH = Paths.get("").toAbsolutePath().toString();

    private final String imageOrigin;

    public LocalImageUploader(@Value("${image.origin}") String imageOrigin) {
        this.imageOrigin = imageOrigin;
        FilePathBuilder.root(ABSOLUTE_LOCAL_PATH)
                .addPath(IMAGE_FOLDER_NAME)
                .mkdir();
    }

    @Override
    public String upload(MultipartFile image, String folderName, String fileName) {
        String uploadFullPath = FilePathBuilder.root(ABSOLUTE_LOCAL_PATH)
                .addPath(IMAGE_FOLDER_NAME)
                .addPath(folderName)
                .mkdir()
                .setFileName(fileName)
                .setExtension(extractExtension(image))
                .build();

        try {
            image.transferTo(new File(uploadFullPath));
        } catch (IOException e) {
            throw new BusinessException(ExceptionData.IMAGE_UPLOAD_ERROR);
        }

        return FilePathBuilder.root(imageOrigin)
                .addPath(IMAGE_FOLDER_NAME)
                .addPath(folderName)
                .setFileName(fileName)
                .setExtension(extractExtension(image))
                .build();
    }

    @Override
    public boolean remove(String imageUrl) {
        String localFullPath = imageUrl.replace(imageOrigin, ABSOLUTE_LOCAL_PATH);
        File file = new File(localFullPath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private String extractExtension(MultipartFile image) {
        return image.getOriginalFilename()
                .split("\\.")[1];
    }
}
