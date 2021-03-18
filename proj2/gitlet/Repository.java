package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *    - INDEX -- file containing the current staging area
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
    public static final File INDEX_File = join(GITLET_DIR, "index");

    /* TODO: fill in the rest of this class. */
    
    public static void init() {
        if(!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOB_DIR.mkdir();
            BRANCH_DIR.mkdir();
            String master_ID = new Commit().saveFile();
            File masterFile = addBranchFile("master", master_ID);
            initIndex();
            Utils.writeObject(HEAD, masterFile);
        }
        else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    private static void initIndex() {
        Utils.writeObject(INDEX_File, new HashMap<File, String>());
    }


    private static File addBranchFile(String branch, String branch_id) {
        File branchFile = join(BRANCH_DIR, branch);
        Utils.writeContents(branchFile, branch_id);
        return branchFile;
    }

    private static void checkInit() {
        if (!GITLET_DIR.isDirectory()
        || !COMMIT_DIR.isDirectory()
        || !BLOB_DIR.isDirectory()
        || !BRANCH_DIR.isDirectory()
        || !HEAD.exists()
        || !INDEX_File.exists()) {
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
        stagingAddFile(addFile);
    }

    private static void stagingAddFile(File addFile) {
        String fileContentID = getContentID(addFile);
        Blob blob = new Blob(addFile);
        if(readCurrentCommit().isIDexist(fileContentID)) {
            removeInIndex(addFile, fileContentID);
            return;
        }
        File newFileInBlob = blob.addFileInBlob();
        addtoIndex(addFile, fileContentID);
    }

    /****************INDEX******************/
    /****
     *INDEX is the staging are of the command.
     * The content of INDEX are entries:
     * file to ID
     * or file to null to remove.
     */
    //TODO: test after finish commit()
    private static void removeInIndex(File file_CWD, String fileUID) {
            HashMap<File, String> readFile = Utils.readObject(INDEX_File, HashMap.class);
            //if(readFile.containsValue(fileUID)) {}
            String removeFileID = readFile.remove(file_CWD);
            if(removeFileID != null) new Blob(removeFileID).removeFileInBlob();

            Utils.writeObject(INDEX_File, readFile);
            if(fileUID != null && Utils.join(BLOB_DIR,fileUID).isFile()) {
                new Blob(fileUID).removeFileInBlob();       //remove the blob which is in index map
            }
    }


    // The function would override the blob which not be in previous Commit
    private static void addtoIndex(File file_CWD, String fileContentID) {
            HashMap<File, String> readFile = Utils.readObject(INDEX_File, HashMap.class);
            String removeFileID = readFile.put(file_CWD, fileContentID);
            if(removeFileID != null
                    &&  !readCurrentCommit().isIDexist(fileContentID)) {
                boolean result = new Blob(removeFileID).removeFileInBlob();
                //TODO the file does not exist
            }
            Utils.writeObject(INDEX_File, readFile);
    }
    /****************INDEX END******************/

    private static Commit readCurrentCommit() {
        File headFilePath = Utils.readObject(HEAD, File.class);
        String headID = Utils.readContentsAsString(headFilePath);
        File currentCommitFile = join(COMMIT_DIR, headID);
        if(!currentCommitFile.exists()) {
            return null;
        }
        return Utils.readObject(currentCommitFile, Commit.class);
    }


         static String getContentID(File fileInCWD) {
            return Utils.sha1(Utils.readContents(fileInCWD));
        }

    /****************Blob (TO Be Class)******************/
    private static boolean removeFileInBlob(String removeFileID) {
        File removeFileInBlob = join(BLOB_DIR, removeFileID);
            return removeFileInBlob.delete();
    }

    private static File addFileInBlob(File addFile, String fileContentID) {
        File addFileInBlob = join(BLOB_DIR, fileContentID);
        Utils.writeContents(addFileInBlob, Utils.readContents(addFile));
        return addFileInBlob;
    }
    /**************Blob END*****************/

    /***
     * The commit is said to be tracking the saved files.
     * 1. By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * 2. A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * 3. A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * 4. Finally, files tracked in the current commit may be untracked in the new commit
     * as a result being staged for removal by the rm command (below).
     * @param arg
     */
    public static void commit(String arg) {
        checkInit();
        Commit currentCommit = readCurrentCommit();
        assert currentCommit != null;
        HashMap<File, String> currentMap = new HashMap<>(currentCommit.getMap());
        for(Map.Entry<File, String> fileEntry : currentMap.entrySet()) {
            if(fileEntry.getValue() != null) {
                Blob blob = new Blob(fileEntry.getValue());
                blob.undeletableFile();
            }
        }
        HashMap<File, String> indexMap = Utils.readObject(INDEX_File, HashMap.class);
        if(indexMap.size() == 0) systemoutWithMessage("No changes added to the commit.");
        currentMap.putAll(indexMap);


        String currentBranchName = Utils.readObject(HEAD, File.class).getName();
        File curentBranch = join(BRANCH_DIR, currentBranchName);

        Commit newCommit = new Commit(arg, currentMap, Utils.readContentsAsString(curentBranch));
        String newCommitID = newCommit.saveFile();

        Commit commit = Utils.readObject(Utils.join(COMMIT_DIR, newCommitID), Commit.class);
        if(!commit.thisID().equals(newCommitID)) {
            Utils.join(COMMIT_DIR, newCommitID).delete();
            newCommitID = commit.saveFile();
        }
        Utils.writeContents(curentBranch, newCommitID);

        initIndex();
    }

    private static void systemoutWithMessage(String s) {
        System.out.println(s);
        System.exit(0);
    }



    //TODO test checkout after branch
    public static void checkoutBranch(String branchName) {
        checkInit();
        checkBranch(branchName);
        takesAllFilesatGivenbranch(branchName);

        File branchFile = Utils.join(BRANCH_DIR, branchName);
        Utils.writeObject(HEAD, branchFile);
        Utils.writeObject(INDEX_File, new HashMap<File, String>());
    }



    private static void checkBranch(String branchName) {
        if(!Utils.join(BRANCH_DIR, branchName).isFile()) {
            systemoutWithMessage("No such branch exists.");
        }
        if(Utils.readObject(HEAD, File.class).getName().equals(branchName)) {
            systemoutWithMessage("No need to checkout the current branch.");
        }
        List<String> filesName = Utils.plainFilenamesIn(CWD);
        for(String fileName:filesName) {
            String blobID = getContentID(Utils.join(CWD, fileName));
            if(!Utils.join(BLOB_DIR, blobID).isFile()) {
                systemoutWithMessage("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
            }
        }
    }

    private static void takesAllFilesatGivenbranch(String branchName) {
        Commit commitCurrent = readCurrentCommit();
        HashMap<File, String> mapCurrent = new HashMap<>(commitCurrent.getMap());
        for(Map.Entry<File, String> eachEntry : mapCurrent.entrySet()) {
            File thisFile = eachEntry.getKey();
            thisFile.delete();
        }

        File branchFile = Utils.join(BRANCH_DIR, branchName);
        String branchID = Utils.readContentsAsString(branchFile);
        Commit commitInBranch = Utils.readObject(Utils.join(COMMIT_DIR, branchID),Commit.class);
        HashMap<File, String> mapInBranch = new HashMap<>(commitInBranch.getMap());
        for(Map.Entry<File, String> eachEntry : mapInBranch.entrySet()) {
            File blob = Utils.join(BLOB_DIR, eachEntry.getValue());
            String contest = Utils.readContentsAsString(blob);
            Utils.writeContents(eachEntry.getKey(), contest);
        }
    }

    public static void checkoutFilename(String arg, String fileName) {
        checkInit();
        if(!arg.equals("--")) {
            systemoutWithMessage("Incorrect operands.");
        }
        String commitID = Utils.readContentsAsString(Utils.readObject(HEAD, File.class));
        checkoutIdWithFilename(commitID, fileName);
    }

    public static void checkoutIdWithFilename(String commitId, String arg, String fileName) {
        checkInit();
        if(!arg.equals("--")) {
            systemoutWithMessage("Incorrect operands.");
        }
        checkoutIdWithFilename(commitId, fileName);
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     * @param commitId
     * @param fileName
     */
    private static void checkoutIdWithFilename(String commitId, String fileName) {
       File commitFile = getCommitFile(commitId);
       File file = Utils.join(CWD, fileName);
       Commit commit = Utils.readObject(commitFile, Commit.class);
       HashMap<File, String> mapInCommit = new HashMap<>(commit.getMap());
       if(!mapInCommit.containsKey(file)) {
           systemoutWithMessage("File does not exist in that commit.");
       }
       String blobId = mapInCommit.get(file);
       Utils.writeContents(file, new Blob(blobId).getContent());
       removeInIndex(file,null);
    }

    private static File getCommitFile(String commitId) {
        List<String> commitFiles = Utils.plainFilenamesIn(COMMIT_DIR);
        int positionInString = -1;
        for(String commitFile : commitFiles) {
            positionInString = commitFile.indexOf(commitId);
            if(positionInString == 0) {
                return Utils.join(COMMIT_DIR, commitFile);
            }
        }
        if(positionInString != 0) {
            systemoutWithMessage("No commit with that id exists.");
        }
        return null;            //Never be reached
    }

    public static void log() {
        Commit commitHead = readCurrentCommit();
        String result = "";
        result = printLogRec(commitHead, result);
        System.out.print(result);
    }

    private static String printLogRec(Commit commitHead, String result) {
        //System.out.print(commitHead);
        result += commitHead.toString();
        String parentID = commitHead.getParentID();
        if(parentID == null) {
            return result;
        }
        return printLogRec(Utils.readObject(Utils.join(COMMIT_DIR, parentID), Commit.class), result);
    }


    //TODO test remove file
    public static void rmFile(String fileNameInCWD) {
        File rmFile = join(CWD, fileNameInCWD);

        if(!rmFile.exists()) {
            return;
        }
            String contestID = getContentID(rmFile);
            removeInIndex(rmFile, contestID);

            Commit currentCommit = readCurrentCommit();

            boolean checkTracked  = currentCommit.getMap().containsKey(rmFile);
            if(checkTracked) {
                addtoIndex(rmFile, null);
                rmFile.delete();
            }
            if(!Utils.readObject(INDEX_File, HashMap.class).containsKey(rmFile)
            && !checkTracked) {
                systemoutWithMessage("No reason to remove the file.");
            }

    }


    public static void globallog() {
    }
}
