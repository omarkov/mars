package server.osi;

import java.io.*;
import java.util.*;


public class FileWalker
{
    // ------------------------------------------------------------------------
    // String constants
    // ------------------------------------------------------------------------

    public static final String personalDirectoryName = "personal";
    public static final String groupsDirectoryName = "groups";

    public static final String audioDirectoryName = "audio";
    public static final String videoDirectoryName = "video";
    public static final String imagesDirectoryName = "images";

    // ------------------------------------------------------------------------

    private File root;

    private List audioFiles = new ArrayList();
    private List videoFiles = new ArrayList();
    private List imageFiles = new ArrayList();


    private void buildFileList(File file, List list) throws IOException
    {
	if (file.isDirectory()) {
	    // recurse into directories
	    String[] entries = file.list();

	    for (int i = 0; i < entries.length; i++)
		buildFileList(new File(file, entries[i]), list);

	} else if (file.isFile()) {
	    // only add files if they're not hidden
	    if (!file.isHidden())
		list.add(file);
	}
    }


    public FileWalker(File root) throws IOException
    {
	this.root = root;

	update();
    }

    public void update() throws IOException
    {
	File personalDirectory = new File(root, personalDirectoryName);
	File groupsDirectory = new File(root, groupsDirectoryName);

	audioFiles.clear();
	videoFiles.clear();
	imageFiles.clear();

	// personal files first
 	buildFileList(new File(personalDirectory, audioDirectoryName), audioFiles);
	buildFileList(new File(personalDirectory, videoDirectoryName), videoFiles);
	buildFileList(new File(personalDirectory, imagesDirectoryName), imageFiles);

	// group files
	buildFileList(new File(groupsDirectory, audioDirectoryName), audioFiles);
	buildFileList(new File(groupsDirectory, videoDirectoryName), videoFiles);
	buildFileList(new File(groupsDirectory, imagesDirectoryName), imageFiles);
    }

    public List getAudioFiles()
    {
	return audioFiles;
    }

    public List getVideoFiles()
    {
	return videoFiles;
    }

    public List getImageFiles()
    {
	return imageFiles;
    }
}
