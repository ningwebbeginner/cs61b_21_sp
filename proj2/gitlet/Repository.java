package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * * .gitlet/ -- top level folder for all persistent data
 *    - commit/ -- folder containing all of the persistent data for commits
 *    - blob/ -- folder containing all of the persistent data for blobs
 *    - branch/ -- folder containing all of the persistent data for branch
 *    - HEAD -- file containing the current HEAD
 *    - (index) -- file containing the current staging area, file is created
 *                 depending on commands
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");
    public static final File HEAD = join(GITLET_DIR, "HEAD");


    /* TODO: fill in the rest of this class. */
    
    public static void init() {
        if(!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOB_DIR.mkdir();
            BRANCH_DIR.mkdir();
            String master_UID = new Commit().saveFile();
            addBranchFile("master", master_UID);
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeContents(HEAD, master_UID);
        }
        else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    private static void addBranchFile(String branch, String branch_uid) {
        File masterFile = join(BRANCH_DIR, branch);
        try {
            masterFile.createNewFile();
            Utils.writeContents(masterFile, branch_uid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkInit() {
        if (!GITLET_DIR.isDirectory()
        || !COMMIT_DIR.isDirectory()
        || !BLOB_DIR.isDirectory()
        || !BRANCH_DIR.isDirectory()
        || !HEAD.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }


    /**
     * adding a file is also called staging the file for addition.
     * 1. Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * 2. If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * 3. The file will no longer be staged for removal (see gitlet rm),
     * if it was at the time of the command.
     */
    public static void add(String arg) {
        checkInit();
        File addFile = join(CWD, arg);
        if(!addFile.exists() || addFile.isDirectory()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        staggingAddFile(addFile);
    }

    private static void staggingAddFile(File addFile) {
        String fileContentUID = getContentID(addFile);
        if(readCurentCommit().isIDexist(fileContentUID)) {
            removeInIndex(addFile, fileContentUID);
            return;
        }
        File newFileInBlob = addFileInBlob(addFile, fileContentUID);
        addtoIndex(addFile, fileContentUID);
    }

    //TODO test after finish commit()
    private static void removeInIndex(File addFile, String fileUID) {
        File index_File = join(GITLET_DIR, "index");
        if(index_File.exists()) {
            HashMap<File, String> readFile = Utils.readObject(index_File, HashMap.class);
            if(readFile.containsValue(fileUID)) {
                String removeFile = readFile.remove(readFile.get(addFile));
                removeFileInBlob(removeFile);
            }
            if(readFile.size() == 0){
                index_File.delete();
            }
            else {
                Utils.writeObject(index_File, readFile);
            }
        }
    }

    private static void addtoIndex(File addFile, String fileContentUID) {
        File index_File = join(GITLET_DIR, "index");
        if(!index_File.exists()) {
            try {
                index_File.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<File, String> save = new HashMap<>();
            save.put(addFile, fileContentUID);
            Utils.writeObject(index_File, save);
        }
        else {
            HashMap<File, String> readFile = Utils.readObject(index_File, HashMap.class);
            String RemoveFileUID = readFile.put(addFile, fileContentUID);
            if(RemoveFileUID != null) {
                boolean result = removeFileInBlob(RemoveFileUID);
                //TODO the file does not exist
            }
            Utils.writeObject(index_File, readFile);
        }
    }


    private static Commit readCurentCommit() {
        String headID = Utils.readContentsAsString(HEAD);
        File currentCommitFile = join(COMMIT_DIR, headID);
        if(!currentCommitFile.exists()) {
            return null;
        }
        return Utils.readObject(currentCommitFile, Commit.class);
    }

    /*private class Blob {
        Blob() {}

    }*/

        static String getContentID(File fileInCWD) {
            return Utils.sha1(Utils.readContents(fileInCWD));
        }

    private static boolean removeFileInBlob(String removeFileUID) {
        File removeFileInBlob = join(BLOB_DIR, removeFileUID);
            return removeFileInBlob.delete();
    }

    private static File addFileInBlob(File addFile, String fileContentUID) {
        File addFileInBlob = join(BLOB_DIR, fileContentUID);
        try {
            addFileInBlob.createNewFile();
            Utils.writeContents(addFileInBlob, Utils.readContents(addFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addFileInBlob;
    }


    public static void commit(String arg) {
    }

    public static void checkoutBranch(String arg) {
    }

    public static void checkoutFilename(String arg, String arg1) {
    }

    public static void checkoutIdWithFilename(String arg, String arg1, String arg2) {
    }

    public static void log() {
    }
}
