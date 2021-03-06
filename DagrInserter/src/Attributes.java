import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;

import org.apache.commons.io.FilenameUtils;

public class Attributes {
	String owner = null;
	long size;
	Timestamp lastModifiedTime = null;
	String fileName = null;
	String extension;
	String path;
	
	public Attributes(String pathString) {
		
		
		fileName = FilenameUtils.getName(pathString);
		extension = FilenameUtils.getExtension(pathString);
		this.path = pathString;
		Path path = Paths.get(pathString);
		FileOwnerAttributeView ownerView = Files.getFileAttributeView(path,
				FileOwnerAttributeView.class);
		BasicFileAttributeView attributeView =  Files.getFileAttributeView(path,
				BasicFileAttributeView.class);
		try {
			BasicFileAttributes attributes = attributeView.readAttributes();
			size = attributes.size();
			lastModifiedTime = new Timestamp(attributes.lastModifiedTime().toMillis());
			owner = ownerView.getOwner().getName();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
