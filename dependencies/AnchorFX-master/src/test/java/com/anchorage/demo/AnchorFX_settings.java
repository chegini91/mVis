/*
 * Copyright 2015-2016 Alessio Vinerbi. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anchorage.demo;

import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.system.AnchorageSystem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.util.Random;

/**
 *
 * @author Alessio
 */
public class AnchorFX_settings extends Application {

    @Override
    public void start(Stage primaryStage) {

        //AnchorageSettings.setDockingPositionPreview(false);
        
        DockStation station = AnchorageSystem.createStation();

        Scene scene = new Scene(station,  1024, 768);

        DockNode node1 = AnchorageSystem.createDock("Not floatable", generateRandomTree());
        node1.dock(station, DockNode.DockPosition.LEFT);
        node1.floatableProperty().set(false);
        
        DockNode node2 = AnchorageSystem.createDock("Not resizable", generateRandomTree());
        node2.dock(station, DockNode.DockPosition.RIGHT);
        node2.resizableProperty().set(false);
        
        DockNode node3 = AnchorageSystem.createDock("Not maximizable", generateRandomTree());
        node3.dock(station, DockNode.DockPosition.TOP);
        node3.maximizableProperty().set(false);
        
        DockNode node4 = AnchorageSystem.createDock("Not closeable", generateRandomTree());
        node4.dock(station, DockNode.DockPosition.BOTTOM);
        node4.closeableProperty().set(false);

        

        primaryStage.setTitle("AnchorFX ");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        AnchorageSystem.installDefaultStyle();
        
    }

    private TreeView<String> generateRandomTree() {
        // create a demonstration tree view to use as the contents for a dock node
        TreeItem<String> root = new TreeItem<String>("Root");
        TreeView<String> treeView = new TreeView<String>(root);
        treeView.setShowRoot(false);

        // populate the prototype tree with some random nodes
        Random rand = new Random();
        for (int i = 4 + rand.nextInt(8); i > 0; i--) {
            TreeItem<String> treeItem = new TreeItem<String>("Item " + i);
            root.getChildren().add(treeItem);
            for (int j = 2 + rand.nextInt(4); j > 0; j--) {
                TreeItem<String> childItem = new TreeItem<String>("Child " + j);
                treeItem.getChildren().add(childItem);
            }
        }

        return treeView;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
