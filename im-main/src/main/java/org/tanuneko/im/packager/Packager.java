package org.tanuneko.im.packager;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by neko32 on 2016/12/10.
 */
@SuppressWarnings("ALL")
public class Packager {

    private static final String SEP = System.getProperty("file.separator");
    private static final String WIN_BATCH_NAME = "tanu_messager.bat";
    private static final String LINUX_BATCH_NAME = "tanu_messager.sh";
    private static final String WIN_UPDATER_BATCH_NAME = "updater.bat";
    private static final String LINUX_UPDATER_BATCH_NAME = "updater.sh";

    public static void main(String[] args) throws ZipException, IOException {
        if(args.length != 1) {
            System.out.println("Usage: Packager [Messager Project Dir]");
            System.exit(1);
        }
        createPackage(args[0]);
    }

    public static void createPackage(final String projectDir) throws ZipException, IOException {
        String jarFile = getJarFile(projectDir);
        String updaterJarFile = getUpdaterJarFile(projectDir);
        String workDir = String.format("%s%s%s%s%sworkarea", projectDir, SEP, "im-main", SEP, "build", SEP);
        System.out.println("work dir is " + workDir);
        File workDirFile = createWorkDir(workDir);
        copyJar(workDirFile, jarFile);
        copyUpdaterJar(workDirFile, updaterJarFile);
        copyResources(workDirFile, projectDir);
        copyScripts(workDirFile, projectDir);
        createArchive(projectDir, workDir);
    }

    public static String getJarFile(final String projectDir) {
        boolean okToProceed = true;
        String path = String.format("%s%s%s%s%s%s%s%s%s", projectDir, SEP, "im-main", SEP, "build", SEP, "libs", SEP, "im-main-all.jar");
        okToProceed = new File(path).exists();
        if(!okToProceed) {
            throw new IllegalStateException("im-main/build/libs/im-main-all.jar doesn't exist. Run shadowJar first");
        }
        return path;
    }

    public static String getUpdaterJarFile(final String updaterDir) {
        boolean okToProceed = true;
        String path = String.format("%s%s%s%s%s%s%s%s%s", updaterDir, SEP, "updater", SEP, "build", SEP, "libs", SEP, "updater-all.jar");
        okToProceed = new File(path).exists();
        if(!okToProceed) {
            throw new IllegalStateException("updater/build/libs/updater-all.jar doesn't exist. Run shadowJar first");
        }
        return path;
    }

    public static File createWorkDir(final String workDir) {
        File workDirFile = new File(workDir);
        if (workDirFile.exists()) {
            throw new IllegalStateException("Workdir exists. Delete the workdir");
        }
        if(!workDirFile.mkdir()) {
            throw new IllegalStateException("Workdir creation failed");
        }
        return workDirFile;
    }

    public static void copyJar(final File workDir, final String jarFile) throws IOException {
        FileUtils.copyFile(new File(jarFile), new File(workDir.getAbsolutePath() + SEP + "im-main-all.jar"));
    }

    public static void copyUpdaterJar(final File workDir, final String jarFile) throws IOException {
        FileUtils.copyFile(new File(jarFile), new File(workDir.getAbsolutePath() + SEP + "updater-all.jar"));
    }

    public static void copyResources(final File workDir, final String projectDir) throws IOException {
        String rcPath = projectDir + SEP + "im-main" + SEP + "src" + SEP + "main" + SEP + "resources";
        String iconPath = workDir.getAbsolutePath() + SEP + "icon";
        String confPath = workDir.getAbsolutePath() + SEP + "conf";
        if(!new File(iconPath).mkdir()) {
            throw new IllegalStateException("Creating icon dir failed");
        }
        if(!new File(confPath).mkdir()) {
            throw new IllegalStateException("Creating conf dir failed");
        }
        FileUtils.copyFile(new File(rcPath + SEP + "images" + SEP + "icon" + SEP + "default.png"),
                           new File(iconPath + SEP + "default.png"));
        FileUtils.copyFile(new File(rcPath + SEP + "conf" + SEP + "tanuim_template.properties"),
                           new File(confPath + SEP + "tanuim.properties"));
    }

    public static void copyScripts(final File workDir, final String projectDir) throws IOException {
        String rcPath = projectDir + SEP + "im-main" + SEP + "src" + SEP + "main" + SEP + "resources";
        String scriptPath = projectDir + SEP + "im-main" + SEP + "src" + SEP + "main" + SEP + "scripts" + SEP + WIN_BATCH_NAME;
        String linuxScriptPath = projectDir + SEP + "im-main" + SEP + "src" + SEP + "main" + SEP + "scripts" + SEP + LINUX_BATCH_NAME;
        String updaterScriptPath = projectDir + SEP + "updater" + SEP + "src" + SEP + "main" + SEP + "scripts" + SEP + WIN_UPDATER_BATCH_NAME;
        String linuxUpdaterScriptPath = projectDir + SEP + "updater" + SEP + "src" + SEP + "main" + SEP + "scripts" + SEP + LINUX_UPDATER_BATCH_NAME;
        String vbsTarget = workDir + SEP + "vbs";
        if(!new File(vbsTarget).mkdir()) {
            throw new IllegalStateException("VBS dir creation failed");
        }
        FileUtils.copyFile(new File(scriptPath), new File(workDir.getAbsolutePath() + SEP + WIN_BATCH_NAME));
        FileUtils.copyFile(new File(updaterScriptPath), new File(workDir.getAbsolutePath() + SEP + WIN_UPDATER_BATCH_NAME));
        FileUtils.copyFile(new File(linuxScriptPath), new File(workDir.getAbsolutePath() + SEP + LINUX_BATCH_NAME));
        FileUtils.copyFile(new File(linuxUpdaterScriptPath), new File(workDir.getAbsolutePath() + SEP + LINUX_UPDATER_BATCH_NAME));
        File linuxFile = new File(workDir.getAbsolutePath() + SEP + LINUX_BATCH_NAME);
        linuxFile.setWritable(false);
        linuxFile.setExecutable(true);
        linuxFile = new File(workDir.getAbsolutePath() + SEP + LINUX_UPDATER_BATCH_NAME);
        linuxFile.setWritable(false);
        linuxFile.setExecutable(true);
        FileUtils.copyFile(new File(rcPath + SEP + "vbs" + SEP + "speech.vbs"), new File(vbsTarget + SEP + "speech.vbs"));
    }

    public static void createArchive(final String projectDir, final String workDir) throws ZipException {
        String buildDir = projectDir + SEP + "im-main" + SEP + "build";
        ZipFile zip = new ZipFile(buildDir + SEP + "tanu-im.zip");
        ZipParameters zipParam = new ZipParameters();
        zipParam.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParam.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        zip.addFile(new File(workDir + SEP + WIN_BATCH_NAME), zipParam);
        zip.addFile(new File(workDir + SEP + "im-main-all.jar"), zipParam);
        zip.addFile(new File(workDir + SEP + "updater-all.jar"), zipParam);
        zip.addFolder(new File(workDir + SEP + "vbs"), zipParam);
        zip.addFolder(new File(workDir + SEP + "conf"), zipParam);
        zip.addFolder(new File(workDir + SEP + "icon"), zipParam);
    }
}
