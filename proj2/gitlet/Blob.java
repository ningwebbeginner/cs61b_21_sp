package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;

public class Blob implements Serializable {


    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    private static final File BLOB_DIR = join(GITLET_DIR, "blob");

    private transient File fileInCWD;
    private transient String fileContentID;
    private String contentInCWD;
    private boolean deletable;


    public Blob(File fileInCWD) {
        this.fileInCWD = fileInCWD;
        this.contentInCWD = Utils.readContentsAsString(fileInCWD);
        this.fileContentID = Utils.sha1(contentInCWD);
        File fileInBlob = join(BLOB_DIR, fileContentID);
        if(join(BLOB_DIR, fileContentID).exists()){
            Blob blob = Utils.readObject(fileInBlob, Blob.class);
            this.deletable = blob.deletable;
        }
        else {
            this.deletable = true;
        }

    }

    public Blob(String fileContentID) {
        File fileInBlob = join(BLOB_DIR, fileContentID);
        if (!fileInBlob.exists()) {
            throw new RuntimeException(
                    String.format("Failed to read blob"));
        }
        Blob blob = Utils.readObject(fileInBlob, Blob.class);
        this.fileInCWD = blob.fileInCWD;
        this.contentInCWD = blob.contentInCWD;
        this.deletable = blob.deletable;
        this.fileContentID = fileContentID;
    }

    public void undeletableFile() {
        deletable = false;
    }

    public boolean removeFileInBlob() {
        File removeFileInBlob = join(BLOB_DIR, fileContentID);
        if(deletable) {
            return removeFileInBlob.delete();
        } else {
            return false;
        }
    }

    public File addFileInBlob() {
        File addFileInBlob = join(BLOB_DIR, fileContentID);
        Utils.writeObject(addFileInBlob, this);
        return addFileInBlob;
    }

}
