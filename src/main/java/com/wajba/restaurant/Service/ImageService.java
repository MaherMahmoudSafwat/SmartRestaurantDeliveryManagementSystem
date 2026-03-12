package com.wajba.restaurant.Service;

import com.wajba.restaurant.Models.Category;
import com.wajba.restaurant.Models.Image;
import com.wajba.restaurant.Models.MenuItems;
import com.wajba.restaurant.Models.Modifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageService
{
    public static String SaveAndUploadFilesToHardDisk(String UploadFileName, MultipartFile Image) throws IOException
    {
        ValidateInputImage(Image);
        String UploadNewFileName = System.currentTimeMillis() + "-" + Image.getOriginalFilename();
        String FullPath = UploadFileName + UploadNewFileName;
        File Directory = new File(UploadFileName);
        if(!Directory.exists())
        {
            Directory.mkdirs();
        }

        Image.transferTo(new File(FullPath));
        return UploadNewFileName;
    }

    public static void ValidateInputImage(MultipartFile Image)
    {
        if(Image.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Image too large. Max 5MB");
        }

        String contentType = Image.getContentType();
        List<String> allowedTypes = Arrays.asList(
                "image/jpeg", "image/jpg", "image/png", "image/gif"
        );

        if(!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, and GIF images allowed");
        }

        String filename = Image.getOriginalFilename();
        if(filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Invalid filename");
        }

        if(filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename characters");
        }
    }

    public static Image CreateNewImage(String FileSystem, String WebUrl, MultipartFile Image) throws IOException {
        String SavedFileName = SaveAndUploadFilesToHardDisk(FileSystem,Image);
        Image image = new Image();
        image.setName(Image.getName());
        image.setType(Image.getContentType());
        image.setUrl(WebUrl+SavedFileName);
        return image;
    }

    public static String extractFileName(String Url)
    {
        return Url.substring(Url.lastIndexOf("/") + 1);
    }

    public static <T> void DeleteFileImage(String FileSystem,T DataImage) throws IOException {
        Image image = null;
        if (DataImage instanceof MenuItems)
        {
            image = ((MenuItems) DataImage).getImage();
        }
        else if (DataImage instanceof Modifier)
        {
            image = ((Modifier) DataImage).getImage();
        }
        else if (DataImage instanceof Category)
        {
            image = ((Category) DataImage).getImage();
        }else
        {
            return;
        }

        if (image != null && image.getUrl() != null)
        {
            String fileName = extractFileName(image.getUrl());
            String imagePath =  FileSystem + fileName;
            Files.deleteIfExists(Paths.get(imagePath));
        }
    }
}
