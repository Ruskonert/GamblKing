package com.ruskonert.GamblKing.engine.program;

import com.ruskonert.GamblKing.ProgramInitializable;
import com.ruskonert.GamblKing.util.SystemUtil;

import com.ruskonert.GamblKing.engine.connect.Update;
import com.ruskonert.GamblKing.engine.ProgramApplication;
import com.ruskonert.GamblKing.engine.program.component.ProgramComponent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

public final class ApplicationLoader extends Application implements ProgramInitializable
{
    private static Stage PreloadStage;
    public static Stage getPreloadStage() { return PreloadStage; }

    public static void main(String[] args){ launch(); }

    @Override public void start(Stage primaryStage)
    {
        try {this.initialize(primaryStage); }
        catch(Exception e)
        {
            SystemUtil.Companion.error(e);
        }
    }

    @Override
    public final boolean initialize(Object handleInstance)
    {
        this.onInit();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                Platform.runLater(() -> {
                    AppFramework framework = new ProgramApplication();
                    try
                    {
                        PreloadStage.close();
                        framework.start(new Stage());
                    }
                    catch (Exception e)
                    {
                        SystemUtil.Companion.error(e);
                    }
                });
                return null;
            }
        };

        try
        {
            this.createSafetyWindow();
            this.checkResourceProcess(task);
            return true;
        }
        catch (Exception e) {
            SystemUtil.Companion.error(e);
            return false;
        }
    }


    private void onInit() {

        File data = new File(System.getProperty("user.dir") + "/data");
        File update = new File(System.getProperty("user.dir") + "/update");
        if (!data.exists())
        {
            data.mkdir();
        }

        if(!update.exists())
        {
            update.mkdir();
        }
    }

    private void createSafetyWindow() throws Exception
    {
        Stage stage = new Stage();
        PreloadStage = stage;
        FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getLayout(this.getClass()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 600, 400);

        stage.setTitle(ProgramComponent.PROGRAM_NAME);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    private void checkResourceProcess(Task<?> syncStart)
    {
        Pointer<Integer> process = new Pointer<>(0);
        Label label = ProgramManager.getPreloadComponent().getStatusLabel();
        File folder = new File(System.getProperty("user.dir"));
        int fileCount = this.getFilesCount(folder);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                Update.update();
                if(fileCount != 0) checkFileResource(folder, label, process);

                Platform.runLater(() -> label.setText("서버 관리 프로그램을 열고 있습니다..."));
                Platform.runLater(() -> ProgramManager.getPreloadComponent().getLoadingProgressBar().setProgress(ProgressBar.INDETERMINATE_PROGRESS));
                Thread.sleep(2000L);
                Thread thread = new Thread(syncStart);
                thread.start();

                return null;
            }
        };

        Service<Void> progressTask = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    private int totalFileCount = fileCount;
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            Platform.runLater(() -> ProgramManager.getPreloadComponent()
                                    .getLoadingProgressBar().setProgress((double) process.ptr / totalFileCount));
                            if (process.ptr == totalFileCount) break;
                            Thread.sleep(3L);
                        }
                        return null;
                    }
                };
            }
        };
        Thread checkingThread = new Thread(task);
        checkingThread.start();
        progressTask.start();
    }

    private int getFilesCount(File file)
    {
        if(file.listFiles().length == 0) return 0;
        return this.getFilesCount(file, 0);
    }

    private int getFilesCount(File file, int count)
    {
        for(File f : file.listFiles())
        {
            if(f.isDirectory())
            {
                return this.getFilesCount(f, count);
            }
            else if(f.isFile()) count++;
        }
        return count;
    }


    private void checkFileResource(final File folder, Label label, Pointer<Integer> progress) throws InterruptedException
    {
        for (final File fileEntry : folder.listFiles())
        {
            if (fileEntry.isDirectory())
            {
                Platform.runLater(() -> label.setText("Analyzing Folder: " + fileEntry.toString()));
                checkFileResource(fileEntry, label, progress);
            }
            else
            {
                Platform.runLater(() -> label.setText("Checking for file: " + fileEntry.toString()));
                progress.ptr++;
            }
            Thread.sleep(2L);
        }
    }

}