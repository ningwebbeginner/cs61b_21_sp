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
        if (!GITLET_DIR.exists() || !GITLET_DIR.isDirectory()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }


    public static void add(String arg) {
        checkInit();
        File addFile = join(CWD, arg);
        if(!addFile.exists() || addFile.isDirectory()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        staggingFile(addFile);
    }

    private static void staggingFile(File addFile) {
        File index_File = join(GITLET_DIR, "index");
        String fileContentUID = Utils.sha1(Utils.readContents(addFile));
        File newFileInBlob = addFileInBlob(addFile, fileContentUID);
        if(!index_File.exists()) {
            try {
                index_File.createNewFile();
                HashMap<File, String> save = new HashMap<>();
                save.put(addFile, fileContentUID);
                Utils.writeObject(index_File, save);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            HashMap<File, String> readFile = Utils.readObject(index_File, HashMap.class);
            String RemoveFileUID = readFile.put(addFile, fileContentUID);
            if(RemoveFileUID != null) {
                boolean result = removeFileInBlob(RemoveFileUID);
            }
            Utils.writeObject(index_File, readFile);
        }
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
