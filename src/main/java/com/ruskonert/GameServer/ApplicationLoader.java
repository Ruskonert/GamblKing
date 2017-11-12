package com.ruskonert.GameServer;

import com.ruskonert.GameServer.data.Pointer;
import com.ruskonert.GameServer.program.AppFramework;
import com.ruskonert.GameServer.program.ProgramManager;
import com.ruskonert.GameServer.program.component.ProgramComponent;
import com.ruskonert.GameServer.util.SystemUtil;

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

import java.io.File;

public final class ApplicationLoader extends Application
{
    private static Stage PreloadStage;
    public static Stage getPreloadStage() { return PreloadStage; }

    public static void main(String[] args){ launch(); }

    @Override public void start(Stage primaryStage)
    {
        this.initialize(primaryStage);
    }

    private boolean initialize(Object handleInstance)
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                Platform.runLater(() -> {
                    AppFramework framework = new AppFramework();
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

    private void createSafetyWindow() throws Exception
    {
        Stage stage = new Stage();
        PreloadStage = stage;
        FXMLLoader loader = new FXMLLoader(ProgramManager.getLayout(this.getClass()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 600, 400);

        stage.setTitle(ProgramComponent.PROGRAM_NAME);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    private synchronized void checkResourceProcess(Task<?> syncStart)
    {
        Pointer<Integer> process = new Pointer<>(0);
        Label label = ProgramManager.getPreloadComponent().getStatusLabel();
        File folder = new File(System.getProperty("user.dir"));
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                checkFileResource(folder, label, process);
                Platform.runLater(() -> label.setText("서버 관리 프로그램을 열고 있습니다..."));
                Platform.runLater(() -> ProgramManager.getPreloadComponent().getLoadingProgressBar().setProgress(ProgressBar.INDETERMINATE_PROGRESS));
                Thread.sleep(3000L);
                Thread thread = new Thread(syncStart);
                thread.start();

                return null;
            }
        };

        Service<Void> progressTask = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    private int totalFileCount = SystemUtil.Companion.getFiles(folder);

                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            Platform.runLater(() -> ProgramManager.getPreloadComponent()
                                    .getLoadingProgressBar().setProgress((double) process.ptr / totalFileCount));
                            if (process.ptr == totalFileCount) break;
                            Thread.sleep(5L);
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
            Thread.sleep(10L);
        }
    }

}